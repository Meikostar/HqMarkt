package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 *爱心家庭类别适配器
 * Created by rzb on 2019/5/20
 */

public class FamilySortListAapter extends MyBaseAdapter<CategoryListdto> {
    ViewHolder holder;
    Context mContext;

    public FamilySortListAapter(Context context, List<CategoryListdto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_family_sort_list, null);
        holder = new ViewHolder();
        holder.iv = (ImageView) view.findViewById(R.id.iv_family_sort);
        holder.tv = (TextView) view.findViewById(R.id.tv_family_sort);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, CategoryListdto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv.setText(var3.getTitle());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv,
                Constants.WEB_IMG_URL_UPLOADS + var3.getIcon());
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
