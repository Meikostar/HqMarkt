package com.hqmy.market.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.bean.GiftBean;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.qiniu.MediaController;
import com.hqmy.market.qiniu.adapter.DanmuAdapter;
import com.hqmy.market.qiniu.adapter.DanmuEntity;
import com.hqmy.market.qiniu.adapter.LiveVideoViewAdapter;
import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.hqmy.market.qiniu.chatroom.gift.GiftSendModel;
import com.hqmy.market.qiniu.chatroom.gift.GiftView;
import com.hqmy.market.qiniu.chatroom.message.ChatroomBarrage;
import com.hqmy.market.qiniu.chatroom.message.ChatroomEnd;
import com.hqmy.market.qiniu.chatroom.message.ChatroomGift;
import com.hqmy.market.qiniu.chatroom.message.ChatroomJifen;
import com.hqmy.market.qiniu.chatroom.message.ChatroomLike;
import com.hqmy.market.qiniu.chatroom.message.ChatroomUser;
import com.hqmy.market.qiniu.chatroom.message.ChatroomUserQuit;
import com.hqmy.market.qiniu.chatroom.message.ChatroomWelcome;
import com.hqmy.market.qiniu.chatroom.messageview.HeartLayout;
import com.hqmy.market.qiniu.live.ui.InputPanel;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.SettingPasswordActivity;
import com.hqmy.market.view.adapter.LiveProductItemAdapter;
import com.hqmy.market.view.mainfragment.community.TopicDetailActivity;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.PhotoPopupWindow;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import com.hqmy.market.view.widgets.dialog.GifListDialog;
import com.hqmy.market.view.widgets.dialog.InputPasswordDialog;
import com.hqmy.market.view.widgets.dialog.PointAmountDialog;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;
import com.orzangleli.xdanmuku.DanmuContainerView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnAudioFrameListener;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoView;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * This is a demo activity of PLVideoView
 * 直播播放
 */
public class LiveVideoViewActivity extends BaseActivity implements Handler.Callback {
    public static final  String SDCARD_DIR        = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final  String DEFAULT_CACHE_DIR = SDCARD_DIR + "/PLDroidPlayer";
    private static final String TAG               = LiveVideoViewActivity.class.getSimpleName();
    @BindView(R.id.VideoView)
    PLVideoView mVideoView;
    @BindView(R.id.iv_icon)
    ImageView   iv_icon;
    @BindView(R.id.iv_point_amount)
    ImageView   iv_point_amount;
    @BindView(R.id.iv_tanmu)
    ImageView   iv_tanmu;

    @BindView(R.id.tv_name)
    TextView     tv_name;
    @BindView(R.id.tv_cout)
    TextView     tv_cout;
    @BindView(R.id.tv_count)
    TextView     tv_count;
    @BindView(R.id.tv_room_num)
    TextView     tv_room_num;
    @BindView(R.id.iv_mute)
    ImageView    ivMute;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_shop)
    RecyclerView recyclerShop;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;
    @BindView(R.id.fl_shop)
    FrameLayout  fl_shop;

    @BindView(R.id.view)
    View     mView;
    @BindView(R.id.tv_size)
    TextView tv_size;


    private LiveVideoViewAdapter   mAdapter;
    private LiveProductItemAdapter mProductAdapter;
    @BindView(R.id.giftView)
    GiftView           giftView;
    @BindView(R.id.iv_attention)
    ImageView          iv_attention;
    @BindView(R.id.heart_layout)
    HeartLayout        heartLayout;
    @BindView(R.id.input_panel)
    InputPanel         inputPanel;
    @BindView(R.id.rl_bottom)
    LinearLayout       rl_bottom;
    @BindView(R.id.danmuContainerView)
    DanmuContainerView danmuContainerView;

    private int     mDisplayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT;
    private boolean mIsLiveStreaming;
    //聊天室ID
    private String  roomId, videoId;
    private List<GiftBean>    giftBeans;
    private String            authorPhone;
    private String            authorId;
    private boolean           isMute  = false;//是否静音播放
    private GifListDialog     gifListDialog;
    private PointAmountDialog pointAmountDialog;
    private Handler           handler = new Handler(this);
    private boolean           isAttention;
    long currentTime = 0;
    int  clickCount  = 0;
    private Random random = new Random();
    private String chatterTotal;

    @Override
    public int getLayoutId() {
        setKeepOn();
        return R.layout.activity_alive_video_view;
    }

    private String userId;

    @Override
    public void initView() {
        initRecyclerView();
        String videoPath = getIntent().getStringExtra("videoPath");
        userId = getIntent().getStringExtra("userId");
        mIsLiveStreaming = getIntent().getIntExtra("liveStreaming", 1) == 1;

        //设置全屏
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        View mCoverView = findViewById(R.id.CoverView);
        mVideoView.setCoverView(mCoverView);

        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);
        // options.setString(AVOptions.KEY_DNS_SERVER, "127.0.0.1");
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, DEFAULT_CACHE_DIR);
        }
        boolean vcallback = getIntent().getBooleanExtra("video-data-callback", false);
        if (vcallback) {
            options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);
        }
        boolean acallback = getIntent().getBooleanExtra("audio-data-callback", false);
        if (acallback) {
            options.setInteger(AVOptions.KEY_AUDIO_DATA_CALLBACK, 1);
        }
        if (!mIsLiveStreaming) {
            int startPos = getIntent().getIntExtra("start-pos", 0);
            options.setInteger(AVOptions.KEY_START_POSITION, startPos * 1000);
        }
        mVideoView.setAVOptions(options);

        // Set some listeners
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnVideoFrameListener(mOnVideoFrameListener);
        mVideoView.setOnAudioFrameListener(mOnAudioFrameListener);
        mVideoView.setVideoPath(videoPath);
        mVideoView.setLooping(getIntent().getBooleanExtra("loop", false));

        ChatroomKit.addEventHandler(handler);
        giftView.setViewCount(2);
        giftView.init();

        danmuContainerView.setAdapter(new DanmuAdapter(this));
    }

    @Override
    public void initData() {
        roomId = getIntent().getStringExtra("roomId");
        videoId = getIntent().getStringExtra("videoId");
        //加入聊天室
        liveVideosInfo();
        getLiveGift();
        joinChatRoom();
    }

    private View view;

    @Override
    public void initListener() {


        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveVideoViewActivity.this, HostManActivity.class);

                intent.putExtra("userId", userId);
                intent.putExtra("videoId", videoId);

                startActivity(intent);
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
                    iv_tanmu.setVisibility(View.VISIBLE);
                    rl_bottom.setVisibility(View.VISIBLE);
                }
            }
        });
        fl_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (products == null) {
                    ToastUtil.showToast("暂无商品");
                    return;
                }
                if (ll_bg.getVisibility() == View.VISIBLE) {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_hide));
                    ll_bg.setVisibility(View.GONE);
                } else {
                    //                    ll_bg.startAnimation(AnimationUtils.loadAnimation(LiveVideoViewActivity.this, R.anim.popw_show));
                    ll_bg.setVisibility(View.VISIBLE);
                    iv_tanmu.setVisibility(View.GONE);
                    rl_bottom.setVisibility(View.GONE);
                }
            }
        });

        inputPanel.setPanelListener(new InputPanel.InputPanelListener() {
            @Override
            public void onSendClick(String text, int type) {
                if (type == InputPanel.TYPE_TEXTMESSAGE) {
                    sendTextMessage(text);
                } else if (type == InputPanel.TYPE_BARRAGE) {
                    sendDanmuMessage(text);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //        mMediaController.getWindow().dismiss();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        ChatroomKit.quitChatRoom(new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                ChatroomKit.removeEventHandler(handler);
                ChatroomUserQuit userQuit = new ChatroomUserQuit();
                userQuit.setId(getUserId());
                userQuit.setName(getUserName());
                userQuit.setUrl(getUserUrl());
                ChatroomKit.sendMessage(userQuit);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ChatroomKit.removeEventHandler(handler);
                Log.i(TAG, "errorCode = " + errorCode);
            }
        });
    }

    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (mVideoView.getDisplayAspectRatio()) {
            case PLVideoView.ASPECT_RATIO_ORIGIN:
                //                Utils.showToastTips(this, "Origin mode");
                break;
            case PLVideoView.ASPECT_RATIO_FIT_PARENT:
                //                Utils.showToastTips(this, "Fit parent !");
                break;
            case PLVideoView.ASPECT_RATIO_PAVED_PARENT:
                //                Utils.showToastTips(this, "Paved parent !");
                break;
            case PLVideoView.ASPECT_RATIO_16_9:
                //                Utils.showToastTips(this, "16 : 9 !");
                break;
            case PLVideoView.ASPECT_RATIO_4_3:
                //                Utils.showToastTips(this, "4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "first video render time: " + extra + "ms");
                    Log.i(TAG, "Response: " + mVideoView.getResponseInfo());
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    //                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_LOOP_DONE:
                    Log.i(TAG, "Loop done");
                    break;
                case PLOnInfoListener.MEDIA_INFO_CACHE_DOWN:
                    Log.i(TAG, "Cache done");
                    break;
                case PLOnInfoListener.MEDIA_INFO_STATE_CHANGED_PAUSED:
                    Log.i(TAG, "State paused");
                    break;
                case PLOnInfoListener.MEDIA_INFO_STATE_CHANGED_RELEASED:
                    Log.i(TAG, "State released");
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    Log.e(TAG, "IO Error!");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to seek !");
                    return true;
                case PLOnErrorListener.ERROR_CODE_CACHE_FAILED:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "failed to cache url !");
                    break;
                default:
                    //                    Utils.showToastTips(PLVideoViewActivity.this, "unknown error !");
                    break;
            }
            //            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.i(TAG, "Play Completed !");
            //            Utils.showToastTips(PLVideoViewActivity.this, "Play Completed !");
            if (!mIsLiveStreaming) {
                //                mMediaController.refreshProgress();
            }
            //finish();
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private PLOnVideoFrameListener mOnVideoFrameListener = new PLOnVideoFrameListener() {
        @Override
        public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
            Log.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", " + format + ", " + ts);
            if (format == PLOnVideoFrameListener.VIDEO_FORMAT_SEI && bytesToHex(Arrays.copyOfRange(data, 19, 23)).equals("74733634")) {
                // If the RTMP stream is from Qiniu
                // Add &addtssei=true to the end of URL to enable SEI timestamp.
                // Format of the byte array:
                // 0:       SEI TYPE                    This is part of h.264 standard.
                // 1:       unregistered user data      This is part of h.264 standard.
                // 2:       payload length              This is part of h.264 standard.
                // 3-18:    uuid                        This is part of h.264 standard.
                // 19-22:   ts64                        Magic string to mark this stream is from Qiniu
                // 23-30:   timestamp                   The timestamp
                // 31:      0x80                        Magic hex in ffmpeg
                Log.i(TAG, " timestamp: " + Long.valueOf(bytesToHex(Arrays.copyOfRange(data, 23, 31)), 16));
            }
        }
    };

    private PLOnAudioFrameListener mOnAudioFrameListener = new PLOnAudioFrameListener() {
        @Override
        public void onAudioFrameAvailable(byte[] data, int size, int samplerate, int channels, int datawidth, long ts) {
            Log.i(TAG, "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);
        }
    };

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerShop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new LiveVideoViewAdapter();
        mProductAdapter = new LiveProductItemAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerShop.setAdapter(mProductAdapter);
        mProductAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_bg:
                        Intent intent = new Intent(LiveVideoViewActivity.this, BrandShopDetailActivity.class);
                        intent.putExtra("id", products.get(position).getId());
                        startActivity(intent);
                        break;
                    case R.id.img:

                        break;
                }
            }
        });
        //        chatListAdapter = new ChatListAdapter(this);
        //        chatListView.setAdapter(chatListAdapter);
    }

    private List<MyOrderItemDto> products;

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosInfo() {
        showLoadDialog();
        DataManager.getInstance().liveVideosInfo(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    chatterTotal = result.getData().getChatter_total();
                    tv_count.setText(chatterTotal + "人");
                    if (result.getData().getUser() != null && result.getData().getUser().getData() != null) {
                        GlideUtils.getInstances().loadRoundImg(LiveVideoViewActivity.this, iv_icon, Constants.WEB_IMG_URL_UPLOADS + result.getData().getUser().getData().getAvatar(), R.drawable.moren_ren);
                        authorPhone = result.getData().getUser().getData().getPhone();
                        authorId = result.getData().getUser().getData().getId();
                        if (TextUtils.isEmpty(result.getData().getUser().getData().getName())) {
                            tv_name.setText(authorPhone);
                        } else {
                            tv_name.setText(result.getData().getUser().getData().getName());
                        }
                        if (result.getData().products != null && result.getData().products.size() > 0) {
                            products = result.getData().products;
                            mProductAdapter.setNewData(products);
                            tv_size.setText("全部宝贝" + products.size());
                            tv_cout.setText("" + products.size());
                        }
                        //                        isAttention = result.getData().getUser().getData().getFollowing();
                        //                        if (isAttention) {
                        //                            iv_attention.setImageResource(R.mipmap.aleady_guanzhu);
                        //                        } else {
                        //                            iv_attention.setImageResource(R.mipmap.guanzhu);
                        //                        }
                    }
                    if (result.getData().getRoom() != null && result.getData().getRoom().getData() != null) {
                        tv_room_num.setText("房间号:" + result.getData().getRoom().getData().getRoom_name());
                    }
                }


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, videoId);
        DataManager.getInstance().liveApply(new DefaultSingleObserver<HttpResult<VideoLiveBean>>() {
            @Override
            public void onSuccess(HttpResult<VideoLiveBean> result) {
//                dissLoadDialog();
                if (result != null && result.getLive_apply() != null&& result.getLive_apply().getUser() != null) {
                                            ;
                                            if (result.getLive_apply().getUser().is_followed!=null) {
                                                iv_attention.setImageResource(R.mipmap.aleady_guanzhu);
                                            } else {
                                                iv_attention.setImageResource(R.mipmap.guanzhu);
                                            }
                }


            }

            @Override
            public void onError(Throwable throwable) {
//                dissLoadDialog();

            }
        }, userId);


    }

    private void getLiveGift() {
        DataManager.getInstance().getLiveGift(new DefaultSingleObserver<HttpResult<List<GiftBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<GiftBean>> result) {
                giftBeans = result.getData();
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void setKeepOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void liveRewardScore(String total, String password) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pay_password", password);
        map.put("type", "score");
        map.put("to", authorPhone);
        map.put("total", total);
        DataManager.getInstance().liveRewardScore(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                if (pointAmountDialog != null) {
                    pointAmountDialog.dismiss();
                }
                //                ToastUtil.showToast("支付成功");
                sendScoreMessage(total);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    if (pointAmountDialog != null) {
                        pointAmountDialog.dismiss();
                    }
                    ToastUtil.showToast("支付成功");
                    sendScoreMessage(total);
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }

    private void liveReward(GiftBean giftBean) {
        HashMap<String, String> map = new HashMap<>();
        map.put("gift_id", giftBean.getId());
        map.put("to", authorPhone);
        DataManager.getInstance().liveReward(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                if (gifListDialog != null) {
                    gifListDialog.dismiss();
                }
                //                ToastUtil.showToast("支付成功");
                sendGifMessage(giftBean);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    if (gifListDialog != null) {
                        gifListDialog.dismiss();
                    }
                    //                    ToastUtil.showToast("支付成功");
                    sendGifMessage(giftBean);
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }

    /**
     * 加入聊天室
     */
    private void joinChatRoom() {
        ChatroomKit.joinChatRoom(roomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                ChatroomWelcome welcomeMessage = new ChatroomWelcome();
                welcomeMessage.setId(getUserId());
                welcomeMessage.setName(getUserName());
                welcomeMessage.setUrl(getUserUrl());
                ChatroomKit.sendMessage(welcomeMessage);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                ToastUtil.showToast("加入聊天室失败");
                finish();
            }
        });
    }

    private void checkWallet(String string, int type, GiftBean giftBean) {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                if (balanceDto != null && balanceDto.isPay_password()) {
                    if (type != 1) {
                        //送礼物
                        liveReward(giftBean);
                        return;
                    }
                    new InputPasswordDialog(LiveVideoViewActivity.this, new InputPasswordDialog.InputPasswordListener() {
                        @Override
                        public void callbackPassword(String password) {
                            //打赏积分
                            liveRewardScore(string, password); //自定义积分

                        }
                    }).show();
                } else {
                    ToastUtil.toast("请先设置支付密码");
                    gotoActivity(SettingPasswordActivity.class);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    /**
     * 发送文本内容
     */
    private void sendTextMessage(String text) {
        if (!TextUtils.isEmpty(text)) {
            final TextMessage content = TextMessage.obtain(text);
            content.setUserInfo(getUserInfo());
            ChatroomKit.sendMessage(content);

        } else {
            ToastUtil.showToast("请输入要发送的内容");
        }
    }

    /**
     * 打赏积分
     */
    private void sendScoreMessage(String score) {

        ChatroomJifen jifen = new ChatroomJifen();
        jifen.setName(getUserName());
        jifen.setUrl(getUserUrl());
        jifen.setJifen(score);
        ChatroomKit.sendMessage(jifen);
    }

    /**
     * 发送礼物类型
     */
    private void sendGifMessage(GiftBean giftBean) {
        ChatroomGift gift = new ChatroomGift();
        gift.setId(giftBean.getId());
        gift.setName(getUserName());
        gift.setUrl(getUserUrl());
        gift.setGift_name(giftBean.getGift_name());
        gift.setGiftURL(Constants.WEB_IMG_URL_UPLOADS + giftBean.getThumb());
        gift.setG_id(giftBean.getId());
        ChatroomKit.sendMessage(gift);
    }

    /**
     * 点赞
     */
    private void sendZanMessage() {
        ChatroomLike likeMessage = new ChatroomLike();
        likeMessage.setCounts(clickCount);
        likeMessage.setUrl(getUserUrl());
        likeMessage.setName(getUserName());
        //        likeMessage.setId(getUserId());
        ChatroomKit.sendMessage(likeMessage);

    }

    /**
     * 弹幕
     */
    private void sendDanmuMessage(String text) {
        ChatroomBarrage barrage = new ChatroomBarrage();
        barrage.setContent(text);
        barrage.setType(0);
        barrage.setName(getUserName());
        barrage.setUrl(getUserUrl());
        ChatroomKit.sendMessage(barrage);
    }

    private UserInfo getUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
        return new UserInfo(userId, nickName, RongHeadImg);
    }

    private String getUserId() {
        return ShareUtil.getInstance().getString(Constants.USER_ID, "");
    }

    private String getUserName() {
        return ShareUtil.getInstance().getString(Constants.USER_NAME, "");
    }

    private String getUserUrl() {
        return Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null);
    }

    private void showGif(ChatroomGift chatroomGift) {
        GiftSendModel model = new GiftSendModel(1);

        switch (chatroomGift.getG_id()) {
            case "1":
                model.setGiftRes(R.mipmap.icon_gift_huoguo);
                break;
            case "2":
                model.setGiftRes(R.mipmap.icon_gift_dalibao);
                break;
            case "3":
                model.setGiftRes(R.mipmap.icon_gift_dangao);
                break;
            case "4":
                model.setGiftRes(R.mipmap.icon_gift_xianhua);
                break;
            case "5":
                model.setGiftRes(R.mipmap.icon_gift_baoshi);
                break;
            case "6":
                model.setGiftRes(R.mipmap.icon_gift_huangguan);
                break;
            case "7":
                model.setGiftRes(R.mipmap.icon_gift_liushengji);
                break;
            case "8":
                model.setGiftRes(R.mipmap.icon_gift_aixin);
                break;
            default:
                model.setGiftRes(R.mipmap.gift_ring);
                break;

        }

        //        if (chatroomGift.getUserInfo() != null) {
        //            model.setNickname(chatroomGift.getUserInfo().getName());
        //            model.setSig("送出礼物");
        //            model.setUserAvatarRes(chatroomGift.getUserInfo().getPortraitUri().toString());
        //        }
        model.setNickname(chatroomGift.getName());
        model.setSig(chatroomGift.getGift_name());
        //         model.setUserAvatarRes();
        giftView.addGift(model);
    }

    /**
     * 接收消息,更新适配器
     */
    private void setData(MessageContent messageContent) {
        if (messageContent instanceof ChatroomBarrage) {
            ChatroomBarrage barrage = (ChatroomBarrage) messageContent;
            DanmuEntity danmuEntity = new DanmuEntity();
            danmuEntity.setContent(barrage.getContent());
            danmuEntity.setName(barrage.getName());
            danmuEntity.setUrl(barrage.getUrl());
            danmuEntity.setType(barrage.getType());
            danmuContainerView.addDanmu(danmuEntity);
        } else if (messageContent instanceof ChatroomEnd) {
            //主播结束直播
            ConfirmDialog dialog = new ConfirmDialog(this);
            dialog.setTitle("温馨提示");
            dialog.setMessage("主播已离开");
            dialog.setCancelable(false);
            dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {

                @Override
                public void onYesClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (messageContent instanceof ChatroomUser) {
            //房间人数
            ChatroomUser chatroomUser = (ChatroomUser) messageContent;
            tv_count.setText(chatroomUser.getExtra() + "人");
        } else {
            if (messageContent instanceof ChatroomGift && ((ChatroomGift) messageContent).getType() == 0) {
                //收到礼物
                showGif(((ChatroomGift) messageContent));
            }
            mAdapter.addData(messageContent);
            recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
        }
    }

    private void postAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("关注成功");
                isAttention = true;
                iv_attention.setImageResource(R.mipmap.aleady_guanzhu);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isAttention = true;
                    iv_attention.setImageResource(R.mipmap.aleady_guanzhu);
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("id", authorId);
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("取消关注成功");
                isAttention = false;
                iv_attention.setImageResource(R.mipmap.guanzhu);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isAttention = false;
                    iv_attention.setImageResource(R.mipmap.guanzhu);
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back, R.id.iv_gif, R.id.iv_mute,
            R.id.iv_zan, R.id.iv_attention, R.id.iv_share, R.id.input_editor,
            R.id.iv_tanmu, R.id.rl_root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            //            case R.id.iv_point_amount:

            //                pointAmountDialog = new PointAmountDialog(LiveVideoViewActivity.this);
            //                pointAmountDialog.setYesOnclickListener1("去支付", new PointAmountDialog.OnYesClickListener1() {
            //                    @Override
            //                    public void onYesClick(String total, String inputAmount) {
            //                        String mTotal = total;
            //                        if (!TextUtils.isEmpty(inputAmount) && Double.valueOf(inputAmount) > 0) {
            //                            mTotal = inputAmount; //自定义积分
            //                        }
            //                        checkWallet(mTotal, 1, null);
            //                    }
            //                });
            //                pointAmountDialog.setCancleClickListener("取消", new BaseDialog.OnCloseClickListener() {
            //
            //                    @Override
            //                    public void onCloseClick() {
            //                        pointAmountDialog.dismiss();
            //                    }
            //                });
            //                pointAmountDialog.show();
            //                break;
            case R.id.iv_gif:
                gifListDialog = new GifListDialog(LiveVideoViewActivity.this, giftBeans);
                gifListDialog.setYesOnclickListener1("赠送", new GifListDialog.OnYesClickListener1() {
                    @Override
                    public void onYesClick(GiftBean giftBean) {
                        checkWallet("", 2, giftBean);

                    }
                });
                gifListDialog.show();
                break;
            case R.id.iv_mute:
                //声音切换
                isMute = !isMute;
                if (isMute) {
                    ivMute.setImageResource(R.mipmap.shengyin1);
                    mVideoView.setVolume(0f, 0f);
                } else {
                    ivMute.setImageResource(R.mipmap.shengyin2);
                    mVideoView.setVolume(1f, 1f);
                }

                break;
            case R.id.iv_zan:
                //点赞
                heartLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        heartLayout.addHeart(rgb);
                    }
                });
                clickCount++;
                currentTime = System.currentTimeMillis();
                checkAfter(currentTime);
                //                sendZanMessage();
                break;
            case R.id.iv_attention:
                if (isAttention) {
                    deleteAttention();
                } else {
                    postAttention();
                }
                break;
            case R.id.iv_share:
                //分享
                ShareModeDialog dialog = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                    @Override
                    public void sureItem(int position) {
                        boolean isTimelineCb = false;
                        //http://ax.jmlax.com/api/package/user/invitation_img?user_id=14
                        String url = Constants.BASE_URL + "api/package/user/invitation_img?user_id=" + ShareUtil.getInstance().getString(Constants.USER_ID, "");
                        String title = "我的推广码";
                        if (position == ShareModeDialog.SHARE_PYQ) {
                            isTimelineCb = true;
                        }
                        ShareUtil.sendToWeaChat(LiveVideoViewActivity.this, isTimelineCb, title, url);
                    }
                });
                dialog.show();
                break;
            case R.id.input_editor:
                //说点什么吧
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.requestTextFocus();
                inputPanel.setType(InputPanel.TYPE_TEXTMESSAGE);
                break;
            case R.id.iv_tanmu:
                //弹幕
                inputPanel.setVisibility(View.VISIBLE);
                inputPanel.requestTextFocus();
                inputPanel.setType(InputPanel.TYPE_BARRAGE);
                break;
            case R.id.rl_root:
                inputPanel.setVisibility(View.GONE);
                inputPanel.hideKeyboard();
                break;
        }

    }

    //500毫秒后做检查，如果没有继续点击了，发消息
    public void checkAfter(final long lastTime) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (lastTime == currentTime) {
                    sendZanMessage();
                    clickCount = 0;
                }
            }
        }, 500);
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
}
