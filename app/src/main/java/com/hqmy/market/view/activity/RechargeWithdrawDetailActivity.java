package com.hqmy.market.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.view.fragments.RechargeWithdrawDetailFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值提现明细
 */
public class RechargeWithdrawDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge_withdraw_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("充值/提现明细");
    }

    @Override
    public void initData() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RechargeWithdrawDetailFragment fragment = new RechargeWithdrawDetailFragment();
        fragmentTransaction.add(R.id.content_frame, fragment);
        fragmentTransaction.commit();
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
