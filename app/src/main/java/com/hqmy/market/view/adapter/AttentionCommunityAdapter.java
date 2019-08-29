package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.AttentionCommunityBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.widgets.CircleImageView;

public class AttentionCommunityAdapter extends BaseQuickAdapter<AttentionCommunityBean, BaseViewHolder> {
    public AttentionCommunityAdapter(Context context) {
        super(R.layout.item_collect_community, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionCommunityBean item) {
        helper.setVisible(R.id.iv_red_point, false)
                .setText(R.id.tv_collect_community_name, item.getName());
        CircleImageView imageView = helper.getView(R.id.iv_collect_community_icon);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView, Constants.WEB_IMG_URL_UPLOADS + item.getAvatar());
    }
}
