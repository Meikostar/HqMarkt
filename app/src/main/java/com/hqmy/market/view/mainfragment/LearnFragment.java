package com.hqmy.market.view.mainfragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.lwkandroid.imagepicker.utils.ImagePickerComUtils;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.view.mainfragment.learn.ActionFragment;
import com.hqmy.market.view.mainfragment.learn.CourseWarehouseFragment;
import com.hqmy.market.view.mainfragment.learn.LearnViewPagerAdapter;
import com.hqmy.market.view.mainfragment.learn.OnLineLiveFragment;

import java.util.ArrayList;
import butterknife.BindView;

/**
 * 学习
 * Created by rzb on 2019/04/18.
 */
public class LearnFragment extends BaseFragment {

    @BindView(R.id.stl_learn_indicator)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.vp_learn_container)
    ViewPager  mViewPager;
    @BindView(R.id.view_state)
    View  mViewState;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_learn;
    }

    @Override
    protected void initView() {
        mViewState.getLayoutParams().height = ImagePickerComUtils.getStatusBarHeight(getActivity());
    }

    @Override
    protected void initData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new OnLineLiveFragment());
        fragments.add(new CourseWarehouseFragment());
        fragments.add(new ActionFragment());

        mViewPager.setAdapter(new LearnViewPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
        String[] titles = {"在线直播", "课件库", "活动"};
        mSlidingTabLayout.setViewPager(mViewPager, titles);
    }

    @Override
    protected void initListener() {

    }
}
