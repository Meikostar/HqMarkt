package com.hqmy.market.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.BaseDto3;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.FootAdapter;
import com.hqmy.market.view.adapter.FootPushAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MyFootprintAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的足迹
 */
public class MyFootprintActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTitleRight;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private FootAdapter mAdapter;
    private int         mPage = 1;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_foot_print;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的足迹");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText("清空");
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
    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();

    private void initListView() {
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.addItemDecoration(new RecyclerItemDecoration(5, 2));
        recyclerView.setLayoutManager(gridLayoutManager2);
        mAdapter = new FootAdapter(goodsLists,this);
        recyclerView.setAdapter(mAdapter);
    }


    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("filter[have_footprint_type]", "SMG\\Mall\\Models\\MallProduct");
        map.put("group_by_date", "1");
        map.put("include", "have_footprint.have_footprint");
//        map.put("object_filter[type]", "lm,gc,st");
        map.put("page", mPage + "");
//        map.put("per_page","100");
        DataManager.getInstance().userFootprints(new DefaultSingleObserver<HttpResult<List<FootInfoDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<FootInfoDto>> httpResult) {
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
    private List<NewListItemDto> list=new ArrayList<>();
    private void setData(HttpResult<List<FootInfoDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null||httpResult.getData().size()==0||httpResult.getData().get(0).have_footprint==null||httpResult.getData().get(0).have_footprint.data==null) {
            mAdapter.addData(list);
            if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
                if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
            }

            return;
        }
//        list.clear();
        for(FootInfoDto infoDto:httpResult.getData()){
            for(BaseDto3 itemDto:infoDto.have_footprint.data){
                list.add(itemDto.have_footprint.data);
            }
        }

        if (mPage <= 1) {
            mAdapter.setNewData(list);
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(list);
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    private void clearFootPrint() {
        Map<String, Object> map = new HashMap<>();
        map.put("filter[have_footprint_type]","SMG\\Mall\\Models\\MallProduct");
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
                if (ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("清空成功");
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(new EmptyView(MyFootprintActivity.this));
                }else {
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
                dialog.setYesOnclickListener("是",new BaseDialog.OnYesClickListener(){

                    @Override
                    public void onYesClick() {
                        dialog.dismiss();
                        clearFootPrint();
                    }
                });
                dialog.setCancleClickListener("否",new BaseDialog.OnCloseClickListener(){

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();

                break;
        }
    }
}
