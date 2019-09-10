package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.view.activity.MyOrderDetailActivity;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<MyOrderDto, BaseViewHolder> {
    public OrderAdapter(Context context) {
        super(R.layout.item_order_fragment, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderDto item) {
        helper.addOnClickListener(R.id.item_order_content);
        if (item.getShop() != null) {
            helper.setText(R.id.tv_shop_name, item.getShop().getShop_name());
        } else {
            helper.setText(R.id.tv_shop_name, "");
        }
        RecyclerView recyclerView = helper.getView(R.id.item_order_goods_list);

        if (item.getItems() != null && item.getItems().getData() != null && item.getItems().getData().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            setGoodsListData(recyclerView, item.getItems().getData(), item);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
        helper.setGone(R.id.tv_button_1, false)
                .setGone(R.id.tv_button_2, false)
                .addOnClickListener(R.id.tv_button_1)
                .addOnClickListener(R.id.tv_button_2);
        if (item.getTotal() != null) {
            helper.setText(R.id.tv_prices, "￥"+item.getTotal());
        }
        helper.setText(R.id.tv_status, item.getStatus_msg());
        helper.setText(R.id.tv_detail, "共"+item.getCount()+"件商品, "+"已付款");
        switch (item.getStatus()) {
            case "created"://待支付
                helper.setVisible(R.id.tv_button_1, true)
                        .setVisible(R.id.tv_button_2, true)
                        .setText(R.id.tv_button_1, "取消订单")
                        .setText(R.id.tv_button_2, "去付款")
                        .setText(R.id.tv_detail, "共"+item.getCount()+"件商品, "+"未付款");

                break;
            case "paid"://待发货
                helper.setVisible(R.id.tv_button_1, true);
                if (item.getRefundInfo() != null && item.getRefundInfo().getData() != null) {
                    //退款中
                    helper.setText(R.id.tv_button_1, "退款中");
                } else {
                    helper.setVisible(R.id.tv_button_2, true)
                            .setText(R.id.tv_button_1, "申请退款")
                            .setText(R.id.tv_button_2, "催发货");
                }


                break;
            case "shipping"://已发货
                helper.setVisible(R.id.tv_button_2, true);
                if ("待消费".equals(item.getStatus_msg())) {
                    helper.setVisible(R.id.tv_button_1, false)
                            .setText(R.id.tv_button_2, "查看兑换券");
                } else {
                    helper.setVisible(R.id.tv_button_1, true)
                            .setText(R.id.tv_button_1, "查看物流")
                            .setText(R.id.tv_button_2, "确认收货");
                }

                break;
            case "shipped"://待评价
                helper.setText(R.id.tv_item_order_status, "待评价");
                helper.setVisible(R.id.tv_button_1, true)
                        .setVisible(R.id.tv_button_2, true)
                        .setText(R.id.tv_button_1, "再来一单")
                        .setText(R.id.tv_button_2, "去评价");
                break;
            default:
                helper.setVisible(R.id.tv_button_1, true)
                        .setText(R.id.tv_button_1, item.getStatus_msg());
                break;
        }
    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items, final MyOrderDto item) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        OrderGoodsListAdapter mAdapter = new OrderGoodsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (item != null) {
                    Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                    intent.putExtra(Constants.INTENT_ID, item.getId());
                    intent.putExtra(Constants.INTENT_TYPE, item.getType());
                    mContext.startActivity(intent);
                    return;
                }
            }
        });

    }
}
