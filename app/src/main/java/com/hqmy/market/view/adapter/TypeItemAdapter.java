package com.hqmy.market.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.utils.TextUtil;

import java.util.List;

public class TypeItemAdapter extends BaseAdapter {

    private Context        context;
    private LayoutInflater inflater;
    private List<String>   list;


    public TypeItemAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<String> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.type_item_view, null);
            holder.name = view.findViewById(R.id.tv_name);
            holder.type = view.findViewById(R.id.tv_type);
            holder.ll_bg = view.findViewById(R.id.ll_bg);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(TextUtil.isNotEmpty(list.get(i))){
            holder.name.setText(list.get(i));
        }
       holder.ll_bg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.itemclick(i,holder.name.getText().toString());
           }
       });
        return view;
    }

    public ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public interface ItemClickListener {
        void itemclick(int poistion, String string);
    }

    class ViewHolder {

        TextView     type;
        TextView     name;
        LinearLayout ll_bg;


    }

}
