package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.qiniu.AVStreamingActivity;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class ConsumePushAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public ConsumePushAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_consume_push_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {
            helper.setText(R.id.tv_title, item.getTitle());
            TextView tvsin = helper.getView(R.id.tv_sign);
            if(item.labels!=null&&item.labels.size()>0){
                tvsin.setVisibility(View.VISIBLE);
                tvsin.setText(item.labels.get(0));
            }else {
                tvsin.setVisibility(View.GONE);
            }
            if(item.ext!=null&&TextUtils.isEmpty(item.ext.slogan)){
                tvsin.setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_tag,item.ext.slogan);
            }
            if(item.brand!=null&&item.brand.data!=null&&TextUtils.isEmpty(item.brand.data.name)){
                tvsin.setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_contury,item.brand.data.name);
            }
            if(item.ext!=null&&TextUtils.isEmpty(item.ext.slogan)){
                tvsin.setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_tag,item.ext.slogan);
                GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.brand.data.logo);

            }
            helper.setText(R.id.tv_item_home_good_shop_price, "¥" + item.getPrice());
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_consume_push_img), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
            helper.getView(R.id.iv_item_home_push).setOnClickListener(new View.OnClickListener() {
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