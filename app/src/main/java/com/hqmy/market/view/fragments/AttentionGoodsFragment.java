package com.hqmy.market.view.fragments;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.view.adapter.AttentionGoodsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 关注 商品
 */
public class AttentionGoodsFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private AttentionGoodsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collect_store;
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

    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mAdapter = new AttentionGoodsAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add(i + "");
        }
        mAdapter.setNewData(data);

    }
}
