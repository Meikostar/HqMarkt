package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ShopCartListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;
import java.util.Map;

/**
 * 购物车
 * Created by rzb on 2019/06/19
 */
public class ShopCartItemAdapter extends BaseQuickAdapter<ShopCartListItemDto, BaseViewHolder> {

    public ShopCartItemAdapter(List<ShopCartListItemDto> data) {
        super(R.layout.item_shop_cart_level_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopCartListItemDto item) {
        helper.setText(R.id.tv_item_shop_cart_title, item.getName())
                .setText(R.id.tv_item_shop_cart_price,"￥"+ item.getPrice())
                .setText(R.id.count, item.getQty())
//                .addOnClickListener(R.id.tv_item_shop_cart_delete)
//                .addOnClickListener(R.id.tv_item_shop_cart_save)
                .addOnClickListener(R.id.increase)
                .addOnClickListener(R.id.decrease);
        helper.setChecked(R.id.cb_item_shop_cart, item.isSelect());
        helper.addOnClickListener(R.id.cb_item_shop_cart);
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_shop_cart_cover),
                Constants.WEB_IMG_URL_UPLOADS + item.getCover());
        String cont="";
        int i=0;

        if(item.options!=null){
            Map<String,String> map=item.options;
            if(map!=null){
                //遍历map中的值
                for (String value : map.values()) {
                    if(i==0){
                        cont=value;
                    }else {
                        cont=cont+","+value;
                    }
                }
                helper.setText(R.id.tv_item_shop_cart_type,cont);
            }

        }

    }
}