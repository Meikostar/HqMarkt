package com.hqmy.market.view.activity;


import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.ExpressList;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.widgets.dialog.ExpressListDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 猜你喜欢-我买到的/我卖出的-去发货
 */
public class ToShipActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_ship_company)
    TextView tvShipCompany;
    @BindView(R.id.et_no)
    EditText etNo;

    private List<ExpressList> reasons = new ArrayList<>();
    ExpressListDialog dialog;
    private String id;
    private String express_code;
    @Override
    public void initListener() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_to_ship_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("发货");
        id = getIntent().getExtras().getString("id");
    }

    @Override
    public void initData() {
        expressList();
    }

    private void showReasonDialog() {
        if (dialog == null) {
            dialog = new ExpressListDialog(this, new ExpressListDialog.DialogListener() {
                @Override
                public void sureItem(int position, ExpressList expressList) {
                    express_code = expressList.getCode();
                    tvShipCompany.setText(expressList.getName());
                }
            }, reasons);
        }
        dialog.show();
        dialog.setTvDialogTitle("物流公司");
    }

    private void expressList() {
        DataManager.getInstance().expressList(new DefaultSingleObserver<HttpResult<List<ExpressList>>>() {
            @Override
            public void onSuccess(HttpResult<List<ExpressList>> httpResult) {
                dissLoadDialog();
                reasons.clear();
                if (httpResult != null && httpResult.getData() != null) {
                    reasons.addAll(httpResult.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        });
    }

    private void toShip() {
        if (TextUtils.isEmpty(express_code)){
            ToastUtil.showToast("请选择物流公司");
            return;
        }
        if (TextUtils.isEmpty(etNo.getText().toString())){
            ToastUtil.showToast("请输入物流单号");
            return;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("express_code",express_code);
        map.put("express_no",etNo.getText().toString());
        DataManager.getInstance().toShip(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                setResult(Activity.RESULT_OK);
                finish();

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        },id,map);
    }
    @OnClick({R.id.iv_title_back, R.id.ll_reason, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_reason:
                showReasonDialog();
                break;
            case R.id.btn_sure:
                toShip();
                break;
        }
    }
}
