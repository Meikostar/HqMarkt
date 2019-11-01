package com.hqmy.market.view.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.view.mainfragment.learn.OnLineLiveFragment;
import com.hqmy.market.view.widgets.autoview.AutoViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MoreHotLiveActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener {
    private static final String TAG = MoreHotLiveActivity.class.getSimpleName();
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    private AutoViewPager  mViewPager;
    private Context        mContext;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //会话列表的fragment






    @Override
    public int getLayoutId() {
        return R.layout.activity_more_hot_live;
    }

    private             int    page_index = 0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字

    @Override
    public void initView() {
        mContext = this;
        mTitleText.setText("热门直播");
        StatusBarUtils.StatusBarLightMode(this);
        mViewPager = findViewById(R.id.main_viewpager);
        initMainViewPager();
        mViewPager.setCurrentItem(page_index, false);

    }

    @Override
    public void initData() {



    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initMainViewPager() {
        OnLineLiveFragment onLineLiveFragment = new OnLineLiveFragment();
        onLineLiveFragment.setState(1);
        fragments.add(onLineLiveFragment);

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }





}

