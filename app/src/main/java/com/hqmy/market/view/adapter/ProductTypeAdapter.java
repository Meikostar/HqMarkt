package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.bean.CategoryListdto;
import java.util.List;

/**
 * 商品类型适配器
 * Created by rzb on 2019/6/20
 */
public class ProductTypeAdapter extends MyBaseAdapter<CategoryListdto> {
    ViewHolder holder;
    Context mContext;

    public ProductTypeAdapter(Context context, List<CategoryListdto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_product_type, null);
        holder = new ViewHolder();
        holder.tv_type_title = (TextView) view.findViewById(R.id.tv_type_title);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, CategoryListdto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_type_title.setText(var3.getTitle());
    }

    class ViewHolder {
        TextView tv_type_title;
    }
}
