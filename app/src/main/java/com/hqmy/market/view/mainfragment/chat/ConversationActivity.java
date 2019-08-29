package com.hqmy.market.view.mainfragment.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.GroupListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.rong.SealAppContext;
import com.hqmy.market.rong.widget.ConversationFragmentEx;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 配置会话界面
 * Created by rzb on 2019/6/20.
 */
public class ConversationActivity extends BaseActivity {
    private static final String TAG = ConversationActivity.class.getSimpleName();
    public static final String TARGET_ID = "target_id";//调用者传递的名字
    public static final String TITLE = "title";//调用者传递的名字
    public static final String CONVERSATION_TYPE = "ConversationType";//调用者传递的名字 会话类型 1= 单聊（默认），3=群聊
    public static final int GROUP_CONVERSATION_CLOSE = 10001;
    public static final int INTENT_REQUESTCODE = 10002;
    private String  targetId;
    private String  title;
    private Conversation.ConversationType mConversationType;//会话类型

    @BindView(R.id.convesation_back)
    ImageView convesation_back;
    @BindView(R.id.convesation_title)
    TextView convesation_title;
    @BindView(R.id.convesation_right)
    RelativeLayout convesation_right;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;
    private ConversationFragmentEx conversationFragmentEx;
    private LocalBroadcastManager  localBroadcastManager;
    LocalReceiver localReceiver;
    private boolean      isOpenAudio = false;//是否按住讲话;
    private GroupListDto mGroupBean  = null;

    @Override
    public int getLayoutId() {
        return R.layout.ui_conversation_layout;
    }

    @Override
    public void initView() {
        initBroadcast();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Uri uri = getIntent().getData();
        if (uri != null) {
            targetId = intent.getData().getQueryParameter("targetId");
            title = intent.getData().getQueryParameter("title");
            mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                    .getLastPathSegment().toUpperCase(Locale.US));
        } else {
            targetId = intent.getExtras().getString(TARGET_ID, null);
            title = intent.getExtras().getString(TITLE, null);
            int conversationType = getIntent().getIntExtra(CONVERSATION_TYPE, 1);
            switch (conversationType) {
                case 1:
                    mConversationType = Conversation.ConversationType.PRIVATE;
                    break;
                case 3:
                    mConversationType = Conversation.ConversationType.GROUP;
                    break;
                default:
                    mConversationType = Conversation.ConversationType.PRIVATE;
                    break;
            }
        }
        if (targetId == null) {
            return;
        }
        setTitle(title);
        enterFragment(mConversationType, targetId);
        setTypingStatusListener(targetId);
        RongIM.getInstance().setMessageAttachedUserInfo(false);
    }

    @Override
    public void initListener() {
        bindClickEvent(convesation_back, () -> {
            finish();
        });

        bindClickEvent(convesation_right, () -> {
            if (mConversationType == Conversation.ConversationType.GROUP) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.GROUP_ID, targetId);
                gotoActivity(GroupDetailActivity.class, false, bundle, GROUP_CONVERSATION_CLOSE);
            }else if(mConversationType == Conversation.ConversationType.PRIVATE){

            }
        });

        //发送自定义图片
        BroadcastManager.getInstance(this).addAction(SealAppContext.READ_PLUGIN_MESSAGE_SEND_MY_IMAGE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String strList =  intent.getStringExtra("String");
                    if(strList != null) {
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        Map<String, Integer> retMap2 = gson.fromJson(strList, new TypeToken<Map<String, Integer>>() {
                        }.getType());
                        for (String key : retMap2.keySet()) {
                            conversationFragmentEx.sendMyImageMessage(targetId, mConversationType, key);
                        }
                    }
                }
            }
        });
    }

    /**
     * 设置输入状态
     * @param targetId 会话 Id
     */
    private void setTypingStatusListener(String targetId) {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (targetId.equals(targetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();
                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });
    }

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     * @param mConversationType 会话类型
     * @param targetId          会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String targetId) {
        conversationFragmentEx = new ConversationFragmentEx();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId).build();
        conversationFragmentEx.setUri(uri);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversation, conversationFragmentEx);
        transaction.commitAllowingStateLoss();
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    setTitle("对方正在输入...");
                    mHandler.removeMessages(SET_TEXT_TYPING_TITLE);
                    break;
                case SET_VOICE_TYPING_TITLE:
                    setTitle("对方正在讲话...");
                    mHandler.removeMessages(SET_VOICE_TYPING_TITLE);
                    break;
                case SET_TARGET_ID_TITLE:
                    setTitle(title);
                    mHandler.removeMessages(SET_TARGET_ID_TITLE);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    private void setTitle(String title) {
        convesation_title.setText(title == null ? " " : title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIMClient.setTypingStatusListener(null);
        localBroadcastManager.unregisterReceiver(localReceiver);
        BroadcastManager.getInstance(this).destroy(SealAppContext.READ_PLUGIN_MESSAGE_SEND_MY_IMAGE);
    }

    private void initBroadcast() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_CHAT);
        localReceiver = new LocalReceiver();
        //注册本地接收器
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    /**
     * @param type InputMethodManager.SHOW_FORCED    type = 0 hide
     */
    private void changeInput(int type) {
    }

    public void closeKeybord() {
    }
}
