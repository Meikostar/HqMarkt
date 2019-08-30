package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

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

    }
}
