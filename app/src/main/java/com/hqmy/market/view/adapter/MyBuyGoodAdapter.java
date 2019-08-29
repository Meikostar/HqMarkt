package com.hqmy.market.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;

import java.util.List;

public class MyBuyGoodAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    Context mContext;
    int mType;

    public MyBuyGoodAdapter(Context mContext, int mType) {
        super(R.layout.item_my_buy_good, null);
        this.mContext = mContext;
        this.mType = mType;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {
        helper.setText(R.id.tv_item_my_buy_good_time, item.getCreated_at());
        if (item.getItems() != null && item.getItems().getData() != null) {
            helper.setVisible(R.id.recyclerView_goods, true);
            setGoodsListData(helper.getView(R.id.recyclerView_goods), item.getItems().getData(), item.getStatus());
        } else {
            helper.setGone(R.id.recyclerView_goods, false);
        }
        helper.setGone(R.id.ll_button, false)
                .setGone(R.id.tv_button_1, false)
                .setGone(R.id.tv_button_2, false)
                .addOnClickListener(R.id.tv_button_1)
                .addOnClickListener(R.id.tv_button_2);
        switch (item.getStatus()) {
            case "created"://待支付
//                if (mType == 0) {
//                    helper.setVisible(R.id.ll_button, true)
//                            .setVisible(R.id.tv_button_1, true)
//                            .setVisible(R.id.tv_button_2, true)
//                            .setText(R.id.tv_button_1, "取消订单")
//                            .setText(R.id.tv_button_2, "去付款");
//                }

                break;
            case "paid"://待发货
                helper.setVisible(R.id.ll_button, true);
                if (mType == 1) {
                    helper.setVisible(R.id.tv_button_2, true)
                            .setText(R.id.tv_button_2, "去发货");
                }
                break;
            case "shipping"://已发货
                if (mType == 0) {
                    helper.setVisible(R.id.ll_button, true)
                            .setVisible(R.id.tv_button_1, true)
                            .setVisible(R.id.tv_button_2, true)
                            .setText(R.id.tv_button_1, "查看物流")
                            .setText(R.id.tv_button_2, "确认收货");
                }
                break;
        }


    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items, String status) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        MyBuyGoodSubAdapter myBuyGoodSubAdapter = new MyBuyGoodSubAdapter(items, status,mType);
        recyclerView.setAdapter(myBuyGoodSubAdapter);
    }
}
