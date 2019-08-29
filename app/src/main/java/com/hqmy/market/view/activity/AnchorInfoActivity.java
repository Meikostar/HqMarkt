package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主播资料
 */
public class AnchorInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @Override
    public int getLayoutId() {
        return R.layout.activity_anchor_info;
    }

    @Override
    public void initView() {
        mTitleText.setText("主播资料");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
    @OnClick({R.id.iv_title_back,R.id.rl_linker,R.id.rl_score})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_linker:
                //关注用户列表
                gotoActivity(AttentionUserListActivity.class);
                break;
            case R.id.rl_score:
                //积分收入
                gotoActivity(PointIncomeActivity.class);
                break;
        }

    }
}
