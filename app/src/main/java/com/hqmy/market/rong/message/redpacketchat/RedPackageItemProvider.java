package com.hqmy.market.rong.message.redpacketchat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.bean.CashCouponUserInfoDto;
import com.hqmy.market.bean.CashCouponUsersDto;
import com.hqmy.market.bean.RedPacketDto;
import com.hqmy.market.bean.RedPacketInfoDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.mainfragment.chat.OpenRedPacketActivity;
import com.hqmy.market.view.widgets.dialog.RedPacketOpenDialog;

import java.util.List;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.message.InformationNotificationMessage;

/**
 * Created by Lenovo on 2019/3/4.
 * desc新建一个红包消息类继承 IContainerItemProvider.MessageProvider 类，实现对应接口方法，
 * 1.注意开头的注解！
 * 2.注意泛型！
 */
@ProviderTag(
        messageContent = RedPackageMessage.class,//（这里是你自定义的消息实体）
        showReadState = true
)
public class RedPackageItemProvider extends IContainerItemProvider.MessageProvider<RedPackageMessage> {
    private static final String TAG = RedPackageItemProvider.class.getSimpleName();
    private Context mContext;

    public RedPackageItemProvider() {
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        //这就是展示在会话界面的自定义的消息的布局
        View view = LayoutInflater.from(context).inflate(R.layout.ry_item_redpackage_message_layout, null);
        this.mContext = context;
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
        holder.tvRedPacketId = (TextView) view.findViewById(R.id.tv_red_packet_id);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, RedPackageMessage redPackageMessage, UIMessage message) {
        //根据需求，适配数据
        ViewHolder holder = (ViewHolder) view.getTag();
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
        } else {
            //holder.message.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        //String title = redPackageMessage.getTitle();
        String redPacketId = redPackageMessage.getCash_coupon_id();
        //LogUtil.i(TAG, "--bindView  title=" + title + "cash_coupon_id=" + redPacketId);
        //if (title == null || title.equals("")) {
        //    title = "大吉大利";
        //}
        //holder.tvTitle.setText(title);
        holder.tvRedPacketId.setText(redPacketId);
    }

    @Override
    public Spannable getContentSummary(RedPackageMessage redPackageMessage) {
//      return new SpannableString(redPackageMessage.getTitle());
        return new SpannableString("[积分红包]");
    }

    @Override
    public void onItemClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
        //String title = redPackageMessage.getTitle();
        String redPacketId = redPackageMessage.getCash_coupon_id();
        //LogUtil.i(TAG, "-- onItemClick title=" + title + "  redPacketId=" + redPacketId);
        if(view.getContext() != null) {
            RedPacketOpenDialog dialog = new RedPacketOpenDialog(view.getContext(), new RedPacketOpenDialog.OnRedPacketClickListener() {
                @Override
                public void onRedPacketClick() {
                    getPacketInfo(redPacketId, uiMessage, view);
                }
            });
            dialog.show();
        }
    }

    @Override
    public void onItemLongClick(View view, int i, RedPackageMessage redPackageMessage, UIMessage uiMessage) {
//        //实现长按删除等功能，咱们直接复制融云其他provider的实现
//        String[] items1;//复制，删除 //view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_copy),
//        items1 = new String[]{view.getContext().getResources().getString(io.rong.imkit.R.string.rc_dialog_item_message_delete)};
//        OptionsPopupDialog.newInstance(view.getContext(), items1).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
//            public void onOptionsItemClicked(int which) {
//                if (which == 0) {
//                    ClipboardManager clipboard = (ClipboardManager) view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                    clipboard.setText(content.getContent());//这里是自定义消息的消息属性
//                } else if (which == 1) {
//                    RongIM.getInstance().deleteMessages(new int[]{uiMessage.getMessageId()}, (RongIMClient.ResultCallback) null);
//                }
//            }
//        }).show();
        showOptDialog(view.getContext(),uiMessage);;
    }

    private static class ViewHolder {
        TextView tvTitle, tvRedPacketId;
    }

    /**
     * 领取红包
     */
    private void getRedPacket(String cash_coupon_user_id, Conversation.ConversationType conversationType, Context mCont) {
        DataManager.getInstance().getRedPacket(new DefaultSingleObserver<HttpResult<RedPacketDto>>() {
            @Override
            public void onSuccess(HttpResult<RedPacketDto> object) {
                if(object != null) {
                    RedPacketDto  redPacketDto = object.getData();
                    Intent intent = new Intent();
                    if (conversationType == Conversation.ConversationType.PRIVATE) {
                        intent.setClass(mCont, OpenRedPacketActivity.class);
                        intent.putExtra(OpenRedPacketActivity.REDPACKET_INFO, redPacketDto);
                    }
                    mCont.startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        }, cash_coupon_user_id);
    }

    /**
     * 红包详情
     */
    private void getPacketInfo(String cash_coupon_id, UIMessage uiMessage, View view) {
        DataManager.getInstance().getPacketInfo(new DefaultSingleObserver<HttpResult<RedPacketInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<RedPacketInfoDto> result) {
                if(result != null) {
                    RedPacketInfoDto redPacketInfoDto = result.getData();
                    CashCouponUsersDto cashCouponUsersDto = redPacketInfoDto.getCash_coupon_users();
                    List<CashCouponUserInfoDto> cList = cashCouponUsersDto.getData();
                    if(cList == null){
                        return;
                    }
                    getRedPacket(cList.get(0).getId(), uiMessage.getConversationType(),view.getContext());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
            }
        }, cash_coupon_id);
    }

    /**
     * 小灰色条消息
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

    private void showOptDialog(Context context, UIMessage message) {
        if(message.getMessageDirection() == MessageDirection.RECEIVE) {
            String[] items = new String[]{"删除消息"};
            OptionsPopupDialog.newInstance(context, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
                        dteMessages(100,message);
                    }
                }
            }).show();
        }else if(message.getMessageDirection() == MessageDirection.SEND){
            String[] items = new String[]{"删除消息"}; //"撤回消息"
            OptionsPopupDialog.newInstance(context, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
                        dteMessages(100,message);
                    }
                    //else if (which == 1) {
                    // recallMessage(100,message);
                    //}
                }
            }).show();
        }
    }

    private static final int SEND_TIME = 1001;
    private  Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SEND_TIME:
                    // RongIM.getInstance().deleteMessages(new int[]{getMessage().getMessageId()}, (RongIMClient.ResultCallback) null);
                    mHandler.removeMessages(SEND_TIME);
                    break;
                default:
                    break;
            }
            return true;
         }
    });

    /**
     * 撤回消息
     *
     * @param time    执行时间
     * @param message
     */
    private void recallMessage(int time, UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().recallMessage(message.getMessage(), null);
            }
        }, time);
    }


    /**
     * 阅后删除消息
     *
     * @param message
     */
    private void deleteMessages(UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
            }
        },  1000);
    }

    /**
     * 删除消息
     *
     * @param message
     */
    private void dteMessages(int time,UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
            }
        }, time);
    }


    /**
     * 删除撤回消息
     *
     * @param message
     */
    private void deleteReceiveMessages(UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
                recallMessage(100, message);
            }
        },  1000);
    }

}