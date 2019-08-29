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
import java.util.Map;

/**
 * 实体店铺详情的适配器
 * Created by rzb on 2019/6/20
 */
public class StProductsAapter extends MyBaseAdapter<NewListItemDto> {
    ViewHolder holder;
    Context mContext;

    public StProductsAapter(Context context, List<NewListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_st_product_list, null);
        holder = new ViewHolder();
        holder.iv_st_logo = (ImageView) view.findViewById(R.id.iv_st_logo);
        holder.tv_st_product_name = (TextView) view.findViewById(R.id.tv_st_product_name);
        holder.tv_st_brief = (TextView) view.findViewById(R.id.tv_st_brief);
        holder.tv_st_label_one = (TextView) view.findViewById(R.id.tv_st_label_one);
        holder.tv_st_label_two = (TextView) view.findViewById(R.id.tv_st_label_two);
        holder.tv_st_price = (TextView) view.findViewById(R.id.tv_st_price);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, NewListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_st_product_name.setText(var3.getTitle());
        if (var3.getParameter() != null){
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> entry : var3.getParameter().entrySet()){
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                builder.append(mapKey+":"+mapValue);
                builder.append("\r\n");
            }
            holder.tv_st_brief.setText(builder.toString());
        }else {
            holder.tv_st_brief.setText("");
        }
        if(var3.getLabels() != null) {
            holder.tv_st_label_one.setText(var3.getLabels().get(0));
            holder.tv_st_label_two.setText(var3.getLabels().get(1));
        }
        holder.tv_st_price.setText("¥"+var3.getPrice());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_st_logo, Constants.WEB_IMG_URL_UPLOADS + var3.getCover());
    }

    class ViewHolder {
        ImageView iv_st_logo;
        TextView tv_st_product_name;
        TextView tv_st_brief;
        TextView tv_st_label_one;
        TextView tv_st_label_two;
        TextView tv_st_price;
    }
}
