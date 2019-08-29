package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.hqmy.market.bean.PublishInfo;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyPublishAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 猜你喜欢-我的发布
 */
public class MyPublishActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTitleRight;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyPublishAdapter mAdapter;
    private int mPage = 1;
    private ArrayList<Object> mGoodsDatas = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_publish;
    }

    @Override
    public void initView() {
        mTitleText.setText("我发布的");
        initListView();
    }

    @Override
    public void initData() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getData();
            }
        });
    }

    private void initListView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyPublishAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_button_2:
                        //下架
                        editProducts(mAdapter.getItem(position).getId(),mAdapter.getItem(position).isOn_sale());
                        break;
                    case R.id.tv_button_3:
                        //编辑宝贝
                        Intent intent = new Intent(MyPublishActivity.this,EditGoodsActivity.class);
                        intent.putExtra("publishInfo",mAdapter.getItem(position));
                        startActivityForResult(intent,1);
                        break;
                }
            }
        });
    }

    /**
     * 我买到的
     */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("include", "category");
        map.put("page", mPage + "");
        DataManager.getInstance().getSellerProducts(new DefaultSingleObserver<HttpResult<List<PublishInfo>>>() {
            @Override
            public void onSuccess(HttpResult<List<PublishInfo>> httpResult) {
                dissLoadDialog();
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, map);
    }

    private void setData(HttpResult<List<PublishInfo>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            mGoodsDatas.clear();
            mGoodsDatas.addAll(httpResult.getData());
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

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }
    private void editProducts(String id,Boolean on_sale) {
        Map<String, Object> map = new HashMap<>();
        if (on_sale){
            //已上架，即将下架
            map.put("on_sale",0);
        }else {
            //已下架，即将上架
            map.put("on_sale",1);
        }
        showLoadDialog();
        DataManager.getInstance().editProducts(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    mRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                }
            }
        },id, map);
    }
    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
           mRefreshLayout.autoRefresh();
        }
    }
}
