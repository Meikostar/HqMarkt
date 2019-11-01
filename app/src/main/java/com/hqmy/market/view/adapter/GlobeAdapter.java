package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;


/**
 * 消费商品推荐适配器
 * Created by rzb on 2019/4/20
 */
public class GlobeAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public GlobeAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_globle_view, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {


            if(item.ext!=null&&item.ext.slogan!=null){
                helper.setText( R.id.tv_title,item.ext.slogan);
            }

            if(item.category!=null&&item.category.data!=null&&!TextUtils.isEmpty(item.category.data.title)){


                GlideUtils.getInstances().loadRoundImg(mContext, helper.getView( R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.category.data.icon);

            }
            if(!TextUtils.isEmpty(item.name)){
                helper.setText( R.id.tv_contury,item.name);
            }


            GlideUtils.getInstances().loadProcuctNormalImg(mContext,helper.getView( R.id.iv_item_consume_push_img) , Constants.WEB_IMG_URL_UPLOADS + item.logo);
            View view = helper.getView(R.id.iv_item_consume_push_img);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BrandShopDetailActivity.class);
                    intent.putExtra("id",item.id);
                    mContext.startActivity(intent);
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