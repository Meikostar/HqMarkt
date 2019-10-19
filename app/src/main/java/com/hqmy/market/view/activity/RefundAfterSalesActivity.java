package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.mainfragment.consume.ShopCartActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.RefundAfterSalesAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 退款/售后
 */
public class RefundAfterSalesActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private RefundAfterSalesAdapter mAdapter;
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_after_sales;
    }

    @Override
    public void initView() {
        mTitleText.setText("退款/售后");
        initRecyclerView();
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
                loadData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                loadData();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new CustomDividerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_order));
        mAdapter = new RefundAfterSalesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_itme_refund_after_sales_text:
                        if(mAdapter.getItem(position).getStatus_msg().equals("再来一单")){
                            if (mAdapter.getItem(position).getOrder().getData().getItems() !=null && mAdapter.getItem(position).getOrder().getData().getItems().getData() != null && mAdapter.getItem(position).getOrder().getData().getItems().getData().size() > 0){
                                ArrayList<String> product_id = new ArrayList<>();
                                ArrayList<String> qty = new ArrayList<>();
                                ArrayList<String> stock_id = new ArrayList<>();
                                for (MyOrderItemDto myOrderItemDto: mAdapter.getItem(position).getOrder().getData().getItems().getData()){
                                    product_id.add(myOrderItemDto.getProduct_id());
                                    qty.add(myOrderItemDto.getQty());
                                    stock_id.add(myOrderItemDto.getSku_id());
                                }
                                showLoadDialog();

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("qty",qty);
                                if(product_id!= null && product_id.size()>0){
                                    for(int i =0;i< product_id.size();i++){
                                        if(!TextUtils.isEmpty(product_id.get(i))){
                                            map.put("product_id["+i+"]",product_id.get(i)) ;
                                        }
                                    }
                                }
                                if(stock_id!= null && stock_id.size()>0){
                                    for(int i =0;i< stock_id.size();i++){
                                        if(!TextUtils.isEmpty(stock_id.get(i))){
                                            map.put("stock_id["+i+"]",stock_id.get(i)) ;
                                        }
                                    }
                                }
                                DataManager.getInstance().addShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
                                    @Override
                                    public void onSuccess(HttpResult<Object> result) {
                                        dissLoadDialog();
                                        finishAll();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(MainActivity.PAGE_INDEX, 3);
                                        gotoActivity(MainActivity.class, true, bundle);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        dissLoadDialog();

                                    }
                                }, "default", map);



                            }
                            break;
                        }else   if(mAdapter.getItem(position).getStatus_msg().equals("审核中")){
                            cancelOrder(mAdapter.getItem(position).getId());
                            break;
                        }else   if(mAdapter.getItem(position).getStatus_msg().equals("待审核")){
                            cancelOrder(mAdapter.getItem(position).getId());
                            break;
                        }else   if(mAdapter.getItem(position).getStatus_msg().equals("取消申请")){
                            if (mAdapter.getItem(position).getOrder().getData().getItems() !=null && mAdapter.getItem(position).getOrder().getData().getItems().getData() != null && mAdapter.getItem(position).getOrder().getData().getItems().getData().size() > 0){
                                ArrayList<String> product_id = new ArrayList<>();
                                ArrayList<String> qty = new ArrayList<>();
                                ArrayList<String> stock_id = new ArrayList<>();
                                for (MyOrderItemDto myOrderItemDto: mAdapter.getItem(position).getOrder().getData().getItems().getData()){
                                    product_id.add(myOrderItemDto.getProduct_id());
                                    qty.add(myOrderItemDto.getQty());
                                    stock_id.add(myOrderItemDto.getSku_id());
                                }
                                showLoadDialog();

                                HashMap<String,Object> map = new HashMap<>();
                                map.put("qty",qty);
                                if(product_id!= null && product_id.size()>0){
                                    for(int i =0;i< product_id.size();i++){
                                        if(!TextUtils.isEmpty(product_id.get(i))){
                                            map.put("product_id["+i+"]",product_id.get(i)) ;
                                        }
                                    }
                                }
                                if(stock_id!= null && stock_id.size()>0){
                                    for(int i =0;i< stock_id.size();i++){
                                        if(!TextUtils.isEmpty(stock_id.get(i))){
                                            map.put("stock_id["+i+"]",stock_id.get(i)) ;
                                        }
                                    }
                                }
                                DataManager.getInstance().addShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
                                    @Override
                                    public void onSuccess(HttpResult<Object> result) {
                                        dissLoadDialog();
                                        finishAll();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(MainActivity.PAGE_INDEX, 3);
                                        gotoActivity(MainActivity.class, true, bundle);
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        dissLoadDialog();

                                    }
                                }, "default", map);



                            }
                            break;
                        }

                }
            }
        });
    }
    /**
     * 取消的订单
     *
     * @param id
     */
    private void cancelOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().getOrdercancel(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("取消成功");
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("取消成功");
                    mRefreshLayout.autoRefresh();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }
    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("include", "order.items.product,order.shop");
        map.put("page", mPage + "");
        DataManager.getInstance().getAllUserRefundList(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<MyOrderDto>> httpResult) {
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

    private void setData(HttpResult<List<MyOrderDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mAdapter.setEmptyView(new EmptyView(this));
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
