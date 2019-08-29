package com.hqmy.market.view.adapter;

import android.content.Context;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NoticeDto;

public class MessageCenterAdapter extends BaseQuickAdapter<NoticeDto, BaseViewHolder> {
    public MessageCenterAdapter(Context context) {
        super(R.layout.item_message_center, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeDto item) {
//        helper.setText(R.id.tv_message_type, item.getTime())
//                .setText(R.id.tv_message_time, item.getTime())
//                .setText(R.id.tv_message_content, item.getDescribe())
//                .setVisible(R.id.iv_message_point, item.isHasNewMsg());
//        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_message_icon));
    }
}
