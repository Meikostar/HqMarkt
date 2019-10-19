package com.hqmy.market.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.view.mainfragment.community.TopicDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderItemDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.OrderGoodsListAdapter;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 退款售后详情
 */
public class RefundAfterSalesDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView     mTitleText;
    @BindView(R.id.tv_status_msg)
    TextView     tvStatusMsg;
    @BindView(R.id.tv_time)
    TextView     tvTime;
    @BindView(R.id.tv_total_money)
    TextView     tvTotalMoney;
    @BindView(R.id.tv_back_money)
    TextView     tvBackMoney;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_back_no)
    TextView     tvBackNo;
    @BindView(R.id.tv_back_money_2)
    TextView     tvBackMoney2;
    @BindView(R.id.tv_apply_time)
    TextView     tvApplyTime;
    @BindView(R.id.tv_back_reason)
    TextView     tvBackReason;
    @BindView(R.id.iv_status)
    ImageView    ivStatus;
    private String id;
    private String customer_phone;

    @Override
    public int getLayoutId() {
        return R.layout.activity_refund_after_sales_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("退款详情");
        id = getIntent().getStringExtra("id");
    }

    @Override
    public void initData() {
        loadData();
    }

    @Override
    public void initListener() {

    }

    private void loadData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("include", "order.items.product,order.shop");
        DataManager.getInstance().getAllUserRefundDetail(new DefaultSingleObserver<HttpResult<MyOrderDto>>() {
            @Override
            public void onSuccess(HttpResult<MyOrderDto> httpResult) {
                dissLoadDialog();
                if (httpResult != null && httpResult.getData() != null) {
                    MyOrderDto myOrderDto = httpResult.getData();
                    if ("0".equals(myOrderDto.getStatus())) {
                        ivStatus.setImageResource(R.mipmap.img_checking);
                    } else {
                        ivStatus.setImageResource(R.mipmap.refund_success);
                    }
                    tvStatusMsg.setText(myOrderDto.getStatus_msg());
                    tvTime.setText(myOrderDto.getUpdated_at());
                    tvTotalMoney.setText("￥" + myOrderDto.getRefund_money());
                    tvBackMoney.setText("￥" + myOrderDto.getRefund_money());
                    tvBackNo.setText("退款编号: " + myOrderDto.getMall_order_sn());
                    tvBackMoney2.setText("退款金额: " + "￥" + myOrderDto.getRefund_money());
                    tvApplyTime.setText("申请时间: " + myOrderDto.getCreated_at());
                    tvBackReason.setText("退款原因: " + myOrderDto.getReason());
                    if (myOrderDto.getOrder() != null && myOrderDto.getOrder().getData() != null && myOrderDto.getOrder().getData().getItems() != null && myOrderDto.getOrder().getData().getItems().getData() != null) {
                        setGoodsListData(myOrderDto.getOrder().getData().getItems().getData());
                    }
                    customer_phone = myOrderDto.getCustomer_phone();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, id, map);
    }

    @OnClick({R.id.iv_title_back, R.id.tv_contact_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_contact_phone:
                if (!TextUtils.isEmpty(customer_phone)) {
                    tipDialog(customer_phone);
                }
                break;
        }
    }

    private OrderGoodsListAdapter mAdapter;

    private void setGoodsListData(List<MyOrderItemDto> items) {
        mAdapter = new OrderGoodsListAdapter(items);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.rl_bg:
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, mAdapter.getData().get(position).getProduct_id());
                        gotoActivity(CommodityDetailActivity.class, bundle);
                        break;

                }
            }
        });
    }
    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(RefundAfterSalesDetailActivity.this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * 是否拨打电话弹窗
     * @param phone
     */
    private void tipDialog(String phone) {
        ConfirmDialog confirmDialog = new ConfirmDialog(this);
        confirmDialog.setTitle("客服电话");
        confirmDialog.setMessage(phone);
        confirmDialog.setCancelText("取消");
        confirmDialog.setYesOnclickListener("呼叫", new BaseDialog.OnYesClickListener() {
            @Override
            public void onYesClick() {
                confirmDialog.dismiss();
                callPhone(phone);
            }
        });
        confirmDialog.show();
    }

    /**
     * 拨打客服电话
     * @param phone
     */
    private void callPhone(String phone) {
        new RxPermissions(this).request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    if (ActivityCompat.checkSelfPermission(RefundAfterSalesDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);
                }
            }
        });
    }
}
