package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.HeadLineDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.ArrayList;

/**
 * 爱心头条适配器
 * Created by rzb on 2019/6/20
 */
public class HeadLineAdapter extends MyBaseAdapter<HeadLineDto> {
    ViewHolder holder;
    Context mContext;

    public HeadLineAdapter(Context context, ArrayList<HeadLineDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_head_line, null);
        holder = new ViewHolder();
        holder.iv_item_head_line_icon = (ImageView) view.findViewById(R.id.iv_item_head_line_icon);
        holder.tv_item_head_line_title = (TextView) view.findViewById(R.id.tv_item_head_line_title);
        holder.tv_item_head_line_time = (TextView) view.findViewById(R.id.tv_item_head_line_time);
        holder.tv_item_head_line_name = (TextView) view.findViewById(R.id.tv_item_head_line_name);
        holder.tv_look_num = (TextView) view.findViewById(R.id.tv_look_num);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, HeadLineDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_item_head_line_title.setText(var3.getTitle());
        holder.tv_item_head_line_time.setText(var3.getCreated_at());
        holder.tv_look_num.setText(var3.getClick());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_item_head_line_icon, Constants.WEB_IMG_URL_UPLOADS + var3.getImg());
    }

    class ViewHolder {
        ImageView iv_item_head_line_icon;
        TextView tv_item_head_line_title;
        TextView tv_item_head_line_time;
        TextView tv_item_head_line_name;
        TextView tv_look_num;
    }
}
