package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderLogisticsDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyOrderLogisticsAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 物流信息
 */
public class MyOrderLogisticsActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.recy_logistics)
    RecyclerView recyclerView;
    MyOrderLogisticsAdapter mAdapter;
    String orderId;
    @BindView(R.id.tv_myorder_consignee)
    TextView tvMyorderConsignee;
    @BindView(R.id.tv_myorder_phone)
    TextView tvMyorderPhone;
    @BindView(R.id.tv_myorder_address)
    TextView tvMyorderAddress;


    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_myorder_logistics_layout;
    }

    @Override
    public void initView() {
        initAdapter();
        tvTitleText.setText("查看物流");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderId = bundle.getString("id");
        }

    }

    @Override
    public void initData() {
        getLogisticsInfo();
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyOrderLogisticsAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void getLogisticsInfo() {
        if (TextUtils.isEmpty(orderId)) {
            ToastUtil.showToast("订单id不能为空");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("extra_include", "track");
        map.put("include", "items.product,shipments,address");
        showLoadDialog();
        DataManager.getInstance().getLogisticsList(new DefaultSingleObserver<HttpResult<MyOrderLogisticsDto>>() {
            @Override
            public void onSuccess(HttpResult<MyOrderLogisticsDto> myOrderLogisticsDto) {
                dissLoadDialog();
                if (myOrderLogisticsDto != null && myOrderLogisticsDto.getData() != null){
                    setData(myOrderLogisticsDto.getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(throwable.getMessage());
            }
        }, orderId, map);
    }

    private void setData(MyOrderLogisticsDto myOrderLogisticsDto) {
        if (myOrderLogisticsDto != null && myOrderLogisticsDto.getAddress() != null && myOrderLogisticsDto.getAddress().getData() != null){
            tvMyorderConsignee.setText("收货人:"+myOrderLogisticsDto.getAddress().getData().getName());
            tvMyorderPhone.setText(myOrderLogisticsDto.getAddress().getData().getMobile());
            String details = myOrderLogisticsDto.getAddress().getData().getArea() + myOrderLogisticsDto.getAddress().getData().getDetail();
            tvMyorderAddress.setText(details);
            if (myOrderLogisticsDto.getItems() != null && myOrderLogisticsDto.getItems().getData() != null && myOrderLogisticsDto.getItems().getData().size()>0){
                if (myOrderLogisticsDto.getShipments() != null && myOrderLogisticsDto.getShipments().getData() != null){
                    mAdapter.setShipmentBeanList(myOrderLogisticsDto.getShipments().getData());
                }
                mAdapter.setNewData(myOrderLogisticsDto.getItems().getData());
            }else {
                mAdapter.setEmptyView(new EmptyView(this));
            }
        }
    }
    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
