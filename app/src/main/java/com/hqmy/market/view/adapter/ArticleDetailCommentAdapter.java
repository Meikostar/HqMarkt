package com.hqmy.market.view.adapter;

import android.content.Context;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommentListBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class ArticleDetailCommentAdapter extends BaseQuickAdapter<CommentListBean, BaseViewHolder> {
    public ArticleDetailCommentAdapter(Context context) {
        super(R.layout.item_article_detail_comment, null);
        mContext = context;

    }

    @Override
    protected void convert(BaseViewHolder helper, CommentListBean item) {
        helper.setText(R.id.tv_item_article_detail_content, item.getComment())
                .setText(R.id.tv_item_article_detail_time, item.getCreated_at())
                .setText(R.id.tv_item_article_dianzan_num, item.getLikers_count()+"")
                .setText(R.id.tv_item_article_detail_name, item.getUser().getData().getName())
                .addOnClickListener(R.id.iv_praise);
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.iv_item_article_detail_icon), Constants.WEB_IMG_URL_UPLOADS+item.getUser().getData().getAvatar());
        if (item.getIs_liked() == 1) {
            helper.setImageResource(R.id.iv_praise, R.mipmap.ic_praise_red);
        } else {
            helper.setImageResource(R.id.iv_praise, R.mipmap.ic_praise);
        }
    }
}
