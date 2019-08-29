package com.hqmy.market.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.RefundAfterSalesAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退款/售后
 */
public class RefundAfterSalesActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private RefundAfterSalesAdapter mAdapter;
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_after_sales;
    }

    @Override
    public void initView() {
        mTitleText.setText("退款/售后");
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
                loadData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                loadData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new RefundAfterSalesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_item_refund_after_sales_detail:
                        Intent intent = new Intent(RefundAfterSalesActivity.this,RefundAfterSalesDetailActivity.class);
                        intent.putExtra("id",mAdapter.getItem(position).getId());
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("include", "order.items.product,order.shop");
        map.put("page", mPage + "");
        DataManager.getInstance().getAllUserRefundList(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<MyOrderDto>> httpResult) {
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

    private void setData(HttpResult<List<MyOrderDto>> httpResult) {
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
