package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.DynamicBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.widgets.NoScrollGridView;

public class MyIssueAdapter extends BaseQuickAdapter<DynamicBean, BaseViewHolder> {
    public MyIssueAdapter(Context context) {
        super(R.layout.item_my_issue, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicBean item) {
        helper.setText(R.id.tv_item_issue_name, ShareUtil.getInstance().getString(Constants.USER_NAME, ""))
                .setText(R.id.tv_item_issue_time, item.getCreated_at())
                .setText(R.id.tv_item_issue_content, item.getContent())
                .setText(R.id.tv_item_issue_read_num, item.getClick() + "人阅读")
                .setText(R.id.tv_item_issue_share_num, item.getShares() + "")
                .setText(R.id.tv_item_issue_comment_num, item.getComments_count() + "")
                .setText(R.id.tv_item_issue_dianzan_num, item.getLikers_count() + "");

        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_issue_user_avatar), Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().getString(Constants.USER_HEAD, ""));

        NoScrollGridView mGridView = helper.getView(R.id.gv_imgs);
        if (item.getImg() != null && item.getImg().size() > 0) {
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(new MyImageAdapter(mContext,item.getImg()));
        }else {
            mGridView.setVisibility(View.GONE);
        }
    }
}
