package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hqmy.market.R;

import java.util.List;


/**
 * 在线直播 头部 GridAdapter
 */

public class OnlineGridAdapter extends BaseAdapter {

    private Context        mContext;
    private LayoutInflater mInflater;
    private List<String>   mList;

    public OnlineGridAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflater.inflate(R.layout.item_online_live_head_grid, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView gridview_iv;

        public ViewHolder(View view) {
            gridview_iv = (ImageView) view.findViewById(R.id.item_online_live_grid);
        }
    }

}
