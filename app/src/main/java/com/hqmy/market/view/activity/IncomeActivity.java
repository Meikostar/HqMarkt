package com.hqmy.market.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.view.fragments.Incomeragment;
import com.hqmy.market.view.widgets.autoview.TabEntity;

import java.util.ArrayList;

public class IncomeActivity extends BaseActivity {
    private static final String TAG = IncomeActivity.class.getSimpleName();

    private String[]                   mSubTitles      = {"全部", "收入", "支出"};
    private ArrayList<CustomTabEntity> mSubTabEntities = new ArrayList<>();
    Incomeragment fragments[] = new Incomeragment[3];
    private String fragments_name[] = {"all", "inCome", "outCome"};

    int currentTabIndex = -1;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_income_layout;
    }

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        StatusBarUtils.StatusBarLightMode(this);
        initTab();
    }

    @Override
    public void initData() {

        switchFragment(0);
    }

    private void initTab() {
        for (int i = 0; i < mSubTitles.length; i++) {
            mSubTabEntities.add(new TabEntity(mSubTitles[i], R.mipmap.icon_arrow, R.mipmap.icon_arrow));
        }

    }

    /**
     * 切换页面
     *
     * @param index
     */
    private void switchFragment(int index) {
        if (currentTabIndex == index) {
            return;
        }
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        if (fragments[index] == null) {
            fragments[index] = new Incomeragment();
            trx.add(R.id.content_layout, fragments[index], fragments_name[index]);
        }
        if (index == 0) {
            fragments[index].setType("");
        } else if (index == 1) {
            fragments[index].setType("1");
        } else if (index == 2) {
            fragments[index].setType("2");
        }
        if (currentTabIndex != -1) {
            trx.hide(fragments[currentTabIndex]);
        }
        trx.show(fragments[index]).commit();
        currentTabIndex = index;
    }




}
