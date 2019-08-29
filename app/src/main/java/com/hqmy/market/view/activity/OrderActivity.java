package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.view.fragments.OrderFragment;
import com.hqmy.market.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 订单
 */
public class OrderActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.vp_action)
    ViewPager        mViewPager;
    private String[] titles = {"全部", "待付款", "待发货", "待收货", "待评价"};

    private int mCurrPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    public void initView() {
        mTitleText.setText("我的订单");
        mCurrPage = getIntent().getIntExtra("page",0);
    }

    @Override
    public void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new OrderFragment(Constants.TREE_ORDER_TYPE_ALL));
        fragments.add(new OrderFragment(Constants.TREE_ORDER_TYPE_PAYMENT));
        fragments.add(new OrderFragment(Constants.TREE_ORDER_TYPE_DELIVERY));
        fragments.add(new OrderFragment(Constants.TREE_ORDER_TYPE_RECEIVE));
        fragments.add(new OrderFragment(Constants.TREE_ORDER_TYPE_EVALUATE));

        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);

        mViewPager.setCurrentItem(mCurrPage);
    }

    @Override
    public void initListener() {

    }

    @OnClick({  R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
