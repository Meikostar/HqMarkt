package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.ArrayList;

public class MyImageAdapter extends BaseAdapter {
    private ArrayList<String> mDatas;
    private Context mContext;
    public MyImageAdapter(Context context, ArrayList<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas == null?0:mDatas.size();
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
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_my_img, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        GlideUtils.getInstances().loadNormalImg(mContext, holder.mImageView, Constants.WEB_IMG_URL_UPLOADS+ mDatas.get(position));

        return convertView;
    }

   public static class ViewHolder{
        ImageView mImageView;
       public ViewHolder(View view) {
           mImageView = (ImageView) view.findViewById(R.id.iv_my_img);
       }

   }
}
