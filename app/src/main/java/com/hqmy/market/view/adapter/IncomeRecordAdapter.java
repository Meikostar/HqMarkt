package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.IncomeDto;
import com.hqmy.market.bean.MoneyRecordItemDto;


/**
 * MYCC 记录适配器
 */
public class IncomeRecordAdapter extends BaseQuickAdapter<IncomeDto, BaseViewHolder> {


    public IncomeRecordAdapter() {
        super(R.layout.item_record_layout);
    }


    @Override
    protected void convert(BaseViewHolder helper, IncomeDto item) {
        helper.setText(R.id.tv_des, item.type_name)
                .setText(R.id.tv_date, item.created_at);
        if(Double.valueOf(item.value)>0){
            helper.setText(R.id.tv_money, "+" + item.value);
        }else {
            helper.setText(R.id.tv_money, item.value);
        }

    }
}