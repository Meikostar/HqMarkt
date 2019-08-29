package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import android.widget.ImageView;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 * 宝贝详情图片适配器
 * Created by rzb on 2019/6/20
 */
public class BabyImageAapter extends MyBaseAdapter<String> {
    ViewHolder holder;
    Context mContext;
    BaseActivity mBaseActivity;

    public BabyImageAapter(Context context, List<String> list, BaseActivity activity) {
        super(context, list);
        this.mContext = context;
        this.mBaseActivity = activity;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_baby_product_list, null);
        holder = new ViewHolder();
        holder.iv_baby_logo = (ImageView) view.findViewById(R.id.iv_baby_logo);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, String var3) {
        holder = (ViewHolder) var1.getTag();
        GlideUtils.getInstances().loadResizeImage(mContext,  holder.iv_baby_logo, Constants.WEB_IMG_URL_UPLOADS + var3, mBaseActivity);
    }

    class ViewHolder {
        ImageView iv_baby_logo;
    }
}
