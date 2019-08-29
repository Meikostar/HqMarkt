package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.HeadLineDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.HeadLineAdapter;
import com.hqmy.market.view.adapter.LoopViewPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
/**
 * 爱心头条
 */
public class HeadLineActivity extends BaseActivity {
    public static final String HEAD_LINE_LIST = "head_line_list";
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.head_line_listview)
    ListView  head_line_listview;
    private HeadLineAdapter mAdapter;
    private ArrayList<HeadLineDto> lineLists = new ArrayList<HeadLineDto>();
    //轮播图
    private ViewPager home_vp_container;
    private LinearLayout homeLlIndicators;
    private LoopViewPagerAdapter loopViewPagerAdapter;
    private List<BannerItemDto> adsList = new ArrayList<BannerItemDto>();
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_head_line;
    }

    @Override
    public void initView() {
        mTitleText.setText("爱心头条");
        initHeadView();
        initViewPager();
        mAdapter = new HeadLineAdapter(HeadLineActivity.this,lineLists);
        head_line_listview.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        mRefreshLayout.autoRefresh();
        getBanner();
        getHeadLines();
    }

    @Override
    public void initListener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getHeadLines();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getHeadLines();
            }
        });

        head_line_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(HeadLineDetailActivity.ARTICLE_ID, lineLists.get(position-1).getId());
                gotoActivity(HeadLineDetailActivity.class, false, bundle);
            }
        });
    }

    private void initHeadView() {
        View mHeadView = View.inflate(HeadLineActivity.this, R.layout.head_head_line, null);
        home_vp_container = mHeadView.findViewById(R.id.home_vp_container);
        homeLlIndicators = mHeadView.findViewById(R.id.home_ll_indicators);
        head_line_listview.addHeaderView(mHeadView);
    }

    private void initViewPager() {
        loopViewPagerAdapter = new LoopViewPagerAdapter(HeadLineActivity.this, home_vp_container, homeLlIndicators);
        home_vp_container.setAdapter(loopViewPagerAdapter);
        loopViewPagerAdapter.setList(adsList);
        home_vp_container.addOnPageChangeListener(loopViewPagerAdapter);
    }

    /**
     * 头条广告轮播图
     */
    private void getBanner(){
        showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        adsList.addAll(result.getData().getLove_headlines_top());
                        loopViewPagerAdapter.setList(adsList);
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        },"love_headlines_top");
    }

    private void getHeadLines(){
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        DataManager.getInstance().getHeadLines(new DefaultSingleObserver<HttpResult<List<HeadLineDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<HeadLineDto>> result) {
                dissLoadDialog();
                setData(result);
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void setData(HttpResult<List<HeadLineDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            lineLists.clear();
            lineLists.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            lineLists.addAll(httpResult.getData());
            mAdapter.notifyDataSetChanged();
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @OnClick({R.id.iv_title_back
            ,R.id.tv_title_right
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
