package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommentTopicBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.utils.ShareUtil;

public class GoodCommentAdapter extends BaseQuickAdapter<CommentTopicBean, BaseViewHolder> {
    public GoodCommentAdapter(Context context) {
        super(R.layout.item_good_comment, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentTopicBean item) {
        if (item.getCommented() != null && item.getCommented().getData() != null && item.getCommented().getData().getUser() != null) {
            GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_item_good_comment_user_avatar), Constants.WEB_IMG_URL_UPLOADS + item.getCommented().getData().getUser().getAvatar());
            helper.setText(R.id.iv_item_good_comment_user_name, item.getCommented().getData().getUser().getName());
        } else {
            GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_item_good_comment_user_avatar), Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, ""));
            helper.setText(R.id.iv_item_good_comment_user_name, ShareUtil.getInstance().getString(Constants.USER_NAME, ""));
        }
        helper.setText(R.id.iv_item_good_comment_time, item.getCreated_at())
                .setText(R.id.iv_item_good_comment_content, item.getComment());
        if (item.getCommented() != null && item.getCommented().getData() != null) {
            helper.setGone(R.id.rl_goods, true)
                    .setGone(R.id.tv_commented_deleted, false);
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_good_icon), Constants.WEB_IMG_URL_UPLOADS + item.getCommented().getData().getCover());
            helper.setText(R.id.tv_item_good_name, item.getCommented().getData().getTitle())
                    .setText(R.id.tv_item_good_price, "¥" + item.getCommented().getData().getPrice());
        } else {
            helper.setGone(R.id.rl_goods, false)
                    .setGone(R.id.tv_commented_deleted, true);
        }
        if (item.getOrderItem() != null && item.getOrderItem().getData() != null) {
            helper.setText(R.id.tv_item_good_num, "x" + item.getOrderItem().getData().getQty());
            StringBuilder options = new StringBuilder();
            if (item.getOrderItem().getData().getOptions() != null && item.getOrderItem().getData().getOptions().get("大小") != null) {
                options.append(item.getOrderItem().getData().getOptions().get("大小") + ",");
            }
            if (item.getOrderItem().getData().getOptions() != null && item.getOrderItem().getData().getOptions().get("颜色") != null) {
                options.append(item.getOrderItem().getData().getOptions().get("颜色"));
            }
            helper.setText(R.id.tv_item_good_color, options.toString());
        } else {
            helper.setText(R.id.tv_item_good_num, "")
                    .setText(R.id.tv_item_good_color, "");
        }


    }
}
