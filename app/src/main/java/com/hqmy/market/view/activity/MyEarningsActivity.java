package com.hqmy.market.view.activity;

import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的收益
 */
public class MyEarningsActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_title_right)
    TextView mTitleRight;
    /**余额*/
    @BindView(R.id.tv_earnings_balance)
    TextView mBalance;
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_earnings;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的收益");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText("明细");
    }

    @Override
    public void initData() {

    }
    private void getData(){
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>(){
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                super.onSuccess(balanceDto);
                mBalance.setText(balanceDto.getMoney()+"");
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void initListener() {

    }
    @OnClick({R.id.iv_title_back
            ,R.id.tv_title_right
            ,R.id.tv_earnings_recharge
            ,R.id.tv_earnings_withdraw
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_title_right://明细
                gotoActivity(RechargeWithdrawDetailActivity.class);
                break;
            case R.id.tv_earnings_recharge://充值
                gotoActivity(RechargeActivity.class);
                break;
            case R.id.tv_earnings_withdraw://提现
                gotoActivity(WithdrawActivity.class);
                break;
        }
    }
}
