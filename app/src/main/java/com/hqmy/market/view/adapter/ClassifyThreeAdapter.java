package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.BaseDto2;
import com.hqmy.market.bean.StoreCategoryDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by lihaoqi on 2019/1/25.
 */

public class ClassifyThreeAdapter extends BaseQuickAdapter<BaseDto2, BaseViewHolder> {
    private Context mContext;

    public ClassifyThreeAdapter(Context context, @Nullable List<BaseDto2> data) {
        super(R.layout.item_classify_three, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BaseDto2 item) {
        helper.setText(R.id.tv_name, item.title);
        ImageView imageView = helper.getView(R.id.iv_img);
        GlideUtils.getInstances().loadNormalImg(mContext, imageView, Constants.WEB_IMG_URL_UPLOADS + item.icon, R.drawable.moren_sf);
        helper.getView(R.id.ll_item_classify_three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
//                bundle.putLong(CommodityDetailActivity.PRODUCT_ID, item.getId());
//                gotoActivity(CommodityDetailActivity.class, bundle);
//                bundle.putString(ShopProductListActivity.ACTION_SEARCH_KEY, item.getCategoryName());
//                gotoActivity(ShopProductListActivity.class, bundle);
            }
        });
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

}
