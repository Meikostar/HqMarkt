package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.OrderProductDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 * 确认订单Item项的订单
 * Created by rzb on 2019/06/19
 */
public class ConfirmOrderItemAdapter extends BaseQuickAdapter<OrderProductDto, BaseViewHolder> {

    public ConfirmOrderItemAdapter(List<OrderProductDto> data) {
        super(R.layout.item_confirm_order_level_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProductDto item) {
        helper.setText(R.id.tv_item_confirm_order_title, item.getName())
                .setText(R.id.tv_item_confirm_order_price, item.getPrice());
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_confirm_order_cover),
                Constants.WEB_IMG_URL_UPLOADS + item.getCover());
    }
}