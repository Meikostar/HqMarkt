package com.hqmy.market.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.BaseDto1;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.FragmentViewPagerAdapter;
import com.hqmy.market.view.adapter.TestAdapter;
import com.hqmy.market.view.fragments.HotFragment;
import com.hqmy.market.view.fragments.SpikeFragment;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;
import com.hqmy.market.view.widgets.NoScrollViewPager;
import com.hqmy.market.view.widgets.autoview.ActionbarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.hqmy.market.R.mipmap.black_message;

/**
 *
 */
public class HotActivity extends BaseActivity {


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
        return R.layout.ui_spike_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private TestAdapter testAdapter;
    private boolean     isShow;
    private BaseDto    spikeDto;
    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("热门产品");
        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        autoScroll.setBackgroundColor(getResources().getColor(R.color.my_color_ql));
        viewpagerMain.setBackgroundColor(getResources().getColor(R.color.my_color_9f));
        actionbar.setTitleColor(R.color.my_color_212121);
        spikeDto= (BaseDto) getIntent().getSerializableExtra("data");
        state=getIntent().getIntExtra("state",0);
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(isFirst){
                    autoScroll.moveToPosition(position);
                }else {
                    isFirst=true;
                }

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

        testAdapter = new TestAdapter();
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
        testAdapter.setItemClick(new TestAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, BannerDto data) {
                autoScroll.moveToPosition(poition);
            }
        });
    }

    private boolean isFirst;

    @Override
    public void initData() {

        getHomeCategorie();



    }
    private List<BannerDto> data=new ArrayList<>();
    private void getHomeCategorie() {
        //showLoadDialog();
        Map<String,String> map = new HashMap<>();
//        map.put("filter[is_hot]","1");
        DataManager.getInstance().AllCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();
                data.clear();
                if (result != null&&result.getData()!=null) {
                    BannerDto bannerDto = new BannerDto();
                    bannerDto.title="全部";
                    bannerDto.id="-1";
                    data.add(bannerDto);
                    data.addAll(result.getData());
                    testAdapter.setDatas(data);
                    testAdapter.notifyDataSetChanged();
                    initFragMents(state);

                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        },map);
    }
    private String[]                 titles;
    private List<Fragment>           list_productfragment;   //定义要装fragment的列表
    private FragmentViewPagerAdapter mainViewPagerAdapter;
    private void initFragMents(int poistion) {
        list_productfragment = new ArrayList<>();
        for (BannerDto dataDto:data){
            HotFragment spikeFragment = new HotFragment();
            spikeFragment.setId(dataDto.id);
            list_productfragment.add(spikeFragment);
        }

        mainViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), list_productfragment);
        viewpagerMain.setAdapter(mainViewPagerAdapter);
        viewpagerMain.setOffscreenPageLimit(data.size() - 1);//设置缓存view 的个数
        viewpagerMain.setCurrentItem(poistion);

    }


}
