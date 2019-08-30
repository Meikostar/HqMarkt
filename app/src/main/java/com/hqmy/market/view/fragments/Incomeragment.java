package com.hqmy.market.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;


import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.view.adapter.IncomeRecordAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.HashMap;

import butterknife.BindView;

public class Incomeragment extends BaseFragment {
    @BindView(R.id.recy_my_comment)
    RecyclerView            rvList;
    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout swipeRefreshLayout;

    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;

    private String type = "";
    IncomeRecordAdapter mAdapter;

    public Incomeragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment_layout;
    }

    @Override
    protected void initView() {
        initAdapter();

    }

    @Override
    protected void initData() {
        mCurrentPage = Constants.PAGE_NUM;
        loadData(true);
    }

    @Override
    protected void initListener() {
        setListener();
    }


    private void loadData(boolean isLoad) {
        if (isLoad) {
            showLoadDialog();
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("page", mCurrentPage + "");




    }


    private void initAdapter() {
        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new IncomeRecordAdapter();
        rvList.setAdapter(mAdapter);
    }


    private void setListener() {
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(swipeRefreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = Constants.PAGE_NUM;
                loadData(false);
            }

            @Override
            public void onLoadMore() {
                mCurrentPage++;
                loadData(false);
            }
        });
    }

    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setLoadMore(false);
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
