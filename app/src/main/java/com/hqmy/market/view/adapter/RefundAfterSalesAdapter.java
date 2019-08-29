package com.hqmy.market.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;

import java.util.List;

public class RefundAfterSalesAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public RefundAfterSalesAdapter(Context context) {
        super(R.layout.item_refund_after_sales, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {
        if (item.getOrder() != null&&item.getOrder().getData() != null && item.getOrder().getData().getShop() != null) {
            helper.setText(R.id.tv_item_order_store_name, item.getOrder().getData().getShop().getShop_name());
        } else {
            helper.setText(R.id.tv_item_order_store_name, "");
        }
        RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);
        if (item.getOrder() != null&&item.getOrder().getData() != null && item.getOrder().getData().getItems() != null && item.getOrder().getData().getItems().getData() != null && item.getOrder().getData().getItems().getData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            setGoodsListData(recyclerView, item.getOrder().getData().getItems().getData());
        } else {
            recyclerView.setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_itme_refund_after_sales_text,item.getStatus_msg());
        helper.addOnClickListener(R.id.tv_item_refund_after_sales_detail);
    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new OrderGoodsListAdapter(items));
    }

}
