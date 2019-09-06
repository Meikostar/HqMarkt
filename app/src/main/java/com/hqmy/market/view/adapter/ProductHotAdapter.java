package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

/**
 * 关注 店铺
 */
public class ProductHotAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    public ProductHotAdapter(Context context) {
        super(R.layout.item_product_hot, null);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {

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

        FrameLayout frameLayout = helper.getView(R.id.fl_line);
        View line = helper.getView(R.id.line);
        int i=0;
        String content="";

        if(item.labels!=null&&item.labels.size()>0){
            helper.setGone(R.id.tv_text1,true);
            helper.setText(R.id.tv_text1,item.labels.get(0));
            helper.setVisible(R.id.tv_text2,false);
            helper.setVisible(R.id.tv_text3,false);
            helper.setVisible(R.id.tv_text4,false);
            if(item.labels.size()==2){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setGone(R.id.tv_text2,true);
                helper.setGone(R.id.tv_text3,false);
                helper.setGone(R.id.tv_text4,false);
            }else if(item.labels.size()==3){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setText(R.id.tv_text3,item.labels.get(2));
                helper.setGone(R.id.tv_text2,true);
                helper.setGone(R.id.tv_text3,true);
                helper.setGone(R.id.tv_text4,false);

            }else if(item.labels.size()>=4){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setText(R.id.tv_text3,item.labels.get(2));
                helper.setText(R.id.tv_text4,item.labels.get(3));
                helper.setGone(R.id.tv_text2,true);
                helper.setGone(R.id.tv_text3,true);
                helper.setGone(R.id.tv_text4,true);

            }
        }else {
            helper.setGone(R.id.tv_text1,false);
            helper.setGone(R.id.tv_text2,false);
            helper.setGone(R.id.tv_text3,false);
            helper.setGone(R.id.tv_text4,false);
        }
        int parentPosition = getParentPosition(item);
        ImageView view = helper.getView(R.id.iv_cate);

        if(parentPosition==0){
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.drawable.top1);
        }else if(parentPosition==1){
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.drawable.top2);
        }else if(parentPosition==2){
            view.setVisibility(View.VISIBLE);
            view.setImageResource(R.drawable.top3);
        }else {
            view.setVisibility(View.GONE);
        }
        if (item.market_price!=null) {

            frameLayout.setVisibility(View.VISIBLE);

            String name =  "￥"+item.market_price;
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(12);
            int with = (int) textPaint.measureText(name);
            FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) line.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

            linearParams.width = ScreenSizeUtil.dp2px(with - 1);// 控件的宽强制设成30

            line.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            helper.setText(R.id.market_price, name);
        } else {

        }
        helper.setText(R.id.tv_title, item.getTitle());
        if (item.brand != null && item.brand.data.category.data != null && !TextUtils.isEmpty(item.brand.data.category.data.title)) {
            //                tvsin.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_contury, item.brand.data.category.data.title);
            GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.brand.data.category.data.icon);

        }
        helper.setText(R.id.tv_price, item.getPrice());
        GlideUtils.getInstances().loadProcuctNormalImg(mContext, helper.getView(R.id.iv_collect_goods_icon), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
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
