package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.LearnRecordInfo;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class LearnRecordAdapter extends BaseQuickAdapter<LearnRecordInfo, BaseViewHolder> {
    public LearnRecordAdapter() {
        super(R.layout.learn_record_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, LearnRecordInfo item) {
        if (item != null && item.getObject() != null && item.getObject().getData() != null){
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS + item.getObject().getData().getCover());
            helper.setText(R.id.tv_title, item.getObject().getData().getTitle());
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS);
            helper.setText(R.id.tv_title, "");
        }

    }
}
