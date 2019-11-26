package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.BaseDto3;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.adapter.AreaImageAdapter;
import com.hqmy.market.view.adapter.Areadapter;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.ConturyAdapter;
import com.hqmy.market.view.adapter.FootAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.ActionbarView;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ConturyDetialActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView     customActionBar;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;

    @Override
    public void initListener() {

    }

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.ui_contury_detail_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private ConturyAdapter         testAdapter;
    private boolean                isShow;
    private BaseDto                spikeDto;
    private AreaImageAdapter       mHomedapter;
    private ConsumePushAdapter     mAdapter;
    private List<NewListItemDto>   goodsLists = new ArrayList<NewListItemDto>();
    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;

    private int                    mPage        = 1;
    private LinearLayoutManager    layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE = 889;
    private int currpage = 1;//第几页
    @Override
    public void initView() {
        id=getIntent().getStringExtra("id");
        title=getIntent().getStringExtra("title");
        actionbar.setImgStatusBar(R.color.my_color_white);
        if(TextUtil.isNotEmpty(title)){
            actionbar.setTitle(title);
        }

        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        getCategorisContury();
        loadData(TYPE_PULL_REFRESH);
        mHomedapter = new AreaImageAdapter(this);



        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mAdapter = new ConsumePushAdapter(goodsLists, this);
        initHeadView();
        mSuperRecyclerView.addItemDecoration(new RecyclerItemDecoration(6, 2));
        mSuperRecyclerView.setLayoutManager(gridLayoutManager2);
        //        layoutManager = new LinearLayoutManager(this);
        //        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                loadData(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);

    }

    private NoScrollGridView   gridContent;
    private View view;
    public void initHeadView(){
        view = LayoutInflater.from(this).inflate(R.layout.contury_head_view, null);
        gridContent= view.findViewById(R.id.grid_content);
        gridContent.setAdapter(mHomedapter);
        mAdapter.addHeaderView(view);
    }
    private void loadData(int loadtype) {

        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("sort", "-sales_count");
        map.put("include", "category");
        map.put("filter[scopeBrandCategory]", id + "");
        DataManager.getInstance().findGoodsList(map,new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();

                onDataLoaded(loadtype,result.getData().size()==Constants.PAGE_SIZE,result.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });

    }

    public void onDataLoaded(int loadType, boolean haveNext, List<NewListItemDto> lists) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            list.addAll(lists);
        } else {
            list.addAll(lists);
        }

        mAdapter.setNewData(list);

        mAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        loadData(TYPE_PULL_MORE);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }

    private List<NewListItemDto> list = new ArrayList<>();


    private void getCategorisContury() {
        //showLoadDialog();
        DataManager.getInstance().getBrandsByCate(new DefaultSingleObserver<HttpResult<List<AreaDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AreaDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        mHomedapter.setData(result.getData());



                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        },id);
    }
    private String id;
    private String title;

//    private void getConturyProducts() {
//        //showLoadDialog();
//        Map<String, String> map = new HashMap<>();
//        map.put("sort", "-sales_count");
//        map.put("include", "category");
//        map.put("filter[scopeBrandCategory]", id + "");
//        DataManager.getInstance().findGoodsList(map,new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
//            @Override
//            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
//                dissLoadDialog();
//                setData(result);
//                refreshLayout.finishLoadMore();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                dissLoadDialog();
//            }
//        });
//    }

    @Override
    public void initData() {

    }


}
