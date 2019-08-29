package com.hqmy.market.view.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

public class MyFootprintAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public MyFootprintAdapter() {
        super(R.layout.item_my_foot_print, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {
        helper.setText(R.id.tv_item_my_buy_good_time, item.getDate());
        if (item.getObject() != null && item.getObject().getData() != null) {
            helper.setVisible(R.id.recyclerView_goods, true);
            setGoodsListData(helper.getView(R.id.recyclerView_goods), item.getObject().getData());
        } else {
            helper.setGone(R.id.recyclerView_goods, false);
        }

    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        MyFootPrintSubAdapter myFootPrintSubAdapter = new MyFootPrintSubAdapter(items);
        recyclerView.setAdapter(myFootPrintSubAdapter);
        myFootPrintSubAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyOrderItemDto myOrderDto = myFootPrintSubAdapter.getItem(position);
                if (myOrderDto.getObject() != null && myOrderDto.getObject().getData() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, myOrderDto.getObject().getData().getId());
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, myOrderDto.getObject().getData().getType());
                    Intent intent = new Intent(mContext,CommodityDetailActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
