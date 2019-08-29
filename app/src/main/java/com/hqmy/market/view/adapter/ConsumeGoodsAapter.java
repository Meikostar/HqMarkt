package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 * 大牌好货的适配器
 * Created by rzb on 2019/5/20
 */
public class ConsumeGoodsAapter extends MyBaseAdapter<RecommendListDto> {
    ViewHolder holder;
    Context mContext;

    public ConsumeGoodsAapter(Context context, List<RecommendListDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_goods_gridview, null);
        holder = new ViewHolder();
        holder.iv = (ImageView) view.findViewById(R.id.iv);
        holder.tv = (TextView) view.findViewById(R.id.tv);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, RecommendListDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv.setText(var3.getShop_name());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv, Constants.WEB_IMG_URL_UPLOADS + var3.getLogo());
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
