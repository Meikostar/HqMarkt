package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.MessageFeedbackTypeBean;

import java.util.ArrayList;

public class MessageFeedbackGridAdapter extends BaseAdapter {
    private ArrayList<MessageFeedbackTypeBean> mDatas;
    private Context                            mContext;

    public MessageFeedbackGridAdapter(Context context, ArrayList<MessageFeedbackTypeBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_message_feedback, null);
            holder = new  ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = ( ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(mDatas.get(position).getType());
        holder.mTextView.setSelected(mDatas.get(position).isSelect());
        return convertView;
    }

    public void RefreshData(int position) {
        if (mDatas == null || position < 0 || position >= mDatas.size()) {
            return;
        }
        for (MessageFeedbackTypeBean bean : mDatas) {
            bean.setSelect(false);
        }
        mDatas.get(position).setSelect(true);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView mTextView;

        public ViewHolder(View view) {
            mTextView =  view.findViewById(R.id.tv_item_feedback_type);
        }
    }
}
