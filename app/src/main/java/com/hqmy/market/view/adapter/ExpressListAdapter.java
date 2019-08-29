package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ExpressList;

import java.util.List;

public class ExpressListAdapter extends BaseQuickAdapter<ExpressList, BaseViewHolder> {
    private int selPositon = 0;

    public ExpressListAdapter(List<ExpressList> data) {
        super(R.layout.item_return_reason_layout, data);
    }

    public void setSelPositon(int selPositon) {
        this.selPositon = selPositon;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, ExpressList item) {
        helper.setText(R.id.tv_title, item.getName());
//        if (helper.getAdapterPosition() == selPositon) {
//            helper.getView(R.id.img_checkbox).setSelected(true);
//        } else {
//            helper.getView(R.id.img_checkbox).setSelected(false);
//        }
    }
}
