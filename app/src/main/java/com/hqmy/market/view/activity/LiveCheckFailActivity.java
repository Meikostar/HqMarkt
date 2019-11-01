package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 主播审核失败
 */

public class LiveCheckFailActivity extends BaseActivity {
    @BindView(R.id.tv_fail_reason)
    TextView tvFailReason;
    @Override
    public int getLayoutId() {
        return R.layout.ui_live_check_fail;
    }

    @Override
    public void initView() {
       String reason = getIntent().getExtras().getString("reasonTip");
       tvFailReason.setText(reason);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
    @OnClick({R.id.btn_sure})
    public void toOnclick(View view){
        switch (view.getId()){
            case R.id.btn_sure:
                applyLives();
                break;
        }
    }
    public void applyLives() {
        //        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                //                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class);
                                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    gotoActivity(LiveCheckingActivity.class);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
                //                dissLoadDialog();
            }
        });
    }
}
