package com.hqmy.market.view.mainfragment.learn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.AreaDto;
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
import com.hqmy.market.view.adapter.ConturyAdapter;
import com.hqmy.market.view.adapter.ConturyCagoriadapter;
import com.hqmy.market.view.adapter.ConturyProcutAdapter;
import com.hqmy.market.view.adapter.Liveadapter;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.UPMarqueeView;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 在线直播
 */
public class OnLineLiveFragment extends BaseFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout       mRefreshLayout;

    @BindView(R.id.tv_upmarquee_view)
    UPMarqueeView            mUPMarqueeView;
    @BindView(R.id.rl_onlive_live_oplayer)
    RelativeLayout           rlOnliveLiveOplayer;
    @BindView(R.id.et_search_room)
    TextView                 etSearchRoom;
    @BindView(R.id.tv_text1)
    TextView                 tvText1;
    @BindView(R.id.tv_text2)
    TextView                 tvText2;
    @BindView(R.id.line)
    View                     line;
    @BindView(R.id.line1)
    View                     line1;
    @BindView(R.id.grid_content)
    NoScrollGridView         gridContent;
    @BindView(R.id.grid)
    GridView                 gridView;
    @BindView(R.id.grid1)
    GridView                 gridView1;
    @BindView(R.id.auto_scroll)
    AutoLocateHorizontalView autoScroll;
    @BindView(R.id.consume_push_recy)
    RecyclerView             consumePushRecy;
    @BindView(R.id.consume_scrollView)
    CustomView               consumeScrollView;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout       consumeSrl;
    Unbinder unbinder;

    private OnlineLiveItemAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_live;
    }

    @Override
    protected void initView() {
        mRefreshLayout.setEnableLoadMore(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setHasFixedSize(true);
        autoScroll.setLayoutManager(linearLayoutManager);
        autoScroll.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                //                viewpagerMain.setCurrentItem(pos, false);




            }
        });

        testAdapter = new ConturyAdapter();
        mAdapter = new OnlineLiveItemAdapter();
        adapter = new Liveadapter(getActivity());
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(DensityUtil.dp2px(10), 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mAdapter);

        testAdapter.setItemClick(new ConturyAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, AreaDto data) {
                autoScroll.moveToPosition(poition);
            }
        });
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
    }
    private  ConturyAdapter testAdapter;
    @Override
    protected void initData() {

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
    private void iniGridView(final List<AreaDto> list) {

        int length = 82;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if(dm==null||getActivity()==null){
            return;
        }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 0, 0, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(10); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setData(list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




            }
        });
    }

    private void iniGridViewSecond(final List<AreaDto> list) {

        int length = 82;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if(dm==null||getActivity()==null){
            return;
        }
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 5) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 0, 0, 0);
        gridView1.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView1.setColumnWidth(itemWidth); // 设置列表项宽
        gridView1.setHorizontalSpacing(10); // 设置列表项水平间距
        gridView1.setStretchMode(GridView.NO_STRETCH);
        gridView1.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setData(list);
        gridView1.setAdapter(adapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




            }
        });
    }
    private Liveadapter adapter;
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
                if (ApiException.getInstance().getCode() == 500) {
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

                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();

            }
        }, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
