package com.hqmy.market.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.view.adapter.MemberStoresAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 店铺会员
 */
public class MemberStoresFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private MemberStoresAdapter mAdapter;
    private int                 mPage;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_member_stores;
    }

    @Override
    protected void initView() {
        initRecyclerView();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new MemberStoresAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
        mAdapter.setNewData(list);
    }
}
