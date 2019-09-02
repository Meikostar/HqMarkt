package com.hqmy.market.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.BaseDto2;
import com.hqmy.market.bean.StoreCategoryDto;
import com.hqmy.market.view.widgets.FullyGridLayoutManager;

import java.util.List;

/**
 * Created by lihaoqi on 2019/1/25.
 */

public class ClassifyTwoAdapter extends BaseQuickAdapter<BaseDto2,BaseViewHolder> {
    private Context mContext;

    public ClassifyTwoAdapter(Context context) {
        super(R.layout.item_classify_two);
        this.mContext = context;
    }


    public ClassifyTwoAdapter(Context context, @Nullable List<BaseDto2> data) {
        super(R.layout.item_classify_two, data);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, BaseDto2 item) {
        helper.setText(R.id.tv_name,item.title);
        RecyclerView rvlist = helper.getView(R.id.rv_three);
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(mContext,3);
        rvlist.setLayoutManager(gridLayoutManager);
        ClassifyThreeAdapter classifyThreeAdapter  = new ClassifyThreeAdapter(mContext,item.children.data);
        classifyThreeAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //TODO 实现 跳转
            }
        });
        rvlist.setAdapter(classifyThreeAdapter);
    }


}
