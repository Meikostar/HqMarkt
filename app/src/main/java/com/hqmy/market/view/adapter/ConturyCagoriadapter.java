package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

/**
 *
 */

public class ConturyCagoriadapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<NewListItemDto>  list;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private int[] imgs;


    private String[] names;

    public ConturyCagoriadapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<NewListItemDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?(list.size()>=9?9:list.size()):0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.contury_cagori_item, null);
            holder.tv_title = view.findViewById(R.id.tv_title);
            holder.tv_contury = view.findViewById(R.id.tv_contury);
            holder.iv_item_consume_push_img = view.findViewById(R.id.iv_item_consume_push_img);
            holder.iv_contury = view.findViewById(R.id.iv_contury);
            holder.iv_item_home_push = view.findViewById(R.id.iv_item_home_push);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        NewListItemDto item = list.get(i);

        if(item.ext!=null&&item.ext.slogan!=null){
            holder.tv_title.setText( item.ext.slogan);
        }

        if(item.category!=null&&item.category.data!=null&&!TextUtils.isEmpty(item.category.data.title)){


            GlideUtils.getInstances().loadRoundImg(context, holder.iv_contury, Constants.WEB_IMG_URL_UPLOADS + item.category.data.icon);

        }
        if(!TextUtils.isEmpty(item.name)){
            holder.tv_contury.setText(item.name);
        }


        GlideUtils.getInstances().loadProcuctNormalImg(context, holder.iv_item_consume_push_img, Constants.WEB_IMG_URL_UPLOADS + item.logo);
        holder.iv_item_home_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BrandShopDetailActivity.class);
                intent.putExtra("id",item.id);
                context.startActivity(intent);
            }
        });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }


    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
    class ViewHolder {
        TextView     tv_title;
        TextView     tv_contury;
        ImageView    iv_item_consume_push_img;
        ImageView    iv_contury;
        LinearLayout iv_item_home_push;

    }
}
