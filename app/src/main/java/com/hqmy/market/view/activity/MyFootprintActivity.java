package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BaseDto3;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.FootAdapter;
import com.hqmy.market.view.adapter.FootPushAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的足迹
 */
public class MyFootprintActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTitleRight;

    @BindView(R.id.iv_title_back)
    ImageView         ivTitleBack;
    @BindView(R.id.layout_top)
    RelativeLayout    layoutTop;
    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;


    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;
    private FootAdapter        mAdapter;
    private int                    mPage        = 1;
    private LinearLayoutManager    layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_foot_print;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的足迹");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText("清空");
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mAdapter = new FootAdapter(goodsLists, this);
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
        initListView();
    }

    @Override
    public void initData() {
        loadData(TYPE_PULL_REFRESH);
    }

    @Override
    public void initListener() {

    }

    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();

    private void initListView() {

    }



    private void loadData(int loadtype) {

        showLoadDialog();

        Map<String, String> map = new HashMap<>();
        map.put("filter[have_footprint_type]", "SMG\\Mall\\Models\\MallProduct");
        map.put("group_by_date", "1");
        map.put("include", "have_footprint.have_footprint");
        //        map.put("object_filter[type]", "lm,gc,st");
        map.put("page", currpage + "");
        //        map.put("per_page","100");
        DataManager.getInstance().userFootprints(new DefaultSingleObserver<HttpResult<List<FootInfoDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<FootInfoDto>> result) {
                dissLoadDialog();
                onDataLoaded(loadtype,result.getData().size()==Constants.PAGE_SIZE,result.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }
    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE = 889;
    private int currpage = 1;//第几页
    private List<FootInfoDto> dats = new ArrayList<>();
    public void onDataLoaded(int loadType, final boolean haveNext, List<FootInfoDto> lists) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            list.clear();
            for (FootInfoDto infoDto : lists) {
                for (BaseDto3 itemDto : infoDto.have_footprint.data) {
                    list.add(itemDto.have_footprint.data);
                }
            }
        } else {
            for (FootInfoDto infoDto : lists) {
                for (BaseDto3 itemDto : infoDto.have_footprint.data) {
                    list.add(itemDto.have_footprint.data);
                }
            }
        }

        mAdapter.setNewData(list);

        mAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (list.size()%15==0) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (list.size()%15==0)
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



    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    private void clearFootPrint() {
        Map<String, Object> map = new HashMap<>();
        map.put("filter[have_footprint_type]", "SMG\\Mall\\Models\\MallProduct");
        DataManager.getInstance().clearFootPrint(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                ToastUtil.showToast("清空成功");

                mAdapter.setNewData(null);
                mAdapter.setEmptyView(new EmptyView(MyFootprintActivity.this));
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("清空成功");
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(new EmptyView(MyFootprintActivity.this));
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back
            , R.id.tv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                ConfirmDialog dialog = new ConfirmDialog(this);
                dialog.setTitle("温馨提示");
                dialog.setMessage("是否清空我的足迹");
                dialog.setCancelable(false);
                dialog.setYesOnclickListener("是", new BaseDialog.OnYesClickListener() {

                    @Override
                    public void onYesClick() {
                        dialog.dismiss();
                        clearFootPrint();
                    }
                });
                dialog.setCancleClickListener("否", new BaseDialog.OnCloseClickListener() {

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();

                break;
        }
    }


}
