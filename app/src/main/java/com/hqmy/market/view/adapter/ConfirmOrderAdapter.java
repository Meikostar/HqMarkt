package com.hqmy.market.view.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.utils.TextUtil;

import java.util.List;

/**
 * 确认订单适配器
 * Created by rzb on 2019/6/19
 */
public class ConfirmOrderAdapter extends BaseQuickAdapter<OrderShopDto, BaseViewHolder> {
    private ConfirmOrderItemAdapter mConfirmOrderItemAdapter;

    public ConfirmOrderAdapter(List<OrderShopDto> data) {
        super(R.layout.item_confirm_order_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderShopDto item) {
        helper.setText(R.id.tv_dianpu_name, item.shop_name==null?"自营店铺":item.shop_name)
              .setText(R.id.tv_shop_price, "¥" + item.product_total);
        if(item.getFreight() != null) {
            helper.setText(R.id.tv_kuaidi_fee, "¥" + item.getFreight().getFreight());
        }
        RecyclerView rvlist = helper.getView(R.id.recy_confirm_order_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        EditText view = helper.getView(R.id.et_beizhu);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(TextUtil.isNotEmpty(s.toString())){
                     item.comment=s.toString();
                 }else {
                     item.comment="";
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rvlist.setLayoutManager(linearLayoutManager);
        mConfirmOrderItemAdapter  = new ConfirmOrderItemAdapter(item.getProducts());
        rvlist.setAdapter(mConfirmOrderItemAdapter);
    }
}