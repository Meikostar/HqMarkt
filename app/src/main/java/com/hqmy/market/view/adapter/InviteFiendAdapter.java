package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.InviteFriendBean;

public class InviteFiendAdapter extends BaseQuickAdapter<InviteFriendBean, BaseViewHolder> {
    public InviteFiendAdapter() {
        super(R.layout.item_invite_friends);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteFriendBean item) {
        helper.setText(R.id.tv_phone,item.getPhone())
                .setText(R.id.tv_time,item.getCreated_at());
    }
}
