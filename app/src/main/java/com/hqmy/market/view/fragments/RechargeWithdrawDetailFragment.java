package com.hqmy.market.view.fragments;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.ScoreIncomeBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.RechargeWithdrawDetailAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 充值/提现明细
 */
@SuppressLint("ValidFragment")
public class RechargeWithdrawDetailFragment extends BaseFragment {
    /**全部*/
    public static final String TYPE_ALL = "TYPE_ALL";
    /**充值*/
    public static final String TYPE_RECHARGE = "TYPE_RECHARGE";
    /**提现*/
    public static final String TYPE_WITHDRAW = "TYPE_WITHDRAW";
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private RechargeWithdrawDetailAdapter mAdapter;
    private String mType;
    private int mPage = 1;
    public RechargeWithdrawDetailFragment( ) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge_withdraw_detail;
    }

    @Override
    protected void initView() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initData() {
        initRecyclerView();
    }

    @Override
    protected void initListener() {
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
    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        mAdapter = new RechargeWithdrawDetailAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);


    }
    private void walletLog() {

        HashMap<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        DataManager.getInstance().walletLog(new DefaultSingleObserver<HttpResult<List<ScoreIncomeBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ScoreIncomeBean>> httpResult) {
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                mRefreshLayout.finishLoadMore();
                mRefreshLayout.setEnableRefresh(true);

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
                mAdapter.setEmptyView(new EmptyView(getActivity()));
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
}
