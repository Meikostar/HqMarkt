package com.hqmy.market.qiniu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.qiniu.chatroom.message.ChatroomGift;
import com.hqmy.market.qiniu.chatroom.message.ChatroomJifen;
import com.hqmy.market.qiniu.chatroom.message.ChatroomLike;
import com.hqmy.market.qiniu.chatroom.message.ChatroomUserQuit;
import com.hqmy.market.qiniu.chatroom.message.ChatroomWelcome;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;
//import com.zhangjiajie.love.R;
//import com.zhangjiajie.love.qiniu.chatroom.message.ChatroomGift;
//import com.zhangjiajie.love.qiniu.chatroom.message.ChatroomJifen;
//import com.zhangjiajie.love.qiniu.chatroom.message.ChatroomLike;
//import com.zhangjiajie.love.qiniu.chatroom.message.ChatroomUserQuit;
//import com.zhangjiajie.love.qiniu.chatroom.message.ChatroomWelcome;
//
//import io.rong.imlib.model.MessageContent;
//import io.rong.message.TextMessage;

public class LiveVideoViewAdapter extends BaseQuickAdapter<MessageContent, BaseViewHolder> {
    public LiveVideoViewAdapter() {
        super(R.layout.live_video_view_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageContent item) {
        helper.setGone(R.id.tv_content, true);
        if (item instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) item;
            if (textMessage.getUserInfo() != null) {
                helper.setText(R.id.tv_user_name, textMessage.getUserInfo().getName());
            } else {
                helper.setText(R.id.tv_user_name, "");
            }
            helper.setText(R.id.tv_content, textMessage.getContent())
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.color_fed55a));
        } else if (item instanceof ChatroomGift) {
            ChatroomGift chatroomGift = (ChatroomGift) item;
            helper.setText(R.id.tv_user_name, chatroomGift.getName());
            helper.setText(R.id.tv_content, "送主播一个" + chatroomGift.getGift_name())
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.color_fed55a));
        } else if (item instanceof ChatroomWelcome) {
            ChatroomWelcome chatroomWelcome = (ChatroomWelcome) item;
            helper.setText(R.id.tv_user_name, chatroomWelcome.getName());
            helper.setText(R.id.tv_content, "加入了直播间")
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.white));
        } else if (item instanceof ChatroomUserQuit) {
            ChatroomUserQuit chatroomUserQuit = (ChatroomUserQuit) item;
            helper.setText(R.id.tv_user_name, chatroomUserQuit.getName());
            helper.setText(R.id.tv_content, "退出了直播间")
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.white));
        } else if (item instanceof ChatroomLike) {
            ChatroomLike chatroomLike = (ChatroomLike) item;
            helper.setText(R.id.tv_user_name, chatroomLike.getName());
            helper.setText(R.id.tv_content, "给主播点了" + chatroomLike.getCounts() + "个赞")
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.color_fed55a));
        } else if (item instanceof ChatroomJifen) {
            ChatroomJifen jifen = (ChatroomJifen) item;
            helper.setText(R.id.tv_user_name, jifen.getName());
            helper.setText(R.id.tv_content, "给主播点送了" + jifen.getJifen() + "积分")
                    .setTextColor(R.id.tv_content, mContext.getResources().getColor(R.color.color_fed55a));
        } else {
            helper.setVisible(R.id.tv_content, false);
        }
    }
}
