package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CouponCodeInfo;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.OrderDetailGoodsListAdapter;
import com.hqmy.market.view.widgets.dialog.CouponCodeDialog;

import java.util.List;

import butterknife.BindView;

/*订单详情页*/
public class MyOrderDetailActivity extends BaseActivity {
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_order_tip)
    TextView tvOrderTip;
    @BindView(R.id.img_order_status)
    ImageView imgOrderStatus;
    @BindView(R.id.tv_myorder_consignee)
    TextView tvMyorderConsignee;
    @BindView(R.id.tv_myorder_phone)
    TextView tvMyorderPhone;
    @BindView(R.id.img_item_address)
    ImageView imgItemAddress;
    @BindView(R.id.tv_myorder_address)
    TextView tvMyorderAddress;
    @BindView(R.id.recy_my_order)
    RecyclerView recyMyOrder;
    @BindView(R.id.tv_detail_price)
    TextView tvDetailPrice;
    @BindView(R.id.tv_detail_express_money)
    TextView tvDetailExpressMoney;
    @BindView(R.id.tv_detail_pay_money)
    TextView tvDetailPayMoney;
    @BindView(R.id.ll_detail_money)
    LinearLayout llDetailMoney;
    @BindView(R.id.tv_detail_number)
    TextView tvDetailNumber;
    @BindView(R.id.tv_detail_creat_time)
    TextView tvDetailCreatTime;
    @BindView(R.id.tv_detail_shipped_time)
    TextView tvDetailShippedTime;
    @BindView(R.id.btn_detail_operation_one)
    TextView btnDetailOperationOne;
    @BindView(R.id.btn_detail_operation_two)
    TextView btnDetailOperationTwo;
    @BindView(R.id.ll_detail_bottom)
    LinearLayout llDetailBottom;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.rl_adress)
    RelativeLayout rl_adress;

    String id = "";
    String type = "";
    MyOrderDto mmyOrderDto;
    private static int UPLOAD_DATA = 100;
    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });
        bindClickEvent(btnDetailOperationOne, () -> {
            switch (btnDetailOperationOne.getText().toString()){
                case "取消订单":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    cancelOrder(mmyOrderDto.getId());
                    break;
                case "申请退款":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(Constants.INTENT_ID,mmyOrderDto.getId());
                    bundle2.putString(Constants.TYPE,mmyOrderDto.getType());
                    if (mmyOrderDto.getItems() != null && mmyOrderDto.getItems().getData() != null && mmyOrderDto.getItems().getData().size() >0){
                        MyOrderItemDto myOrderItemDto = mmyOrderDto.getItems().getData().get(0);
                        if (myOrderItemDto.getProduct().getData().getCover() != null){
                            bundle2.putString("cover",myOrderItemDto.getProduct().getData().getCover());
                        }
                        if ("ax".equals(mmyOrderDto.getType())){
                            bundle2.putString("score",myOrderItemDto.getScore());
                            bundle2.putBoolean("isFlag2",true);
                        }else {
                            bundle2.putString("price",myOrderItemDto.getPrice());
                            bundle2.putBoolean("isFlag2",false);
                        }
                        bundle2.putString("title",myOrderItemDto.getTitle());
                        bundle2.putString("num",myOrderItemDto.getQty());
                    }
                    gotoActivity(MyOrderReturnActivity.class, false, bundle2,UPLOAD_DATA);
                    break;
                case "查看物流":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("id",mmyOrderDto.getId());
                    gotoActivity(MyOrderLogisticsActivity.class,false,bundle3);
                    break;
            }
        });
        bindClickEvent(btnDetailOperationTwo, () -> {
            switch (btnDetailOperationTwo.getText().toString()){
                case "去付款":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + mmyOrderDto.getNo());
                    bundle.putString(Constants.INTENT_WEB_TITLE, "支付");
                    gotoActivity(RechargeWebActivity.class, false, bundle,UPLOAD_DATA);
                    break;
                case "催发货":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    hurryOrder(mmyOrderDto.getId());
                    break;
                case "确认收货":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    confirmOrder(mmyOrderDto.getId());
                    break;
                case "去评价":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(Constants.INTENT_ID,mmyOrderDto.getId());
                    gotoActivity(MyOrderEvaluateListActivity.class, false, bundle1);
                    break;
                case "查看兑换券":

                    if (mmyOrderDto.getItems() != null && mmyOrderDto.getItems().getData().size() > 0) {
                        CouponCodeInfo codeInfo = mmyOrderDto.getItems().getData().get(0).getVcode().getData();
                        CouponCodeDialog dialog = new CouponCodeDialog(this, codeInfo.getCode());
                        dialog.show();
                    }

                    break;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPLOAD_DATA ){
           getOrderDetail();
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.ui_my_order_detail_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("订单详情");
        Intent intent = getIntent();
        id = intent.getStringExtra(Constants.INTENT_ID);
        type = intent.getStringExtra(Constants.INTENT_TYPE);

    }

    @Override
    public void initData() {
        getOrderDetail();
    }

    private void getOrderDetail() {
        DataManager.getInstance().getOrderDetail(new DefaultSingleObserver<HttpResult<MyOrderDto>>() {
            @Override
            public void onSuccess(HttpResult<MyOrderDto> result) {
                dealView(result.getData());
            }
        }, id, type, "shop,address,items.product,items.vcode");
    }

    private void dealView(MyOrderDto myOrderDto) {
        if (myOrderDto != null) {
            mmyOrderDto = myOrderDto;
            tvOrderStatus.setText(myOrderDto.getStatus_msg());
            if (myOrderDto.getAddress() != null && myOrderDto.getAddress().getData() != null) {
                tvMyorderConsignee.setText("收货人：" + myOrderDto.getAddress().getData().getName());
                tvMyorderPhone.setText(myOrderDto.getAddress().getData().getMobile());
                tvMyorderAddress.setText("收货地址：" + myOrderDto.getAddress().getData().getArea() + myOrderDto.getAddress().getData().getDetail());
            } else {
                rl_adress.setVisibility(View.GONE);
            }
            if (myOrderDto.getItems() != null && myOrderDto.getItems().getData() != null && myOrderDto.getItems().getData().size() > 0) {
                recyMyOrder.setVisibility(View.VISIBLE);
                setGoodsListData(recyMyOrder, myOrderDto.getItems().getData());
            } else {
                recyMyOrder.setVisibility(View.GONE);
            }
            tvDetailPrice.setText("￥" + myOrderDto.getProduct_total());
            tvDetailExpressMoney.setText("￥" + myOrderDto.getShipping_price());
            tvTotalNum.setText("共" + myOrderDto.getCount() + "件商品,总计：");
            tvDetailPayMoney.setText("￥" + myOrderDto.getPay_total());
            tvDetailNumber.setText("订单编号：" + myOrderDto.getNo());
            tvDetailCreatTime.setText("创建时间：" + myOrderDto.getCreated_at());
            switch (myOrderDto.getStatus()) {
                case "created":
                    //待付款
                    tvOrderTip.setText("喜欢就别犹豫哦");
                    imgOrderStatus.setImageResource(R.mipmap.img_pre_pay);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    btnDetailOperationOne.setVisibility(View.VISIBLE);
                    btnDetailOperationOne.setText("取消订单");
                    btnDetailOperationTwo.setText("去付款");
                    tvTotalNum.setText("共" + myOrderDto.getCount() + "件商品,待支付：");
                    break;
                case "paid":
                    //待发货
                    if (myOrderDto.getRefundInfo() != null && myOrderDto.getRefundInfo().getData() != null) {
                        //退款中
                        return;
                    }
                    tvOrderTip.setText("卖家正在为你整装包裹");
                    imgOrderStatus.setImageResource(R.mipmap.img_packing);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    btnDetailOperationTwo.setText("催发货");
                    btnDetailOperationOne.setVisibility(View.VISIBLE);
                    btnDetailOperationOne.setText("申请退款");
                    break;
                case "shipping"://待消费 待收货
                    llDetailBottom.setVisibility(View.VISIBLE);
                    if ("待消费".equals(myOrderDto.getStatus_msg())){
                        btnDetailOperationTwo.setText("查看兑换券");
                      return;
                    }
                    tvOrderTip.setText("订单已发货，物流派送中");
                    imgOrderStatus.setImageResource(R.mipmap.img_shipping);
                    btnDetailOperationOne.setVisibility(View.VISIBLE);
                    btnDetailOperationOne.setText("查看物流");
                    btnDetailOperationTwo.setText("确认收货");
                    break;
                case "shipped":
                    //待评价
                    tvOrderTip.setText("您已确认收货，订单已完成");
                    imgOrderStatus.setImageResource(R.mipmap.img_receiver);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    btnDetailOperationTwo.setText("去评价");
                    break;
            }
        }
    }

    private void setGoodsListData(RecyclerView recyclerView, List<MyOrderItemDto> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        OrderDetailGoodsListAdapter mAdapter = new OrderDetailGoodsListAdapter(items);
        recyclerView.setAdapter(mAdapter);
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
                getOrderDetail();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("取消成功");
                    getOrderDetail();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
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
                ToastUtil.showToast("确认收货成功");
                getOrderDetail();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("确认收货成功");
                    getOrderDetail();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                dissLoadDialog();
            }
        }, id);
    }
}
