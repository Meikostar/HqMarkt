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
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_order_goods_icon), Constants.WEB_IMG_URL_UPLOADS+item.getProduct().getData().getCover());
        helper.setText(R.id.tv_order_goods_name, item.getTitle())
                .setText(R.id.tv_order_goods_price, "￥" + item.getPrice())
                .setText(R.id.tv_order_goods_num, "x" + item.getQty());
        if (item.getOptions() != null) {
            StringBuilder options = new StringBuilder();
            if (item.getOptions() != null && item.getOptions().get("尺码") != null) {
                options.append(item.getOptions().get("尺码") + ",");
            }
            if (item.getOptions() != null && item.getOptions().get("颜色") != null) {
                options.append(item.getOptions().get("颜色"));
            }
            if (item.getOptions() != null && item.getOptions().get("套餐") != null) {
                options.append(item.getOptions().get("套餐"));
            }
            helper.setText(R.id.tv_order_goods_params, options.toString());
        } else {
            helper.setText(R.id.tv_order_goods_params, "");
        }
    }
}
