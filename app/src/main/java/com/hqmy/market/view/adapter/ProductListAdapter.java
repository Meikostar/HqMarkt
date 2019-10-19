package com.hqmy.market.view.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

/**
 * 产品列表
 * Created by dahai on 2019/01/18.
 */
public class ProductListAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {

    public ProductListAdapter() {
        super(R.layout.item_product_list_layout);
    }



    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {

//            TextView tvsin = helper.getView(R.id.tv_sign);
//            if (item.labels != null && item.labels.size() > 0) {
//                tvsin.setVisibility(View.VISIBLE);
//                tvsin.setText(item.labels.get(0));
//            } else {
//                tvsin.setVisibility(View.GONE);
//            }
//            if (item.ext != null && !TextUtils.isEmpty(item.ext.slogan)) {
//                tvsin.setVisibility(View.VISIBLE);
//                helper.setText(R.id.tv_tag, item.ext.slogan);
//            }
            if (item.brand != null && item.brand.data.category.data != null && !TextUtils.isEmpty(item.brand.data.category.data.title)) {
//                tvsin.setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_contury, item.brand.data.category.data.title);
                GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.brand.data.category.data.icon);

            }
//            FrameLayout frameLayout = helper.getView(R.id.fl_line);
            TextView labe = helper.getView(R.id.tv_labe);
            int i=0;
            String content="";
            if (item.labels!=null&&item.labels.size()>0) {
                for(String con:item.labels){
                    if(i==0){
                        content=con;
                    }else {
                        content=content+","+con;
                    }
                    i++;
                }
                String sapce = getSapce(content.length());
                if(TextUtil.isNotEmpty(content)){
                    labe.setVisibility(View.VISIBLE);
                }else {
                    labe.setVisibility(View.GONE);
                }
                helper.setText(R.id.tv_labe,content);
                helper.setText(R.id.tv_title,sapce+ item.getTitle());
//                frameLayout.setVisibility(View.VISIBLE);

                String name ="￥"+ item.market_price;

                TextView textview = helper.getView(R.id.market_price);
                textview.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); //中间横线下划线
                textview.getPaint().setAntiAlias(true);// 抗锯齿
                textview.setText(name);
//                helper.setText(R.id.market_price, name);
            } else {
                labe.setVisibility(View.GONE);
                helper.setText(R.id.tv_title, item.getTitle());
            }

            if (item.ext != null && TextUtils.isEmpty(item.ext.slogan)) {

                helper.setText(R.id.tv_tag, item.ext.slogan);

            }
            helper.setText(R.id.tv_item_home_good_shop_price, item.getPrice());
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

        public String getSapce(int length){
         String content="\t";
         for(int i=0;i<length;i++){
             content=content+"\t";
         }
         return content;
        }
}