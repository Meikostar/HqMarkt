package com.hqmy.market.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.MapDto;

import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class ShopItemAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 3;

    private Context mContext;


    public ShopItemAdapter(Context context) {
        this.mContext = context;

    }
    private List<MapDto> list;
    public void setData(List<MapDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public MapDto getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, R.layout.video_item_view, null);
            holder = new CityViewHolder();

            holder.tv_name = view.findViewById(R.id.tv_name);
            holder.tv_commodity_info_courier = view.findViewById(R.id.tv_commodity_info_courier);

            view.setTag(holder);
        } else {
            holder = (CityViewHolder) view.getTag();
        }
        MapDto mapDto = list.get(position);
        if(!TextUtils.isEmpty(mapDto.getKey())){
            holder.tv_name.setText(mapDto.getKey());
        }
        if(!TextUtils.isEmpty(mapDto.getValue())){
            holder.tv_commodity_info_courier.setText(mapDto.getValue());
        }

        return view;
    }
    public interface TakeawayListener {
        void listener(int poistion);

    }


    private TakeawayListener listener;
    public void setTakeListener(TakeawayListener listener) {
        this.listener = listener;
    }
    public static class CityViewHolder {



        TextView tv_name;
        TextView tv_commodity_info_courier;

    }




}
