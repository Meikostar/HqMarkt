package com.hqmy.market.view.mainfragment.consume;

import android.app.Activity;
import android.content.Context;
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
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.OrderProductDto;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.bean.WEIXINREQ;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.MD5Utils;
import com.hqmy.market.common.utils.PayUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.SettingPasswordActivity;
import com.hqmy.market.view.activity.OrderActivity;
import com.hqmy.market.view.activity.PaySuccessActivity;
import com.hqmy.market.view.activity.ShippingAddressActivity;
import com.hqmy.market.view.adapter.ConfirmOrderAdapter;
import com.hqmy.market.view.fragments.PayResultListener;
import com.hqmy.market.view.widgets.InputPwdDialog;
import com.hqmy.market.view.widgets.MCheckBox;
import com.hqmy.market.view.widgets.PhotoPopupWindow;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rzb on 2019/6/24
 */
public class ConfirmOrderActivity extends BaseActivity {
    public static final String ROW_STR        = "row_str";
    public static final String MALL_TYPE      = "mall_type";
    public static final String ADDRESS_DETAIL = "address_detail";
    @BindView(R.id.iv_confirm_order_back)
    ImageView       iv_confirm_order_back;
    @BindView(R.id.tv_order_to_name)
    TextView        tv_order_to_name;
    @BindView(R.id.tv_order_to_phone)
    TextView        tv_order_to_phone;
    @BindView(R.id.tv_order_address_title)
    TextView        tv_order_address_title;
    @BindView(R.id.recycle_confirm_order)
    MaxRecyclerView recycle_confirm_order;
    @BindView(R.id.tv_confirm_order_price)
    TextView        tv_confirm_order_price;
    @BindView(R.id.tv_shop_cart_submit)
    TextView        tv_shop_cart_submit;
    @BindView(R.id.rl_address)
    RelativeLayout  rlAddress;
    @BindView(R.id.ll_address)
    LinearLayout    llAddress;

    private ConfirmOrderAdapter mConfirmOrderAdapter;
    private String              mall_type;
    private String              product_id;
    private String              stock_id;
    private String              countBuy;
    private String              id;
    private String              addressId;
    private AddressDto          defaultAddress = null;
    private ArrayList<String>   rowList        = new ArrayList<String>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConfirmOrderActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycle_confirm_order.setLayoutManager(linearLayoutManager);
        mConfirmOrderAdapter = new ConfirmOrderAdapter(null);
        recycle_confirm_order.setAdapter(mConfirmOrderAdapter);

        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this, ShippingAddressActivity.class);
                intent.putExtra("tag", 1);
                startActivityForResult(intent, 6);
            }
        });

        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = false;
                Intent intent = new Intent(ConfirmOrderActivity.this, ShippingAddressActivity.class);
                intent.putExtra("tag", 1);
                startActivityForResult(intent, 6);
            }
        });
    }


    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            product_id = bundle.getString("product_id");
            stock_id = bundle.getString("stock_id");
            countBuy = bundle.getString("countBuy");
            if (TextUtil.isNotEmpty(id)) {
                rlAddress.setVisibility(View.VISIBLE);
                llAddress.setVisibility(View.GONE);
                defaultAddress = (AddressDto) bundle.getSerializable(ADDRESS_DETAIL);
                rowList = bundle.getStringArrayList(ROW_STR);
                mall_type = bundle.getString(MALL_TYPE);

                if (defaultAddress != null) {
                    addressId = "" + defaultAddress.getId();
                    tv_order_to_name.setText("收货人: " + defaultAddress.getName());
                    tv_order_to_phone.setText(defaultAddress.getMobile());
                    tv_order_address_title.setText("收货地址 " + defaultAddress.getArea() + "," + defaultAddress.getDetail());
                } else {
                    addressId = "" + id;
                }


                HashMap<String, String> map = new HashMap<>();
                if (rowList != null) {
                    for (int i = 0; i < rowList.size(); i++) {
                        map.put("rows[" + String.valueOf(i) + "]", rowList.get(i));
                    }
                }
                if (TextUtil.isNotEmpty(stock_id)) {
                    map.put("stock_id", stock_id);
                }
                if (TextUtil.isNotEmpty(countBuy)) {
                    map.put("qty", countBuy);
                }

                if (TextUtil.isNotEmpty(product_id)) {
                    map.put("product_id", product_id);

                }
                map.put("address_id", addressId);
                map.put("include", "shop_name");
                getOrderPreInfo(mall_type, map);
            } else {
                rowList = bundle.getStringArrayList(ROW_STR);
                mall_type = bundle.getString(MALL_TYPE);
                HashMap<String, String> map = new HashMap<>();
                if (rowList != null && rowList.size() > 0) {
                    for (int i = 0; i < rowList.size(); i++) {
                        map.put("rows[" + String.valueOf(i) + "]", rowList.get(i));
                    }
                }
                if (TextUtil.isNotEmpty(countBuy)) {
                    map.put("qty", countBuy);
                }
                if (TextUtil.isNotEmpty(stock_id)) {
                    map.put("stock_id", stock_id);
                }

                if (TextUtil.isNotEmpty(product_id)){
                    map.put("product_id", product_id);
                }
                map.put("include", "shop_name");
                getOrderPreInfo(mall_type, map);
                rlAddress.setVisibility(View.GONE);
                llAddress.setVisibility(View.VISIBLE);
            }
        }
        getBalance();
    }


    @Override
    protected void onResume() {
        if (isShow) {
            getAddressListData();
        } else {
            isShow = true;
        }
        super.onResume();
    }

    private boolean          isShow;
    /**
     * 获取收货地址
     */
    private List<AddressDto> mAddressDatas = new ArrayList<>();

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
                            defaultAddress = mAddressDatas.get(i);
                        }
                    }
                }
                if (defaultAddress != null && defaultAddress.getId() != 0) {
                    rlAddress.setVisibility(View.VISIBLE);
                    llAddress.setVisibility(View.GONE);
                    addressId = "" + defaultAddress.getId();

                    tv_order_to_name.setText("收货人: " + defaultAddress.getName());
                    tv_order_to_phone.setText(defaultAddress.getMobile());
                    tv_order_address_title.setText("收货地址 " + defaultAddress.getArea() + "," + defaultAddress.getDetail());

                    HashMap<String, String> map = new HashMap<>();
                    if (rowList != null) {
                        for (int i = 0; i < rowList.size(); i++) {
                            map.put("rows[" + String.valueOf(i) + "]", rowList.get(i));
                        }
                    }
                    if (TextUtil.isNotEmpty(stock_id)) {
                        map.put("stock_id", stock_id);
                    } else {
                        map.put("product_id", product_id);
                    }
                    map.put("address_id", addressId);
                    map.put("include", "shop_name");
                    getOrderPreInfo(mall_type, map);
                } else {
                    rlAddress.setVisibility(View.GONE);
                    llAddress.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private int RQ_WEIXIN_PAY = 12;
    private int RQ_PAYPAL_PAY = 16;
    private int RQ_ALIPAY_PAY = 10;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RQ_WEIXIN_PAY) {
                ToastUtil.showToast("支付成功");
                gotoActivity(PaySuccessActivity.class);
                finish();
                //           if (requestCode == RQ_WEIXIN_PAY) {
                //                RxBus.getInstance().send(SubscriptionBean.createSendBean(SubscriptionBean.CHAEGE_SUCCESS,""));
                //            }
            } else {
                Serializable serializable = data.getSerializableExtra("result");
                if (serializable != null) {
                    AddressDto addressDto = (AddressDto) serializable;
                    initAddress(addressDto);
                }
            }

        } else {
            if (resultCode == 111) {
                Intent intent = new Intent(ConfirmOrderActivity.this, OrderActivity.class);
                intent.putExtra("page", 1);
                startActivity(intent);
                finish();

            } else {
                if (defaultAddress == null) {
                    getAddressListData();
                }
            }


        }
    }

    private double GdBalance;

    public void getBalance() {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                super.onSuccess(balanceDto);
                GdBalance = balanceDto.getMoney();

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    public void initAddress(AddressDto addressDto) {
        rlAddress.setVisibility(View.VISIBLE);
        llAddress.setVisibility(View.GONE);
        addressId = "" + addressDto.getId();

        tv_order_to_name.setText("收货人: " + addressDto.getName());
        tv_order_to_phone.setText(addressDto.getMobile());
        tv_order_address_title.setText("收货地址 " + addressDto.getArea() + "," + addressDto.getDetail());

        HashMap<String, String> map = new HashMap<>();
        if (rowList != null) {
            for (int i = 0; i < rowList.size(); i++) {
                map.put("rows[" + String.valueOf(i) + "]", rowList.get(i));
            }
        }
        if (TextUtil.isNotEmpty(stock_id)) {
            map.put("stock_id", stock_id);
        } else {
            map.put("product_id", product_id);
        }
        map.put("address_id", addressId);
        map.put("include", "shop_name");
        getOrderPreInfo(mall_type, map);
    }

    /**
     * 获取预订单详情
     */
    private void getOrderPreInfo(String mType, HashMap<String, String> map) {
        showLoadDialog();
        DataManager.getInstance().getOrderPreInfo(new DefaultSingleObserver<HttpResult<OrderPreviewDto>>() {
            @Override
            public void onSuccess(HttpResult<OrderPreviewDto> result) {
                LogUtil.i(TAG, "--RxLog-Thread: onSuccess()");
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        OrderPreviewDto orderPreviewDto = result.getData();
                        List<OrderShopDto> shopLists = orderPreviewDto.getShops();
                        mConfirmOrderAdapter.setNewData(shopLists);
                        tv_confirm_order_price.setText(orderPreviewDto.getTotal());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.i(TAG, "--RxLog-Thread: onError()");
                dissLoadDialog();
            }
        }, mType, map);
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_confirm_order_back, () -> {
            finish();
        });

        bindClickEvent(tv_shop_cart_submit, () -> {
            List<OrderShopDto> shopLists = mConfirmOrderAdapter.getData();
            List<OrderProductDto> products = new ArrayList<OrderProductDto>();
            String rowsStr = null;
            for (int i = 0; i < shopLists.size(); i++) {
                products.addAll(shopLists.get(i).getProducts());
            }
            if (TextUtil.isEmpty(addressId)) {
                ToastUtil.showToast("请先添加收货地址");
                return;
            }
            checkOutOrder(shopLists,products, addressId);
        });
    }

    private List<CheckOutOrderResult> ruslts;

    private void checkOutOrder( List<OrderShopDto> data,List<OrderProductDto> products, String addressId) {
        HashMap<String, String> map = new HashMap<>();
        for(OrderShopDto dto:data){
            if(TextUtil.isNotEmpty(dto.comment)){
                map.put("comment[" + dto.getShop_id() + "]", dto.comment);
            }
        }
        if (rowList != null) {
            if (products != null) {
                for (int i = 0; i < products.size(); i++) {
                    map.put("rows[" + String.valueOf(i) + "]", products.get(i).getRowId());
                }
            }
        } else {
            if (TextUtil.isNotEmpty(stock_id)) {
                map.put("stock_id", stock_id);
            } else {
                map.put("product_id", product_id);
            }
        }

        map.put("address_id", addressId);
        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
                ruslts = result.getData();
                if (ruslts != null && ruslts.size() > 0) {
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
                //                finish();
            }
        }, mall_type, map);
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
            tv_balance = view.findViewById(R.id.tv_balance);
            if (GdBalance != 0) {
                tv_balance.setText("(" + GdBalance + ")");
            }
            llBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChoose(0);
                    state = 0;
                }
            });
            llWx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 1;
                    setChoose(1);
                }
            });
            llZfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    state = 2;
                    setChoose(2);
                }
            });
            btSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (state) {
                        case 0:
                            if (Double.valueOf(GdBalance) < Double.valueOf(tv_confirm_order_price.getText().toString())) {
                                ToastUtil.showToast("余额不足");
                                return;
                            }
                            getMemberBaseInfo(new InputPwdDialog.InputPasswordListener() {
                                @Override
                                public void callbackPassword(String password) {
                                    if (Double.valueOf(GdBalance) < Double.valueOf(tv_confirm_order_price.getText().toString())) {
                                        ToastUtil.showToast("余额不足");
                                        return;
                                    }
                                    submitWalletOrder(password);
                                }
                            });
                            break;

                        case 1:
                            submitWxOrder();
                            break;
                        case 2:
                            submitOrder();
                            break;
                    }
                    mWindowAddPhoto.dismiss();
                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mWindowAddPhoto.dismiss();
                }
            });

            mWindowAddPhoto = new PhotoPopupWindow(this).bindView(view);
            mWindowAddPhoto.showAtLocation(tv_confirm_order_price, Gravity.BOTTOM, 0, 0);
        } else {
            mWindowAddPhoto.showAtLocation(tv_confirm_order_price, Gravity.BOTTOM, 0, 0);
        }


    }

    /**
     * 余额支付时，查询是否设置支付密码
     */
    private void getMemberBaseInfo(InputPwdDialog.InputPasswordListener listener) {
        if (BaseApplication.isSetPay == 1) {
            InputPwdDialog inputPasswordDialog = new InputPwdDialog(ConfirmOrderActivity.this, listener);
            inputPasswordDialog.show();
        } else {
            gotoActivity(SettingPasswordActivity.class);
        }
    }

    /**
     * zfb
     */
    private void submitOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        for (CheckOutOrderResult result : ruslts) {
            map.put("order_no[" + i + "]", result.no);
        }
        map.put("platform", "alipay");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitZfbOrder(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> httpResult) {
                dissLoadDialog();
                if (httpResult != null && !TextUtils.isEmpty(httpResult.getData())) {
                    PayUtils.getInstances().zfbPaySync(ConfirmOrderActivity.this, httpResult.getData(), new PayResultListener() {
                        @Override
                        public void zfbPayOk(boolean payOk) {
                            Intent intent = new Intent(ConfirmOrderActivity.this, PaySuccessActivity.class);

                            if (payOk) {
                                intent.putExtra("state", 1);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtil.showToast("支付已取消");
                                Intent intents = new Intent(ConfirmOrderActivity.this, OrderActivity.class);
                                intents.putExtra("page", 1);
                                startActivity(intents);
                                finish();
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
     * zfb
     */
    private void submitWalletOrder(String payment_password) {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        for (CheckOutOrderResult result : ruslts) {
            map.put("order_no[" + i + "]", result.no);
        }
        map.put("platform", "wallet");
        map.put("scene", "balance");
        map.put("payment_password", payment_password);
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney

        DataManager.getInstance().submitWalletOrder(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                Intent intent = new Intent(ConfirmOrderActivity.this, PaySuccessActivity.class);
                intent.putExtra("state", 1);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    Intent intent = new Intent(ConfirmOrderActivity.this, PaySuccessActivity.class);
                    intent.putExtra("state", 1);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, map);


    }

    /**
     * wx
     */
    private void submitWxOrder() {

        HashMap<String, String> map = new HashMap<>();
        int i = 0;
        for (CheckOutOrderResult result : ruslts) {
            map.put("order_no[" + i + "]", result.no);
        }
        map.put("platform", "wechat");
        map.put("scene", "app");
        //        map.put("realOrderMoney", realOrderMoney);  //订单支付 去掉参数 订单金额  realOrderMoney


        DataManager.getInstance().submitWxOrder(new DefaultSingleObserver<HttpResult<WEIXINREQ>>() {
            @Override
            public void onSuccess(HttpResult<WEIXINREQ> httpResult) {
                dissLoadDialog();
                PayUtils.getInstances().WXPay(ConfirmOrderActivity.this, httpResult.getData());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }

    private View mView;

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
    private TextView         tv_balance;
    private double           total;
    private double           totals;

    public void setChoose(int state) {
        switch (state) {
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

    private int              state;
    private PhotoPopupWindow mWindowAddPhoto;

}
