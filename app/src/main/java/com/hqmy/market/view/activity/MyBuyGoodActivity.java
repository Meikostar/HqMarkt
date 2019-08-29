package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.view.fragments.MyBuyGoodsFragment;
import com.hqmy.market.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我买到的
 */
public class MyBuyGoodActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.vp_action)
    ViewPager mViewPager;
    private int mType;
    private List<String> titles = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_buy_good;
    }

    @Override
    public void initView() {
        mType = getIntent().getIntExtra("type", 0);
        tvTitleText.setText(mType == 0 ? "我买到的" : "我卖出的");
        titles.add("全部");
        titles.add("待付款");
        titles.add("待发货");
        if (mType == 0){
            titles.add("待收货");
        }
    }

    @Override
    public void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyBuyGoodsFragment(Constants.TYPE_ALL,mType));
        fragments.add(new MyBuyGoodsFragment(Constants.TYPE_PAYMENT,mType));
        fragments.add(new MyBuyGoodsFragment(Constants.TYPE_DELIVERY,mType));
        if (mType == 0){
            fragments.add(new MyBuyGoodsFragment(Constants.TYPE_BUY_RECEIVE,mType));
        }
        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles.toArray(new String[titles.size()]));
    }

    @Override
    public void initListener() {
    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
