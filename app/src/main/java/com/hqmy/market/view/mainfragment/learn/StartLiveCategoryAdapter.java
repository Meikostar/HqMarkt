package com.hqmy.market.view.mainfragment.learn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.LiveCatesBean;

import java.util.List;


/**
 * 开播 直播类别 GridAdapter
 */

public class StartLiveCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<LiveCatesBean> mList;
    private int selPosition = -1;

    public StartLiveCategoryAdapter(Context context, List<LiveCatesBean> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<LiveCatesBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public LiveCatesBean getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_start_live_category, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LiveCatesBean catesBean = getItem(position);
        viewHolder.mTextView.setText(catesBean.getCat_name());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selPosition = position;
                notifyDataSetChanged();
            }
        });
        if (selPosition == position) {
            viewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.my_color_ce2b2c));
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_3_ce2b2c));
        } else {
            viewHolder.mTextView.setTextColor(mContext.getResources().getColor(R.color.my_color_666666));
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_3_f7f7f7));
        }
        return convertView;
    }

    public int getSelPosition() {
        return selPosition;
    }
    public String getCateId(){
        LiveCatesBean catesBean = getItem(selPosition);
        return String .valueOf(catesBean.getId());
    }
    public static class ViewHolder {
        TextView mTextView;

        public ViewHolder(View view) {
            mTextView = (TextView) view.findViewById(R.id.tv_item_start_live_category);
        }
    }

}
