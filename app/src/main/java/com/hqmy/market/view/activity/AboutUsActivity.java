package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.utils.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_version)
    TextView mVersionName;
    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        mTitleText.setText("版本信息");
    }

    @Override
    public void initData() {
        mVersionName.setText("环球贸易 V"+AppUtils.getVersionName(this));
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
