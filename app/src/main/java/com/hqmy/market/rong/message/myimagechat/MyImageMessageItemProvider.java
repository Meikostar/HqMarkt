package com.hqmy.market.rong.message.myimagechat;

/**
 * 自定义图片消息
 * Created by rzb on 2019/4/20.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hqmy.market.R;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.message.utils.BitmapUtil;

@ProviderTag(
        messageContent = MyImageMessage.class,
        showProgress = false,
        showReadState = true
)
public class MyImageMessageItemProvider extends IContainerItemProvider.MessageProvider<MyImageMessage> {
    private static final String TAG = "MyImageMessageItemProvider";

    public MyImageMessageItemProvider() {
    }

    public View newView(Context context, ViewGroup group) {
//      View view = LayoutInflater.from(context).inflate(io.rong.imkit.R.layout.rc_item_image_message, (ViewGroup)null);
        View view = LayoutInflater.from(context).inflate(R.layout.ry_item_myimage_message_layout, (ViewGroup) null);
        MyImageMessageItemProvider.ViewHolder holder = new MyImageMessageItemProvider.ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.rc_msg);
        holder.img = (AsyncImageView) view.findViewById(R.id.rc_img);
//      holder.iv_add_lock = (ImageView) view.findViewById(R.id.iv_add_lock);
//      holder.tv_add_lock_icon = (ImageView) view.findViewById(R.id.tv_add_lock_icon);
//      holder.tvLeftTime = (ImageView) view.findViewById(R.id.tv_left_time);
//      holder.tvRightTime = (ImageView) view.findViewById(R.id.tv_right_time);
        view.setTag(holder);
        return view;
    }

    public void onItemClick(View view, int position, MyImageMessage content, UIMessage message) {
        if (content != null) {
//          Intent intent = new Intent("io.rong.imkit.intent.action.picturepagerview");
//          intent.setPackage(view.getContext().getPackageName())
            Intent intent = new Intent(view.getContext(), MyPicturePagerActivity.class);
            intent.putExtra("message", message.getMessage());
            view.getContext().startActivity(intent);
//         if (message.getMessageDirection() == MessageDirection.RECEIVE) {//消息方向
//          holder.tvRightTime.setBackgroundResource(R.mipmap.rc_plugin_read_lock_open);
//          holder.tvLeftTime.setBackgroundResource(R.mipmap.rc_plugin_read_lock_open);
//          deleteReceiveMessages(message);
//          }
        }
    }

    @Override
    public void onItemLongClick(View view, int position, MyImageMessage content, final UIMessage message){
        showOptDialog(view.getContext(),message);
    }

    public void bindView(View v, int position, MyImageMessage content, UIMessage message) {
        MyImageMessageItemProvider.ViewHolder holder = (MyImageMessageItemProvider.ViewHolder) v.getTag();
        if (message.getMessageDirection() == MessageDirection.SEND) {
//            v.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_right);
//            holder.rcMessageFrameLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_right);
//            holder.iv_add_lock.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_right);
//            holder.tvLeftTime.setVisibility(View.VISIBLE);
//            holder.tvRightTime.setVisibility(View.GONE);
        } else {
//            v.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_left);
//            holder.rcMessageFrameLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_left);
//            holder.iv_add_lock.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_left);
//            holder.tvRightTime.setVisibility(View.VISIBLE);
//            holder.tvLeftTime.setVisibility(View.GONE);
        }
        if (content.isDestruct()) {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(content.getThumUri().toString());
            if (bitmap != null) {
                Bitmap blurryBitmap = BitmapUtil.getBlurryBitmap(v.getContext(), bitmap, 5.0F, 0.25F);
                holder.img.setBitmap(blurryBitmap);
            }
        } else {
            holder.img.setResource(content.getThumUri());
        }

        int progress = message.getProgress();
        SentStatus status = message.getSentStatus();
        if (status.equals(SentStatus.SENDING) && progress < 100) {
            holder.message.setText(progress + "%");
            holder.message.setVisibility(View.VISIBLE);
//            holder.tv_add_lock_icon.setVisibility(View.GONE);
//            if (holder.iv_add_lock.getVisibility() == View.VISIBLE) {
//                holder.iv_add_lock.setVisibility(View.GONE);
//            }
        } else {
            holder.message.setVisibility(View.GONE);
//            holder.tv_add_lock_icon.setVisibility(View.VISIBLE);
//            holder.iv_add_lock.setVisibility(View.VISIBLE);
        }

    }

    public Spannable getContentSummary(MyImageMessage data) {
        return null;
    }

    public Spannable getContentSummary(Context context, MyImageMessage data) {
       return new SpannableString(context.getString(io.rong.imkit.R.string.rc_message_content_image));
//     return new SpannableString("[阅后即焚图片]");
    }

    private static class ViewHolder {
        AsyncImageView img;
        TextView       message;
        //ImageView iv_add_lock,tv_add_lock_icon;

        private ViewHolder() {
        }
    }

    private static final int SEND_TIME = 1001;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SEND_TIME:
//                    RongIM.getInstance().deleteMessages(new int[]{getMessage().getMessageId()}, (RongIMClient.ResultCallback) null);
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
     * 删除消息
     *
     * @param message
     */
    private void deleteMessages(int time,UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
            }
        }, time);
    }


    /**
     * 删除消息
     *
     * @param message
     */
    private void deleteReceiveMessages(UIMessage message) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()}, null);
                recallMessage(100, message);
            }
        }, 1000);
    }

    private void showOptDialog(Context context, UIMessage message) {
        if(message.getMessageDirection() == MessageDirection.RECEIVE) {
            String[] items = new String[]{"删除消息"};
            OptionsPopupDialog.newInstance(context, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
                        deleteMessages(100,message);
                    }
                }
            }).show();
        }else if(message.getMessageDirection() == MessageDirection.SEND){
            String[] items = new String[]{"删除消息","撤回消息"};
            OptionsPopupDialog.newInstance(context, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if (which == 0) {
                        deleteMessages(100,message);
                    } else if (which == 1) {
                        recallMessage(100, message);
                    }
                }
            }).show();
        }
    }
}
