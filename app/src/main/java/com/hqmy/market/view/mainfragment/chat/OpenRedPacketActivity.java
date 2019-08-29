package com.hqmy.market.view.mainfragment.chat;

import android.widget.ImageView;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.InformationNotificationMessage;

/**
 * 打开红包
 */
public class OpenRedPacketActivity extends BaseActivity {
    public static final String REDPACKET_INFO = "RedPacketInfo";//调用者传递的名字
    public static final String TARGET_ID = "targetId";//调用者传递的名字
    @BindView(R.id.iv_redpacket_back)
    ImageView iv_redpacket_back;
    @BindView(R.id.open_red_packet_title)
    TextView open_red_packet_title;
    @BindView(R.id.open_red_packet_content)
    TextView open_red_packet_content;

    @Override
    public int getLayoutId() {
        return R.layout.ui_open_red_packet_layout;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
//        RedPacketDto redPacketBean = (RedPacketDto) getIntent().getSerializableExtra(REDPACKET_INFO);
//        open_red_packet_integral.setText((redPacketBean.getBalance() == null ? "00.0" : redPacketBean.getAmount()) + "积分");
//        if (redPacketBean.getIsFinished() == 0) {//红包是否抢完 1抢完 0未抢完
//            String targetId = getIntent().getExtras().getString(TARGET_ID, null);
//            sendOpenRedPacketMessage(targetId, Conversation.ConversationType.PRIVATE, "红包已被领取");
//        }
//
//        String userId = Constants.getInstance().getString(Constants.USER_ID,"");
//        if(redPacketBean.getUserId().equals(userId)){
//            open_red_packet_title.setText("对方获得");
//        }
    }

    @Override
    public void initListener() {

    }

    /**
     * 小灰色条消息
     * @param targetId
     * @param conversationType
     * @param message          消息
     */
    private void sendOpenRedPacketMessage(String targetId, Conversation.ConversationType conversationType, String message) {
        InformationNotificationMessage myTextMessage = InformationNotificationMessage.obtain(message);
        Message myMessage = Message.obtain(targetId, conversationType, myTextMessage);
        RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {
                //消息本地数据库存储成功的回调
            }

            @Override
            public void onSuccess(Message message) {
                //消息通过网络发送成功的回调
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                //消息发送失败的回调
            }
        });
    }
}
