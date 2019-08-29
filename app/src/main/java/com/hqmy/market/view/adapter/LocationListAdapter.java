package com.hqmy.market.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.LocationAddressInfo;

import java.util.List;

/**
 * Created by rzb on 2018/5/20.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.Holder> {

    private Context                   context;
    private List<LocationAddressInfo> data;
    public LocationListAdapter(Context context, List<LocationAddressInfo> data){
        this.context =context;
        this.data = data;
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_location, null);
        Holder holder = new Holder(inflate);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.tv_adress.setText(data.get(position).getDetailAddress());
        holder.tv_content.setText(data.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_adress,tv_content;
        public Holder(View itemView) {
            super(itemView);
            tv_adress = (TextView) itemView.findViewById(R.id.tv_adress);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

}
