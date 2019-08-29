package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;

import java.util.List;


/**
 * 开播 直播类别 GridAdapter
 */

public class StartLiveGridListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mList;

    public StartLiveGridListAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
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
            convertView = mInflater.inflate(R.layout.item_start_live_grid_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView mName;
        ImageView mIcon;

        public ViewHolder(View view) {
            mName = (TextView) view.findViewById(R.id.tv_name);
            mIcon = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

}
