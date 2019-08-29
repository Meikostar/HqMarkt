package com.hqmy.market.qiniu;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AudioSourceCallback;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

public abstract class StreamingBaseActivity extends BaseActivity implements
        StreamingSessionListener,
        StreamStatusCallback,
        StreamingStateChangedListener,
        AudioSourceCallback {
    private static final String TAG = "StreamingBaseActivity";

    public static final String PUBLIC_URL = "public_url";
    public static final String AUDIO_CHANNEL_STEREO = "AUDIO_CHANNEL_STEREO";
    public static final String TRANSFER_MODE_QUIC = "TRANSFER_MODE_QUIC";

    protected boolean mShutterButtonPressed = false;
    protected boolean mIsEncOrientationPort = true;
    protected boolean mIsReady;
    private String mStatusMsgContent;
    private String mLogContent = "\n";
//
//    protected TextView mLogTextView;
//    protected TextView mStatusTextView;
//    protected TextView mStatView;
//
//    protected Button mShutterButton;

    protected StreamingProfile mProfile = new StreamingProfile();

//    protected abstract void initView();
    protected abstract void initStreamingManager();
    protected abstract boolean startStreaming();
    protected abstract boolean stopStreaming();

    protected boolean mAudioStereoEnable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEncodingProfile();
        initView();

//        mLogTextView = (TextView) findViewById(R.id.log_info);
//        mStatusTextView = (TextView) findViewById(R.id.streamingStatus);
//        mStatView = (TextView) findViewById(R.id.stream_status);
//        mShutterButton = (Button) findViewById(R.id.toggleRecording_button);
//
//        mShutterButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mShutterButtonPressed) {
//                    stopStreamingInternal();
//                } else {
//                    startStreamingInternal();
//                }
//            }
//        });

        Intent intent = getIntent();
        boolean quicEnable = intent.getBooleanExtra(TRANSFER_MODE_QUIC, false);
        mProfile.setQuicEnable(quicEnable);
        String publicUrl = intent.getStringExtra(PUBLIC_URL);
        // publish url
        try {
            mProfile.setPublishUrl(publicUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mAudioStereoEnable = intent.getBooleanExtra(AUDIO_CHANNEL_STEREO, false);

        initStreamingManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mShutterButtonPressed = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStateChanged(StreamingState streamingState, Object extra) {
        Log.i(TAG, "StreamingState streamingState:" + streamingState + ",extra:" + extra);
        switch (streamingState) {
            case PREPARING:
                mStatusMsgContent = getString(R.string.string_state_preparing);
                break;
            case READY:
                mIsReady = true;
                mStatusMsgContent = getString(R.string.string_state_ready);
                /**
                 * Start streaming when `READY`
                 */
                startStreamingInternal();
                break;
            case CONNECTING:
                mStatusMsgContent = getString(R.string.string_state_connecting);
                break;
            case STREAMING:
                mStatusMsgContent = getString(R.string.string_state_streaming);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(true);
                break;
            case SHUTDOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                setShutterButtonPressed(false);
                break;
            case IOERROR:
                /**
                 * Network-connection is unavailable when `startStreaming`.
                 * You can `startStreaming` later or just finish the streaming
                 */
                mLogContent += "IOERROR\n";
                mStatusMsgContent = getString(R.string.string_state_ready);
                setShutterButtonEnabled(true);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startStreamingInternal();
                    }
                }, 2000);
                break;
            case DISCONNECTED:
                /**
                 * Network-connection is broken when streaming
                 * You can do reconnecting in `onRestartStreamingHandled`
                 */
                mLogContent += "DISCONNECTED\n";
                break;
            case UNKNOWN:
                mStatusMsgContent = getString(R.string.string_state_ready);
                break;
            case SENDING_BUFFER_EMPTY:
                break;
            case SENDING_BUFFER_FULL:
                break;
            case AUDIO_RECORDING_FAIL:
                break;
            case INVALID_STREAMING_URL:
                Log.e(TAG, "Invalid streaming url:" + extra);
                break;
            case UNAUTHORIZED_STREAMING_URL:
                Log.e(TAG, "Unauthorized streaming url:" + extra);
                mLogContent += "Unauthorized Url\n";
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if (mLogTextView != null) {
//                    mLogTextView.setText(mLogContent);
//                }
//                mStatusTextView.setText(mStatusMsgContent);
            }
        });
    }

    @Override
    public boolean onRecordAudioFailedHandled(int code) {
        Log.i(TAG, "onRecordAudioFailedHandled");
        return false;
    }

    @Override
    public boolean onRestartStreamingHandled(int code) {
        /**
         * When the network-connection is broken, StreamingState#DISCONNECTED will notified first,
         * and then invoked this method if the environment of restart streaming is ready.
         *
         * @return true means you handled the event; otherwise, given up and then StreamingState#SHUTDOWN
         * will be notified.
         */
        Log.i(TAG, "onRestartStreamingHandled");
        startStreamingInternal();
        return true;
    }

    @Override
    public void onAudioSourceAvailable(ByteBuffer byteBuffer, int i, long l, boolean b) {
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        return null;
    }

    @Override
    public int onPreviewFpsSelected(List<int[]> list) {
        return -1;
    }

    protected void setShutterButtonEnabled(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mShutterButton.setFocusable(enable);
//                mShutterButton.setClickable(enable);
//                mShutterButton.setEnabled(enable);
            }
        });
    }

    protected void setShutterButtonPressed(final boolean pressed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mShutterButtonPressed = pressed;
//                mShutterButton.setPressed(pressed);
            }
        });
    }

    @Override
    public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mStatView.setText("bitrate:" + streamStatus.totalAVBitrate / 1024 + " kbps"
//                        + "\naudio:" + streamStatus.audioFps + " fps"
//                        + "\nvideo:" + streamStatus.videoFps + " fps");
            }
        });
    }

    protected void startStreamingInternal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setShutterButtonEnabled(false);
                boolean res = startStreaming();
                mShutterButtonPressed = true;
                if (!res) {
                    mShutterButtonPressed = false;
                    setShutterButtonEnabled(true);
                }
                setShutterButtonPressed(mShutterButtonPressed);
            }
        }).start();
    }

    protected void stopStreamingInternal() {
        if (mShutterButtonPressed) {
            // disable the shutter button before stopStreaming
            setShutterButtonEnabled(false);
            boolean res = stopStreaming();
            if (!res) {
                mShutterButtonPressed = true;
                setShutterButtonEnabled(true);
            }
            setShutterButtonPressed(mShutterButtonPressed);
        }
    }

    private void initEncodingProfile() {

            mProfile = new StreamingProfile();
            mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH1)
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
                    .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_480)
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY);
//                    .setPublishUrl(publishURLFromServer);  // You can invoke this before startStreaming, but not in initialization phase.

        }

    /**
     * If you want to use a custom DNS server, config it
     * Not required.
     */
    private static DnsManager getMyDnsManager() {
        IResolver r0 = null;
        IResolver r1 = new DnspodFree();
        IResolver r2 = AndroidDnsServer.defaultResolver();
        try {
            r0 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }
}
