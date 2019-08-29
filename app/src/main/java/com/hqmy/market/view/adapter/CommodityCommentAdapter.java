package com.hqmy.market.view.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;

public class CommodityCommentAdapter extends BaseQuickAdapter<CommentDto, BaseViewHolder> {
    public CommodityCommentAdapter() {
        super(R.layout.adapter_evaluate_item,null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentDto item) {
        BaseRatingBar baseRatingBar = helper.getView(R.id.rb_evaluate);
        baseRatingBar.setClickable(false);
        baseRatingBar.setRating(Integer.valueOf(item.getScore()));
        if (item.getUser() != null && item.getUser().getData() != null){
            helper.setText(R.id.tv_commodity_comments_name,item.getUser().getData().getName());
        }
        helper.setText(R.id.tv_commodity_comments_msg,item.getComment());
       RecyclerView recyclerViewImages = helper.getView(R.id.recyclerView_images);
        if (item.getImages() != null && item.getImages().size() > 0){
            recyclerViewImages.setVisibility(View.VISIBLE);
            recyclerViewImages.setLayoutManager(new GridLayoutManager(mContext,3));
            CommodityCommentImageAdapter mAdapter = new CommodityCommentImageAdapter(item.getImages());
            recyclerViewImages.setAdapter(mAdapter);
        }else {
            recyclerViewImages.setVisibility(View.GONE);
        }
    }
}
