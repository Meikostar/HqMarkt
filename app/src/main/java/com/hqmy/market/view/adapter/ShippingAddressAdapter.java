package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.view.activity.AddShippingAddressActivity;
import com.hqmy.market.view.widgets.swipemenu.BaseSwipListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShippingAddressAdapter extends BaseSwipListAdapter {
    private Context          mContext;
    private List<AddressDto> data = new ArrayList<>();
    private LayoutInflater   inflater;

    public ShippingAddressAdapter(Context context, List<AddressDto> data) {
        this.mContext = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_shipping_address, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AddressDto addressDto = data.get(position);
        holder.mConsigneeName.setText(addressDto.getName());
        holder.mConsigneePhone.setText(addressDto.getMobile());
        holder.mConsigneeAddress.setText("收货地址："+addressDto.getArea()+addressDto.getDetail());
        holder.mSelectStatus.setChecked("1".equals(addressDto.getIs_default()));
        holder.mCnsigneeNormal.setVisibility("1".equals(addressDto.getIs_default())?View.VISIBLE:View.GONE);

        holder.mAddressContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressDto addressDto = data.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("address",addressDto);

                Intent intent = new Intent(mContext, AddShippingAddressActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.rl_address_container)
        RelativeLayout mAddressContainer;

        @BindView(R.id.tv_consignee_name)
        TextView       mConsigneeName;

        @BindView(R.id.tv_consignee_phone)
        TextView mConsigneePhone;
        @BindView(R.id.tv_consignee_address)
        TextView mConsigneeAddress;

        @BindView(R.id.cb_consignee_select_status)
        CheckBox mSelectStatus;
        @BindView(R.id.tv_consignee_normal)
        TextView mCnsigneeNormal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
