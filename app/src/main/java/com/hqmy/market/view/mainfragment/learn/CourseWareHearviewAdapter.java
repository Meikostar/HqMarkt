package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CategorieBean;

public class CourseWareHearviewAdapter extends BaseQuickAdapter<CategorieBean, BaseViewHolder> {
    private int selectedPosition = 0;

    public CourseWareHearviewAdapter(Context context) {
        super(R.layout.item_courseware_hearview, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategorieBean item) {
        helper.setText(R.id.tv_title, item.getTitle());
        if (helper.getPosition() == selectedPosition) {
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.white));
            helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_radius_3_e10020);
        } else {
            helper.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.my_color_535353));
            helper.setBackgroundRes(R.id.tv_title, R.drawable.shape_radius_3_f7f7f7);
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
