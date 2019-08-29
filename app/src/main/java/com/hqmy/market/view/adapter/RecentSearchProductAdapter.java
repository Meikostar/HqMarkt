package com.hqmy.market.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.hqmy.market.R;
import com.hqmy.market.utils.ShareUtil;

public class RecentSearchProductAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Boolean isCourseWare;
    public RecentSearchProductAdapter() {
        super(R.layout.recent_search_product_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_recent_product_text,item);
        ImageView imageView = helper.getView(R.id.iv_clear);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              remove(helper.getPosition());
              ShareUtil.getInstance().save("productSearchData", new Gson().toJson(getData()));
            }
        });
    }
}
