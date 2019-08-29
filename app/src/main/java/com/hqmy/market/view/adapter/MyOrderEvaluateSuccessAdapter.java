package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ProductBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class MyOrderEvaluateSuccessAdapter extends BaseQuickAdapter<ProductBean, BaseViewHolder> {
    public MyOrderEvaluateSuccessAdapter() {
        super(R.layout.adapter_myorder_evaluate_success_layout, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_goods_icon), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
        helper.setText(R.id.tv_goods_name, item.getTitle())
                .setText(R.id.tv_goods_price, "￥" + item.getPrice());
        helper.setGone(R.id.tv_status, false);
        if (item.isIs_hot()) {
            helper.setVisible(R.id.tv_status, true)
                    .setText(R.id.tv_status, "爆款");
        }else if (item.isIs_new()){
            helper.setVisible(R.id.tv_status, true)
                    .setText(R.id.tv_status, "新品");
        }else if (item.isIs_recommend()){
            helper.setVisible(R.id.tv_status, true)
                    .setText(R.id.tv_status, "特惠");
        }else if (item.isOn_sale()){
            helper.setVisible(R.id.tv_status, true)
                    .setText(R.id.tv_status, "在售");
        }

    }
}
