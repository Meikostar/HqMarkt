package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.DynamicBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyIssueAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我发布的
 */
public class MyIssueActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView  mRecyclerView;
    private MyIssueAdapter mAdapter;
    private int mPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_issue;
    }

    @Override
    public void initView() {
        mTitleText.setText("我发布的");
    }

    @Override
    public void initData() {
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    /**
     * 我的动态列表
     */
    private void getDynamicList() {
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage +"");
        DataManager.getInstance().getDynamicList(new DefaultSingleObserver<HttpResult<List<DynamicBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<DynamicBean>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void setData(HttpResult<List<DynamicBean>> httpResult) {
        if(httpResult == null || httpResult.getData()==null){
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

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }


    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getDynamicList();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getDynamicList();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new MyIssueAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DynamicBean bean = (DynamicBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", bean.getId());
                gotoActivity(MyIssueDetailActivity.class, false ,bundle);
            }
        });
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
