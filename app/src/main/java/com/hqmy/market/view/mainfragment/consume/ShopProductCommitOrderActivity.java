package com.hqmy.market.view.mainfragment.consume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.MyCouponActivity;
import com.hqmy.market.view.activity.RechargeWebActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 店铺产品-提交订单
 */
public class ShopProductCommitOrderActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_product_price_2)
    TextView tvProductPrice2;
    @BindView(R.id.rl_comment)
    RelativeLayout rlComment;
    @BindView(R.id.tv_product_price_3)
    TextView tvProductPrice3;
    @BindView(R.id.tv_to_pay)
    TextView tvToPay;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rl_coupon)
    RelativeLayout rlCoupon;
    @BindView(R.id.tv_dis_value)
    TextView tvDisValue;
    private String productId, mall_type,shopId;
    private String couponId = "";
    private int currentPosition = -1;
    private OrderPreviewDto orderPreviewDto;
    private List<OrderShopDto> shopLists;

    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });
        bindClickEvent(tvToPay, () -> {
            //提交订单
            checkOutOrder();
        });
        bindClickEvent(rlCoupon, () -> {
            //跳转到选择优惠券界面
            Bundle bundle = new Bundle();
            bundle.putInt("currentPosition", currentPosition);
            bundle.putString("mallType",mall_type);
            bundle.putString("productId",productId);
            bundle.putString("shopId",shopId);
            gotoActivity(MyCouponActivity.class, false, bundle, 1);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_product_commit_order;
    }

    @Override
    public void initView() {
        tvTitleText.setText("提交订单");
    }

    @Override
    public void initData() {
        productId = getIntent().getStringExtra("productId");
        shopId = getIntent().getStringExtra("shopId");
        mall_type = getIntent().getStringExtra("mall_type");
        getPreviewData();
    }

    private void getPreviewData() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", productId);
        DataManager.getInstance().getOrderPreInfo(new DefaultSingleObserver<HttpResult<OrderPreviewDto>>() {
            @Override
            public void onSuccess(HttpResult<OrderPreviewDto> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null) {
                    orderPreviewDto = result.getData();
                    shopLists = orderPreviewDto.getShops();
                    if (shopLists != null && shopLists.size() > 0) {
                        if (shopLists.get(0).getProducts() != null && shopLists.get(0).getProducts().size() > 0) {
                            tvProductName.setText(shopLists.get(0).getProducts().get(0).getName());
                            tvProductPrice.setText(shopLists.get(0).getProducts().get(0).getPrice() + "元");
                        }
                        tvProductPrice2.setText("¥" + shopLists.get(0).getTotal());
                        if (shopLists.get(0).isAvailable_coupon()) {
                            tvCoupon.setText("有可用优惠券");
                        } else {
                            tvCoupon.setText("无可用优惠券");
                        }
                    }
                    tvProductPrice3.setText("¥" + orderPreviewDto.getTotal());
                    tvToPay.setText("去支付" + orderPreviewDto.getTotal() + "元");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, mall_type, map);
    }

    private void checkOutOrder() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", productId);
        DataManager.getInstance().checkOutOrder2(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> checkOutOrderResultList) {
                dissLoadDialog();
                ToastUtil.showToast("下单成功");
                String orderId = "";
                if (checkOutOrderResultList != null && checkOutOrderResultList.getData() != null && checkOutOrderResultList.getData().size() > 0) {
                    orderId = checkOutOrderResultList.getData().get(0).getNo();
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + orderId);
                bundle.putString(Constants.INTENT_WEB_TITLE, "支付");
                gotoActivity(RechargeWebActivity.class, true, bundle);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("下单成功");
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, mall_type, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            currentPosition = data.getIntExtra("currentPosition", -1);
            if (currentPosition == -1) {
                couponId = "";
                if (shopLists != null && shopLists.size() > 0 && shopLists.get(0).isAvailable_coupon()) {
                    tvCoupon.setText("有可用优惠券");
                } else {
                    tvCoupon.setText("无可用优惠券");
                }
                tvProductPrice3.setText("¥" + orderPreviewDto.getTotal());
                tvToPay.setText("去支付" + orderPreviewDto.getTotal() + "元");
                tvDisValue.setText("");
                return;
            }
            couponId = data.getStringExtra("couponId");
            String discountValue = data.getStringExtra("discountValue");
            if (!TextUtils.isEmpty(discountValue)) {
                tvCoupon.setText("-¥" + discountValue);
                tvDisValue.setText("(已优惠" + discountValue + "元)");
                if (orderPreviewDto != null) {
                    Double total = Double.valueOf(orderPreviewDto.getTotal()) - Double.valueOf(discountValue);
                    tvProductPrice3.setText("¥" + total);
                    tvToPay.setText("去支付" + total + "元");
                }
            }
        }
    }
}
