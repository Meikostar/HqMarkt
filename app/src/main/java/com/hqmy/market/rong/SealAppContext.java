package com.hqmy.market.rong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.bean.FriendPageDto;
import com.hqmy.market.bean.GroupAddBean;
import com.hqmy.market.bean.GroupInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.rong.message.module.SealExtensionModule;
import com.hqmy.market.rong.message.myimagechat.MyImageMessage;
import com.hqmy.market.rong.message.myimagechat.MyImageMessageItemProvider;
import com.hqmy.market.rong.message.redpacketchat.RedPackageItemProvider;
import com.hqmy.market.rong.message.redpacketchat.RedPackageMessage;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongMessageItemLongClickActionManager;
import io.rong.imkit.model.GroupNotificationMessageData;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.MessageItemLongClickAction;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 融云相关监听 事件集合类
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class SealAppContext implements RongIM.ConversationListBehaviorListener,
        RongIMClient.OnReceiveMessageListener,
        RongIM.UserInfoProvider,
        RongIM.GroupInfoProvider,
        RongIM.GroupUserInfoProvider,
        RongIM.LocationProvider,
        RongIMClient.ConnectionStatusListener,
        RongIM.ConversationBehaviorListener,
        RongIM.IGroupMembersProvider {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private final static String TAG = "SealAppContext";
    public static final String UPDATE_FRIEND = "update_friend";
    public static final String UPDATE_RED_DOT = "update_red_dot";
    public static final String UPDATE_GROUP_NAME = "update_group_name";
    public static final String UPDATE_GROUP_MEMBER = "update_group_member";
    public static final String GROUP_DISMISS = "group_dismiss";
    public static final String READ_PLUGIN_MESSAGE_SEND_MY_IMAGE = "read_plugin_message_send_my_image";//发送自定义图片

    private Context mContext;
    private static SealAppContext mRongCloudInstance;
    private LocationCallback mLastLocationCallback;
    private static ArrayList<Activity> mActivities;

    public SealAppContext(Context mContext) {
        this.mContext = mContext;
        initListener();
        mActivities = new ArrayList<>();
//      SealUserInfoManager.init(mContext);
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {
            synchronized (SealAppContext.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new SealAppContext(context);
                }
            }
        }
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static SealAppContext getInstance() {
        return mRongCloudInstance;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * init 后就能设置的监听
     */
    private void initListener() {
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setConversationListBehaviorListener(this);
        RongIM.setConnectionStatusListener(this);
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        setInputProvider();
//      setUserInfoEngineListener();//移到SealUserInfoManager
        setReadReceiptConversationType();
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
        RongIM.getInstance().setGroupMembersProvider(this);
        RongIM.setGroupUserInfoProvider(this, true);//seal app暂时未使用这种方式,目前使用UserInfoProvider
        RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
        setMessageItemLongClickAction(mContext);
    }

    private static void setMessageItemLongClickAction(Context context) {
        MessageItemLongClickAction action = new MessageItemLongClickAction.Builder()
                .titleResId(R.string.rc_dialog_item_message_delete)
                .actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
                    @Override
                    public boolean onMessageItemLongClick(Context context, UIMessage message) {
                        Message[] messages = new Message[1];
                        messages[0] = message.getMessage();
                        if (message.getConversationType().equals(Conversation.ConversationType.PRIVATE)) {
                            RongIM.getInstance().deleteRemoteMessages(message.getConversationType(), message.getTargetId(), messages, null);
                        } else {
                            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
                        }
                        return false;
                    }
                }).build();

        MessageItemLongClickAction actionRecall = new MessageItemLongClickAction.Builder()
                .titleResId(R.string.rc_dialog_item_message_recall)
                .actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
                    @Override
                    public boolean onMessageItemLongClick(Context context, UIMessage message) {
                        Message[] messages = new Message[1];
                        messages[0] = message.getMessage();
                        if(message.getMessageDirection() == MessageDirection.SEND){
                           RongIM.getInstance().recallMessage(message.getMessage(), null);
                        }else{
                           ToastUtil.showToast("别人的消息无法撤回");
                        }
                        return false;
                    }
                }).build();
        RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(action, 1);
        RongMessageItemLongClickActionManager.getInstance().addMessageItemLongClickAction(actionRecall, 2);

    }


    private void setReadReceiptConversationType() {
        Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.DISCUSSION
        };
        RongIM.getInstance().setReadReceiptConversationTypeList(types);
    }

    private void setInputProvider() {
        RongIM.setOnReceiveMessageListener(this);
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule());
                //注册小视频
                //RongExtensionManager.getInstance().registerExtensionModule(new SightExtensionModule());
            }
        }
        //注册自定义加群通知消息
        //RongIM.registerMessageType(JoinGroupMsgType.class);

        //注册自定义图片消息
        RongIM.registerMessageType(MyImageMessage.class);
        RongIM.registerMessageTemplate(new MyImageMessageItemProvider());

        //注册红包消息
        RongIM.registerMessageType(RedPackageMessage.class);
        RongIM.registerMessageTemplate(new RedPackageItemProvider());
    }

    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        MessageContent messageContent = uiConversation.getMessageContent();
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
                // 被加方同意请求后
                if (contactNotificationMessage.getExtra() != null) {
//                    ContactNotificationMessageData bean = null;
//                    try {
//                        bean = JsonMananger.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    RongIM.getInstance().startPrivateChat(context, uiConversation.getConversationSenderId(), bean.getSourceUserNickname());
                }
            }  else{
//               context.startActivity(new Intent(context, NewFriendListActivity.class));
            }
            return true;
        }
        return false;
    }


    /**
     * @param message 收到的消息实体。
     * @param i
     * @return
     */
    @Override
    public boolean onReceived(Message message, int i) {
         LogUtil.e(TAG, "----" + i + " onReceived " + message.getTargetId() + "  " + message.getSenderUserId());
         MessageContent messageContent = message.getContent();
         if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
            if (contactNotificationMessage.getOperation().equals("Request")) {
               //对方发来好友邀请
               //BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
             } else if (contactNotificationMessage.getOperation().equals("AcceptResponse")) {
//                //对方同意我的好友请求
//                ContactNotificationMessageData contactNotificationMessageData;
//                try {
//                    contactNotificationMessageData = JsonMananger.jsonToBean(contactNotificationMessage.getExtra(), ContactNotificationMessageData.class);
//                } catch (HttpException e) {
//                    e.printStackTrace();
//                    return false;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//                if (contactNotificationMessageData != null) {
//                    if (SealUserInfoManager.getInstance().isFriendsRelationship(contactNotificationMessage.getSourceUserId())) {
//                        return false;
//                    }
//                    SealUserInfoManager.getInstance().addFriend(
//                            new Friend(contactNotificationMessage.getSourceUserId(),
//                                    contactNotificationMessageData.getSourceUserNickname(),
//                                    null, null, null, null,
//                                    null, null,
//                                    CharacterParser.getInstance().getSpelling(contactNotificationMessageData.getSourceUserNickname()),
//                                    null));
//                }
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_FRIEND);
//                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
//            }
                //发广播通知更新好友列表
//            BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_RED_DOT);
             }
        } else if (messageContent instanceof GroupNotificationMessage) {
            GroupNotificationMessage groupNotificationMessage = (GroupNotificationMessage) messageContent;
            LogUtil.e(TAG, "--onReceived:" + groupNotificationMessage.getMessage());
            String groupID = message.getTargetId();
            GroupNotificationMessageData data = null;
            try {
                String currentID = RongIM.getInstance().getCurrentUserId();
                try {
                    data = jsonToBean(groupNotificationMessage.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (groupNotificationMessage.getOperation().equals("Create")) {
                    //创建群组
//                    SealUserInfoManager.getInstance().getGroups(groupID);
//                    SealUserInfoManager.getInstance().getGroupMember(groupID);
                } else if (groupNotificationMessage.getOperation().equals("Dismiss")) {
                    //解散群组
                    hangUpWhenQuitGroup();      //挂断电话
                    handleGroupDismiss(groupID);
                } else if (groupNotificationMessage.getOperation().equals("Kicked")) {
                    //群组踢人
                    if (data != null) {
                        List<String> memberIdList = data.getTargetUserIds();
                        if (memberIdList != null) {
                            for (String userId : memberIdList) {
                                if (currentID.equals(userId)) {
                                    hangUpWhenQuitGroup();
                                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, message.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            Log.e("SealAppContext", "Conversation remove successfully.");
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode e) {

                                        }
                                    });
                                }
                            }
                        }
//                      List<String> kickedUserIDs = data.getTargetUserIds();
//                      SealUserInfoManager.getInstance().deleteGroupMembers(groupID, kickedUserIDs);
                        BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
                    }
                } else if (groupNotificationMessage.getOperation().equals("Add")) {
                    //群组添加人员
//                    SealUserInfoManager.getInstance().getGroups(groupID);
//                    SealUserInfoManager.getInstance().getGroupMember(groupID);
                    BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
                } else if (groupNotificationMessage.getOperation().equals("Quit")) {
                    //退出群组
                    if (data != null) {
                        List<String> quitUserIDs = data.getTargetUserIds();
                        if (quitUserIDs.contains(currentID)) {
                            hangUpWhenQuitGroup();
                        }
//                        SealUserInfoManager.getInstance().deleteGroupMembers(groupID, quitUserIDs);
                        BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, groupID);
                    }
                } else if (groupNotificationMessage.getOperation().equals("Rename")) {//群组重命名
                  String extraStr =  ((GroupNotificationMessage) messageContent).getExtra();
                  if(!TextUtils.isEmpty(extraStr) && extraStr.indexOf(",")!= -1){
                       String groupInfo[] = extraStr.split(",");
                       if(groupInfo != null && groupInfo.length > 1){
                           Group group = new Group(groupInfo[0], ((GroupNotificationMessage) messageContent).getMessage(), Uri.parse(groupInfo[1]));
                           RongIM.getInstance().refreshGroupInfoCache(group);
                       }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        } else if (messageContent instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) messageContent;
        }
        else if(messageContent instanceof MyImageMessage){
            MyImageMessage myImageMessage = (MyImageMessage) messageContent;
        }
        else if("Custom:DeleteFriend".equals(message.getObjectName())){
//          RongIMClient.getInstance().clearConversations(null,Conversation.ConversationType.PRIVATE, message.getTargetId());
            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, message.getTargetId(), null);
        }else if("Custom:applyForJoinGroup".equals(message.getObjectName())){
//            if(messageContent instanceof JoinGroupMsgType) {
//                 JoinGroupMsgType jmt = (JoinGroupMsgType) message.getContent();
//                 String userStr = jmt.getExtra();
//                String userId = Constants.getInstance().getString(Constants.USER_ID, "");
//                 ArrayList<GroupAddBean> userLists = Constants.getInstance().getList(userId);
//                try {
//                    JSONObject jsonObject = new JSONObject(userStr);
//                    GroupAddBean gaBean = new GroupAddBean();
//                    gaBean.setGroupId(jsonObject.get("groupId").toString());
//                    gaBean.setOwnerId(jsonObject.get("ownerId").toString());
//                    gaBean.setUserHeader(jsonObject.get("userHeader").toString());
//                    gaBean.setUserId(jsonObject.get("userId").toString());
//                    gaBean.setUserName(jsonObject.get("userName").toString());
//                    gaBean.setStatus("2");
//                    if(userLists != null){
//                        userLists.add(gaBean);
//                    }else{
//                        userLists = new ArrayList<GroupAddBean>();
//                        userLists.add(gaBean);
//                    }
//                    ArrayList<GroupAddBean> gabList  = removeDuplicteUsers(userLists);
//                    Constants.getInstance().saveList(userId,gabList);
//                    Constants.getInstance().saveInt(Constants.TAG_SIZE + userId, gabList.size());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        }
        return false;
    }

    /**
     * 解散群组
     * @param groupID
     */
    private void handleGroupDismiss(final String groupID) {
        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupID, new RongIMClient.ResultCallback<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupID, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupID, null);
                    }
                    @Override
                    public void onError(RongIMClient.ErrorCode e) {

                    }
                });
            }
            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        });
//        SealUserInfoManager.getInstance().deleteGroups(new Groups(groupID));
//        SealUserInfoManager.getInstance().deleteGroupMembers(groupID);
//        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
          BroadcastManager.getInstance(mContext).sendBroadcast(GROUP_DISMISS, groupID);
    }

    /**
     * 用户信息提供者的逻辑移到SealUserInfoManager
     * 先从数据库读,没有数据时从网络获取
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        DataManager.getInstance().queryUserInfoPage(new DefaultSingleObserver<FriendPageDto>() {
            @Override
            public void onSuccess(FriendPageDto friendPageDto) {
                String nameStr = null;
                String avatarStr = null;
                if(friendPageDto != null) {
                    if(friendPageDto.getUser().getName() != null){
                        nameStr = friendPageDto.getUser().getName();
                    }else{
                        nameStr = friendPageDto.getUser().getPhone();
                    }
                    if(friendPageDto.getUser().getAvatar() != null){
                        avatarStr = Constants.WEB_IMG_URL_UPLOADS + friendPageDto.getUser().getAvatar();
                    }else{
                        avatarStr = "android.resource://" + mContext.getApplicationContext().getPackageName() + "/" +R.mipmap.ic_head_img;
                    }
                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(friendPageDto.getUser().getId(), nameStr, Uri.parse(avatarStr)));
                }
            }
            @Override
            public void onError(Throwable throwable) {
            }
        }, userId);
       return null;
    }


    @Override
    public Group getGroupInfo(String groupsId) {
        LogUtil.i(TAG, "-------------------getGroupInfo=" + groupsId);
        //return GroupInfoEngine.getInstance(mContext).startEngine(s);
        //SealUserInfoManager.getInstance().getGroupInfo(s);
        DataManager.getInstance().getGroupInfo(new DefaultSingleObserver<HttpResult<GroupInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<GroupInfoDto> result) {
                if(result.getData() != null){
                    GroupInfoDto groupInfoDto = result.getData();
                    Group groupInfo = null;
                    if(groupInfoDto.getAvatar() != null){
                        groupInfo = new Group(groupsId, groupInfoDto.getGroup_name(), Uri.parse(Constants.WEB_IMG_URL_UPLOADS + groupInfoDto.getAvatar()));
                    }else{
                        groupInfo = new Group(groupsId, groupInfoDto.getGroup_name(), Uri.parse("android.resource://" + mContext.getApplicationContext().getPackageName() + "/" +R.mipmap.icon_group));
                    }
                    RongIM.getInstance().refreshGroupInfoCache(groupInfo);
                }
            }
            @Override
            public void onError(Throwable throwable) {
            }
        }, groupsId);
        return null;
    }

    @Override
    public GroupUserInfo getGroupUserInfo(String groupId, String userId) {
        LogUtil.i(TAG, "-------------------getGroupUserInfo groupId=" + groupId + "    userId=" + userId);
        //return GroupUserInfoEngine.getInstance(mContext).startEngine(groupId, userId);
        return null;
    }


    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (conversationType == Conversation.ConversationType.CUSTOMER_SERVICE || conversationType == Conversation.ConversationType.PUBLIC_SERVICE || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {
            return false;
        }
        //开发测试时,发送系统消息的userInfo只有id不为空
        if (userInfo != null && userInfo.getName() != null && userInfo.getPortraitUri() != null) {
//            Bundle bundle = new Bundle();
//            bundle.putString(Constants.USER_ID, userInfo.getUserId());
//            Intent intent = new Intent(context, UserDetailActivity.class);
//            intent.putExtras(bundle);
//            intent.putExtra("conversationType", conversationType.getValue());
//            Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
//            intent.putExtra("friend", friend);
//            intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
//            context.startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(final Context context, final View view, final Message message) {
        //real-time location message end
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (message.getContent() instanceof ImageMessage) {
            /*Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("message", message);
            context.startActivity(intent);*/
        }
        return false;
    }


    private void startRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    private void joinRealTimeLocation(Context context, Conversation.ConversationType conversationType, String targetId) {

    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }


    public LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        LogUtil.d(TAG, "--ConnectionStatus onChanged Status="+ connectionStatus+" | "+ connectionStatus.getMessage());
        switch (connectionStatus) {
            case CONNECTED://连接成功。
                break;
            case DISCONNECTED://断开连接。
                break;
            case CONNECTING://连接中。
                break;
            case NETWORK_UNAVAILABLE://网络不可用。
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
//                quit(true);
                LogUtil.e(TAG,"--ConnectionStatus onChanged = "+connectionStatus+" 用户账户在其他设备登录，本机会被踢掉线");
                ToastUtil.showToast("用户账户在其他设备登录");
//                showLoginHintDialog();
                break;
            case TOKEN_INCORRECT://无效Token
                LogUtil.e(TAG,"--ConnectionStatus onChanged = "+connectionStatus+" 无效Token");
                String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, "");
                if (!TextUtils.isEmpty(cacheToken)) {
                    ToastUtil.showToast("无效Token");
                    RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
                } else {
                    ToastUtil.showToast("Token为空");
                    LogUtil.e("MyConnectionStatusListener", "--token is empty, can not reconnect");
                }
                //showLoginHintDialog();
                break;
        }
    }

    public void pushActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void popActivity(Activity activity) {
        if (mActivities.contains(activity)) {
            activity.finish();
            mActivities.remove(activity);
        }
    }
//    public void popAllActivity() {
//        try {
//            if (MainActivity.mViewPager != null) {
//                MainActivity.mViewPager.setCurrentItem(0);
//            }
//            for (Activity activity : mActivities) {
//                if (activity != null) {
//                    activity.finish();
//                }
//            }
//            mActivities.clear();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public RongIMClient.ConnectCallback getConnectCallback() {
        RongIMClient.ConnectCallback connectCallback = new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                LogUtil.d(TAG, "ConnectCallback connect onTokenIncorrect");
                ShareUtil.getInstance().save(Constants.APP_USER_KEY, "");
            }

            @Override
            public void onSuccess(String userId) {
                LogUtil.d(TAG, "--ConnectCallback connect onSuccess");
                ShareUtil.getInstance().save(Constants.USER_ID, userId);
            }

            @Override
            public void onError(final RongIMClient.ErrorCode e) {
                ToastUtil.showToast("" + e.getMessage());
                LogUtil.d(TAG, "--ConnectCallback connect onError-ErrorCode=" + e.getMessage());
            }
        };
        return connectCallback;
    }


    private GroupNotificationMessageData jsonToBean(String data) {
        GroupNotificationMessageData dataEntity = new GroupNotificationMessageData();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("operatorNickname")) {
                dataEntity.setOperatorNickname(jsonObject.getString("operatorNickname"));
            }
            if (jsonObject.has("targetGroupName")) {
                dataEntity.setTargetGroupName(jsonObject.getString("targetGroupName"));
            }
            if (jsonObject.has("timestamp")) {
                dataEntity.setTimestamp(jsonObject.getLong("timestamp"));
            }
            if (jsonObject.has("targetUserIds")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserIds");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserIds().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("targetUserDisplayNames")) {
                JSONArray jsonArray = jsonObject.getJSONArray("targetUserDisplayNames");
                for (int i = 0; i < jsonArray.length(); i++) {
                    dataEntity.getTargetUserDisplayNames().add(jsonArray.getString(i));
                }
            }
            if (jsonObject.has("oldCreatorId")) {
                dataEntity.setOldCreatorId(jsonObject.getString("oldCreatorId"));
            }
            if (jsonObject.has("oldCreatorName")) {
                dataEntity.setOldCreatorName(jsonObject.getString("oldCreatorName"));
            }
            if (jsonObject.has("newCreatorId")) {
                dataEntity.setNewCreatorId(jsonObject.getString("newCreatorId"));
            }
            if (jsonObject.has("newCreatorName")) {
                dataEntity.setNewCreatorName(jsonObject.getString("newCreatorName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataEntity;
    }

    private void quit(boolean isKicked) {
        Log.d(TAG, "--quit isKicked " + isKicked);
        /*//这些数据清除操作之前一直是在login界面,因为app的数据库改为按照userID存储,退出登录时先直接删除
        //这种方式是很不友好的方式,未来需要修改同app server的数据同步方式
        //SealUserInfoManager.getInstance().deleteAllUserInfo();*/
//        SealUserInfoManager.getInstance().closeDB();
        ShareUtil.getInstance().cleanUserInfo();
        Intent loginActivityIntent = new Intent();
        loginActivityIntent.setClass(mContext, LoginActivity.class);
        loginActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (isKicked) {
            loginActivityIntent.putExtra("kickedByOtherClient", true);
        }
        mContext.startActivity(loginActivityIntent);
    }

    private void showLoginHintDialog() {
        ConfirmDialog dialog = new ConfirmDialog((Activity) mContext);
        dialog.setTitle("登录提示");
        dialog.setMessage("该帐号已经在其他设备登录,是否重新登录.");
        dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
            @Override
            public void onYesClick() {
                quit(true);
            }
        });
        dialog.setCancleClickListener("取消", new BaseDialog.OnCloseClickListener() {
            @Override
            public void onCloseClick() {
                String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, "");
                if (!TextUtils.isEmpty(cacheToken)) {
                    RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
                } else {
                    Log.e("seal", "token is empty, can not reconnect");
                }
            }
        });
        dialog.show();
    }


    /**
     * 将列表中重复的用户移除，重复指的是name相同
     *
     * @param userList
     * @return
     */
    public static ArrayList<GroupAddBean> removeDuplicteUsers(ArrayList<GroupAddBean> userList) {
        Set<GroupAddBean> s = new TreeSet<GroupAddBean>(new Comparator<GroupAddBean>() {

            @Override
            public int compare(GroupAddBean o1, GroupAddBean o2) {
                return o1.getUserId().compareTo(o2.getUserId());
            }
        });
        s.addAll(userList);
        return new ArrayList<GroupAddBean>(s);
    }

    @Override
    public void getGroupMembers(String groupId, final RongIM.IGroupMemberCallback callback) {
//        SealUserInfoManager.getInstance().getGroupMembers(groupId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
//            @Override
//            public void onSuccess(List<GroupMember> groupMembers) {
//                List<UserInfo> userInfos = new ArrayList<>();
//                if (groupMembers != null) {
//                    for (GroupMember groupMember : groupMembers) {
//                        if (groupMember != null) {
//                            UserInfo userInfo = new UserInfo(groupMember.getUserId(), groupMember.getName(), groupMember.getPortraitUri());
//                            userInfos.add(userInfo);
//                        }
//                    }
//                }
//                callback.onGetGroupMembersResult(userInfos);
//            }
//            @Override
//            public void onError(String errString) {
//                callback.onGetGroupMembersResult(null);
//            }
//        });
    }

    //挂断电话
    private void hangUpWhenQuitGroup() {
//        RongCallSession session = RongCallClient.getInstance().getCallSession();
//        if (session != null) {
//            RongCallClient.getInstance().hangUpCall(session.getCallId());
//        }
    }

    /**
     * :融云发送消息监听(connect方法之后)
     */
    private class MySendMessageListener implements RongIM.OnSendMessageListener {
        /**
         * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
         * @param message 发送的消息实例。
         * @return 处理后的消息实例。
         */
        @Override
        public Message onSend(Message message) {
            //开发者根据自己需求自行处理逻辑
            return message;
        }
        /**
         * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
         * @param message              消息实例。
         * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
         * @return true 表示走自已的处理方式，false 走融云默认处理方式。
         */
        @Override
        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
            Log.d(TAG, "--onSent--");
            if (message.getSentStatus() == Message.SentStatus.FAILED) {
                if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {
                    //不在聊天室
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {
                    //不在讨论组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {
                    //不在群组
                } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {
                    //你在他的黑名单中
                }else{
                    sentMessageErrorCode = sentMessageErrorCode;
                }
            }
            MessageContent messageContent = message.getContent();
            if (messageContent instanceof TextMessage) {//文本消息
                TextMessage textMessage = (TextMessage) messageContent;
                Log.d(TAG, "--onSent-TextMessage:" + textMessage.getContent());
            } else if (messageContent instanceof ImageMessage) {//图片消息
                ImageMessage imageMessage = (ImageMessage) messageContent;
                Log.d(TAG, "--onSent-ImageMessage:" + imageMessage.getRemoteUri());
            }
            else if (messageContent instanceof MyImageMessage) {//图片消息
                MyImageMessage imageMessage = (MyImageMessage) messageContent;
                Log.d(TAG, "--onSent-ImageMessage:" + imageMessage.getRemoteUri());
            }
            else if (messageContent instanceof VoiceMessage) {//语音消息
                VoiceMessage voiceMessage = (VoiceMessage) messageContent;
                Log.d(TAG, "--onSent-voiceMessage:" + voiceMessage.getUri().toString());
            } else if (messageContent instanceof RichContentMessage) {//图文消息
                RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                Log.d(TAG, "--onSent-RichContentMessage:" + richContentMessage.getContent());
            }
            else {
                Log.d(TAG, "--onSent-其他消息，自己来判断处理");
            }
            return false;
        }
    }
}
