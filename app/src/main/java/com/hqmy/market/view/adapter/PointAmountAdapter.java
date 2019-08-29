package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

import java.util.List;

public class PointAmountAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private int currentPosition = 0;
    public PointAmountAdapter(List<String> items) {
        super(R.layout.point_amount_item, items);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
          helper.setText(R.id.tv_amount_select,item);
          if (currentPosition == helper.getPosition()){
              helper.setTextColor(R.id.tv_amount_select,mContext.getResources().getColor(R.color.my_color_E10020));
              helper.setBackgroundRes(R.id.tv_amount_select,R.drawable.dialog_point_amount_red_shape);
           }else {
              helper.setTextColor(R.id.tv_amount_select,mContext.getResources().getColor(R.color.my_color_666666));
              helper.setBackgroundRes(R.id.tv_amount_select,R.drawable.dialog_point_amount_shape);
          }
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }
    public int getCurrentPosition() {
        return currentPosition;
    }
}
