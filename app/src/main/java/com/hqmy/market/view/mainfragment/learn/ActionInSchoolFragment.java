package com.hqmy.market.view.mainfragment.learn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import com.hqmy.market.bean.ActionBean;
import com.hqmy.market.bean.OnlineApplyInfo;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.ActionDetailActivity;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 活动 (校内校外活动)
 */
@SuppressLint("ValidFragment")
public class ActionInSchoolFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private BaseQuickAdapter mAdapter;
    /**
     * 活动类型 0 校内活动  1 校外活动 2 已报名
     */
    private String mType = "0";
    private int mPage = 1;

    public ActionInSchoolFragment() {
    }

    public ActionInSchoolFragment(String type) {
        this.mType = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_action_in_shcool;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        initRecyclerView();

        getActionListData();
    }

    /**
     * 活动列表
     */
    private void getActionListData() {
        if ("2".equals(mType)) {
            DataManager.getInstance().onlineApplyList(new DefaultSingleObserver<HttpResult<List<OnlineApplyInfo>>>() {
                @Override
                public void onSuccess(HttpResult<List<OnlineApplyInfo>> httpResult) {
                    //dissLoadDialog();
                    List<ActionBean> actionBeans = new ArrayList<>();
                    if (httpResult != null && httpResult.getData() != null) {
                        for (int i = 0; i < httpResult.getData().size(); i++) {
                            if (httpResult.getData().get(i).getPage() != null && httpResult.getData().get(i).getPage().getData() != null) {
                                actionBeans.add(httpResult.getData().get(i).getPage().getData());
                            }
                        }
                    }
                    setActionData(actionBeans);
                    if (actionBeans != null && httpResult.getMeta().getPagination() != null) {
                        if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    //dissLoadDialog();
                }
            });
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("filter[flag1]", mType);
            map.put("mPage", mPage + "");
            map.put("sort", "-order,-id");

            DataManager.getInstance().getActionListData(new DefaultSingleObserver<HttpResult<List<ActionBean>>>() {
                @Override
                public void onSuccess(HttpResult<List<ActionBean>> httpResult) {
                    //dissLoadDialog();
                    if (httpResult == null || httpResult.getData() == null) {
                        return;
                    }
                    setActionData(httpResult.getData());
                    if (httpResult.getData() != null && httpResult.getMeta().getPagination() != null) {
                        if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                            mRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    //dissLoadDialog();
                }
            }, map);
        }

    }

    private void setActionData(List<ActionBean> actionBeanList) {
        if (mPage <= 1) {
            mAdapter.setNewData(actionBeanList);
            if (actionBeanList == null || actionBeanList.size() == 0) {
                mAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(actionBeanList);
        }
    }

    @Override
    protected void initListener() {

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionBean bean = (ActionBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt(ActionDetailActivity.ACTION_ID, bean.getId());
                bundle.putString("mType", mType);
                gotoActivity(ActionDetailActivity.class, false, bundle);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getActionListData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getActionListData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        if ("2".equals(mType)) {
            mAdapter = new OnlieApplyAdapter(getActivity());
        } else {
            mAdapter = new ActionInSchoolAdapter(getActivity());
        }
        mRecyclerView.setAdapter(mAdapter);

    }
}
