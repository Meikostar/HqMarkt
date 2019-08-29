package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.Map;

/**
 * 实体店铺详情的适配器
 * Created by rzb on 2019/6/20
 */
public class ShopProductsAapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public ShopProductsAapter() {
        super(R.layout.item_st_product_list, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        helper.setText(R.id.tv_st_product_name,item.getTitle());
        if (item.getParameter() != null){
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> entry : item.getParameter().entrySet()){
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                builder.append(mapKey+":"+mapValue);
                builder.append("\r\n");
            }
            helper.setText(R.id.tv_st_brief,builder.toString());
        }else {
            helper.setText(R.id.tv_st_brief,"");
        }
        if(item.getLabels() != null) {
            helper.setText(R.id.tv_st_label_one,item.getLabels().get(0));
            helper.setText(R.id.tv_st_label_two,item.getLabels().get(1));
        }else {
            helper.setText(R.id.tv_st_label_one,"");
            helper.setText(R.id.tv_st_label_two,"");
        }
        helper.setText(R.id.tv_st_price,"¥"+item.getPrice());
        GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_st_logo), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
    }
}
