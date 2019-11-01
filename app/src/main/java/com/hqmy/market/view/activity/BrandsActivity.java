package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.BaseDto2;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.adapter.BrandBaseAdapter;
import com.hqmy.market.view.adapter.FragmentViewPagerAdapter;
import com.hqmy.market.view.adapter.TestAdapter;
import com.hqmy.market.view.fragments.BrandFragment;
import com.hqmy.market.view.fragments.SpikeFragment;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;
import com.hqmy.market.view.widgets.NoScrollViewPager;
import com.hqmy.market.view.widgets.autoview.ActionbarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 *品牌
 */
public class BrandsActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView            customActionBar;
    @BindView(R.id.auto_scroll)
    AutoLocateHorizontalView autoScroll;

    @BindView(R.id.viewpager_main)
    NoScrollViewPager viewpagerMain;
    private int currentPage = Constants.PAGE_NUM;

    @Override
    public void initListener() {

    }
   private int state;
    @Override
    public int getLayoutId() {
        return R.layout.ui_brands_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private BrandBaseAdapter testAdapter;
    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("品牌馆");
        actionbar.setTitleColor(R.color.my_color_212121);
        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });

        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
             autoScroll.moveToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        StatusBarUtils.StatusBarLightMode(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setHasFixedSize(true);
        autoScroll.setLayoutManager(linearLayoutManager);
        autoScroll.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
                viewpagerMain.setCurrentItem(pos, false);


            }
        });

        testAdapter = new BrandBaseAdapter();
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
        testAdapter.setItemClick(new BrandBaseAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, BaseDto2 data) {
                autoScroll.moveToPosition(poition);
            }
        });
    }



    @Override
    public void initData() {

        findOneCategory();



    }

    private void findOneCategory() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("no_tree", ""+1);
        map.put("parent_id", "0");
        map.put("filter[scopeHasMallBrands]", ""+1);

        DataManager.getInstance().findOtherCategory(new DefaultSingleObserver<HttpResult<List<BaseDto2>>>() {
            @Override
            public void onSuccess(HttpResult<List<BaseDto2>> datas) {
                data.clear();
                //                LogUtil.i(TAG, "--RxLog-Thread: onSuccess() = " + data);

                if(datas!=null){

                    BaseDto2 bannerDto = new BaseDto2();
                    bannerDto.title="全部";
                    bannerDto.id="-1";

                    data.add(bannerDto);
                    data.addAll(datas.getData());
                    BaseDto2 bannerDto1 = new BaseDto2();
                    bannerDto1.title="";
                    bannerDto1.id="-2";
                    data.add(bannerDto1);
                    testAdapter.setDatas(data);
                    testAdapter.notifyDataSetChanged();
                    initFragMents();
                }

                dissLoadDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }

            }
        },map);
    }
    private List<BaseDto2> data=new ArrayList<>();

    private String[]                 titles;
    private List<Fragment>           list_productfragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private void initFragMents() {
        list_productfragment = new ArrayList<>();
       for (BaseDto2 dataDto:data){
           BrandFragment spikeFragment = new BrandFragment();
           spikeFragment.setId(dataDto.id);
           list_productfragment.add(spikeFragment);
       }

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list_productfragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(data.size() - 1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(0);

    }


}
