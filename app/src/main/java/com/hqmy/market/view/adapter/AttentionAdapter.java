package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

/**
 * 关注 店铺
 */
public class AttentionAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public AttentionAdapter(Context context) {
        super(R.layout.item_collect_store, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setVisible(R.id.iv_red_point, true);
    }
}
