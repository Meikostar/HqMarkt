package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class MyFootPrintSubAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {
    public MyFootPrintSubAdapter(List<MyOrderItemDto> items) {
        super(R.layout.item_my_buy_good_sub, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        if (item.getObject() != null && item.getObject().getData() != null){
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_my_buy_good_icon), Constants.WEB_IMG_URL_UPLOADS+item.getObject().getData().getCover());
            helper.setText(R.id.tv_item_my_buy_good_name,item.getObject().getData().getTitle())
                    .setText(R.id.tv_item_my_buy_good_integral,"¥"+item.getObject().getData().getPrice());
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_my_buy_good_icon), R.drawable.glide_default_picture);
            helper.setText(R.id.tv_item_my_buy_good_name,"")
                    .setText(R.id.tv_item_my_buy_good_integral,"");
        }

    }

}
