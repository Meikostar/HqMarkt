package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.BrandListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

/**
 *联盟商城品牌列表适配器
 * Created by rzb on 2019/4/20
 */

public class MallBrandListAapter extends MyBaseAdapter<BrandListItemDto> {
    ViewHolder holder;
    Context mContext;

    public MallBrandListAapter(Context context, List<BrandListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_mall_brand_list, null);
        holder = new ViewHolder();
        holder.iv = (ImageView) view.findViewById(R.id.iv_mall_brand);
        holder.tv = (TextView) view.findViewById(R.id.tv_mall_brand);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, BrandListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv.setText(var3.getTitle());
        GlideUtils.getInstances().loadNormalImg(mContext, holder.iv,
                Constants.WEB_IMG_URL_UPLOADS + var3.getLogo());
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
