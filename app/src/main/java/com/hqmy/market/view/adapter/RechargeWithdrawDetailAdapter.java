package com.hqmy.market.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ScoreIncomeBean;

public class RechargeWithdrawDetailAdapter extends BaseQuickAdapter<ScoreIncomeBean, BaseViewHolder> {
    public RechargeWithdrawDetailAdapter(Context context) {
        super(R.layout.item_recharge_withdraw_detail, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreIncomeBean item) {
        String value = item.getValue();
        helper.setText(R.id.tv_item_recharget_withdraw_detail_time,item.getCreated_at());
        if (!TextUtils.isEmpty(value)) {
            if (value.startsWith("-")) {
                helper.setTextColor(R.id.tv_item_recharget_withdraw_detail_price, Color.parseColor("#333333"))
                        .setText(R.id.tv_item_recharget_withdraw_detail_price,value)
                        .setText(R.id.tv_item_recharget_withdraw_detail_type, "提现")
                        .setImageResource(R.id.iv_item_recharget_withdraw_detail_icon, R.mipmap.withdraw);
            } else {
                helper.setText(R.id.tv_item_recharget_withdraw_detail_price, "+" + value)
                        .setText(R.id.tv_item_recharget_withdraw_detail_type, "充值")
                        .setImageResource(R.id.iv_item_recharget_withdraw_detail_icon, R.mipmap.recharget)
                        .setTextColor(R.id.tv_item_recharget_withdraw_detail_price, mContext.getResources().getColor(R.color.my_color_fd2323));
            }
        } else {
            helper.setText(R.id.tv_item_recharget_withdraw_detail_price, "");
        }
//        switch (item) {
//            case RechargeWithdrawDetailFragment.TYPE_ALL:
//                if(helper.getAdapterPosition()%2==0){
//                    setWitjdrawData(helper);
//                }else {
//                    setRechargeData(helper);
//                }
//
//                break;
//            case RechargeWithdrawDetailFragment.TYPE_RECHARGE:
//                setRechargeData(helper);
//                break;
//            case RechargeWithdrawDetailFragment.TYPE_WITHDRAW:
//                setWitjdrawData(helper);
//                break;
//        }
    }

    private void setWitjdrawData(BaseViewHolder helper) {
        helper.setTextColor(R.id.tv_item_recharget_withdraw_detail_price, Color.parseColor("#333333"))
                .setText(R.id.tv_item_recharget_withdraw_detail_price, "+100")
                .setText(R.id.tv_item_recharget_withdraw_detail_type, "提现")
                .setImageResource(R.id.iv_item_recharget_withdraw_detail_icon, R.mipmap.withdraw);
    }

    private void setRechargeData(BaseViewHolder helper) {
        helper.setTextColor(R.id.tv_item_recharget_withdraw_detail_price, Color.parseColor("#FD2323"))
                .setText(R.id.tv_item_recharget_withdraw_detail_price, "+100")
                .setText(R.id.tv_item_recharget_withdraw_detail_type, "充值")
                .setImageResource(R.id.iv_item_recharget_withdraw_detail_icon, R.mipmap.recharget);
    }
}
