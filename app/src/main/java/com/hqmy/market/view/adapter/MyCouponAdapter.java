package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CouponDto;

public class MyCouponAdapter extends BaseQuickAdapter<CouponDto, BaseViewHolder> {
    private int selectPosition;

    public MyCouponAdapter(Context context,int currentPosition) {
        super(R.layout.item_my_coupon, null);
        this.selectPosition = currentPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponDto item) {
        helper.setText(R.id.tv_coupon_expire_date, "有效期:" + item.getStart_at() + "-" + item.getExpire_at());
        if (item.getCoupon() != null && item.getCoupon().getData() != null) {
            if ("2".equals(item.getCoupon().getData().getDiscount_type())) {
                helper.setText(R.id.tv_coupon_title, Double.valueOf(item.getCoupon().getData().getDiscount_value()) * 10 + "折优惠券");
            } else {
                helper.setText(R.id.tv_coupon_title, item.getCoupon().getData().getDiscount_value() + "元优惠券");
            }
        } else {
            helper.setText(R.id.tv_coupon_money, "")
                    .setText(R.id.tv_coupon_title, "");
        }
        helper.getView(R.id.cb_shop_cart_all_sel).setSelected(selectPosition == helper.getAdapterPosition());
    }
}
