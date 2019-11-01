package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
public class LiveProductItemAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {

    public LiveProductItemAdapter(Context context) {
        super(R.layout.item_live_product_item, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_img), Constants.WEB_IMG_URL_UPLOADS+item.getCover());
        helper.setText(R.id.tv_name, item.getTitle())
                .setText(R.id.tv_price, "¥" + item.getPrice());

        helper.addOnClickListener(R.id.rl_bg)
                .addOnClickListener(R.id.img);

    }
}
