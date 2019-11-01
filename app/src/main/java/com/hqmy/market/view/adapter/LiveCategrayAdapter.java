package com.hqmy.market.view.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class LiveCategrayAdapter extends BaseRecycleViewAdapter implements AutoLocateHorizontalView.IAutoLocateHorizontalView{
    private View view;
    private Context context;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_categray_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder holders = (ItemViewHolder) holder;
        LiveCatesBean data= (LiveCatesBean) datas.get(position);
        if(TextUtil.isNotEmpty(data.cat_name)){
            holders.tv_content.setText(data.cat_name);
        }else {
            holders.tv_content.setText("");
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
        void itemClick(int poition, LiveCatesBean data);
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
            holders.tv_content.setTextSize(18);
            TextPaint tp = holders.tv_content.getPaint();
            tp.setFakeBoldText(true);

            holders.tv_content.setTextColor(context.getColor(R.color.my_color_zs));

        }else {

            holders.tv_content.setTextSize(13);
            TextPaint tp = holders.tv_content.getPaint();
            tp.setFakeBoldText(false);
            holders.tv_content.setTextColor(context.getColor(R.color.my_color_333333));

        }
    }
    public class ItemViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_content)
        TextView       tv_content;

        @BindView(R.id.acb_root_view)
        RelativeLayout acb_root_view;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
