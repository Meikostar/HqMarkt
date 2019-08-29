package com.hqmy.market.view.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class CommodityCommentImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CommodityCommentImageAdapter(@Nullable List<String> data) {
        super(R.layout.adapter_comment_image,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_commodity_comments_img), Constants.WEB_IMG_URL_UPLOADS + item, R.mipmap.img_default_1);

    }
}
