package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.GiftBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class GifListAdapter extends BaseQuickAdapter<GiftBean, BaseViewHolder> {
    private int currentPosition = 0;

    public GifListAdapter(List<GiftBean> items) {
        super(R.layout.dialog_gif_item, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_thumb), Constants.WEB_IMG_URL_UPLOADS + item.getThumb());
        helper.setText(R.id.tv_gif_name, item.getGift_name())
                .setText(R.id.tv_price, item.getPrice());
        if (currentPosition == helper.getPosition()){
            helper.setBackgroundColor(R.id.ll_item,mContext.getResources().getColor(R.color.my_color_f5f5f5));
        }else {
            helper.setBackgroundColor(R.id.ll_item,mContext.getResources().getColor(R.color.white));
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
