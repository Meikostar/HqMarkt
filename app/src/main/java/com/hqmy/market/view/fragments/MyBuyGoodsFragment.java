package com.hqmy.market.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.MyOrderLogisticsActivity;
import com.hqmy.market.view.activity.MyOrderReturnActivity;
import com.hqmy.market.view.activity.RechargeWebActivity;
import com.hqmy.market.view.activity.ToShipActivity;
import com.hqmy.market.view.adapter.MyBuyGoodAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 我买到的，我卖出的
 */
@SuppressLint("ValidFragment")
public class MyBuyGoodsFragment extends BaseFragment {
    private int PAY_SUCCESS = 120;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    private MyBuyGoodAdapter mAdapter;
    private String type = "";
    private int mPage = 1;
    private int mType; //0,1

    public MyBuyGoodsFragment(String type, int mType) {
        this.type = type;
        this.mType = mType;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_buy_good;
    }

    @Override
    protected void initView() {
        initRecyclerView();

    }

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
                mPage = 1;
                if (mType == 0) {
                    getBuyData();
                } else {
                    getSellerData();
                }
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                if (mType == 0) {
                    getBuyData();
                } else {
                    getSellerData();
                }
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MyBuyGoodAdapter(getActivity(),mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    switch (textView.getText().toString()) {
                        case "去付款":
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + mAdapter.getItem(position).getNo());
                            bundle.putString(Constants.INTENT_WEB_TITLE, "支付");
                            gotoActivity(RechargeWebActivity.class, false, bundle,PAY_SUCCESS);
                            break;
                        case "取消订单":
                            cancelOrder(mAdapter.getItem(position).getId());
                            break;
                        case "催发货":
                            hurryOrder(mAdapter.getItem(position).getId());
                            break;
                        case "确认收货":
                            confirmOrder(mAdapter.getItem(position).getId());
                            break;
                        case "申请退款":
                            Bundle bundle2 = new Bundle();
                            bundle2.putString(Constants.INTENT_ID,mAdapter.getItem(position).getId());
                            bundle2.putString(Constants.TYPE,mAdapter.getItem(position).getType());
                            if (mAdapter.getItem(position).getItems() != null && mAdapter.getItem(position).getItems().getData() != null && mAdapter.getItem(position).getItems().getData().size() >0){
                                MyOrderItemDto myOrderItemDto = mAdapter.getItem(position).getItems().getData().get(0);
                                if (myOrderItemDto.getProduct().getData().getCover() != null){
                                    bundle2.putString("cover",myOrderItemDto.getProduct().getData().getCover());
                                }
                                if ("ax".equals(mAdapter.getItem(position).getType())){
                                    bundle2.putString("score",myOrderItemDto.getScore());
                                    bundle2.putBoolean("isFlag2",true);
                                }else {
                                    bundle2.putString("price",myOrderItemDto.getPrice());
                                    bundle2.putBoolean("isFlag2",false);
                                }
                                bundle2.putString("title",myOrderItemDto.getTitle());
                                bundle2.putString("num",myOrderItemDto.getQty());
                            }
                            gotoActivity(MyOrderReturnActivity.class, false, bundle2,2);
                            break;
                        case "查看物流":
                            Bundle bundle3 = new Bundle();
                            bundle3.putString("id",mAdapter.getItem(position).getId());
                            gotoActivity(MyOrderLogisticsActivity.class,false,bundle3);
                            break;
                        case "去发货":
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("id",mAdapter.getItem(position).getId());
                            gotoActivity(ToShipActivity.class,false,bundle1);
                            break;
                    }
                }
            }
        });
    }

    /**
     * 我买到的
     */
    private void getBuyData() {
        Map<String, String> map = new HashMap<>();
        switch (type) {
            case Constants.TYPE_ALL:
                map.put("filter[type]", "ax");
                map.put("include", "shop,refundInfo,items.product");
                break;
            case Constants.TYPE_PAYMENT://待付款
                map.put(" filter[status]", "created");
                map.put("include", "shop,items.product");
                break;
            case Constants.TYPE_DELIVERY://待发货
                map.put(" filter[status]", "paid");
                map.put("include", "shop,items.product");
                break;
            case Constants.TYPE_BUY_RECEIVE://待收货
                map.put(" filter[status]", "shipping");
                map.put("include", "shop,items.product");
                break;
        }
        map.put("filter[type]", "ax");
        map.put("page", mPage + "");

        DataManager.getInstance().getAllUserOrders(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
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

    /**
     * 我卖出的
     */
    private void getSellerData() {
        Map<String, String> map = new HashMap<>();
        switch (type) {
            case Constants.TYPE_ALL:
                map.put("include", "refundInfo,items.product");
                break;
            case Constants.TREE_ORDER_TYPE_PAYMENT://待付款
                map.put("filter[status]", "created");
                map.put("include", "refundInfo,items.product");
                break;
            case Constants.TREE_ORDER_TYPE_DELIVERY://待发货
                map.put("filter[status]", "paid");
                map.put("include", "refundInfo,items.product");
                break;
        }
        map.put("page", mPage + "");
        DataManager.getInstance().getSellerOrders(new DefaultSingleObserver<HttpResult<List<MyOrderDto>>>() {
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
                mAdapter.setEmptyView(new EmptyView(getActivity()));
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
    /**
     * 取消的订单
     *
     * @param id
     */
    private void cancelOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().cancelOrder(new DefaultSingleObserver<HttpResult<Object>>() {
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

    /**
     * 催发货
     *
     * @param id
     */
    private void hurryOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().hurryOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("催促中");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("催促中");
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        }, id);
    }

    /**
     * 确认收货
     *
     * @param id
     */
    private void confirmOrder(String id) {
        showLoadDialog();
        DataManager.getInstance().confirmOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                mRefreshLayout.autoRefresh();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            mRefreshLayout.autoRefresh();
        }else if(requestCode == PAY_SUCCESS){
            mRefreshLayout.autoRefresh();
        }
    }
}
