package com.hqmy.market.qiniu.chatroom;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;


import com.hqmy.market.R;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.qiniu.chatroom.message.ChatroomBarrage;
import com.hqmy.market.qiniu.chatroom.message.ChatroomEnd;
import com.hqmy.market.qiniu.chatroom.message.ChatroomGift;
import com.hqmy.market.qiniu.chatroom.message.ChatroomJifen;
import com.hqmy.market.qiniu.chatroom.message.ChatroomLike;
import com.hqmy.market.qiniu.chatroom.message.ChatroomStart;
import com.hqmy.market.qiniu.chatroom.message.ChatroomUserQuit;
import com.hqmy.market.qiniu.chatroom.message.ChatroomWelcome;
import com.hqmy.market.qiniu.chatroom.messageview.BaseMsgView;
import com.hqmy.market.qiniu.chatroom.panel.EmojiManager;
import com.hqmy.market.utils.ScreenUtils;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

//import io.rong.imkit.RongIM;
import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * ChatroomKit是融云聊天室Demo对IMLib库的接口封装类。目的是在IMLib库众多通用接口中，提炼出与直播聊天室应用相关的常用接口，
 * 方便开发者了解IMLib库的调用流程，降低学习成本。同时也开发者可以此为基础扩展，并快速移植到自己的应用中去。
 * <p/>
 * <strong>注意：</strong>此种封装并不是集成IMLib库的唯一方法，开发者可以根据自身需求添加修改，或者直接使用IMLib接口。
 */

public class ChatroomKit {

    /**
     * 事件代码
     */
    public static final int MESSAGE_ARRIVED = 0;
    public static final int MESSAGE_SENT = 1;

    /**
     * 事件错误代码
     */
    public static final int MESSAGE_SEND_ERROR = -1;

    /**
     * 消息类与消息UI展示类对应表
     */
    private static HashMap<Class<? extends MessageContent>, Class<? extends BaseMsgView>> msgViewMap = new HashMap<>();

    /**
     * 事件监听者列表
     */
    private static ArrayList<Handler> eventHandlerList = new ArrayList<>();

    /**
     * 当前登录用户id
     */
    private static UserInfo currentUser;

    /**
     * 当前聊天室房间id
     */
    private static String currentRoomId;

    /**
     * 消息接收监听者
     */
    private static RongIMClient.OnReceiveMessageListener onReceiveMessageListener = new RongIMClient.OnReceiveMessageListener() {
        @Override
        public boolean onReceived(Message message, int left) {
            handleEvent(MESSAGE_ARRIVED, message);

//            ShowNotification(message, left);
            return false;
        }
    };

    private static int num = 0;//初始通知数量为1

    //按钮点击事件（通知栏）
    public static void ShowNotification(Message message, int left) {
        LogUtil.e("ChatroomKit", "消息接收监听者=" + message.toString());
        num++;
        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
        NotificationManager manager = (NotificationManager) BaseApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        //8.0 以后需要加上channelId 才能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            String channelName = "默认通知";
            manager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        //设置TaskStackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(BaseApplication.getInstance());
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(BaseApplication.getInstance(), "default");
        builder.setSmallIcon(R.mipmap.ic_logo);
        RemoteViews rv = new RemoteViews(BaseApplication.getInstance().getPackageName(), R.layout.custom_notification_view);
        TextMessage content = (TextMessage) message.getContent();
        rv.setTextViewText(R.id.content_tv,content.getContent());//修改自定义View中的歌名
        rv.setTextViewText(R.id.create_time_tv, ScreenUtils.formatToFileDi(message.getSentTime()));
        rv.setTextViewText(R.id.number_tv, "还有" + left + "个通知");
        //修改自定义View中的图片(两种方法)
        //        rv.setImageViewResource(R.id.iv,R.mipmap.ic_launcher);
//        rv.setImageViewBitmap(R.id.iv, BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), R.mipmap.ic_logo));
        builder.setContent(rv);

        //设置点击通知跳转页面后，通知消失
        builder.setAutoCancel(true);
//        Intent intent = new Intent(BaseApplication.getInstance(), MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(BaseApplication.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) BaseApplication.getInstance().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0x1, notification);
    }

    /**
     * 初始化方法，在整个应用程序全局只需要调用一次，建议在Application 继承类中调用。
     * <p/>
     * <strong>注意：</strong>其余方法都需要在这之后调用。
     *
     * @param context Application类的Context
     * @param appKey  融云注册应用的AppKey
     */
    public static void init(Context context, String appKey) {

        RongIMClient.init(context, appKey);
        EmojiManager.init(context);

        RongIMClient.setOnReceiveMessageListener(onReceiveMessageListener); //设置消息接收监听器

        registerMessageType(ChatroomWelcome.class);
//        registerMessageView(ChatroomWelcome.class, WelcomeMsgView.class);
//
//        registerMessageType(ChatroomFollow.class);
//        registerMessageView(ChatroomFollow.class, FollowMsgView.class);
//
        registerMessageType(ChatroomBarrage.class);
//
        registerMessageType(ChatroomGift.class);
//
        registerMessageType(ChatroomLike.class);
//        registerMessageView(ChatroomLike.class, LikeMsgView.class);
//
        registerMessageType(ChatroomUserQuit.class);
//        registerMessageView(ChatroomUserQuit.class, UserQuitMsgView.class);
//
//        registerMessageView(TextMessage.class, TextMsgView.class);
//
        registerMessageType(ChatroomStart.class);
//        registerMessageView(ChatroomStart.class, StartMsgView.class);
//
//        registerMessageType(ChatroomAdminAdd.class);
//        registerMessageView(ChatroomAdminAdd.class, AdminAddView.class);
//
//        registerMessageType(ChatroomAdminRemove.class);
//        registerMessageView(ChatroomAdminRemove.class, AdminRemoveView.class);
//
//        registerMessageType(ChatroomUserBan.class);
//        registerMessageView(ChatroomUserBan.class, UserBanView.class);
//
//        registerMessageType(ChatroomUserUnBan.class);
//        registerMessageView(ChatroomUserUnBan.class, UserUnBanView.class);
//
//        registerMessageType(ChatroomUserBlock.class);
//        registerMessageView(ChatroomUserBlock.class, UserBlockView.class);
//
//        registerMessageType(ChatroomUserUnBlock.class);
//        registerMessageView(ChatroomUserUnBlock.class, UserUnBlockView.class);
//
        registerMessageType(ChatroomEnd.class);
        registerMessageType(ChatroomJifen.class);
//        registerMessageView(ChatroomEnd.class, EndView.class);
//
//        registerMessageType(BanWarnMessage.class);
//        registerMessageView(BanWarnMessage.class, BanWarnView.class);


    }

    /**
     * 设置当前登录用户，通常由注册生成，通过应用服务器来返回。
     *
     * @param user 当前用户
     */
    public static void setCurrentUser(UserInfo user) {
        currentUser = user;
    }

    /**
     * 获得当前登录用户。
     *
     * @return
     */
    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentRoomId() {
        return currentRoomId;
    }

    /**
     * 注册自定义消息。
     *
     * @param msgType 自定义消息类型
     */
    public static void registerMessageType(Class<? extends MessageContent> msgType) {
        try {
            RongIMClient.registerMessageType(msgType);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册消息展示界面类。
     *
     * @param msgContent 消息类型
     * @param msgView    对应的界面展示类
     */
    public static void registerMessageView(Class<? extends MessageContent> msgContent, Class<? extends BaseMsgView> msgView) {
        msgViewMap.put(msgContent, msgView);
    }

    /**
     * 获取注册消息对应的UI展示类。
     *
     * @param msgContent 注册的消息类型
     * @return 对应的UI展示类
     */
    public static Class<? extends BaseMsgView> getRegisterMessageView(Class<? extends MessageContent> msgContent) {
        return msgViewMap.get(msgContent);
    }

    /**
     * 连接融云服务器，在整个应用程序全局，只需要调用一次，需在 {@link #init(Context, String)} 之后调用。
     * </p>
     * 如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。
     *
     * @param token    从服务端获取的用户身份令牌（Token）
     * @param callback 连接回调
     */
    public static void connect(String token, final RongIMClient.ConnectCallback callback) {
        RongIMClient.connect(token, callback);
    }

    /**
     * 断开与融云服务器的连接，并且不再接收 Push 消息。
     */
    public static void logout() {
        RongIMClient.getInstance().logout();
    }

    /**
     * 加入聊天室。如果聊天室不存在，sdk 会创建聊天室并加入，如果已存在，则直接加入。加入聊天室时，可以选择拉取聊天室消息数目。
     *
     * @param roomId          聊天室 Id
     * @param defMessageCount 默认开始时拉取的历史记录条数
     * @param callback        状态回调
     */
    public static void joinChatRoom(String roomId, int defMessageCount, final RongIMClient.OperationCallback callback) {
        currentRoomId = roomId;
        RongIMClient.getInstance().joinChatRoom(currentRoomId, defMessageCount, callback);
    }

    /**
     * 退出聊天室，不在接收其消息。
     */
    public static void quitChatRoom(final RongIMClient.OperationCallback callback) {
        RongIMClient.getInstance().quitChatRoom(currentRoomId, callback);
    }

    /**
     * 向当前聊天室发送消息。
     * </p>
     * <strong>注意：</strong>此函数为异步函数，发送结果将通过handler事件返回。
     *
     * @param msgContent 消息对象
     */
    public static void sendMessage(final MessageContent msgContent) {
        if (currentUser == null) {
            currentUser = getUserInfo();
        }
        if (currentUser == null) {
            throw new RuntimeException("currentUser should not be null.");
        }

        Message msg = Message.obtain(currentRoomId, Conversation.ConversationType.CHATROOM, msgContent);

        RongIMClient.getInstance().sendMessage(msg, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                handleEvent(MESSAGE_SENT, message);

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                handleEvent(MESSAGE_SEND_ERROR, errorCode.getValue(), 0, message);
            }
        });
    }

    /**
     * 添加IMLib 事件接收者。
     *
     * @param handler
     */
    public static void addEventHandler(Handler handler) {
        if (!eventHandlerList.contains(handler)) {
            eventHandlerList.add(handler);
        }
    }

    /**
     * 移除IMLib 事件接收者。
     *
     * @param handler
     */
    public static void removeEventHandler(Handler handler) {
        eventHandlerList.remove(handler);
    }

    private static void handleEvent(int what) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }

    private static void handleEvent(int what, int arg1, int arg2, Object obj) {
        for (Handler handler : eventHandlerList) {
            android.os.Message m = android.os.Message.obtain();
            m.what = what;
            m.arg1 = arg1;
            m.arg2 = arg2;
            m.obj = obj;
            handler.sendMessage(m);
        }
    }

    private static UserInfo getUserInfo() {
        Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, null));
        String userId = ShareUtil.getInstance().getString(Constants.USER_ID, "");
        String nickName = ShareUtil.getInstance().getString(Constants.USER_NAME, "");
        return new UserInfo(userId, nickName, RongHeadImg);
    }
}
