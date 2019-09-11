package com.hqmy.market.view.activity;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyOrderEvaluateAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评价列表
 */
public class MyOrderEvaluateListActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_item_order_status)
    TextView tvItemOrderStatus;
    @BindView(R.id.recy_evaluate)
    RecyclerView recyclerView;
    MyOrderEvaluateAdapter mAdapter;
    private String id;
    private String orderNo = "";

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_myorder_evaluate_list_layout;
    }

    @Override
    public void initView() {
        initAdapter();
        tvTitleText.setText("评价列表");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Constants.INTENT_ID);
        }
    }

    @Override
    public void initData() {
        getEvaluateList(id);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyOrderEvaluateAdapter(null, this);
        recyclerView.addItemDecoration(new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyOrderItemDto myOrderItemDto = mAdapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_ORDER_NO, orderNo);
                bundle.putString(Constants.INTENT_ORDER_STATUS,tvItemOrderStatus.getText().toString() );
                bundle.putString(Constants.INTENT_ID, myOrderItemDto.getProduct().getData().getId());
                bundle.putString(Constants.IMAGEITEM_IMG_URL, myOrderItemDto.getProduct().getData().getCover());
                bundle.putString("goods_title", myOrderItemDto.getTitle());
                bundle.putString("goods_price", myOrderItemDto.getPrice());
                bundle.putString("goods_num", myOrderItemDto.getQty());
                StringBuilder options = new StringBuilder();
                if (myOrderItemDto.getOptions() != null && myOrderItemDto.getOptions().get("尺码") != null) {
                    options.append(myOrderItemDto.getOptions().get("尺码") + ",");
                }
                if (myOrderItemDto.getOptions() != null && myOrderItemDto.getOptions().get("颜色") != null) {
                    options.append(myOrderItemDto.getOptions().get("颜色"));
                }
                bundle.putString("goods_options", options.toString());
                bundle.putString("orderId", myOrderItemDto.getId());
                gotoActivity(MyOrderEvaluateActivity.class, false, bundle);
            }
        });
    }

    private void getEvaluateList(String id) {
        if (TextUtils.isEmpty(id)) return;
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "items.product,items.comment");
        DataManager.getInstance().commentList(new DefaultSingleObserver<HttpResult<MyOrderDto>>() {
            @Override
            public void onSuccess(HttpResult<MyOrderDto> myOrderDto) {
                dissLoadDialog();
                if (myOrderDto != null && myOrderDto.getData() != null && myOrderDto.getData().getItems() != null) {
                    orderNo = myOrderDto.getData().getNo();
                    tvOrderNo.setText("订单编号：" + orderNo);
                    tvItemOrderStatus.setText(myOrderDto.getData().getStatus_msg());
                    mAdapter.setNewData(myOrderDto.getData().getItems().getData());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(throwable.getMessage());
            }
        }, id, map);
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
