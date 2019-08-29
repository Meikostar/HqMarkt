package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.ShopDetailActivity;

import java.util.List;
import java.text.DecimalFormat;

/**
 * 实体店铺适配器
 * Created by rzb on 2019/5/20
 */
public class EntityStoreAdapter extends BaseQuickAdapter<RecommendListDto, BaseViewHolder> {
    private Context mContext;

    public EntityStoreAdapter(List<RecommendListDto> data, Context mContext) {
        super(R.layout.item_entity_store_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendListDto item) {
        if (item != null) {
            helper.setText(R.id.tv_entity_store_name, item.getShop_name());
            helper.setText(R.id.tv_entity_store_location, item.getAddress());
            helper.setText(R.id.tv_entity_store_brief, item.getBrief());
            Double disDouble = Double.valueOf(item.getDistance());
            DecimalFormat format = new DecimalFormat("#0.00");
            String strDis = format.format(disDouble);
            helper.setText(R.id.tv_entity_store_distance, strDis + "m");
            if(item.getLabels() != null) {
                helper.setText(R.id.tv_label_one, item.getLabels().get(0));
                helper.setText(R.id.tv_label_two, item.getLabels().get(1));
            }
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_entity_store), Constants.WEB_IMG_URL_UPLOADS + item.getLogo());
            helper.getView(R.id.layout_entity_store_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_ID, item.getId());
                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_LAT, item.getLat());
                    bundle.putString(ShopDetailActivity.SHOP_DETAIL_LNG, item.getLng());
                    gotoActivity(ShopDetailActivity.class, bundle);
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