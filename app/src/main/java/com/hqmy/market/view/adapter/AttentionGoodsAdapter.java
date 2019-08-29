package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

public class AttentionGoodsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public AttentionGoodsAdapter(Context context) {
        super(R.layout.item_collect_goods, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
