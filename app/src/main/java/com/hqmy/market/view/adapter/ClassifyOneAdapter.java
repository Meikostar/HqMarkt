package com.hqmy.market.view.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.StoreCategoryDto;
;

import java.util.List;


/**
 * Created by lihaoqi on 2019/01/25.
 */

public class ClassifyOneAdapter extends BaseQuickAdapter<StoreCategoryDto,BaseViewHolder> {



    public int selctedPos = 0;

    public ClassifyOneAdapter() {
        super(R.layout.item_classify_one);
    }

    public ClassifyOneAdapter(@Nullable List<StoreCategoryDto> data) {
        super(R.layout.item_classify_one, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreCategoryDto item) {
        int pos = helper.getAdapterPosition();
        if(selctedPos==pos){
            helper.setTextColor(R.id.tv_name,Color.parseColor("#FF008CD6"));
            helper.setBackgroundColor(R.id.tv_name,Color.parseColor("#FFF1F1F1"));
        }else{
            helper.setTextColor(R.id.tv_name,mContext.getResources().getColor(R.color.my_color_333333));
            helper.setBackgroundColor(R.id.tv_name,mContext.getResources().getColor(R.color.my_color_white));
        }
        helper.setText(R.id.tv_name,item.getCategoryName());
    }


    @Override
    public void setNewData(@Nullable List<StoreCategoryDto> data) {
        super.setNewData(data);
        selctedPos = 0;
    }


}
