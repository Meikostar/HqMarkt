package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommentTopicBean;
import com.hqmy.market.bean.Commented;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.utils.ShareUtil;

/**
 * 社区留言
 */
public class CommunityMessageAdapter extends BaseQuickAdapter<CommentTopicBean, BaseViewHolder> {
    public CommunityMessageAdapter(Context context) {
        super(R.layout.item_community_message, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentTopicBean item) {
        helper.setText(R.id.tv_item_community_message_user_name, ShareUtil.getInstance().getString(Constants.USER_NAME,""))
                .setText(R.id.tv_item_community_message_time, item.getCreated_at())
                .setText(R.id.tv_item_community_message_content, item.getComment());
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.iv_item_community_message_user_avatar), Constants.WEB_IMG_URL_UPLOADS+ ShareUtil.getInstance().getString(Constants.USER_HEAD,""));
        helper.addOnClickListener(R.id.rl_comment);
        Commented commented = item.getCommented();
        if (commented != null && commented.getData() != null) {
            helper.setGone(R.id.rl_goods, true)
                    .setGone(R.id.tv_commented_deleted, false);
            helper.setText(R.id.tv_item_community_name, commented.getData().getUser().getName())
                    .setText(R.id.tv_item_community_time, commented.getData().getCreated_at())
                    .setText(R.id.tv_item_community_content, commented.getData().getContent());
            GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.iv_item_community_icon), Constants.WEB_IMG_URL_UPLOADS + commented.getData().getUser().getAvatar());
        }else {
            helper.setGone(R.id.rl_goods, false)
                    .setGone(R.id.tv_commented_deleted, true);
        }
    }
}
