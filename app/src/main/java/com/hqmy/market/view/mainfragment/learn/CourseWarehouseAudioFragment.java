package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;
import android.media.AudioManager;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;

public class CourseWarehouseAudioFragment extends BaseFragment {
    String TAG = "CourseWarehouseAudioFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    VideoBean videoBean;
    CourseWarehouseAudioAdapter mAdapter;
    PLMediaPlayer mMediaPlayer;
    BaseDownloadTask fileDownloader;
    private boolean isPlaying = false;

    public static CourseWarehouseAudioFragment newInstance(VideoBean videoBean) {
        CourseWarehouseAudioFragment fragment = new CourseWarehouseAudioFragment();
        fragment.videoBean = videoBean;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_warehouse_audio_fragment;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new CourseWarehouseAudioAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //点击音频播放
                String playUrl = Constants.WEB_IMG_URL_UPLOADS + mAdapter.getItem(position);
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && position == mAdapter.getCurrentPlay()) {
                    isPlaying = false;
                    stopAudio();
                } else {
                    isPlaying = true;
                    playAudio(playUrl);
                }
                mAdapter.setCurrentPlay(position);

            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String downUrl = Constants.WEB_IMG_URL_UPLOADS + mAdapter.getItem(position);
                downAudio(downUrl);
            }
        });
        initAudio();
    }

    @Override
    protected void initData() {
        if (videoBean != null && videoBean.getCourse_info() != null && videoBean.getCourse_info().getData() != null && videoBean.getCourse_info().getData().getAudio() != null) {
            mAdapter.setNewData(videoBean.getCourse_info().getData().getAudio());
        }
    }

    @Override
    protected void initListener() {

    }

    class CourseWarehouseAudioAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        private int currentPlay = -1;

        public CourseWarehouseAudioAdapter() {
            super(R.layout.course_ware_house_audio_item, null);
        }

        public int getCurrentPlay() {
            return this.currentPlay;
        }

        public void setCurrentPlay(int currentPlay) {
            this.currentPlay = currentPlay;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.addOnClickListener(R.id.btn_down);
            String subStr = item;
            if (!TextUtils.isEmpty(item) && item.indexOf("/") > 0 && item.indexOf(".") > 0) {
                subStr = item.substring(item.indexOf("/") + 1, item.indexOf(".") - 1);
            }
            helper.setText(R.id.tv_title, subStr);
            if (currentPlay == helper.getPosition() && isPlaying) {
                GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_yinpin), R.mipmap.play_pause);
            } else {
                GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_yinpin), R.mipmap.yinpin);
            }
        }
    }

    private void initAudio() {
        AVOptions mAVOptions = new AVOptions();
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mAVOptions.setInteger(AVOptions.KEY_START_POSITION, 0 * 1000);

        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (mMediaPlayer == null) {
            mMediaPlayer = new PLMediaPlayer(getActivity(), mAVOptions);
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnInfoListener(mOnInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mMediaPlayer.setWakeMode(getActivity(), PowerManager.PARTIAL_WAKE_LOCK);
        }

    }

    private PLOnPreparedListener mOnPreparedListener = new PLOnPreparedListener() {
        @Override
        public void onPrepared(int preparedTime) {
            Log.i(TAG, "On Prepared !");
            mMediaPlayer.start();
//            mIsStopped = false;
        }
    };

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
//                    mLoadingView.setVisibility(View.VISIBLE);
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
//                    mLoadingView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int percent) {
            Log.d(TAG, "onBufferingUpdate: " + percent + "%");
        }
    };

    /**
     * Listen the event of playing complete
     * For playing local file, it's called when reading the file EOF
     * For playing network stream, it's called when the buffered bytes played over
     * <p>
     * If setLooping(true) is called, the player will restart automatically
     * And ｀onCompletion｀ will not be called
     */
    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.d(TAG, "Play Completed !");
//            finish();
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
//                    Utils.showToastTips(PLAudioPlayerActivity.this, "IO Error !");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
//                    Utils.showToastTips(PLAudioPlayerActivity.this, "failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
//                    Utils.showToastTips(PLAudioPlayerActivity.this, "failed to seek !");
                    break;
                default:
//                    Utils.showToastTips(PLAudioPlayerActivity.this, "unknown error !");
                    break;
            }
//            finish();
            return true;
        }
    };

    private void playAudio(String url) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
//            mMediaPlayer.release();
        }
        try {
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
//            mMediaPlayer.release();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void downAudio(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        showLoadDialog();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/love";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String pathName = url.substring(url.lastIndexOf("/"), url.length());

        final String pathFile = path + pathName;
        if (new File(pathFile).exists()) {
            ToastUtil.showToast("该音频已经下载");
            return;
        }
        fileDownloader = FileDownloader.getImpl().create(url)
                .setPath(pathFile, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "progress" + soFarBytes + "totalBytes=" + totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        LogUtil.d("down", "error");
                        ToastUtil.showToast("下载失败");
                        dissLoadDialog();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        LogUtil.d("down", "connected");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "warn");
                        dissLoadDialog();
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        LogUtil.d("down", "completed");
                        dissLoadDialog();
                        ToastUtil.showToast("音频下载成功");
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        LogUtil.d("down", "warn");
                    }
                });
        fileDownloader.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        stopTelephonyListener();
        release();
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
    }

}
