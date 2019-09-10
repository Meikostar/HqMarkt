package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;

import java.util.List;


public class AreaImageAdapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<AreaDto>  list;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private int[] imgs;


    private String[] names;

    public AreaImageAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<AreaDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list!=null?list.size():0;
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
            view = inflater.inflate(R.layout.item_area_img_layout, null);

            holder.iv_img = view.findViewById(R.id.iv_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AreaDto bannerDto = list.get(i);
         holder.iv_img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, BrandShopDetailActivity.class);
                 intent.putExtra("id",bannerDto.id+"");
                 context.startActivity(intent);
             }
         });
        GlideUtils.getInstances().loadNormalImg(context,holder.iv_img,bannerDto.logo,R.drawable.moren_sf);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {

        ImageView iv_img;

    }
}
