package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

public class MemberStoresAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public MemberStoresAdapter(Context context) {
        super(R.layout.item_member_stores, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
