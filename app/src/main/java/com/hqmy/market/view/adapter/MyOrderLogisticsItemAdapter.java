package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.TrackBean;

import java.util.List;

/**
 * 物流进度跟踪
 */
public class MyOrderLogisticsItemAdapter extends BaseQuickAdapter<TrackBean, BaseViewHolder> {

    public MyOrderLogisticsItemAdapter(List<TrackBean> data) {
        super(R.layout.item_myorder_logistics_layout, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, TrackBean item) {
        helper.setText(R.id.tv_logistics_time, item.getTime())
                .setText(R.id.tv_logistics_des, item.getContext());
        if (helper.getPosition() == getItemCount() - 1) {
            helper.setBackgroundRes(R.id.tv_logistics_flag, R.drawable.bg_logistics_flag_sel_shape)
                    .setVisible(R.id.tv_logistics_line_end, false);
        } else {
            helper.setBackgroundRes(R.id.tv_logistics_flag, R.drawable.bg_logistics_flag_default_shape)
                    .setVisible(R.id.tv_logistics_line_end, true);

        }
    }
}
