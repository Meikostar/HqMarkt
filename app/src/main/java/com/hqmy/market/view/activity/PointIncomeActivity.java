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
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.bean.ScoreIncomeBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.PointIncomeAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分收入
 */
public class PointIncomeActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_total_score)
    TextView tvTotalScore;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private int mPage = 1;
    private PointIncomeAdapter mAdapter;

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                walletLog();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                walletLog();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.point_income_layout;
    }

    @Override
    public void initView() {
        mTitleText.setText("积分收入");
        initRecyclerView();
    }

    @Override
    public void initData() {
        getBalance();
        mRefreshLayout.autoRefresh();
    }

    private void getBalance() {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                if (balanceDto != null) {
                    tvTotalScore.setText(balanceDto.getMoney() + "");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    /**
     * 获取首页直播列表数据
     */
    private void walletLog() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("money_type", "score");
        map.put("page", mPage + "");
        DataManager.getInstance().walletLog(new DefaultSingleObserver<HttpResult<List<ScoreIncomeBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ScoreIncomeBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }

    private void setData(HttpResult<List<ScoreIncomeBean>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                //mAdapter.setEmptyView(new EmptyView(CollectionActivity.this));
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

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));
        mAdapter = new PointIncomeAdapter();
        mRecyclerView.setAdapter(mAdapter);
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
