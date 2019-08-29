package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;

import java.util.List;

public class ReturnReasonAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int selPositon = 0;

    public ReturnReasonAdapter(List<String> data) {
        super(R.layout.item_return_reason_layout, data);
    }

    public void setSelPositon(int selPositon) {
        this.selPositon = selPositon;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
//        if (helper.getAdapterPosition() == selPositon) {
//            helper.getView(R.id.img_checkbox).setSelected(true);
//        } else {
//            helper.getView(R.id.img_checkbox).setSelected(false);
//        }
    }
}
