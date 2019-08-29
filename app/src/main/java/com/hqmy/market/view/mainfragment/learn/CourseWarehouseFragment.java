package com.hqmy.market.view.mainfragment.learn;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CategorieBean;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.CourseWarehouseDetailActivity;
import com.hqmy.market.view.activity.LearnRecordActivity;
import com.hqmy.market.view.activity.LiveSearchActivity;
import com.hqmy.market.view.adapter.LoopViewPagerAdapter;
import com.hqmy.market.view.widgets.RecycleViewDivider;
import com.hqmy.market.view.widgets.RecycleViewDividerHorizontal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 课件库
 */
public class CourseWarehouseFragment extends BaseFragment {

    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    //轮播图
    ViewPager home_vp_container;
    LinearLayout homeLlIndicators;

    private CourseWareAdapter mAdapter;
    private LoopViewPagerAdapter loopViewPagerAdapter;
    private List<BannerItemDto> adsList = new ArrayList<BannerItemDto>();
    private CourseWareHearviewAdapter wareHearviewAdapter;
    private String currentCates;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_courseware_warehouse;
    }

    @Override
    protected void initView() {
        initRecyclerView();
        initViewPager();
    }

    @Override
    protected void initData() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getSlidersList();
                categories(); //分类
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(DensityUtil.dp2px(10), DensityUtil.dp2px(15)));
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new CourseWareAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initHeadView();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CourseWarehouseDetailActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                intent.putExtra("name", currentCates);
                startActivity(intent);
            }
        });
    }


    private void initHeadView() {
        View mHeadView = View.inflate(getActivity(), R.layout.head_course_ware, null);
        home_vp_container = mHeadView.findViewById(R.id.home_vp_container);
        homeLlIndicators = mHeadView.findViewById(R.id.home_ll_indicators);
        //学习记录
        RelativeLayout rlLearnRecord = mHeadView.findViewById(R.id.rl_learn_record);
        rlLearnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LearnRecordActivity.class));
            }
        });
        //公告
        RecyclerView mHeadViewRecyclerView = mHeadView.findViewById(R.id.rv_head_course_ware);

        mHeadViewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mHeadViewRecyclerView.addItemDecoration(new RecycleViewDividerHorizontal(DensityUtil.dp2px(10)));
        wareHearviewAdapter = new CourseWareHearviewAdapter(getActivity());
        mHeadViewRecyclerView.setAdapter(wareHearviewAdapter);
        wareHearviewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentCates = wareHearviewAdapter.getItem(position).getTitle();
                wareHearviewAdapter.setSelectedPosition(position);
                getCourseProducts(wareHearviewAdapter.getItem(position).getId());
            }
        });
        TextView tvSearch =  mHeadView.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LiveSearchActivity.class);
                intent.putExtra("isCourseWare",true);
                startActivity(intent);
            }
        });
        mAdapter.addHeaderView(mHeadView);
    }

    private void initViewPager() {
        loopViewPagerAdapter = new LoopViewPagerAdapter(getActivity(), home_vp_container, homeLlIndicators);
        home_vp_container.setAdapter(loopViewPagerAdapter);
        loopViewPagerAdapter.setList(adsList);
        home_vp_container.addOnPageChangeListener(loopViewPagerAdapter);
    }

    /**
     * 课件广告轮播图
     */
    private void getSlidersList() {
        //showLoadDialog();
        DataManager.getInstance().getSlidersList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                adsList.clear();
                if (result != null && result.getData() != null && result.getData().getStudy_course_top() != null && result.getData().getStudy_course_top().size() > 0) {
                    adsList.addAll(result.getData().getStudy_course_top());
                    loopViewPagerAdapter.setList(adsList);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();

            }
        });
    }

    private void categories() {
        //showLoadDialog();
        DataManager.getInstance().categories(new DefaultSingleObserver<HttpResult<List<CategorieBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<CategorieBean>> result) {
                dissLoadDialog();
                if (result != null) {
                    wareHearviewAdapter.setNewData(result.getData());
                    if (result.getData() != null && result.getData().size() > 0) {
                        getCourseProducts(result.getData().get(0).getId());
                        currentCates = result.getData().get(0).getTitle();
                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();

            }
        });
    }

    private void getCourseProducts(String category_id) {
        //showLoadDialog();
        Map<String,String> map = new HashMap<>();
        map.put("include","course_info");
        map.put("filter[category_id]",category_id);
        DataManager.getInstance().getCourseProducts(new DefaultSingleObserver<HttpResult<List<VideoBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<VideoBean>> result) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();
                if (result != null) {
                    mAdapter.setNewData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                mRefreshLayout.finishRefresh();

            }
        }, map);
    }
}
