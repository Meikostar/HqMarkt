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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.activity.LiveCheckFailActivity;
import com.hqmy.market.view.activity.LiveCheckingActivity;
import com.hqmy.market.view.activity.LiveSearchActivity;
import com.hqmy.market.view.activity.LiveVideoViewActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.MoreHotLiveActivity;
import com.hqmy.market.view.activity.RequestLivePermissionActivity;
import com.hqmy.market.view.activity.StartLiveActivity;
import com.hqmy.market.view.adapter.HotLiveAdapter;
import com.hqmy.market.view.adapter.LiveCategrayAdapter;
import com.hqmy.market.view.adapter.Liveadapter;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import java.util.ArrayList;
import java.util.HashMap;
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
    SmartRefreshLayout mRefreshLayout;

    //    @BindView(R.id.tv_upmarquee_view)
    //    UPMarqueeView            mUPMarqueeView;
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

    Unbinder unbinder;
    @BindView(R.id.rl_bgs)
    RelativeLayout rlBgs;
    @BindView(R.id.ll_bs)
    LinearLayout   llBgs;
    @BindView(R.id.ll_bbg)
    LinearLayout   ll_bbg;

    private OnlineLiveItemAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_online_live;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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
//                                viewpagerMain.setCurrentItem(pos, false);


            }
        });

        testAdapter = new LiveCategrayAdapter();
        mAdapter = new OnlineLiveItemAdapter();
        adapter = new Liveadapter(getActivity());
        hotAdapter = new HotLiveAdapter(getActivity());
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(DensityUtil.dp2px(10), 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mAdapter);

        testAdapter.setItemClick(new LiveCategrayAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, LiveCatesBean data) {
                autoScroll.moveToPosition(poition);
                id = data.getId();
                liveVideos(data.getId());
            }
        });
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
    }

    private LiveCategrayAdapter testAdapter;

    @Override
    protected void initData() {
     if(state==1){
         autoScroll.setVisibility(View.GONE);
         rlBgs.setVisibility(View.GONE);
         llBgs.setVisibility(View.GONE);
     }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private int state;

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(state==0){

                mRefreshLayout.autoRefresh();
                getLiveTop();
                getHotLive();
            }

            getLiveCates();

        }
    }

    @Override
    protected void initListener() {
        ll_bbg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),MoreHotLiveActivity.class));
            }
        });
        tvText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "week";
                tvText1.setTextColor(getResources().getColor(R.color.my_color_333333));
                line.setVisibility(View.VISIBLE);
                line1.setVisibility(View.INVISIBLE);
                tvText2.setTextColor(getResources().getColor(R.color.my_color_666666));
                getLiveTop();
            }
        });
        tvText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "month";
                tvText1.setTextColor(getResources().getColor(R.color.my_color_666666));
                line.setVisibility(View.INVISIBLE);
                line1.setVisibility(View.VISIBLE);
                tvText2.setTextColor(getResources().getColor(R.color.my_color_333333));
                getLiveTop();
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (TextUtil.isNotEmpty(id)) {
                    liveVideos(id);
                }
            }
        });
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

    private void iniGridView(final List<UserInfoDto> list) {

        int length = 82;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if (dm == null || getActivity() == null) {
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

    private String id;

    private void iniGridViewSecond(final List<VideoLiveBean> list) {

        int length = 120;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if (dm == null || getActivity() == null) {
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
        hotAdapter.setData(list);
        gridView1.setAdapter(hotAdapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startLiveVideoActivity(list.get(position));


            }
        });
    }

    private void startLiveVideoActivity(VideoLiveBean videoLiveBean) {
        Intent intent = new Intent(getActivity(), LiveVideoViewActivity.class);
        intent.putExtra("videoPath", videoLiveBean.getRtmp_play_url());
        if (videoLiveBean.getRoom() != null && videoLiveBean.getRoom().getData() != null) {
            intent.putExtra("roomId", videoLiveBean.getRoom().getData().getId());
        }
        if (videoLiveBean.apply != null && videoLiveBean.apply.getData() != null) {
            intent.putExtra("userId", videoLiveBean.apply.getData().getUser_id());
        }

        intent.putExtra("videoId", videoLiveBean.getId());
        intent.putExtra("liveStreaming", 1);
        startActivity(intent);
    }

    private Liveadapter    adapter;
    private HotLiveAdapter hotAdapter;

    private void isPlayer() {
        //showLoadDialog();
        DataManager.getInstance().isliveing(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto result) {
                //dissLoadDialog();


                if (result != null) {

                    if (result.is_realname) {
                        if (result.apply_check_status == 0) {
                            applyLives();

                            //                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (result.apply_check_status == 1) {
                            gotoActivity(LiveCheckingActivity.class);

                        } else if (result.apply_check_status == 2) {
                            if (result.is_live) {
                                gotoActivity(StartLiveActivity.class);
                            } else {

                            }

                        } else if (result.apply_check_status == 3) {
                            Bundle bundle = new Bundle();
                            bundle.putString("reasonTip", "");
                            gotoActivity(LiveCheckFailActivity.class, false, bundle);


                        }

                    } else {
                        if (BaseApplication.real_state.equals("-1")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        } else if (BaseApplication.real_state.equals("0")) {
                            ToastUtil.showToast("实名认证中，请耐心等待审核!");
                        } else if (BaseApplication.real_state.equals("1")) {

                        } else if (BaseApplication.real_state.equals("2")) {
                            gotoActivity(RequestLivePermissionActivity.class);
                        }

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

    public void applyLives() {
        //        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class);

                //                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    gotoActivity(LiveCheckingActivity.class);
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                //                dissLoadDialog();
            }
        });
    }

    private void getHotLive() {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[is_hot]", "1");
        map.put("include", "apply,room");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();

                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    iniGridViewSecond(result.getData());
                } else {
                    mAdapter.setNewData(null);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();


            }
        }, map);
    }

    private void liveVideos(String id) {

        //        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        if(state==0){
            map.put("filter[live_video_cate_id]", id);
        }else {
            map.put("filter[is_hot]", "1");
        }

        map.put("include", "apply,room");
        DataManager.getInstance().liveVideos(new DefaultSingleObserver<HttpResult<List<VideoLiveBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoLiveBean>> result) {
                //                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    mAdapter.setNewData(result.getData());
                } else {
                    mAdapter.setNewData(null);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();

            }
        }, map);
    }

    private List<LiveCatesBean> datas;
    private String              type = "week";

    public void getLiveTop() {
        //        showLoadDialog();
        DataManager.getInstance().getLiveTop(new DefaultSingleObserver<HttpResult<List<UserInfoDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<UserInfoDto>> result) {
                //                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    iniGridView(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, type);
    }

    private List<LiveCatesBean> data = new ArrayList<>();

    public void getLiveCates() {
        data.clear();
        //        showLoadDialog();
        DataManager.getInstance().getLiveCates(new DefaultSingleObserver<HttpResult<List<LiveCatesBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<LiveCatesBean>> result) {
                //                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    datas = result.getData();

                        liveVideos(result.getData().get(0).getId());
                        if (TextUtil.isEmpty(id)) {
                            id = result.getData().get(0).getId();
                        }

                        data.addAll(result.getData());
                        LiveCatesBean liveCatesBean = new LiveCatesBean();
                        liveCatesBean.cat_name = "";
                        liveCatesBean.setId("-1");
                        data.add(liveCatesBean);
                        testAdapter.setDatas(data);
                        testAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
