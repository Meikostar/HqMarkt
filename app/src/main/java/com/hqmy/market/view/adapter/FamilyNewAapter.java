package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 * 爱心家庭最近上新的适配器
 * Created by rzb on 2019/5/20
 */

public class FamilyNewAapter extends MyBaseAdapter<NewListItemDto> {
    ViewHolder holder;
    Context mContext;

    public FamilyNewAapter(Context context, List<NewListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_family_new_gridview, null);
        holder = new ViewHolder();
        holder.iv_item_family_new = (ImageView) view.findViewById(R.id.iv_item_family_new);
        holder.tv_item_family_new_title = (TextView) view.findViewById(R.id.tv_item_family_new_title);
        holder.tv_item_family_integral = (TextView) view.findViewById(R.id.tv_item_family_integral);
        holder.tv_item_family_name = view.findViewById(R.id.tv_item_family_name);
        holder.iv_item_family_head = view.findViewById(R.id.iv_item_family_head);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, NewListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_item_family_new_title.setText(var3.getTitle());
        holder.tv_item_family_integral.setText(var3.getScore());
        holder.tv_item_family_name.setText(var3.getUser().getData().getName());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_item_family_new, Constants.WEB_IMG_URL_UPLOADS + var3.getCover());
        GlideUtils.getInstances().loadRoundImg(mContext, holder.iv_item_family_head, Constants.WEB_IMG_URL_UPLOADS + var3.getUser().getData().getAvatar());
    }

    class ViewHolder {
        ImageView iv_item_family_new;
        TextView tv_item_family_new_title;
        TextView tv_item_family_integral;
        TextView tv_item_family_name;
        ImageView iv_item_family_head;
    }
}
