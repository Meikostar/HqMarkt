package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.PublishInfo;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class MyPublishAdapter extends BaseQuickAdapter<PublishInfo, BaseViewHolder> {
    public MyPublishAdapter() {
        super(R.layout.adapter_my_publish, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, PublishInfo item) {
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_my_buy_good_icon), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
        helper.setText(R.id.tv_item_my_buy_good_name, item.getTitle())
                .setText(R.id.tv_item_my_buy_good_integral, item.getScore())
                .setText(R.id.tv_time, item.getCreated_at() + "  发布");
        helper.setGone(R.id.tv_button_1, false)
                .setGone(R.id.tv_button_2, false)
                .setGone(R.id.tv_button_3, false)
                .addOnClickListener(R.id.tv_button_2)
                .addOnClickListener(R.id.tv_button_3);
        switch (item.getStock()) {
            case "0":
                helper.setVisible(R.id.tv_button_1, true)
                        .setText(R.id.tv_button_1, "已卖出");
                break;
            case "1":
                helper.setVisible(R.id.tv_button_2, true)
                        .setVisible(R.id.tv_button_3, true);
                if (item.isOn_sale()){
                    //已上架
                    helper.setText(R.id.tv_button_2,"下架");
                }else {
                    //已下架
                    helper.setText(R.id.tv_button_2,"上架");
                }
                break;
            default:
                helper.setVisible(R.id.tv_button_1, true)
                        .setText(R.id.tv_button_1, "已下架");
                break;
        }


    }
}
