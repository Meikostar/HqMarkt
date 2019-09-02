package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.bean.DetailDto;
import com.hqmy.market.view.activity.EnterprisePermissionActivity;
import com.hqmy.market.view.activity.WebUtilsActivity;

import java.util.List;

/**
 * 商品类型适配器
 * Created by rzb on 2019/6/20
 */
public class HelperAdapter extends MyBaseAdapter<DetailDto> {
    ViewHolder holder;
    Context mContext;

    public HelperAdapter(Context context, List<DetailDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_helper, null);
        holder = new ViewHolder();
        holder.tv_type_title = (TextView) view.findViewById(R.id.tv_content);
        holder.rlBg = (RelativeLayout) view.findViewById(R.id.rl_bg);

        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, DetailDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_type_title.setText(var3.title);
        holder.rlBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebUtilsActivity.class);
                intent.putExtra("id",var3.id);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder {
        TextView tv_type_title;
        RelativeLayout rlBg;
    }
}
