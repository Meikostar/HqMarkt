package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.OrderProductDto;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.RechargeWebActivity;
import com.hqmy.market.view.activity.ShippingAddressActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by rzb on 2019/6/24
 */
public class BuyBabyActivity extends BaseActivity {
    public static final String MALL_TYPE = "mall_type";
    public static final String PRODUCT_ID = "productId";
    public static final String ADDRESS_DETAIL = "address_detail";
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_order_to_name)
    TextView tv_order_to_name;
    @BindView(R.id.tv_order_to_phone)
    TextView tv_order_to_phone;
    @BindView(R.id.tv_order_address_title)
    TextView tv_order_address_title;
    @BindView(R.id.iv_buy_baby)
    ImageView      iv_buy_baby;
    @BindView(R.id.tv_buy_baby_content)
    TextView       tv_buy_baby_content;
    @BindView(R.id.tv_buy_baby_score)
    TextView       tv_buy_baby_score;
    @BindView(R.id.tv_buy_baby_fee)
    TextView       tv_buy_baby_fee;
    @BindView(R.id.tv_buy_baby_num)
    TextView       tv_buy_baby_num;
    @BindView(R.id.tv_confirm_order_price)
    TextView       tv_confirm_order_price;
    @BindView(R.id.tv_shop_cart_submit)
    TextView       tv_shop_cart_submit;
    @BindView(R.id.layout_site)
    RelativeLayout  layout_site;

    private String mall_type;
    private String addressId;
    private String productId;
    private AddressDto defaultAddress = null;
    private OrderPreviewDto orderPreviewDto = null;

    @Override
    public int getLayoutId() {
        return R.layout.activity_buy_baby;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        defaultAddress = (AddressDto) bundle.getSerializable(ADDRESS_DETAIL);
        mall_type = bundle.getString(MALL_TYPE);
        addressId = ""+defaultAddress.getId();
        productId = bundle.getString(PRODUCT_ID);

        tv_order_to_name.setText("收货人: " + defaultAddress.getName());
        tv_order_to_phone.setText(defaultAddress.getMobile());
        tv_order_address_title.setText("收货地址 "+ defaultAddress.getArea() + "," + defaultAddress.getDetail());

        HashMap<String,String> map = new HashMap<>();
        map.put("product_id", productId);
        map.put("address_id", addressId);
        getOrderPreInfo(mall_type, map);
    }

    /**
     * 获取预订单详情
     */
    private void getOrderPreInfo(String mType,  HashMap<String,String> map) {
        showLoadDialog();
        DataManager.getInstance().getOrderPreInfo(new DefaultSingleObserver<HttpResult<OrderPreviewDto>>() {
            @Override
            public void onSuccess(HttpResult<OrderPreviewDto> result) {
                LogUtil.i(TAG, "--RxLog-Thread: onSuccess()");
                dissLoadDialog();
                if(result != null){
                    if(result.getData() != null) {
                        orderPreviewDto = result.getData();
                        setDataView(orderPreviewDto);
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
        bindClickEvent(iv_back, () -> {
            finish();
        });

        bindClickEvent(layout_site,() ->{
            gotoActivity(ShippingAddressActivity.class, false);
        });

        bindClickEvent(tv_shop_cart_submit, () -> {
            List<OrderShopDto> shopLists = orderPreviewDto.getShops();
            List<OrderProductDto> products = new ArrayList<OrderProductDto>();
            String productId = null;
            for(int i=0; i<shopLists.size(); i++){
                products.addAll(shopLists.get(i).getProducts());
            }
            productId = products.get(0).getId();
            checkOutOrder(productId, addressId);
        });
    }

    private void setDataView(OrderPreviewDto orderPreviewDto){
        if(orderPreviewDto.getShops() != null){
            List<OrderShopDto> osList = orderPreviewDto.getShops();
            if(osList != null){
                OrderShopDto orderShopDto = osList.get(0);
                if(orderShopDto != null) {
                    List<OrderProductDto> opList = orderShopDto.getProducts();
                    if(opList != null) {
                        OrderProductDto opDto = opList.get(0);
                        GlideUtils.getInstances().loadNormalImg(BuyBabyActivity.this, iv_buy_baby,
                                Constants.WEB_IMG_URL_UPLOADS + opDto.getCover());
                        tv_buy_baby_content.setText(opDto.getName());
                        tv_buy_baby_score.setText(opDto.getScore()+"积分");
                        tv_buy_baby_num.setText("×" + opDto.getQty());
                        tv_buy_baby_fee.setText(orderShopDto.getFreight().getFreight());
                        tv_confirm_order_price.setText(orderShopDto.getScore()+"积分");
                    }
                }
            }
        }
    }

    private void  checkOutOrder(String product_id, String addressId){
        HashMap<String,String> map = new HashMap<>();
        map.put("product_id", product_id);
        map.put("address_id", addressId);
        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
                ToastUtil.showToast("下单成功");
                String orderId = "";
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    orderId = result.getData().get(0).getNo();
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + orderId);
                bundle.putString(Constants.INTENT_WEB_TITLE, "支付");
                gotoActivity(RechargeWebActivity.class, true, bundle);
            }
            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("下单成功");
                    finish();
                } else {
                    ToastUtil.showToast("下单失败");
                }
            }
        }, mall_type, map);
    }
}
