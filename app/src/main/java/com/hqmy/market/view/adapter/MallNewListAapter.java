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
 * 联盟商城 新品驾到
 * Created by rzb on 2019/5/20
 */
public class MallNewListAapter extends MyBaseAdapter<NewListItemDto> {
    ViewHolder holder;
    Context mContext;

    public MallNewListAapter(Context context, List<NewListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_mall_new_list, null);
        holder = new ViewHolder();
        holder.iv_mall_new = (ImageView) view.findViewById(R.id.iv_mall_new);
        holder.tv_mall_new = (TextView) view.findViewById(R.id.tv_mall_new);
        holder.tv_mall_new_price = (TextView) view.findViewById(R.id.tv_mall_new_price);
        holder.tv_mall_new_type = view.findViewById(R.id.tv_mall_new_type);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int var2, NewListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        holder.tv_mall_new.setText(var3.getTitle());
        holder.tv_mall_new_price.setText("¥" + var3.getPrice());
        String labelStr = "";
        List<String> labLists = var3.getLabels();
        if(labLists != null && labLists.size() > 0) {
            for (int i = 0; i < labLists.size(); i++) {
                if(i==0){
                    labelStr = labelStr + labLists.get(i);
                }else {
                    labelStr = labelStr + "," + labLists.get(i);
                }
            }
        }
        if(labelStr != "") {
            holder.tv_mall_new_type.setVisibility(View.VISIBLE);
            holder.tv_mall_new_type.setText(labelStr);
        }else{
            holder.tv_mall_new_type.setVisibility(View.GONE);
        }
        if(var3.getImgs() != null) {
            GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_mall_new, Constants.WEB_IMG_URL_UPLOADS + var3.getImgs().get(0));
        }
    }

    class ViewHolder {
        ImageView iv_mall_new;
        TextView tv_mall_new;
        TextView tv_mall_new_price;
        TextView tv_mall_new_type;
    }
}
