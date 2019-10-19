package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.CouponCodeInfo;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.OrderProductDto;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.bean.WEIXINREQ;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.PayUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.adapter.OrderDetailGoodsListAdapter;
import com.hqmy.market.view.fragments.PayResultListener;
import com.hqmy.market.view.mainfragment.consume.ConfirmOrderActivity;
import com.hqmy.market.view.mainfragment.consume.ShopCartActivity;
import com.hqmy.market.view.widgets.MCheckBox;
import com.hqmy.market.view.widgets.PhotoPopupWindow;
import com.hqmy.market.view.widgets.dialog.CouponCodeDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    @BindView(R.id.rl_bg)
    RelativeLayout rl_bg;

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
                case "再来一单":
                    if (mmyOrderDto.getItems() !=null && mmyOrderDto.getItems().getData() != null && mmyOrderDto.getItems().getData().size() > 0) {
                        ArrayList<String> product_id = new ArrayList<>();
                        ArrayList<String> qty = new ArrayList<>();
                        ArrayList<String> stock_id = new ArrayList<>();
                        for (MyOrderItemDto myOrderItemDto : mmyOrderDto.getItems().getData()) {
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
        });
        bindClickEvent(btnDetailOperationTwo, () -> {
            switch (btnDetailOperationTwo.getText().toString()){
                case "去付款":
                    if (mmyOrderDto == null) {
                        return;
                    }
                    checkOutOrder();
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
        if (requestCode == RQ_WEIXIN_PAY) {
            ToastUtil.showToast("支付成功");
            gotoActivity(PaySuccessActivity.class);
            finish();
            //           if (requestCode == RQ_WEIXIN_PAY) {
            //                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
            //            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressListData();
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
        }, id, "default", "shop,items.product,shipment,refundInfo,address");
    }

    private void dealView(MyOrderDto myOrderDto) {
        if (myOrderDto != null) {
            mmyOrderDto = myOrderDto;
            tvOrderStatus.setText(myOrderDto.getStatus_msg());
            if (myOrderDto.getAddress() != null && myOrderDto.getAddress().getData() != null) {
                rl_adress.setVisibility(View.VISIBLE);
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
                    rl_bg.setBackgroundResource(R.color.my_color_order_state1);
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
                    rl_bg.setBackgroundResource(R.color.my_color_order_state2);


                    if (myOrderDto.getRefundInfo() != null && myOrderDto.getRefundInfo().getData() != null) {
                        tvOrderStatus.setText("退款中");
                        imgOrderStatus.setImageResource(R.mipmap.img_packing);
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
                    rl_bg.setBackgroundResource(R.color.my_color_order_state3);
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
                    rl_bg.setBackgroundResource(R.color.my_color_order_state4);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    //待评价
                    tvOrderTip.setText("您已确认收货，订单已完成");
                    imgOrderStatus.setImageResource(R.mipmap.img_receiver);
                    llDetailBottom.setVisibility(View.VISIBLE);

                    btnDetailOperationTwo.setText("去评价");
                    break;
                case "closed":
                    rl_bg.setBackgroundResource(R.color.my_color_order_state4);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    imgOrderStatus.setImageResource(R.mipmap.img_receiver);
                    btnDetailOperationOne.setVisibility(View.VISIBLE);
                    btnDetailOperationTwo.setVisibility(View.GONE);
                    btnDetailOperationOne.setText("再来一单");
                    break;
                case "completed":
                    rl_bg.setBackgroundResource(R.color.my_color_order_state4);
                    llDetailBottom.setVisibility(View.VISIBLE);
                    imgOrderStatus.setImageResource(R.mipmap.img_receiver);
                    btnDetailOperationOne.setVisibility(View.VISIBLE);
                    btnDetailOperationTwo.setVisibility(View.GONE);
                    btnDetailOperationOne.setText("再来一单");
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

    public void showPopPayWindows() {

        if (view == null) {
            view = LayoutInflater.from(this).inflate(R.layout.pay_popwindow_view, null);

            btSure = view.findViewById(R.id.bt_sure);
            iv_close = view.findViewById(R.id.iv_close);
            llBalance = view.findViewById(R.id.ll_balance);
            llZfb = view.findViewById(R.id.ll_zfb);
            llWx = view.findViewById(R.id.ll_wx);
            mcbBalance = view.findViewById(R.id.mcb_balance);
            mcbZfb = view.findViewById(R.id.mcb_zfb);
            mcbWx = view.findViewById(R.id.mcb_wx);

            llBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChoose(0);
                    state=0;
                }
            });
            llWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state=1;
                    setChoose(1);
                }
            });
            llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state=2;
                    setChoose(2);
                }
            });
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state){
                        case 0:
                            //                            getMemberBaseInfo(new InputPwdDialog.InputPasswordListener() {
                            //                                @Override
                            //                                public void callbackPassword(String password) {
                            //                                    if(Double.valueOf(GdBalance)<Double.valueOf(tv_confirm_order_total_money.getText().toString())){
                            //                                        ToastUtil.showToast("余额不足");
                            //                                        return;
                            //                                    }
                            //                                    submitOrder(balance.getId() + "", password,balance.getPaymentCode());
                            //                                }
                            //                            });
                            break;

                        case 1:
                            submitWxOrder();
                            break;
                        case 2:
                            submitOrder();
                            break;
                    }
                    mWindowAddPhoto
                            .dismiss();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhoto.dismiss();
                }
            });

            mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
            mWindowAddPhoto.showAtLocation(tv_title, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(tv_title, Gravity.BOTTOM, 0, 0);
        }


    }
    /**
     * 获取收货地址
     */
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private String addressId;
    private void getAddressListData() {
        //showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                //dissLoadDialog();
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                if (mAddressDatas.size() > 0) {
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if (mAddressDatas.get(i).getIs_default().equals("1")) {
                            addressId = mAddressDatas.get(i).getId()+"";
                        }
                    }
                }


            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private List<CheckOutOrderResult> ruslts;
    private int RQ_WEIXIN_PAY = 12;
    private void checkOutOrder() {
        HashMap<String, String> map = new HashMap<>();
        map.put("rows[" + String.valueOf(1) + "]", mmyOrderDto.getNo());
        map.put("address_id", addressId);
        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
                ruslts=result.getData();
                if(ruslts!=null&&ruslts.size()>0){
                    showPopPayWindows();
                }

            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("下单成功");
                } else {
                    ToastUtil.showToast("下单失败");
                }
                finish();
            }
        }, "default", map);
    }

    /**
     * zfb
     */
    private void submitOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i=0;
        for(CheckOutOrderResult result:ruslts){
            map.put("order_no["+i+"]",result.no);
        }
        map.put("platform", "alipay");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitZfbOrder(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> httpResult) {
                dissLoadDialog();
                if(httpResult != null && !TextUtils.isEmpty(httpResult.getData())){
                    PayUtils.getInstances().zfbPaySync(MyOrderDetailActivity.this, httpResult.getData(), new PayResultListener() {
                        @Override
                        public void zfbPayOk(boolean payOk) {
                            Intent intent = new Intent(MyOrderDetailActivity.this, PaySuccessActivity.class);

                            if(payOk){
                                intent.putExtra("state",1);
                                startActivity(intent);
                                finish();
                            }else {
                                ToastUtil.showToast("支付已取消");
                                //                                finish();
                            }


                        }

                        @Override
                        public void wxPayOk(boolean payOk) {

                        }
                    });
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);


    }
    /**
     * wx
     */
    private void submitWxOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i=0;
        for(CheckOutOrderResult result:ruslts){
            map.put("order_no["+i+"]",result.no);
        }
        map.put("platform", "wechat");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney


        DataManager.getInstance().submitWxOrder(new DefaultSingleObserver<HttpResult<WEIXINREQ>>() {
            @Override
            public void onSuccess(HttpResult<WEIXINREQ> httpResult) {
                dissLoadDialog();
                PayUtils.getInstances().WXPay(MyOrderDetailActivity.this, httpResult.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }
    private View             mView;

    private RecyclerView     recyclerView;
    private Button           btSure;
    private TextView         tv_title;
    private ImageView        iv_close;
    private View             view;
    private PhotoPopupWindow mWindowpayPhoto;
    private LinearLayout     llBalance;
    private LinearLayout     llZfb;
    private LinearLayout     llWx;
    private MCheckBox        mcbBalance;
    private MCheckBox        mcbZfb;
    private MCheckBox        mcbWx;
    private double           total;
    private double           totals;
    public void setChoose(int state){
        switch (state){
            case 0:
                mcbBalance.setChecked(true);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(false);
                break;

            case 1:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(false);
                mcbWx.setChecked(true);
                break;
            case 2:
                mcbBalance.setChecked(false);
                mcbZfb.setChecked(true);
                mcbWx.setChecked(false);
                break;
        }
    }
    private int state;
    private PhotoPopupWindow mWindowAddPhoto;

}
