package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class NewsTwoAdapter extends MyBaseAdapter<NewsRecommendListItemDto> {
    ViewHolder holder;
    Context mContext;

    public NewsTwoAdapter(Context context, List<NewsRecommendListItemDto> list) {
        super(context, list);
        this.mContext = context;
    }

    @Override
    protected View newView(Context var1, int var2, ViewGroup var3) {
        View view = View.inflate(var1, R.layout.item_news_two_list_layout, null);
        holder = new ViewHolder();
        holder.news_two_layout_top = (RelativeLayout)view.findViewById(R.id.news_two_layout_top);
        holder.news_two_layout_bottom = (RelativeLayout)view.findViewById(R.id.news_two_layout_bottom);
        holder.iv_news_top = (ImageView) view.findViewById(R.id.iv_news_top);
        holder.tv_news_title = view.findViewById(R.id.tv_news_title);
        holder.iv_news_bottom_right = (ImageView) view.findViewById(R.id.iv_news_bottom_right);
        holder.tv_two_title = (TextView) view.findViewById(R.id.tv_two_title);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View var1, int position, NewsRecommendListItemDto var3) {
        holder = (ViewHolder) var1.getTag();
        if(var3 != null) {
            if (position == 0) {
                holder.news_two_layout_top.setVisibility(View.VISIBLE);
                holder.news_two_layout_bottom.setVisibility(View.GONE);
                GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_news_top, Constants.WEB_IMG_URL_UPLOADS + var3.getImg());
                holder.tv_news_title.setText(var3.getTitle());

                if(var3.getImg() != null && var3.getImg() != "") {
                    GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_news_top, Constants.WEB_IMG_URL_UPLOADS + var3.getImg());
                }else{
                    holder.iv_news_top.setVisibility(View.GONE);
                }
                if(var3.getTitle() != null && var3.getTitle() != "") {
                    holder.tv_news_title.setText(var3.getTitle());
                }else{
                    holder.tv_news_title.setVisibility(View.GONE);
                }

            }

            if(position != 0){
                holder.news_two_layout_top.setVisibility(View.GONE);
                holder.news_two_layout_bottom.setVisibility(View.VISIBLE);
                if(var3.getImg() != null && var3.getImg() != "") {
                    GlideUtils.getInstances().loadNormalImg(mContext, holder.iv_news_bottom_right, Constants.WEB_IMG_URL_UPLOADS + var3.getImg());
                }else{
                    holder.iv_news_bottom_right.setVisibility(View.GONE);
                }
                if(var3.getTitle() != null && var3.getTitle() != "") {
                    holder.tv_two_title.setText(var3.getTitle());
                }else{
                    holder.tv_two_title.setVisibility(View.GONE);
                }
            }
        }
    }

    class ViewHolder {
        RelativeLayout news_two_layout_top;
        RelativeLayout news_two_layout_bottom;
        ImageView iv_news_top;
        TextView tv_news_title;
        ImageView iv_news_bottom_right;
        TextView tv_two_title;
    }
}
