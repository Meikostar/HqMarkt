package com.hqmy.market.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CouponDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.DiscountCouponAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的卡包
 */
public class MyCardBagActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    DiscountCouponAdapter mAdapter;
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_card_bag;
    }

    @Override
    public void initView() {
        tvTitleText.setText("我的卡包");
        initRecyclerView();
    }

    @Override
    public void initData() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DiscountCouponAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void getData() {
        Map<String, Object> map = new HashMap<>();
        map.put("include", "coupon");
        DataManager.getInstance().getUserCoupons(new DefaultSingleObserver<HttpResult<List<CouponDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CouponDto>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, map);
    }

    private void setData(HttpResult<List<CouponDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
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
