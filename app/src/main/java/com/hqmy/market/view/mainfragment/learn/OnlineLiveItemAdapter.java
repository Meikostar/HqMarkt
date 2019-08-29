package com.hqmy.market.view.mainfragment.learn;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class OnlineLiveItemAdapter extends BaseQuickAdapter<VideoLiveBean, BaseViewHolder> {
    public OnlineLiveItemAdapter() {
        super(R.layout.item_online_live, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoLiveBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS + item.getImages());
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_play_count, item.getPlay_count())
                .setText(R.id.tv_chatter_total, item.getChatter_total());
    }
}
