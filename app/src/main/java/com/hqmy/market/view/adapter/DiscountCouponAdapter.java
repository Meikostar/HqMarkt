package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CouponDto;

public class DiscountCouponAdapter extends BaseQuickAdapter<CouponDto, BaseViewHolder> {
    public DiscountCouponAdapter(Context context) {
        super(R.layout.item_discount_coupon, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponDto item) {
        helper.setText(R.id.tv_coupon_expire_date, "有效期:" + item.getStart_at() + "-" + item.getExpire_at());
        if (item.getCoupon() != null && item.getCoupon().getData() != null) {
            if ("2".equals(item.getCoupon().getData().getDiscount_type())) {
                helper.setText(R.id.tv_coupon_money, Double.valueOf(item.getCoupon().getData().getDiscount_value()) * 10 + "折")
                        .setText(R.id.tv_coupon_title, Double.valueOf(item.getCoupon().getData().getDiscount_value()) * 10 + "折优惠券");
            } else {
                helper.setText(R.id.tv_coupon_money, "￥" + item.getCoupon().getData().getDiscount_value())
                        .setText(R.id.tv_coupon_title, item.getCoupon().getData().getDiscount_value() + "元优惠券");
            }
        }else {
            helper.setText(R.id.tv_coupon_money, "")
                    .setText(R.id.tv_coupon_title, "");
        }
        if (item.getUsed()) {
            helper.setText(R.id.tv_coupon_status, "已使用")
                    .setTextColor(R.id.tv_coupon_money, mContext.getResources().getColor(R.color.my_color_666666))
                    .setTextColor(R.id.tv_coupon_title, mContext.getResources().getColor(R.color.my_color_666666))
                    .setTextColor(R.id.tv_coupon_expire_date, mContext.getResources().getColor(R.color.my_color_666666))
                    .setTextColor(R.id.tv_coupon_status, mContext.getResources().getColor(R.color.my_color_666666))
                    .setBackgroundColor(R.id.line, mContext.getResources().getColor(R.color.my_color_666666));
        } else {
            helper.setText(R.id.tv_coupon_status, "可使用")
                    .setTextColor(R.id.tv_coupon_money, mContext.getResources().getColor(R.color.pldroid_streaming_red))
                    .setTextColor(R.id.tv_coupon_title, mContext.getResources().getColor(R.color.pldroid_streaming_red))
                    .setTextColor(R.id.tv_coupon_expire_date, mContext.getResources().getColor(R.color.pldroid_streaming_red))
                    .setTextColor(R.id.tv_coupon_status, mContext.getResources().getColor(R.color.pldroid_streaming_red))
                    .setBackgroundColor(R.id.line, mContext.getResources().getColor(R.color.pldroid_streaming_red));
        }

    }
}
