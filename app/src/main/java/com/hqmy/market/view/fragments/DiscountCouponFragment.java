package com.hqmy.market.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.view.adapter.DiscountCouponAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 优惠券
 */
public class DiscountCouponFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    private DiscountCouponAdapter mAdapter;
    private int                   mPage;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discount_coupon;
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

        mAdapter = new DiscountCouponAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        ArrayList<String> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add("");
        }
//        mAdapter.setNewData(list);
    }
}
