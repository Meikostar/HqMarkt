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
import com.hqmy.market.bean.AttentionCommunityBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.AttentionCommunityAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关注用户列表
 */
public class AttentionUserListActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    int mPage = 1;
    private AttentionCommunityAdapter mAdapter;

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getAttentionList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getAttentionList();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_attention_user_list;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AttentionCommunityAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mTitleText.setText("关注用户列表");
        mRefreshLayout.autoRefresh();
    }

    /**
     * 我的关注列表
     */
    private void getAttentionList() {
        Map<String, String> map = new HashMap<>();
        mPage = 1;
        map.put("page", mPage + "");
        DataManager.getInstance().getAttentionList(new DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<AttentionCommunityBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void setData(HttpResult<List<AttentionCommunityBean>> httpResult) {
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

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
