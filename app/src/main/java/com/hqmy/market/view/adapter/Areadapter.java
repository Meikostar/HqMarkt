package com.hqmy.market.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * Created by mykar on 161/4/13.
 */

public class Areadapter extends BaseAdapter {
    private Context        context;
    private LayoutInflater inflater;
    private List<AreaDto>  list;
    private int            type = 0;//0 表示默认使用list数据
    private String         types;


    private int[] imgs;


    private String[] names;

    public Areadapter(Context context) {
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
            view = inflater.inflate(R.layout.tools_item, null);
            holder.txt_name = view.findViewById(R.id.txt_name);
            holder.img_icon = view.findViewById(R.id.img_icon);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        AreaDto bannerDto = list.get(i);

        if(!TextUtils.isEmpty(bannerDto.title)){
            holder.txt_name.setText(bannerDto.title);
        }

        GlideUtils.getInstances().loadRoundImg(context,holder.img_icon,bannerDto.icon,R.drawable.moren_sf);
        // PROFILE_ITEM item = list.get(i);
        return view;
    }



    class ViewHolder {
        TextView txt_name;
        ImageView img_icon;

    }
}
