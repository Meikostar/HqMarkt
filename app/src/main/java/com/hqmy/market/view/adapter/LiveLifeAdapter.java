package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

public class LiveLifeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public LiveLifeAdapter(Context context) {
        super(R.layout.item_live_life, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
