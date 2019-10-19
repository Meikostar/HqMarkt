package com.hqmy.market.view.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.MCheckBox;

public class CollectGoodsAdapter extends BaseQuickAdapter<TopicListItemDto, BaseViewHolder> {
    public CollectGoodsAdapter() {
        super(R.layout.item_collect_goods, null);
    }
    public interface  ChooseListener{
        void choose();
    }
    private ChooseListener mChooseListener;
    public void setChooseListener(ChooseListener mChooseListener){
        this.mChooseListener=mChooseListener;
    }
    @Override
    protected void convert(BaseViewHolder helper, TopicListItemDto item) {
        if (item.getCover() != null){
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_collect_goods_icon), Constants.WEB_IMG_URL_UPLOADS + item.getCover());
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_collect_goods_icon), R.drawable.glide_default_picture);
        }

        if(!TextUtils.isEmpty(item.getPrice())){
            helper.setText(R.id.tv_price,  item.getPrice());
        }
        MCheckBox choose = helper.getView(R.id.iv_choose);
        if(type!=0){
            choose.setVisibility(View.VISIBLE);
        }else {
            choose.setVisibility(View.GONE);
        }
        choose.setOnCheckChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    item.isChoose=true;
                }else {
                    item.isChoose=false;
                }
                notifyDataSetChanged();
                if(mChooseListener!=null){
                    mChooseListener.choose();
                }
            }
        });
        FrameLayout frameLayout = helper.getView(R.id.fl_line);
        LinearLayout llab = helper.getView(R.id.ll_lab);
        LinearLayout llbg = helper.getView(R.id.ll_bg);
        llbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                Intent intent = new Intent(mContext,CommodityDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        View line = helper.getView(R.id.line);
        if(!TextUtils.isEmpty(item.market_price)){
            frameLayout.setVisibility(View.VISIBLE);

            String name = "￥"+item.market_price;
            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(12);
            int with = (int) textPaint.measureText(name);
            FrameLayout.LayoutParams linearParams =(FrameLayout.LayoutParams) line.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

            linearParams.width = ScreenSizeUtil.dp2px(with+3);// 控件的宽强制设成30

            line.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            helper.setText(R.id.market_price,name);
        }else {
            frameLayout.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(item.getTitle())){
            helper .setText(R.id.tv_title, item.getTitle());
        }
        if(item.brand!=null&&item.brand.data!=null&&item.brand.data.category.data!=null&&item.brand.data.category.data.title!=null){
            helper.setText(R.id.tv_contury,  item.brand.data.category.data.title);
            GlideUtils.getInstances().loadUserRoundImg(mContext, helper.getView(R.id.iv_contury), Constants.WEB_IMG_URL_UPLOADS + item.brand.data.category.data.icon);

        }


        if(item.labels!=null&&item.labels.size()>0){
            llab.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_text1,item.labels.get(0));
            helper.setVisible(R.id.tv_text2,false);
            helper.setVisible(R.id.tv_text3,false);
            helper.setVisible(R.id.tv_text4,false);
            if(item.labels.size()==2){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setVisible(R.id.tv_text2,true);
                helper.setVisible(R.id.tv_text3,false);
                helper.setVisible(R.id.tv_text4,false);
            }else if(item.labels.size()==3){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setText(R.id.tv_text3,item.labels.get(2));
                helper.setVisible(R.id.tv_text2,true);
                helper.setVisible(R.id.tv_text3,true);
                helper.setVisible(R.id.tv_text4,false);

            }else if(item.labels.size()>=4){
                helper.setText(R.id.tv_text2,item.labels.get(1));
                helper.setText(R.id.tv_text3,item.labels.get(2));
                helper.setText(R.id.tv_text4,item.labels.get(3));
                helper.setVisible(R.id.tv_text2,true);
                helper.setVisible(R.id.tv_text3,true);
                helper.setVisible(R.id.tv_text4,true);

            }
        }else {
            llab.setVisibility(View.INVISIBLE);
        }
    }
    private int type;
    public void setType(int type){
        this.type=type;
    }
}
