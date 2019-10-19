package com.hqmy.market.view.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.activity.RefundAfterSalesActivity;
import com.hqmy.market.view.activity.RefundAfterSalesDetailActivity;

import java.util.List;

/**
 * 订单列表中 商品的adapter
 */
public class OrderGoodsListAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public OrderGoodsListAdapter(List<MyOrderItemDto> items) {
        super(R.layout.item_order_goods_list, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_img), Constants.WEB_IMG_URL_UPLOADS+item.getProduct().getData().getCover());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_cout, "x" + item.getQty())
                .setText(R.id.tv_price, "￥" + item.getPrice());
        String content="";
        helper.addOnClickListener(R.id.rl_bg);
        if(item.getOptions()!=null){
            int i=0;

            //value
            for(String value : item.getOptions().values()){
                if(i==0){
                    content=value;
                }else {
                    content=content+","+value;
                }
            }
            helper.setText(R.id.tv_gg,content);
        }else {
            helper.setText(R.id.tv_gg,"");
        }




    }
}
