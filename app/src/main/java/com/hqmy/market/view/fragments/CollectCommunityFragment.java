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
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.CollectCommunityAdapter;
import com.hqmy.market.view.mainfragment.community.TopicDetailActivity;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 收藏 社区
 */
public class CollectCommunityFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CollectCommunityAdapter mAdapter;
    private int mPage;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collect_community;
    }

    @Override
    protected void initView() {
        initListView();

    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();
    }

    /**
     * 获取收藏数据
     */
    private void getCollectData() {
        Map<String, String> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("page", mPage + "");
        map.put("include", "user,favoritersCount,likersCount");
        DataManager.getInstance().getCollectionList(new DefaultSingleObserver<HttpResult<List<TopicListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<TopicListItemDto>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void setData(HttpResult<List<TopicListItemDto>> httpResult) {
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

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getCollectData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getCollectData();
            }
        });
    }

    private void initListView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new CollectCommunityAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.content:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(TopicDetailActivity.TOPIC_ID, mAdapter.getItem(position).getId());
                        gotoActivity(TopicDetailActivity.class, false, bundle);
                        break;
                    case R.id.tv_deleted:
                        removeCollect(mAdapter.getItem(position).getId());
                        break;
                }
            }
        });
    }

    /**
     * 移除收藏夹
     *
     * @param id
     */
    private void removeCollect(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", id);

        DataManager.getInstance().delCollection(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                ToastUtil.showToast("移除成功");
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("移除成功");
                    mRefreshLayout.autoRefresh();
                } else {
                    dissLoadDialog();
                    ApiException.getHttpExceptionMessage(throwable);
                }
            }
        }, map);
    }
}
