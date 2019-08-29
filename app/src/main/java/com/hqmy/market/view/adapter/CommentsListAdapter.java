package com.hqmy.market.view.adapter;

import android.content.Context;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;


public class CommentsListAdapter extends BaseQuickAdapter<CommentDto, BaseViewHolder> {
    public CommentsListAdapter(Context mContext) {
        super(R.layout.item_comment_list_layout, null);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentDto item) {
        helper.setText(R.id.tv_comment_name, item.getUser().getData().getName())
               .setText(R.id.tv_comment_time, item.getCreated_at())
               .setText(R.id.tv_comment_content, item.getComment())
               .setText(R.id.tv_item_article_dianzan_num, item.getLikers_count()+"")
               .addOnClickListener(R.id.iv_praise);
        GlideUtils.getInstances().loadRoundImg(mContext,helper.getView(R.id.img_comment_head),
                Constants.WEB_IMG_URL_UPLOADS + item.getUser().getData().getAvatar());
        if (item.getIs_liked().equals("1")) {
            helper.setImageResource(R.id.iv_praise, R.mipmap.ic_praise_red);
        } else {
            helper.setImageResource(R.id.iv_praise, R.mipmap.ic_praise);
        }
    }
}
