package com.hqmy.market.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class MyOrderEvaluateAdapter extends BaseQuickAdapter<MyOrderItemDto, BaseViewHolder> {
    private Context mContext;

    public MyOrderEvaluateAdapter(List<MyOrderItemDto> data, Context mContext) {
        super(R.layout.item_myorder_evaluate_layout, data);
        this.mContext = mContext;
    }


    @Override
    protected void convert(BaseViewHolder helper, MyOrderItemDto item) {
        helper.setText(R.id.tv_evaluate_des, item.getTitle())
                .setText(R.id.tv_evaluate_money, "￥" + item.getPrice())
                .setText(R.id.tv_evaluate_number, "X" + item.getQty());
        String imgUrl = Constants.WEB_IMG_URL_UPLOADS + item.getProduct().getData().getCover();
        GlideUtils.getInstances().loadNormalImg(mContext, (ImageView) helper.getView(R.id.tv_evaluate_logo), imgUrl, R.mipmap.img_default_1);
        String rating = item.getRating();
        if (item.getComment() != null && item.getComment().getData() != null) {
            helper.setText(R.id.tv_evaluate_discuss, "已评价");
            helper.setOnClickListener(R.id.tv_evaluate_discuss,null);
        } else {
            helper.setText(R.id.tv_evaluate_discuss, "去评价");
            helper.addOnClickListener(R.id.tv_evaluate_discuss);
        }
        if (item.getOptions() != null) {
            StringBuilder options = new StringBuilder();
            if (item.getOptions() != null && item.getOptions().get("尺码") != null) {
                options.append(item.getOptions().get("尺码") + ",");
            }
            if (item.getOptions() != null && item.getOptions().get("颜色") != null) {
                options.append(item.getOptions().get("颜色"));
            }
            helper.setText(R.id.tv_order_goods_params, options.toString());
        } else {
            helper.setText(R.id.tv_order_goods_params, "");
        }
    }
}
