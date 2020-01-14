package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.ProductBean;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyOrderEvaluateSuccessAdapter;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MyOrderEvaluateSuccessActivity extends BaseActivity {
    private final String TAG = MyOrderEvaluateSuccessActivity.class.getName();
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcyclerView)
    RecyclerView rcyclerView;
    private int mPage = 1;
    private MyOrderEvaluateSuccessAdapter mAdapter;
    private String type;
    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getProductsRandom();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getProductsRandom();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_myorder_evaluate_success_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("评价结果");
        type=getIntent().getStringExtra("type");
        rcyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter = new MyOrderEvaluateSuccessAdapter();
        rcyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID,mAdapter.getData().get(position).getId());
                bundle.putString(CommodityDetailActivity.MALL_TYPE, mAdapter.getData().get(position).getType());
                gotoActivity(CommodityDetailActivity.class, false, bundle);
            }
        });
    }

    @Override
    public void initData() {
       mRefreshLayout.autoRefresh();
    }

    private void getProductsRandom() {
        Map<String, String> map = new HashMap<>();
        map.put("page",mPage+"");

        DataManager.getInstance().getProductsRandom(new DefaultSingleObserver<HttpResult<List<ProductBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ProductBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }

    private void setData(HttpResult<List<ProductBean>> httpResult) {
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
        mRefreshLayout.finishLoadMoreWithNoMoreData();
//        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
//            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
//
//            }
//        }
    }
    @OnClick({R.id.iv_title_back,R.id.tv_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
            case R.id.tv_finish:
                finish();
                break;
        }
    }
}
