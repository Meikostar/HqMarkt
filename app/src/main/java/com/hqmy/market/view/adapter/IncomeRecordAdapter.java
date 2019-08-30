package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MoneyRecordItemDto;


/**
 * MYCC 记录适配器
 */
public class IncomeRecordAdapter extends BaseQuickAdapter<MoneyRecordItemDto, BaseViewHolder> {


    public IncomeRecordAdapter() {
        super(R.layout.item_record_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, MoneyRecordItemDto item) {
        helper.setText(R.id.tv_des, item.getTransferRemark())
                .setText(R.id.tv_date, item.getTransferTime());
        helper.setText(R.id.tv_money, "+" + item.getTransferAmount());
    }
}