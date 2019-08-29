package com.hqmy.market.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by rzb on 2017/12/28.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<T> list;

    public MyBaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        if (list.size() == 0) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = this.newView(context, position, parent);
        } else {
            view = convertView;
        }
        this.bindView(view, position, this.getItem(position));
        return view;
    }

    protected abstract View newView(Context var1, int var2, ViewGroup var3);

    protected abstract void bindView(View var1, int var2, T var3);

}
