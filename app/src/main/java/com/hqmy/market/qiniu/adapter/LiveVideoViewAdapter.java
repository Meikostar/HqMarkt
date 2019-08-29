package com.hqmy.market.qiniu.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.qiniu.chatroom.message.ChatRoomGift;
import com.hqmy.market.qiniu.chatroom.message.ChatroomWelcome;

import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class LiveVideoViewAdapter extends BaseQuickAdapter<MessageContent, BaseViewHolder> {
    public LiveVideoViewAdapter() {
        super(R.layout.live_video_view_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageContent item) {
        if (item.getUserInfo() != null) {
            helper.setText(R.id.tv_user_name, item.getUserInfo().getName());
        }
        helper.setGone(R.id.tv_content, true);
        if (item instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) item;
            helper.setText(R.id.tv_content, textMessage.getContent());
        } else if (item instanceof ChatRoomGift) {
            ChatRoomGift chatroomGift = (ChatRoomGift) item;
            if (chatroomGift.getType() == 1) {
                helper.setText(R.id.tv_content, "给主播点了一个赞");
            } else if (chatroomGift.getType() == 2) {
                helper.setText(R.id.tv_content, "给主播打赏了" + chatroomGift.getContent() + "积分");
            } else {
                helper.setText(R.id.tv_content, "");
            }
        } else if (item instanceof ChatroomWelcome) {
            helper.setText(R.id.tv_content, "加入直播室");
        } else {
            helper.setGone(R.id.tv_content, false);
        }
    }
}
