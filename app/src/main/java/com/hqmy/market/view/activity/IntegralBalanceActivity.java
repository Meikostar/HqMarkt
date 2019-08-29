package com.hqmy.market.view.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 积分余额
 */
public class IntegralBalanceActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager        mViewPager;
    @BindView(R.id.tv_score)
    TextView tvScore;
    private String[] titles = {"0-500", "500-1000","1000-3000","3000以上"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_balance;
    }

    @Override
    public void initView() {
        mTitleText.setText("积分余额");
    }

    @Override
    public void initData() {
        getData();
//        ArrayList<Fragment> fragments = new ArrayList<>();
//        for (int i = 0; i < titles.length; i++) {
//            fragments.add(new IntegralBalanceFragment());
//        }
//        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
//        mTabLayout.setViewPager(mViewPager, titles);
    }

    @Override
    public void initListener() {

    }
    private void getData(){
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>(){
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                super.onSuccess(balanceDto);
                tvScore.setText(balanceDto.getScore());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
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
