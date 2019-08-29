package com.hqmy.market.rong.widget;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.rong.message.myimagechat.MyImageMessage;
import org.json.JSONObject;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.cs.CustomServiceManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by Lenovo on 2019/2/26.
 */
public class ConversationFragmentEx extends ConversationFragment {
    public static final String TAG = ConversationFragmentEx.class.getSimpleName();
    public  RongExtension rongExtension;
    private ListView      listView;
    public  LinearLayout  mMainBar;
    private EditText      mEditText;
    private ImageView     mVoiceToggle, mPluginToggle, mEmoticonToggle;
    private ViewGroup mPluginLayout;
    public  View mVoiceInputToggle;//按住说话
    private String mTargetId = "";
    private boolean            isReadPluginMessage;//是否是阅后即焚
    private MessageListAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongIMClient.getInstance().setCustomServiceHumanEvaluateListener(new CustomServiceManager.OnHumanEvaluateListener() {
            @Override
            public void onHumanEvaluate(JSONObject evaluateObject) {
                JSONObject jsonObject = evaluateObject;
                LogUtil.i(TAG, "--jsonObject=" + jsonObject);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        rongExtension = v.findViewById(io.rong.imkit.R.id.rc_extension);
        View messageListView = findViewById(v, io.rong.imkit.R.id.rc_layout_msg_list);
        listView = findViewById(messageListView, io.rong.imkit.R.id.rc_list);
        mListAdapter = this.onResolveAdapter(this.getActivity());
        initView(v);
        initListener();
        return v;
    }

    private void initView(View view) {
        mMainBar = findViewById(view, io.rong.imkit.R.id.ext_main_bar);
        mEditText = findViewById(view, io.rong.imkit.R.id.rc_edit_text);
        mVoiceToggle = findViewById(view, io.rong.imkit.R.id.rc_voice_toggle);
        mPluginToggle = findViewById(view, io.rong.imkit.R.id.rc_plugin_toggle);
        mPluginLayout = findViewById(view, io.rong.imkit.R.id.rc_plugin_layout);
        mEmoticonToggle = findViewById(view, io.rong.imkit.R.id.rc_emoticon_toggle);
        mVoiceInputToggle = findViewById(view, io.rong.imkit.R.id.rc_audio_input_toggle);
    }

    public void initListener() {
        //发送阅后即焚
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.READPLUGIN_MESSAGE_SEND, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                isReadPluginMessage = true;
//                mVoiceToggle.setImageDrawable(getResources().getDrawable(R.mipmap.voice_toggle));
//                mPluginToggle.setImageDrawable(getResources().getDrawable(R.mipmap.upload_close));
//                mEmoticonToggle.setImageDrawable(getResources().getDrawable(R.mipmap.add_lock));
//                mEditText.setBackgroundResource(R.drawable.bg_welfare_pink_shape);
//                mPluginToggle.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (isReadPluginMessage) {
//                            BroadcastManager.getInstance(getActivity()).sendBroadcast(SealAppContext.READPLUGIN_MESSAGE_QUIT);
//                        } else {
//                            rongExtension.setInputBarStyle(InputBar.Style.STYLE_CONTAINER_EXTENSION);
//                        }
//                    }
//                });
//            }
//        });
        //退出阅后即焚
//        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.READPLUGIN_MESSAGE_QUIT, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                isReadPluginMessage = false;
//                mVoiceToggle.setImageDrawable(getResources().getDrawable(io.rong.imkit.R.drawable.rc_voice_toggle_selector));
//                mPluginToggle.setImageDrawable(getResources().getDrawable(io.rong.imkit.R.drawable.rc_plugin_toggle_selector));
//                mEmoticonToggle.setImageDrawable(getResources().getDrawable(io.rong.imkit.R.drawable.rc_emotion_toggle_selector));
//                mEditText.setBackgroundResource(io.rong.imkit.R.drawable.rc_edit_text_background_selector);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.READPLUGIN_MESSAGE_SEND);
//        BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.READPLUGIN_MESSAGE_QUIT);
    }

    /**
     * 发送图片
     * @param targetId
     * @param type
     * @param localImagePath
     */
    public void sendMyImageMessage(String targetId, Conversation.ConversationType type, String localImagePath) {
        Uri localImageUri = null;
        if(localImagePath.contains("file:")){
            localImageUri = Uri.parse(localImagePath);
        }else{
            localImageUri = Uri.parse("file:///" + localImagePath);
        }
        MyImageMessage imgMsg = MyImageMessage.obtain(localImageUri, localImageUri, true);
        //      imgMsg.setBase64(base64Str);
        RongIM.getInstance().sendImageMessage(type, targetId, imgMsg, null, null, new RongIMClient.SendImageMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //保存数据库成功
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //发送失败
                LogUtil.i(TAG, "-- sendImageMessage onError errorCode=" + errorCode.getValue() + "  errorMessage=" + errorCode.getMessage());
                if (errorCode.getValue() == RongIMClient.ErrorCode.PARAMETER_ERROR.getValue()) {
                    onResendItemClick(message);
                }
            }

            @Override
            public void onSuccess(Message message) {
                //发送成功
            }

            @Override
            public void onProgress(Message message, int i) {
                //发送进度
            }
        });
    }
}