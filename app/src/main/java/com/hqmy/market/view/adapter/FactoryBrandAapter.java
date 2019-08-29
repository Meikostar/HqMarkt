package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ProductDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

/**
 * 爱心工厂，品牌推荐的适配器
 * Created by rzb on 2019/4/20
 */
public class FactoryBrandAapter extends BaseQuickAdapter<ProductDto, BaseViewHolder> {
    private Context mContext;

    public FactoryBrandAapter(List<ProductDto> list, Context context) {
        super(R.layout.item_factory_brand_gridview, list);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductDto item) {
        if(item != null) {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_factory_brand), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
            helper.setText(R.id.tv_factoty_brand, item.getTitle());
            helper.setText(R.id.tv_factoty_brand_price, "¥" + item.getPrice());
            String labelStr = "";
            List<String> labLists = item.getLabels();
            if(labLists != null && labLists.size() > 0) {
                for (int i = 0; i < labLists.size(); i++) {
                    if(i==0){
                        labelStr = labelStr + labLists.get(i);
                    }else {
                        labelStr = labelStr + "," + labLists.get(i);
                    }
                }
            }
            if(labelStr != "") {
                helper.setVisible(R.id.tv_brand_type, true);
                helper.setText(R.id.tv_brand_type, labelStr);
            }else{
                helper.setVisible(R.id.tv_brand_type, false);
            }
            helper.getView(R.id.layout_item_brand).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommodityDetailActivity.FROM, "gc");
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                    gotoActivity(CommodityDetailActivity.class, bundle);
                }
            });
        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
