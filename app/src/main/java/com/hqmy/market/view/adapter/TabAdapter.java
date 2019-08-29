package com.hqmy.market.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by rzb on 2019/4/18
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private List<Fragment>  fragments;
    private List<String>    tabTitle;
    private FragmentManager fm;

    public TabAdapter(FragmentManager fm, List<Fragment> fragments, List<String> tabTitle) {
        super(fm);
        this.fragments = fragments;
        this.tabTitle = tabTitle;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position % fragments.size());
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void setFragments(List fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            ft = null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    //设置tablayout标题
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position);
    }

}