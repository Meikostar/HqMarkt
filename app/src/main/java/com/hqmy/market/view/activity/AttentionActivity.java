package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.view.fragments.AttentionCommunityFragment;
import com.hqmy.market.view.fragments.AttentionStoreFragment;
import com.hqmy.market.view.mainfragment.learn.CollectViewPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关注
 */
public class AttentionActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager        mViewPager;
    private String[] titles = {"店铺",  "社区"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        mTitleText.setText("关注");

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new AttentionStoreFragment());
        fragments.add(new AttentionCommunityFragment());
        mViewPager.setAdapter(new CollectViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setViewPager(mViewPager, titles);
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
