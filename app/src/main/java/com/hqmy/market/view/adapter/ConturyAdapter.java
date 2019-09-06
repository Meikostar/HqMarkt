package com.hqmy.market.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class ConturyAdapter extends BaseRecycleViewAdapter implements AutoLocateHorizontalView.IAutoLocateHorizontalView{
    private View view;
    private Context context;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contury_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder holders = (ItemViewHolder) holder;
        AreaDto data= (AreaDto) datas.get(position);
        if(TextUtil.isNotEmpty(data.title)){
            holders.tv_content.setText(data.title);
        }

//
//        holders.tvTime.setText(data.getTime());
//        holders.tvState.setText(data.getState()==1?"抢购进行中":(data.getState()==2?"已开抢":"即将开始"));
        holders.acb_root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemClick(position,data);
            }
        });
    }

    public interface  ItemClickListener{
        void itemClick(int poition, AreaDto data);
    }
    public ItemClickListener listener;
    public void setItemClick(ItemClickListener listeners){
        listener=listeners;
    }
    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        ItemViewHolder holders = (ItemViewHolder) holder;

        if(isSelected){
            holders.tv_content.setTextSize(15);
            TextPaint tp = holders.tv_content.getPaint();
            tp.setFakeBoldText(true);
            holders.line.setVisibility(View.VISIBLE);
            holders.tv_content.setTextColor(context.getColor(R.color.color_zs));

        }else {
            holders.line.setVisibility(View.GONE);
            holders.tv_content.setTextSize(14);
            TextPaint tp = holders.tv_content.getPaint();
            tp.setFakeBoldText(false);
            holders.tv_content.setTextColor(context.getColor(R.color.my_color_333333));

        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_content)
        TextView       tv_content;
        @BindView(R.id.line)
        View       line;

        @BindView(R.id.acb_root_view)
        RelativeLayout acb_root_view;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
