package com.hqmy.market.view.mainfragment.learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.bean.LiveMessageInfo;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LiveCheckFailActivity;
import com.hqmy.market.view.activity.LiveCheckingActivity;
import com.hqmy.market.view.activity.LiveSearchActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.RequestLivePermissionActivity;
import com.hqmy.market.view.activity.StartLiveActivity;
import com.hqmy.market.view.widgets.UPMarqueeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 在线直播
 */
public class OnLineLiveFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    ListView mRecyclerView;
    @BindView(R.id.tv_upmarquee_view)
    UPMarqueeView mUPMarqueeView;

    private OnlineLiveAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_live;
    }

    @Override
    protected void initView() {
        mRefreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void initData() {
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getConfigs();
                getHomeData();
            }
        });
    }

    private void initRecyclerView() {
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));
        mAdapter = new OnlineLiveAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initHeadView(List<String> list) {
//        mAdapter.removeAllHeaderView();
//        View mHeadView = View.inflate(getActivity(), R.layout.head_online_live_notice, null);
//        //公告
//        UPMarqueeView mUPMarqueeView = mHeadView.findViewById(R.id.tv_upmarquee_view);
//        setUPMarqueeView(mUPMarqueeView, list);
////        mAdapter.addHeaderView(mHeadView);
//        mRecyclerView.addHeaderView(mHeadView);
    }

    private void setUPMarqueeView(UPMarqueeView upMarqueeView, List<String> list) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TextView tv = new TextView(getActivity());
            tv.setText(list.get(i));
            tv.setSingleLine();
            tv.setEllipsize(TextUtils.TruncateAt.END);
            views.add(tv);
        }
        upMarqueeView.setViews(views);
    }

    @OnClick({R.id.rl_onlive_live_oplayer, R.id.et_search_room})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_onlive_live_oplayer:
                if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                    gotoActivity(LoginActivity.class);
                    return;
                }
                isPlayer();
                break;
            case R.id.et_search_room:
                startActivity(new Intent(getActivity(), LiveSearchActivity.class));
                break;
        }
    }

    private void isPlayer() {
        //showLoadDialog();
        DataManager.getInstance().getLiveInfo(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //dissLoadDialog();
                if (result != null && result.getLive_apply() != null) {
                    //申请状态 0未申请 1审核通过 2审核中 3审核失败
                    int status = result.getLive_apply().getStatus();
                    if (status == 1) {
                        gotoActivity(StartLiveActivity.class);
                    } else if (status == 2) {
                        gotoActivity(LiveCheckingActivity.class);
                    } else if (status == 3) {
                        Bundle bundle = new Bundle();
                        bundle.putString("reasonTip", result.getLive_apply().getReason());
                        gotoActivity(LiveCheckFailActivity.class, false, bundle);
                    } else {
                        gotoActivity(RequestLivePermissionActivity.class);
                    }

                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                if(ApiException.getInstance().getCode() == 500){
                    gotoActivity(RequestLivePermissionActivity.class);
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        });
    }

    /**
     * 获取直播公告
     */
    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getLiveConfigs(new DefaultSingleObserver<HttpResult<LiveMessageInfo>>() {
            @Override
            public void onSuccess(HttpResult<LiveMessageInfo> result) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();
                if (result != null && result.getData() != null && result.getData().getLive_message() != null && result.getData().getLive_message().getTop_msg() != null && result.getData().getLive_message().getTop_msg().size() > 0) {
//                    initHeadView(Arrays.asList(result.getData().getLive_message().getTop_msg().split(",")));
                    setUPMarqueeView(mUPMarqueeView, result.getData().getLive_message().getTop_msg());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();

            }
        });
    }

    /**
     * 获取首页直播列表数据
     */
    private void getHomeData() {
        //showLoadDialog();
        DataManager.getInstance().getHomeLiveCates(new DefaultSingleObserver<HttpResult<List<LiveCatesBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<LiveCatesBean>> result) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();
                if (result != null) {
                    mAdapter.setDataList(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();

            }
        }, 1);
    }
}
