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
 * 爱心工厂类别推荐适配器
 * Created by rzb on 2019/6/20
 */
public class TypeFactoryAdapter extends BaseQuickAdapter<ProductDto, BaseViewHolder> {
    private Context mContext;

    public TypeFactoryAdapter(List<ProductDto> data, Context mContext) {
        super(R.layout.item_type_factory_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductDto item) {
        if (item != null) {
            helper.setText(R.id.tv_type_factory_title, item.getTitle());
            helper.setText(R.id.tv_item_type_factory_price, item.getPrice());
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_type_factory), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
            helper.getView(R.id.layout_type_factory).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
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