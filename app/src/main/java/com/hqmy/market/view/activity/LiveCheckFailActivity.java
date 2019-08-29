package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
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
                gotoActivity(RequestLivePermissionActivity.class,true);
                break;
        }
    }
}
