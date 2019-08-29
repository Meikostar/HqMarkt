package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CouponDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyCouponAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的优惠券-可用
 */
public class MyCouponActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    MyCouponAdapter mAdapter;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    private int mPage = 1;
    private int currentPosition = -1;
    private String mallType,productId,shopId;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_coupon;
    }

    @Override
    public void initView() {
        tvTitleText.setText("我的优惠券");
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText("确定");
        initRecyclerView();
    }

    @Override
    public void initData() {
        currentPosition = getIntent().getIntExtra("currentPosition", -1);
        mallType = getIntent().getStringExtra("mallType");
        productId = getIntent().getStringExtra("productId");
        shopId = getIntent().getStringExtra("shopId");
        getData();
    }

    @Override
    public void initListener() {
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                mPage = 1;
//                getData();
//            }
//        });
//
//        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                ++mPage;
//                getData();
//            }
//        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyCouponAdapter(this, currentPosition);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == currentPosition){
                    currentPosition = -1;
                }else {
                    currentPosition = position;
                }
                mAdapter.setSelectPosition(currentPosition);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getData() {
        Map map = new HashMap<>();
        map.put("include", "coupon");
        map.put("product_id",productId);
        map.put("shop_id",shopId);
        showLoadDialog();
        DataManager.getInstance().getPreviewCoupons(new DefaultSingleObserver<HttpResult<List<CouponDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CouponDto>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, mallType,map);
    }

    private void setData(HttpResult<List<CouponDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        mAdapter.setSelectPosition(currentPosition);
        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }

        } else {

            mAdapter.addData(httpResult.getData());
        }

    }

    @OnClick({R.id.iv_title_back, R.id.tv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent();
                intent.putExtra("currentPosition", currentPosition);
                if (currentPosition >=0){
                    intent.putExtra("discountValue", mAdapter.getItem(currentPosition).getCoupon().getData().getDiscount_value());
                    intent.putExtra("couponId", mAdapter.getItem(currentPosition).getCoupon().getData().getId());
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }
}
