package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class MyBuyGoodSubAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {
    private String status;
    private int mType;

    public MyBuyGoodSubAdapter(List<MyOrderItemDto> items, String status, int mType) {
        super(R.layout.item_my_buy_good_sub, items);
        this.status = status;
        this.mType = mType;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_my_buy_good_icon), Constants.WEB_IMG_URL_UPLOADS + item.getProduct().getData().getCover());
        helper.setText(R.id.tv_item_my_buy_good_name, item.getTitle())
                .setText(R.id.tv_item_my_buy_good_integral, item.getScore() + "积分");
        switch (status) {
            case "created"://待支付
                if (mType == 0) {
                    helper.setText(R.id.tv_status, "待付款");
                } else {
                    helper.setText(R.id.tv_status, "待买家付款");
                }
                break;
            case "paid"://待发货
                helper.setText(R.id.tv_status, "待发货");
                break;
            case "shipping"://已发货
                if (mType == 0) {
                    helper.setText(R.id.tv_status, "待收货");
                } else {
                    helper.setText(R.id.tv_status, "待买家收货");
                }
                break;
            case "shipped"://待评价
                helper.setText(R.id.tv_status, "已收货");
                break;
            case "closed":
                //已关闭
                helper.setText(R.id.tv_status, "已关闭");
                break;
            case "cancelled":
                //已取消
                helper.setText(R.id.tv_status, "已取消");
                break;
            case "completed":
                //已完成
                helper.setText(R.id.tv_status, "已完成");
                break;
        }
    }

}
