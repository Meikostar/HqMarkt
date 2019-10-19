package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.AttentionCommunityBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.widgets.CircleImageView;

/**
 * 关注店铺
 */
public class AttentionStoreAdapter extends BaseQuickAdapter<AttentionCommunityBean, BaseViewHolder> {
    public AttentionStoreAdapter(Context context) {
        super(R.layout.item_collect_store, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionCommunityBean item) {
        helper.setText(R.id.tv_collect_store_name, item.getShop_name());
        helper.setText(R.id.tv_care, item.followers_count+"人关注");
        CircleImageView imageView = helper.getView(R.id.iv_collect_store_icon);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView, Constants.WEB_IMG_URL_UPLOADS + item.getLogo());
    }
}
