package com.hqmy.market.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.hqmy.market.R;
import com.hqmy.market.utils.ShareUtil;

public class RecentSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Boolean isCourseWare;
    public RecentSearchAdapter(Boolean isCourseWare) {
        super(R.layout.recent_search_item, null);
        this.isCourseWare = isCourseWare;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_recent_text,item);
        ImageView imageView = helper.getView(R.id.iv_clear);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              remove(helper.getPosition());
              if (isCourseWare){
                  ShareUtil.getInstance().save("courseWareSearchData", new Gson().toJson(getData()));
              }else {
                  ShareUtil.getInstance().save("historySearchData", new Gson().toJson(getData()));
              }
            }
        });
    }
}
