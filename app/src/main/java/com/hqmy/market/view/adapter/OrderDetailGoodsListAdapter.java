package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * 订单详情中 商品的adapter
 */
public class OrderDetailGoodsListAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public OrderDetailGoodsListAdapter(List<MyOrderItemDto> items) {
        super(R.layout.item_order_detail_goods_list, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_order_goods_icon), Constants.WEB_IMG_URL_UPLOADS+item.getProduct().getData().getCover());
        helper.setText(R.id.tv_order_goods_name, item.getTitle())
                .setText(R.id.tv_order_goods_price, "¥" + item.getPrice())
                .setText(R.id.tv_order_goods_num, "x" + item.getQty());
        String content="";
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
