package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.view.fragments.CollectCommunityFragment;
import com.hqmy.market.view.fragments.CollectGoodsFragment;
import com.hqmy.market.view.mainfragment.learn.CollectViewPagerAdapter;
import com.hqmy.market.view.widgets.autoview.AutoViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏
 */
public class CollectActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.stb_tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    AutoViewPager mViewPager;
    private String[] titles = {"商品", "社区"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }

    @Override
    public void initView() {
        mTitleText.setText("收藏");
        mViewPager.setScanScroll(false);
        ArrayList<Fragment> fragments = new ArrayList<>();
//        fragments.add(new CollectStoreFragment());
        fragments.add(new CollectGoodsFragment());
        fragments.add(new CollectCommunityFragment());
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
