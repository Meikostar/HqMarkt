package com.hqmy.market.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {
    public static final int CHECK_BANK_CARD = 101;

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_recharge_money)
    EditText etRechargeMoney;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView() {
        mTitleText.setText("充值");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    private void moneyRecharges() {
        if(TextUtils.isEmpty(etRechargeMoney.getText().toString().trim())){
            ToastUtil.showToast("请输入充值金额");
            return;
        }
        Map map = new HashMap();
        map.put("total",etRechargeMoney.getText().toString().trim());
        DataManager.getInstance().moneyRecharges(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                Bundle bundle= new Bundle();
                bundle.putString(Constants.INTENT_WEB_URL,result.getData());
                bundle.putString(Constants.INTENT_WEB_TITLE,"充值");
                gotoActivity(RechargeWebActivity.class,false,bundle);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, map);
    }

    @OnClick({R.id.iv_title_back
            , R.id.tv_recharge_affirm
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_recharge_affirm://确认充值
                moneyRecharges();
                break;
        }
    }


}
