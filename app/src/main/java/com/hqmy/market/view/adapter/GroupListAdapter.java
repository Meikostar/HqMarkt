package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.GroupListDto;
import com.hqmy.market.common.utils.GlideUtils;

public class GroupListAdapter extends BaseQuickAdapter<GroupListDto, BaseViewHolder> {

    public GroupListAdapter() {
        super(R.layout.item_group_list_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupListDto item) {
        helper.setText(R.id.tv_group_name, item.getGroup_name());
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.tv_group_icon), item.getAvatar());
    }
}
