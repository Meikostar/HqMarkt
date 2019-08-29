package com.hqmy.market.view.mainfragment.learn;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

/**
 * 课件库
 */
public class CourseWareAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {
    public CourseWareAdapter() {
        super(R.layout.item_course_ware, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_image), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_count, item.getSales_count() + "人观看")
                .setText(R.id.tv_sore, item.getScore() + "积分");
        if (item.getCourse_info() != null && item.getCourse_info().getData() != null && !TextUtils.isEmpty(item.getCourse_info().getData().getAuthor())) {
            helper.setText(R.id.tv_name, item.getCourse_info().getData().getAuthor());
        } else {
            helper.setText(R.id.tv_name, "");
        }

    }
}
