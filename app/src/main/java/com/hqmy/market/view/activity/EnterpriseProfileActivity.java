package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业简介
 */
public class EnterpriseProfileActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_enterprise_profile;
    }

    @Override
    public void initView() {
        mTitleText.setText("企业简介");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
