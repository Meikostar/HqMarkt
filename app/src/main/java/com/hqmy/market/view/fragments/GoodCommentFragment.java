package com.hqmy.market.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.CommentTopicBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.GoodCommentAdapter;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 商品评价
 */
public class GoodCommentFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private GoodCommentAdapter mAdapter;
    private int mPage = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_good_comment;
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
                getMyCommentListData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getMyCommentListData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new GoodCommentAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                CommentTopicBean commentTopicBean = mAdapter.getItem(position);
                if (commentTopicBean.getOrderItem() != null && commentTopicBean.getOrderItem().getData() != null){
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, commentTopicBean.getOrderItem().getData().getProduct_id());
                }else {
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, mAdapter.getItem(position).getCommented_id());
                }
                if (commentTopicBean.getCommented() != null &&  commentTopicBean.getCommented().getData() != null){
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, commentTopicBean.getCommented().getData().getType());
                }
                bundle.putString(CommodityDetailActivity.FROM, "gc");
                gotoActivity(CommodityDetailActivity.class, false, bundle);
            }
        });
    }

    /**
     * 商品评价列表
     */
    private void getMyCommentListData() {
        Map<String, String> map = new HashMap<>();
        map.put("filter[commented_type]", "SMG\\Mall\\Models\\MallProduct");
        map.put("page", mPage + "");
        map.put("include", "commented.user,orderItem");

        DataManager.getInstance().getMyCommentListData(new DefaultSingleObserver<HttpResult<List<CommentTopicBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentTopicBean>> httpResult) {
                dissLoadDialog();

                setCommentListData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, map);
    }

    private void setCommentListData(HttpResult<List<CommentTopicBean>> httpResult) {
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
