package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.OrderProductDto;
import com.hqmy.market.bean.OrderShopDto;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ConfirmOrderAdapter;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;

/**
 * Created by rzb on 2019/6/24
 */
public class ConfirmOrderActivity extends BaseActivity {
    public static final String ROW_STR = "row_str";
    public static final String MALL_TYPE = "mall_type";
    public static final String ADDRESS_DETAIL = "address_detail";
    @BindView(R.id.iv_confirm_order_back)
    ImageView iv_confirm_order_back;
    @BindView(R.id.tv_order_to_name)
    TextView tv_order_to_name;
    @BindView(R.id.tv_order_to_phone)
    TextView tv_order_to_phone;
    @BindView(R.id.tv_order_address_title)
    TextView tv_order_address_title;
    @BindView(R.id.recycle_confirm_order)
    MaxRecyclerView recycle_confirm_order;
    @BindView(R.id.tv_confirm_order_price)
    TextView tv_confirm_order_price;
    @BindView(R.id.tv_shop_cart_submit)
    TextView tv_shop_cart_submit;

    private ConfirmOrderAdapter mConfirmOrderAdapter;
    private String mall_type;
    private String addressId;
    private AddressDto defaultAddress = null;
    private ArrayList<String> rowList  = new ArrayList<String>();

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
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        defaultAddress = (AddressDto) bundle.getSerializable(ADDRESS_DETAIL);
        rowList = bundle.getStringArrayList(ROW_STR);
        mall_type = bundle.getString(MALL_TYPE);
        addressId = ""+defaultAddress.getId();

        tv_order_to_name.setText("收货人: " + defaultAddress.getName());
        tv_order_to_phone.setText(defaultAddress.getMobile());
        tv_order_address_title.setText("收货地址 "+ defaultAddress.getArea() + "," + defaultAddress.getDetail());

        HashMap<String,String> map = new HashMap<>();
        if(rowList != null) {
            for(int i = 0; i<rowList.size(); i++){
                map.put("rows["+ String.valueOf(i) +"]", rowList.get(i));
            }
        }
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
            for(int i=0; i<shopLists.size(); i++){
                products.addAll(shopLists.get(i).getProducts());
            }
            checkOutOrder(products, addressId);
        });
    }

    private void  checkOutOrder(List<OrderProductDto> products, String addressId){
        HashMap<String, String> map = new HashMap<>();
        if(products != null) {
            for(int i = 0; i<products.size(); i++){
                map.put("rows["+ String.valueOf(i) +"]", products.get(i).getRowId());
            }
        }
        map.put("address_id", addressId);
        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
                ToastUtil.showToast("下单成功");
                finish();
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
        }, mall_type, map);
    }
}
