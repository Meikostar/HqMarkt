package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class BrandImgAdapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<String>  data;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private String[] names;

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    public void setTypes(String types) {
        this.types = types;
        notifyDataSetChanged();
    }

    public BrandImgAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> data) {
        this.data = data;

    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_good_gab_view, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GlideUtils.getInstances().loadNormalImg(context,holder.iv_img,data.get(i));

        holder.ll_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // PROFILE_ITEM item = list.get(i);
        return view;
    }

    //新建两个内部接口：
    public interface OnItemClickListener {
        void onItemClick(String url);
    }

    //新建两个私有变量用于保存用户设置的监听器及其set方法：
    private OnItemClickListener mOnItemClickListener;


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    static
    class ViewHolder {
        @BindView(R.id.iv_img)
        ImageView    iv_img;

        @BindView(R.id.ll_bg)
        LinearLayout ll_bg;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
