package com.hqmy.market.view.mainfragment.learn;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import java.util.ArrayList;
import butterknife.BindView;

/**
 * 活动
 */
public class ActionFragment extends BaseFragment {
    @BindView(R.id.stb_tab_layout)
    SegmentTabLayout mTabLayout;
    @BindView(R.id.vp_action)
    ViewPager  mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_action;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        String[] titles = {"校内活动", " 校外活动","已报名"};
        mTabLayout.setTabData(titles);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ActionInSchoolFragment("0"));
        fragments.add(new ActionInSchoolFragment("1"));
        fragments.add(new ActionInSchoolFragment("2"));
        mViewPager.setAdapter(new LearnActionViewPagerAdapter(getChildFragmentManager(), fragments));
    }

    @Override
    protected void initListener() {
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
