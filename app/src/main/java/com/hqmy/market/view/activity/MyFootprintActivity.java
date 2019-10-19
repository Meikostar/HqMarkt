package com.hqmy.market.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.BaseDto3;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.FootAdapter;
import com.hqmy.market.view.adapter.FootPushAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;
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
    TextView     mTitleText;
    @BindView(R.id.tv_title_right)
    TextView     mTitleRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.line)
    View line;

    @BindView(R.id.refresh)
    SuperSwipeRefreshLayout mRefreshLayout;

    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private int                    mCurrentPage = Constants.PAGE_NUM;
    private FootAdapter            mAdapter;
    private int                    mPage = 1;
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
        loadData(true);
    }

    @Override
    public void initListener() {
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(mRefreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mPage = Constants.PAGE_NUM;
                loadData(false);
            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadData(false);
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
        mAdapter = new FootAdapter(goodsLists,this);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(5, 2));
        recyclerView.setLayoutManager(gridLayoutManager2);

        recyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
            mRefreshLayout.setLoadMore(false);
        }
    }
    private void loadData(boolean isLoad) {
        if (isLoad) {
            showLoadDialog();
        }
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

            }
        }, map);
    }
    private List<NewListItemDto> list=new ArrayList<>();
    private void setData(HttpResult<List<FootInfoDto>> httpResult) {

//        list.clear();



        list.clear();
        dissLoadDialog();
        if (null != httpResult.getData() && httpResult.getData().size() > 0) {
            for(FootInfoDto infoDto:httpResult.getData()){
                for(BaseDto3 itemDto:infoDto.have_footprint.data){
                    list.add(itemDto.have_footprint.data);
                }
            }
            if (mCurrentPage == 1) {

                mAdapter.setNewData(list);
                mRefreshLayout.setRefreshing(false);
            } else {

                mAdapter.addData(list);
                mRefreshLayout.setLoadMore(false);
            }

//            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) consume_scrollView.getLayoutParams();
//           int cout= list.size()/2;
//           int size= list.size()%2;
//           float height;
//           if(list.size()>6){
//               if(size==0){
//                 height=dpToPx(255)*cout;
//               }else {
//                   height=dpToPx(255)*(cout+1);
//               }
//               //获取当前控件的布局对象
//               params.height= (int) height;//设置当前控件布局的高度
//               consume_scrollView.setLayoutParams(params);
//           }

            line.setVisibility(View.GONE);

        } else {
            EmptyView emptyView = new EmptyView(MyFootprintActivity.this);

            emptyView.setTvEmptyTip("暂无数据");

            mAdapter.setEmptyView(emptyView);


        }
        mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, httpResult.getMeta().getPagination().getTotal());

    }
    private float dpToPx(float dp)
    {
        return dp * getResources().getDisplayMetrics().density;
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
