package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CollectionDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

/**
 * 收藏 店铺
 */
public class CollectStoreAdapter extends BaseQuickAdapter<CollectionDto, BaseViewHolder> {

    public CollectStoreAdapter() {
        super(R.layout.collect_store_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectionDto item) {
        helper.setText(R.id.tv_collect_store_name,item.getTitle());
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_collect_store_icon), Constants.WEB_IMG_URL_STORAGE+item.getCover());
        helper.addOnClickListener(R.id.content)
                .addOnClickListener(R.id.tv_deleted);
    }
}
