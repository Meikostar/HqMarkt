package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * 联盟商城 新品驾到
 * Created by rzb on 2019/5/20
 */
public class MallShopNewListAapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public MallShopNewListAapter() {
        super(R.layout.item_mall_new_list, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        helper.setText(R.id.tv_mall_new, item.getTitle())
                .setText(R.id.tv_mall_new_price, "¥" + item.getPrice());
        String labelStr = "";
        List<String> labLists = item.getLabels();
        if (labLists != null && labLists.size() > 0) {
            for (int i = 0; i < labLists.size(); i++) {
                if (i == 0) {
                    labelStr = labelStr + labLists.get(i);
                } else {
                    labelStr = labelStr + "," + labLists.get(i);
                }
            }
        }
        if (labelStr != "") {
            helper.setText(R.id.tv_mall_new_type, labelStr)
                    .setVisible(R.id.tv_mall_new_type, true);
        } else {
            helper.setVisible(R.id.tv_mall_new_type, true);
        }
        if (item.getImgs() != null) {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_mall_new), Constants.WEB_IMG_URL_UPLOADS + item.getImgs().get(0));
        }
    }
}
