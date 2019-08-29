package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.BabyDetailActivity;

import java.util.List;

/**
 * 爱心家庭  猜你喜欢适配器
 * Created by rzb on 2019/4/20
 */
public class FamilyLikeAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public FamilyLikeAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_family_like, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {
            helper.setText(R.id.tv_family_brand, item.getTitle());
            helper.setText(R.id.tv_item_family_integral, item.getScore());
            helper.setText(R.id.tv_item_family_name, item.getUser().getData().getName());
            GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.iv_item_family_head),
                    Constants.WEB_IMG_URL_UPLOADS + item.getUser().getData().getAvatar());
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_family_brand),
                    Constants.WEB_IMG_URL_UPLOADS + item.getCover());
            helper.getView(R.id.layout_item_family_like).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BabyDetailActivity.PRODUCT_ID, item.getId());
                    bundle.putString(BabyDetailActivity.MALL_TYPE, "ax");
                    gotoActivity(BabyDetailActivity.class, bundle);
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