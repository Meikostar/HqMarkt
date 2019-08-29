package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.NewsRecommendListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

/**
 * 新闻频道适配器
 * Created by rzb on 2019/4/20
 */
public class NewsThreeAdapter extends MyBaseAdapter<NewsRecommendListItemDto> {
    ViewHolder holder;
    Context mContext;

    public NewsThreeAdapter(Context context, List<NewsRecommendListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_news_three_list_layout, null);
        holder = new ViewHolder();
        holder.iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);
        holder.tv_bottom_title = view.findViewById(R.id.tv_bottom_title);
        holder.tv_bottom_time = view.findViewById(R.id.tv_bottom_time);
        holder.tv_bottom_content = view.findViewById(R.id.tv_bottom_content);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int position, NewsRecommendListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        GlideUtils.getInstances().loadRoundCornerImg(mContext,holder.iv_bottom,1.5f,Constants.WEB_IMG_URL_UPLOADS + var3.getImg());
        holder.tv_bottom_title.setText(var3.getTitle());
        holder.tv_bottom_content.setText(var3.getIntroduce());
    }

    class ViewHolder {
        ImageView      iv_bottom;
        TextView       tv_bottom_title;
        TextView       tv_bottom_time;
        TextView       tv_bottom_content;
    }
}
