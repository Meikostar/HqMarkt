package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;
import java.util.List;

public class FactotyBrandListAdapter extends MyBaseAdapter<RecommendListDto> {
    private Context context;
    private ViewHolder holder;

    public FactotyBrandListAdapter(Context context, List<RecommendListDto> mPostList) {
        super(context, mPostList);
        this.context = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.factory_brand_list_item, null);
        holder = new ViewHolder();
        holder.img_brand_store = (ImageView) view.findViewById(R.id.img_brand_store);
        holder.tv_brand_name = (TextView) view.findViewById(R.id.tv_brand_name);
        holder.rb_brand = (BaseRatingBar) view.findViewById(R.id.rb_brand);
        holder.tv_brand_look = view.findViewById(R.id.tv_brand_look);
        holder.hzl_brand = (RecyclerView) view.findViewById(R.id.gv_brand);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, RecommendListDto var3) {
        holder = (ViewHolder) var1.getTag();
        GlideUtils.getInstances().loadRoundImg(context, holder.img_brand_store, Constants.WEB_IMG_URL_UPLOADS + var3.getLogo());
        holder.tv_brand_name.setText(var3.getShop_name());
//        if(!var3.getRatNum().isEmpty() && !var3.getRatNum().equals("0")) {
//            holder.rb_brand.setVisibility(View.VISIBLE);
//            if (var3.getRatNum().equals("5")) {
//                holder.rb_brand.setNumStars(5);
//                holder.rb_brand.setRating(5);
//            } else if (var3.getRatNum().equals("4")) {
//                holder.rb_brand.setNumStars(4);
//                holder.rb_brand.setRating(4);
//            } else if (var3.getRatNum().equals("3")) {
//                holder.rb_brand.setNumStars(3);
//                holder.rb_brand.setRating(3);
//            } else if (var3.getRatNum().equals("2")) {
//                holder.rb_brand.setNumStars(2);
//                holder.rb_brand.setRating(2);
//            } else if (var3.getRatNum().equals("1")) {
//                holder.rb_brand.setNumStars(1);
//                holder.rb_brand.setRating(1);
//            }
//        }else{
//            holder.rb_brand.setVisibility(View.GONE);
//        }
        LinearLayoutManager layout = new LinearLayoutManager(context);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        //holder.hzl_brand.addItemDecoration(new RecycleViewDivider_PovertyRelief(DensityUtil.dp2px(10), DensityUtil.dp2px(0)));
        holder.hzl_brand.setLayoutManager(layout);
        FactoryBrandAapter factoryBrandAapter = new FactoryBrandAapter(var3.getProducts().getData(),context);
        holder.hzl_brand.setAdapter(factoryBrandAapter);
        holder.tv_brand_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BrandShopDetailActivity.MALL_TYPE, var3.getType());
                bundle.putString(BrandShopDetailActivity.SHOP_DETAIL_ID, var3.getId());
                gotoActivity(BrandShopDetailActivity.class, bundle);
            }
        });
    }

    class ViewHolder {
        ImageView     img_brand_store;
        TextView      tv_brand_name;
        BaseRatingBar rb_brand;
        TextView      tv_brand_look;
        RecyclerView  hzl_brand;
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}

