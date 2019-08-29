package com.hqmy.market.qiniu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.FrameCapturedCallback;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingPreviewCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.SurfaceTextureCallback;
import com.qiniu.pili.droid.streaming.av.common.PLFourCC;
import com.hqmy.market.R;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.RoomBean;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.qiniu.adapter.LiveVideoViewAdapter;
import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.hqmy.market.qiniu.chatroom.gift.GiftSendModel;
import com.hqmy.market.qiniu.chatroom.gift.GiftView;
import com.hqmy.market.qiniu.chatroom.message.ChatRoomGift;
import com.hqmy.market.qiniu.chatroom.panel.EmojiBoard;
import com.hqmy.market.qiniu.live.gles.FBO;
import com.hqmy.market.qiniu.live.ui.CameraPreviewFrameView;
import com.hqmy.market.qiniu.live.utils.Config;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.OnlineLiveFinishActivity;
import com.hqmy.market.view.activity.RoomUserListActivity;
import com.hqmy.market.view.widgets.dialog.BeautyDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

public class AVStreamingActivity extends StreamingBaseActivity implements StreamingPreviewCallback, CameraPreviewFrameView.Listener,
        SurfaceTextureCallback, Handler.Callback {
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_room_num)
    TextView tvRoomNum;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.input_emoji_board)
    EmojiBoard emojiBoard;
    @BindView(R.id.input_editor)
    EditText textEditor;
    @BindView(R.id.giftView)
    GiftView giftView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private LiveVideoViewAdapter mAdapter;

    private static final String TAG = "AVStreamingActivity";
    private CameraStreamingSetting mCameraStreamingSetting;
    private boolean mIsNeedMute = false;
    private int mCurrentZoom = 0;
    private int mMaxZoom = 0;
    private boolean mOrientationChanged = false;
    private int mCurrentCamFacingIndex;
    private FBO mFBO = new FBO();
    private Switcher mSwitcher = new Switcher();
    private ImageSwitcher mImageSwitcher;
    private MediaStreamingManager mMediaStreamingManager;
    //    private String mAudioFile;
    private Handler mHandler;
    private int mTimes = 0;
    private boolean mIsPictureStreaming = false;
    RoomBean roomBean;
    String currentRoomId;
    String id;
    private Handler handler = new Handler(this);

    @Override
    public int getLayoutId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return R.layout.activity_av_streaming;
    }

    @Override
    public void initView() {
        initRecyclerView();
        mCameraStreamingSetting = buildCameraStreamingSetting();
        mCurrentCamFacingIndex = 1;
        giftView.setViewCount(2);
        giftView.init();
    }

    @Override
    public void initData() {
        id = getIntent().getStringExtra("id");
        Serializable serializable = getIntent().getSerializableExtra("room");
        if (serializable != null) {
            roomBean = (RoomBean) serializable;
            currentRoomId = roomBean.getId();
//            ChatroomKit.currentRoomId = currentRoomId;
        }
        ChatroomKit.addEventHandler(handler);
        ChatroomKit.setCurrentUser(new UserInfo(Constants.USER_ID, Constants.USER_NAME, null));
//        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
//            @Override
//            public boolean onReceived(Message message, int i) {
//                ToastUtil.showToast("message==收到");
//                return false;
//            }
//        });
        if (RongIMClient.getInstance().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) {
            if (roomBean != null) {
                ChatroomKit.joinChatRoom(roomBean.getId(), 50, new RongIMClient.OperationCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
//                RongIMClient.getInstance().joinChatRoom(roomBean.getId(), 50, new RongIMClient.OperationCallback() {
//                    @Override
//                    public void onSuccess() {
//                        ToastUtil.showToast("成功");
//                    }
//
//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        ToastUtil.showToast("失败" + errorCode);
//                    }
//                });
            }
        }
        liveVideosInfo();
        GlideUtils.getInstances().loadUserRoundImg(AVStreamingActivity.this, ivIcon, ShareUtil.getInstance().get(Constants.USER_HEAD));
        tvName.setText(ShareUtil.getInstance().get(Constants.USER_NAME));
    }

    @Override
    public void initListener() {
        emojiBoard.setItemClickListener(new EmojiBoard.OnEmojiItemClickListener() {
            @Override
            public void onClick(String code) {
                if (code.equals("/DEL")) {
                    textEditor.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    textEditor.getText().insert(textEditor.getSelectionStart(), code);
                }
            }
        });
    }

    @OnClick({R.id.iv_title_back, R.id.iv_scan, R.id.iv_mute, R.id.iv_beauty, R.id.iv_user_list,
            R.id.emojiBtn, R.id.input_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                //结束直播
                liveVideosClose();
                break;
            case R.id.iv_scan:
                //切换摄像头
                if (isPictureStreaming()) {
                    return;
                }
                ivScan.removeCallbacks(mSwitcher);
                ivScan.postDelayed(mSwitcher, 100);
                break;
            case R.id.iv_mute:
                //静音
                mIsNeedMute = !mIsNeedMute;
                mMediaStreamingManager.mute(mIsNeedMute);
                if (mIsNeedMute) {
                    ivMute.setImageResource(R.mipmap.unmai);
                } else {
                    ivMute.setImageResource(R.mipmap.mai);
                }
                break;
            case R.id.iv_beauty:
                //美白
                BeautyDialog dialog = new BeautyDialog(AVStreamingActivity.this, new BeautyDialog.SeekBarChangeListener() {

                    @Override
                    public void onSeekBarChange(int progress) {
                        CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                        fbSetting.whiten = progress / 100.0f;
                        mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                    }

                    @Override
                    public void onMoPiSeekBarChange(int progress) {
                        //磨皮
                        CameraStreamingSetting.FaceBeautySetting fbSetting = mCameraStreamingSetting.getFaceBeautySetting();
                        fbSetting.beautyLevel = progress / 100.0f;
                        mMediaStreamingManager.updateFaceBeautySetting(fbSetting);
                    }
                });
                dialog.show();
                break;
            case R.id.iv_user_list:
                //房间成员列表
                Intent intent = new Intent(AVStreamingActivity.this, RoomUserListActivity.class);
                intent.putExtra("room_id", roomBean.getId());
                startActivity(intent);
                break;
            case R.id.emojiBtn:
                emojiBoard.setVisibility(emojiBoard.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.input_send:
                sendTextMessage();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        normalPause();
    }

    private void normalPause() {
        mIsReady = false;
        mShutterButtonPressed = false;
        mIsPictureStreaming = false;
        if (mHandler != null) {
            mHandler.getLooper().quit();
        }
        mMediaStreamingManager.pause();
    }

    @Override
    public void onBackPressed() {
        //结束直播
        liveVideosClose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
        ChatroomKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                ChatroomKit.removeEventHandler(handler);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ChatroomKit.removeEventHandler(handler);
                ToastUtil.showToast("退出聊天室失败! errorCode = " + errorCode);

                Log.i(TAG, "errorCode = " + errorCode);
            }
        });
    }

    @Override
    protected void initStreamingManager() {
        CameraPreviewFrameView cameraPreviewFrameView = (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        mMediaStreamingManager = new MediaStreamingManager(this, cameraPreviewFrameView, AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);
        mProfile.setPictureStreamingResourceId(R.drawable.pause_publish);

        MicrophoneStreamingSetting microphoneStreamingSetting = null;
        if (mAudioStereoEnable) {
            /**
             * Notice !!! {@link AudioFormat#CHANNEL_IN_STEREO} is NOT guaranteed to work on all devices.
             */
            microphoneStreamingSetting = new MicrophoneStreamingSetting();
            microphoneStreamingSetting.setChannelConfig(AudioFormat.CHANNEL_IN_STEREO);
        }
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mProfile);
        mMediaStreamingManager.setAutoRefreshOverlay(true);
        mMediaStreamingManager.setSurfaceTextureCallback(this);
        cameraPreviewFrameView.setListener(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
        mMediaStreamingManager.setStreamStatusCallback(this);
        mMediaStreamingManager.setAudioSourceCallback(this);
        mMediaStreamingManager.setStreamingStateListener(this);

//        mAudioMixer = mMediaStreamingManager.getAudioMixer();
//        mAudioMixer.setOnAudioMixListener(new OnAudioMixListener() {
//            @Override
//            public void onStatusChanged(MixStatus mixStatus) {
//                mMixToggleBtn.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(AVStreamingActivity.this, "mix finished", Toast.LENGTH_LONG).show();
//                        updateMixBtnText();
//                    }
//                });
//            }
//
//            @Override
//            public void onProgress(long l, long l1) {
//                mMixProgress.setProgress((int) l);
//                mMixProgress.setMax((int) l1);
//            }
//        });
//        mAudioFile = Cache.getAudioFile(this);
//        if (mAudioFile != null) {
//            try {
//                mAudioMixer.setFile(mAudioFile, true);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    protected boolean startStreaming() {
        return mMediaStreamingManager.startStreaming();
    }

    @Override
    protected boolean stopStreaming() {
        return mMediaStreamingManager.stopStreaming();
    }


    private class EncodingOrientationSwitcher implements Runnable {
        @Override
        public void run() {
            Log.i(TAG, "mIsEncOrientationPort:" + mIsEncOrientationPort);
            mOrientationChanged = true;
            mIsEncOrientationPort = !mIsEncOrientationPort;
            mProfile.setEncodingOrientation(mIsEncOrientationPort ? StreamingProfile.ENCODING_ORIENTATION.PORT : StreamingProfile.ENCODING_ORIENTATION.LAND);
            mMediaStreamingManager.setStreamingProfile(mProfile);
            stopStreamingInternal();
            setRequestedOrientation(mIsEncOrientationPort ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mMediaStreamingManager.notifyActivityOrientationChanged();
//            updateOrientationBtnText();
            Toast.makeText(AVStreamingActivity.this, Config.HINT_ENCODING_ORIENTATION_CHANGED,
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "EncodingOrientationSwitcher -");
        }
    }

    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();
            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            Log.i(TAG, "switchCamera:" + facingId);
            mMediaStreamingManager.switchCamera(facingId);

        }
    }

    private class ScreenShooter implements Runnable {
        @Override
        public void run() {
            final String fileName = "PLStreaming_" + System.currentTimeMillis() + ".jpg";
            mMediaStreamingManager.captureFrame(100, 100, new FrameCapturedCallback() {
                private Bitmap bitmap;

                @Override
                public void onFrameCaptured(Bitmap bmp) {
                    if (bmp == null) {
                        return;
                    }
                    bitmap = bmp;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                saveToSDCard(fileName, bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }).start();
                }
            });
        }
    }

    /**
     * switch picture during streaming
     */
    private class ImageSwitcher implements Runnable {
        @Override
        public void run() {
            if (!mIsPictureStreaming) {
                Log.d(TAG, "is not picture streaming!!!");
                return;
            }

            if (mTimes % 2 == 0) {
                mMediaStreamingManager.setPictureStreamingResourceId(R.mipmap.ic_launcher);
            } else {
                mMediaStreamingManager.setPictureStreamingResourceId(R.drawable.pause_publish);
            }
            mTimes++;
            if (mHandler != null && mIsPictureStreaming) {
                mHandler.postDelayed(this, 1000);
            }
        }
    }

    private boolean isPictureStreaming() {
        if (mIsPictureStreaming) {
            Toast.makeText(AVStreamingActivity.this, "is picture streaming, operation failed!", Toast.LENGTH_SHORT).show();
        }
        return mIsPictureStreaming;
    }

    private void togglePictureStreaming() {
        boolean isOK = mMediaStreamingManager.togglePictureStreaming();
        if (!isOK) {
            Toast.makeText(AVStreamingActivity.this, "toggle picture streaming failed!", Toast.LENGTH_SHORT).show();
            return;
        }

        mIsPictureStreaming = !mIsPictureStreaming;

        mTimes = 0;
        if (mIsPictureStreaming) {
            if (mImageSwitcher == null) {
                mImageSwitcher = new ImageSwitcher();
            }

            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
            mHandler.postDelayed(mImageSwitcher, 1000);
        } else {
            if (mHandler != null) {
                mHandler.getLooper().quit();
            }
        }
    }

    private void saveToSDCard(String filename, Bitmap bmp) throws IOException {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.PNG, 90, bos);
                bmp.recycle();
                bmp = null;
            } finally {
                if (bos != null) bos.close();
            }

            final String info = "Save frame to:" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filename;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AVStreamingActivity.this, info, Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private CameraStreamingSetting buildCameraStreamingSetting() {

        CameraStreamingSetting cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setContinuousFocusModeEnabled(true)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

        return cameraStreamingSetting;
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        /**
         * You should choose a suitable size to avoid image scale
         * eg: If streaming size is 1280 x 720, you should choose a camera preview size >= 1280 x 720
         */
        Camera.Size size = null;
        if (list != null) {
            StreamingProfile.VideoEncodingSize encodingSize = mProfile.getVideoEncodingSize(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);
            for (Camera.Size s : list) {
                if (s.width >= encodingSize.width && s.height >= encodingSize.height) {
                    size = s;
                    Log.d(TAG, "selected size :" + size.width + "x" + size.height);
                    break;
                }
            }
        }
        return size;
    }

    private class ViewTouchListener implements View.OnTouchListener {
        private float lastTouchRawX;
        private float lastTouchRawY;
        private boolean scale;
        private View mView;

        public ViewTouchListener(View view) {
            mView = view;
        }

        GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                ((RelativeLayout) findViewById(R.id.content)).removeView(mView);
                mMediaStreamingManager.removeOverlay(mView);
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return true;
            }
        };

        final GestureDetector gestureDetector = new GestureDetector(AVStreamingActivity.this, simpleOnGestureListener);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }

            int action = event.getAction();
            float touchRawX = event.getRawX();
            float touchRawY = event.getRawY();
            float touchX = event.getX();
            float touchY = event.getY();

            if (action == MotionEvent.ACTION_DOWN) {
                boolean xOK = touchX >= v.getWidth() * 3 / 4 && touchX <= v.getWidth();
                boolean yOK = touchY >= v.getHeight() * 2 / 4 && touchY <= v.getHeight();
                scale = xOK && yOK;
            }

            if (action == MotionEvent.ACTION_MOVE) {
                float deltaRawX = touchRawX - lastTouchRawX;
                float deltaRawY = touchRawY - lastTouchRawY;

                if (scale) {
                    // rotate
                    float centerX = v.getX() + (float) v.getWidth() / 2;
                    float centerY = v.getY() + (float) v.getHeight() / 2;
                    double angle = Math.atan2(touchRawY - centerY, touchRawX - centerX) * 180 / Math.PI;
                    v.setRotation((float) angle - 45);

                    // scale
                    float xx = (touchRawX >= centerX ? deltaRawX : -deltaRawX);
                    float yy = (touchRawY >= centerY ? deltaRawY : -deltaRawY);
                    float sf = (v.getScaleX() + xx / v.getWidth() + v.getScaleY() + yy / v.getHeight()) / 2;
                    v.setScaleX(sf);
                    v.setScaleY(sf);
                } else {
                    // translate
                    v.setTranslationX(v.getTranslationX() + deltaRawX);
                    v.setTranslationY(v.getTranslationY() + deltaRawY);
                }
            }

            if (action == MotionEvent.ACTION_UP) {
//                当 mMediaStreamingManager.setAutoRefreshOverlay(false) 时自动刷新关闭，建议在 UP 事件里进行手动刷新。
//                mMediaStreamingManager.refreshOverlay(v, false);
            }

            lastTouchRawX = touchRawX;
            lastTouchRawY = touchRawY;
            return true;
        }
    }

//    private void initButtonText() {
//        updateFBButtonText();
//        updateCameraSwitcherButtonText(mCameraStreamingSetting.getReqCameraId());
//        mCaptureFrameBtn.setText("Capture");
//        updateFBButtonText();
//        updateMuteButtonText();
//        updateOrientationBtnText();
//    }

//    private void initAudioMixerPanel() {
//        Button mixPanelBtn = (Button) findViewById(R.id.mix_panel_btn);
//        mixPanelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                View panel = findViewById(R.id.mix_panel);
//                panel.setVisibility(panel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//            }
//        });
//
//        mMixProgress = (SeekBar) findViewById(R.id.mix_progress);
//        mMixProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mAudioMixer != null) {
//                    mAudioMixer.seek(1.0f * seekBar.getProgress() / seekBar.getMax());
//                }
//            }
//        });
//
//        SeekBar mixVolume = (SeekBar) findViewById(R.id.mix_volume);
//        mixVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mAudioMixer != null) {
//                    mAudioMixer.setVolume(1.0f, 1.0f * seekBar.getProgress() / seekBar.getMax());
//                }
//            }
//        });
//
//        Button mixFileBtn = (Button) findViewById(R.id.mix_file_btn);
//        mixFileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                DialogProperties properties = new DialogProperties();
////                properties.selection_mode = DialogConfigs.SINGLE_MODE;
////                properties.selection_type = DialogConfigs.FILE_SELECT;
////                properties.root = new File(DialogConfigs.STORAGE_DIR);
////                properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
////                properties.extensions = new String[]{"mp3"};
////
////                FilePickerDialog dialog = new FilePickerDialog(AVStreamingActivity.this, properties);
////                dialog.setTitle("Select a File");
////                dialog.setDialogSelectionListener(new DialogSelectionListener() {
////                    @Override
////                    public void onSelectedFilePaths(String[] files) {
////                        String filePath = files[0];
////                        try {
////                            mAudioMixer.setFile(filePath, true);
////                            Cache.setAudioFile(AVStreamingActivity.this, filePath);
////                            Toast.makeText(AVStreamingActivity.this, "setup mix file " + filePath + " success. duration:" + mAudioMixer.getDuration(), Toast.LENGTH_LONG).show();
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                            Toast.makeText(AVStreamingActivity.this, "setup mix file " + filePath + " failed !!!", Toast.LENGTH_LONG).show();
////                        }
////                    }
////                });
////                dialog.show();
//            }
//        });
//
//        mMixToggleBtn = (Button) findViewById(R.id.mix_btn);
//        mMixToggleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mAudioMixer != null) {
//                    String text;
//                    if (mAudioMixer.isRunning()) {
//                        boolean s = mAudioMixer.pause();
//                        text = s ? "mixing pause success" : "mixing pause failed !!!";
//                    } else {
//                        boolean s = mAudioMixer.play();
//                        text = s ? "mixing play success" : "mixing play failed !!!";
//                    }
//                    Toast.makeText(AVStreamingActivity.this, text, Toast.LENGTH_LONG).show();
//
//                    updateMixBtnText();
//                }
//            }
//        });
//
//        Button mixStopBtn = (Button) findViewById(R.id.mix_stop_btn);
//        mixStopBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mAudioMixer != null) {
//                    boolean stopSuccess = mAudioMixer.stop();
//                    String text = stopSuccess ? "mixing stop success" : "mixing stop failed !!!";
//                    Toast.makeText(AVStreamingActivity.this, text, Toast.LENGTH_LONG).show();
//                    if (stopSuccess) {
//                        updateMixBtnText();
//                    }
//                }
//            }
//        });
//
//        Button playbackToggleBtn = (Button) findViewById(R.id.playback_btn);
//        playbackToggleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mIsPlayingback) {
//                    mMediaStreamingManager.stopPlayback();
//                } else {
//                    mMediaStreamingManager.startPlayback();
//                }
//                mIsPlayingback = !mIsPlayingback;
//            }
//        });
//
//        updateMixBtnText();
//    }
//
//    private void updateMixBtnText() {
//        if (mAudioMixer != null && mAudioMixer.isRunning()) {
//            mMixToggleBtn.setText("Pause");
//        } else {
//            mMixToggleBtn.setText("Play");
//        }
//    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        /**
         * general states are handled in the `StreamingBaseActivity`
         */
        super.onStateChanged(streamingState, extra);
        switch (streamingState) {
            case READY:
                mMaxZoom = mMediaStreamingManager.getMaxZoom();
                break;
            case SHUTDOWN:
                if (mOrientationChanged) {
                    mOrientationChanged = false;
                    startStreamingInternal();
                }
                break;
            case OPEN_CAMERA_FAIL:
                Log.e(TAG, "Open Camera Fail. id:" + extra);
                break;
            case CAMERA_SWITCHED:
                if (extra != null) {
                    Log.i(TAG, "current camera id:" + (Integer) extra);
                }
                Log.i(TAG, "camera switched");
                final int currentCamId = (Integer) extra;
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        updateCameraSwitcherButtonText(currentCamId);
                    }
                });
                break;
            case TORCH_INFO:
                if (extra != null) {
                    final boolean isSupportedTorch = (Boolean) extra;
                    Log.i(TAG, "isSupportedTorch=" + isSupportedTorch);
                    this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if (isSupportedTorch) {
//                                mTorchBtn.setVisibility(View.VISIBLE);
//                            } else {
//                                mTorchBtn.setVisibility(View.GONE);
//                            }
                        }
                    });
                }
                break;
        }
    }

    protected void setFocusAreaIndicator() {
//        if (mRotateLayout == null) {
//            mRotateLayout = (RotateLayout) findViewById(R.id.focus_indicator_rotate_layout);
//            mMediaStreamingManager.setFocusAreaIndicator(mRotateLayout,
//                    mRotateLayout.findViewById(R.id.focus_indicator));
//        }
    }

    private void setTorchEnabled(final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                String flashlight = enabled ? getString(R.string.flash_light_off) : getString(R.string.flash_light_on);
//                mTorchBtn.setText(flashlight);
            }
        });
    }

//    private void updateOrientationBtnText() {
//        if (mIsEncOrientationPort) {
//            mEncodingOrientationSwitcherBtn.setText("Land");
//        } else {
//            mEncodingOrientationSwitcherBtn.setText("Port");
//        }
//    }
//
//    private void updateFBButtonText() {
//        if (mFaceBeautyBtn != null) {
//            mFaceBeautyBtn.setText(mIsNeedFB ? "FB Off" : "FB On");
//        }
//    }
//
//    private void updateMuteButtonText() {
//        if (mMuteButton != null) {
//            mMuteButton.setText(mIsNeedMute ? "Unmute" : "Mute");
//        }
//    }
//
//    private void updateCameraSwitcherButtonText(int camId) {
//        if (mCameraSwitchBtn == null) {
//            return;
//        }
//        if (camId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            mCameraSwitchBtn.setText("Back");
//        } else {
//            mCameraSwitchBtn.setText("Front");
//        }
//    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i(TAG, "onSingleTapUp X:" + e.getX() + ",Y:" + e.getY());
        if (mIsReady) {
            setFocusAreaIndicator();
            mMediaStreamingManager.doSingleTapUp((int) e.getX(), (int) e.getY());
            return true;
        }
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        if (mIsReady && mMediaStreamingManager.isZoomSupported()) {
            mCurrentZoom = (int) (mMaxZoom * factor);
            mCurrentZoom = Math.min(mCurrentZoom, mMaxZoom);
            mCurrentZoom = Math.max(0, mCurrentZoom);
            Log.d(TAG, "zoom ongoing, scale: " + mCurrentZoom + ",factor:" + factor + ",maxZoom:" + mMaxZoom);
            mMediaStreamingManager.setZoomValue(mCurrentZoom);
        }
        return false;
    }

    @Override
    public void onSurfaceCreated() {
        Log.i(TAG, "onSurfaceCreated");
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged width:" + width + ",height:" + height);
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        Log.i(TAG, "onSurfaceDestroyed");
        /**
         * only used in custom beauty algorithm case
         */
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        /**
         * When using custom beauty algorithm, you should return a new texId from the SurfaceTexture.
         * newTexId should not equal with texId, Otherwise, there is no filter effect.
         */
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
        return newTexId;
    }

    @Override
    public boolean onPreviewFrame(byte[] bytes, int width, int height, int rotation, int fmt, long tsInNanoTime) {
        Log.i(TAG, "onPreviewFrame " + width + "x" + height + ",fmt:" + (fmt == PLFourCC.FOURCC_I420 ? "I420" : "NV21") + ",ts:" + tsInNanoTime + ",rotation:" + rotation);
        /**
         * When using custom beauty algorithm in sw encode mode, you should change the bytes array's values here
         * eg: byte[] beauties = readPixelsFromGPU();
         * System.arraycopy(beauties, 0, bytes, 0, bytes.length);
         */
        return true;
    }

    /**
     * 向当前聊天室发送消息。
     * </p>
     * <strong>注意：</strong>此函数为异步函数，发送结果将通过handler事件返回。
     *
     * @param messageContent 消息对象
     */
    public void sendMessage(final MessageContent messageContent) {


        Message msg = Message.obtain(currentRoomId, Conversation.ConversationType.CHATROOM, messageContent);

        RongIMClient.getInstance().sendMessage(msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                setData(messageContent);
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                ToastUtil.showToast("发消息失败");
            }
        });
    }

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosInfo() {
        DataManager.getInstance().liveVideosInfo(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                if (result != null && result.getData() != null) {
                    tvCount.setText(result.getData().getChatter_total() + "人");
                    if (result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        tvRoomNum.setText("房间号:" + result.getData().getRoom().getData().getRoom_name());
                    }
                }


            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, id);
    }

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosClose() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        DataManager.getInstance().liveVideosClose(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                Intent intent = new Intent(AVStreamingActivity.this, OnlineLiveFinishActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, map);
    }


    @Override
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case ChatroomKit.MESSAGE_ARRIVED:
            case ChatroomKit.MESSAGE_SENT: {
                MessageContent messageContent = ((Message) msg.obj).getContent();
                setData(messageContent);
                break;
            }

        }
        return false;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveVideoViewAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 发送文本内容
     */
    private void sendTextMessage() {
        if (!TextUtils.isEmpty(textEditor.getText().toString().trim())) {
            final TextMessage content = TextMessage.obtain(textEditor.getText().toString().trim());
            content.setUserInfo(getUserInfo());
            sendMessage(content);
            textEditor.setText("");
        } else {
            ToastUtil.showToast("请输入要发送的内容");
        }

    }

    private UserInfo getUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
        return new UserInfo(userId, nickName, RongHeadImg);
    }

    private void showGif(ChatRoomGift chatroomGift) {
        GiftSendModel model = new GiftSendModel(1);
        model.setGiftRes(R.mipmap.gift_ring);
        if (chatroomGift.getUserInfo() != null) {
            model.setNickname(chatroomGift.getUserInfo().getName());
            model.setUserAvatarRes(chatroomGift.getUserInfo().getPortraitUri().toString());
        }
        model.setSig("送出礼物");
        giftView.addGift(model);
    }

    /**
     * 接收消息,更新适配器
     */
    private void setData(MessageContent messageContent) {
        if (messageContent instanceof ChatRoomGift && ((ChatRoomGift) messageContent).getType() == 0) {
            //收到礼物
            showGif(((ChatRoomGift) messageContent));
        } else {
            mAdapter.addData(messageContent);
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

}
