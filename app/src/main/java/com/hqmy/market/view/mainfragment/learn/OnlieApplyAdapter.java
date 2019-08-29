package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ActionBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class OnlieApplyAdapter extends BaseQuickAdapter<ActionBean, BaseViewHolder> {
    public OnlieApplyAdapter(Context context ) {
        super(R.layout.item_action_on_line_apply, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionBean item) {
        helper.setText(R.id.tv_item_action_title, item.getTitle())
                .setText(R.id.tv_item_action_introduce, item.getIntroduce())
                .setText(R.id.tv_item_action_time, item.getCreated_at());
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_action_icon), Constants.WEB_IMG_URL_UPLOADS+item.getImg());
    }
}
