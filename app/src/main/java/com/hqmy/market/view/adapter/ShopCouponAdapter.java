package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.CouponBean;

import java.util.List;

public class ShopCouponAdapter extends MyBaseAdapter<CouponBean> {
    ShopCouponAdapter.ViewHolder holder;
    Context mContext;

    public ShopCouponAdapter(Context context, List<CouponBean> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_shop_coupon, null);
        holder = new ShopCouponAdapter.ViewHolder();
        holder.tv_coupon_title = (TextView) view.findViewById(R.id.tv_coupon_title);
        holder.tv_coupon_description = (TextView) view.findViewById(R.id.tv_coupon_description);
        holder.tv_coupon_expire_date = (TextView) view.findViewById(R.id.tv_coupon_expire_date);
        holder.tv_coupon_status = (TextView) view.findViewById(R.id.tv_coupon_status);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, CouponBean item) {
        holder = (ShopCouponAdapter.ViewHolder) var1.getTag();
        switch (item.getDay_type()) {
            case "1":
                holder.tv_coupon_expire_date.setText("有效期:" + item.getShow_start() + "-" + item.getShow_end());
                break;
            case "2":
                holder.tv_coupon_expire_date.setText("领取后" + item.getDays() + "天内有效");
                break;
            default:
                holder.tv_coupon_expire_date.setText("领取后 第二天起" + item.getDays() + "天内有效");
                break;
        }
        if (item.getLimit() > 0 && item.getLimit() > item.getUser_coupons_count()) {
            holder.tv_coupon_status.setText("立即领取");
        } else {
            holder.tv_coupon_status.setText("已领取");
        }
        if ("2".equals(item.getDiscount_type())) {
            holder.tv_coupon_title.setText(Double.valueOf(item.getDiscount_value()) * 10 + "折优惠券");
        } else {
            holder.tv_coupon_title.setText("¥" + item.getDiscount_value() + "元优惠券");
        }
        if ("2".equals(item.getCoupon_type())) {
            holder.tv_coupon_description.setVisibility(View.VISIBLE);
            holder.tv_coupon_description.setText("需用" + item.getPrice() + "积分换购");
        } else {
            holder.tv_coupon_description.setVisibility(View.GONE);
        }

    }

    class ViewHolder {
        TextView tv_coupon_title;
        TextView tv_coupon_description;
        TextView tv_coupon_expire_date;
        TextView tv_coupon_status;
    }
}
