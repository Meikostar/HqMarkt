package com.hqmy.market.view.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 输入交易密码
 * Created by dahai on 2018/12/18.
 */

public class InputPwdAdapter extends BaseAdapter {
    private Context       context;
    private List<Integer> list;// 传过来的数据


    public InputPwdAdapter(Context context, List<Integer> lists) {
        super();
        this.context = context;
        this.list = lists;
    }

    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        //如果convertView为空就创建一个View，否则使用getTag缓存的View
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_input_pwd_layout, null);
            holder = new ChildViewHolder();
            holder.delete = convertView.findViewById(R.id.item_input_password_del);
            holder.num = convertView.findViewById(R.id.item_input_password_num);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.num.setText(position + 1 + "");
        if (position == 9) {
            holder.num.setText("");
        }
        if (position == 10) {
            holder.num.setText(0 + "");
        }
        if (position == 11) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.num.setVisibility(View.GONE);
        }
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140);
        convertView.setLayoutParams(param);
        return convertView;
    }


    @SuppressLint("CheckResult")
    private void bindClickEvent(View view, final Action action) {
        Observable<Object> observable = RxView.clicks(view);
        observable.throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(trigger -> action.run());
    }

    class ChildViewHolder {
        ImageView delete;
        TextView  num;
    }
}