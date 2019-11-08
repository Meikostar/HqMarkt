package com.hqmy.market.view.mainfragment.consume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.ConfigDto;
import com.hqmy.market.bean.FirstClassItem;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.bean.SecondClassItem;
import com.hqmy.market.bean.TagBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ScreenUtils;
import com.hqmy.market.view.adapter.EntityStoreAdapter;
import com.hqmy.market.view.adapter.FirstClassAdapter;
import com.hqmy.market.view.adapter.LoopViewPagerAdapter;
import com.hqmy.market.view.adapter.SecondClassAdapter;
import com.hqmy.market.view.adapter.SingleFirstClassAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;

import android.graphics.drawable.PaintDrawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

/**
 * 实体店铺
 * Created by rzb on 2019/6/16.
 */
public class EntityStoreActivity extends BaseActivity {
    @BindView(R.id.store_layout_back)
    LinearLayout store_layout_back;
    @BindView(R.id.find)
    TextView find;
    @BindView(R.id.store_ll_location)
    RelativeLayout store_ll_location;
    @BindView(R.id.tv_location)
    TextView tv_location;
    @BindView(R.id.store_vp_container)
    ViewPager store_vp_container;
    @BindView(R.id.store_ll_indicators)
    LinearLayout store_ll_indicators;
    @BindView(R.id.layout_all)
    RelativeLayout layout_all;
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.iv_all)
    ImageView iv_all;
    @BindView(R.id.layout_near)
    RelativeLayout layout_near;
    @BindView(R.id.tv_near)
    TextView tv_near;
    @BindView(R.id.iv_near)
    ImageView iv_near;
    @BindView(R.id.entity_store_push_recy)
    MaxRecyclerView entity_store_push_recy;
    private EntityStoreAdapter   mEntityStoreAdapter;
    private List<RecommendListDto> storeLists = new ArrayList<RecommendListDto>();
    //使用PopupWindow只显示一级分类
    private PopupWindow          levelsAllPopupWindow;
    //只显示一个ListView
    private ListView             singleLeftLV;
    //分类数据
    private List<FirstClassItem> singleFirstList = new ArrayList<FirstClassItem>();;
    //使用PopupWindow显示一级分类和二级分类
    private PopupWindow levelsPopupWindow;
    //左侧和右侧两个ListView
    private ListView leftLV, rightLV;
    //左侧一级分类的数据
    private List<FirstClassItem>  firstList = new ArrayList<FirstClassItem>();
    //右侧二级分类的数据
    private List<SecondClassItem> secondList = new ArrayList<SecondClassItem>();;

    private LoopViewPagerAdapter loopViewPagerAdapter;
    private List<BannerItemDto> adsList = new ArrayList<BannerItemDto>();
    public static final int GETCITY = 1001;
    private String sitId = null;
    private String categoryId = null;
    private String areaId = null;
    private String distance = null;


    @Override
    public int getLayoutId() {
        return R.layout.activity_entity_store;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        initAdapter();
        getTopBanner();
        getConfigs();
        getTagsList();
        getLocation();
        getShopList();
    }

    @Override
    public void initListener() {
        bindClickEvent(store_layout_back, () -> {
            finish();
        });

        bindClickEvent(find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "st");
            gotoActivity(ProductSearchActivity.class,false, bundle);
        });

        bindClickEvent(store_ll_location, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStore");
            gotoActivity(SelectCityActivity.class,false, bundle, GETCITY);
        });

        bindClickEvent(layout_all, () -> {
            if (levelsPopupWindow.isShowing()) {
                levelsPopupWindow.dismiss();
                iv_near.setImageResource(R.mipmap.icon_arrow_up);
                tv_near.setTextColor(getResources().getColor(R.color.my_color_black));
            }
            iv_all.setImageResource(R.mipmap.icon_arrow_down);
            tv_all.setTextColor(getResources().getColor(R.color.my_color_E10020));
            levelsAllPopupWindow.showAsDropDown(findViewById(R.id.store_div_line_three));
            levelsAllPopupWindow.setAnimationStyle(-1);
        });

        bindClickEvent(layout_near, () -> {
            if (levelsAllPopupWindow.isShowing()) {
                levelsAllPopupWindow.dismiss();
                iv_all.setImageResource(R.mipmap.icon_arrow_up);
                tv_all.setTextColor(getResources().getColor(R.color.my_color_black));
            }
            iv_near.setImageResource(R.mipmap.icon_arrow_down);
            tv_near.setTextColor(getResources().getColor(R.color.my_color_E10020));
            levelsPopupWindow.showAsDropDown(findViewById(R.id.store_div_line_three));
            levelsPopupWindow.setAnimationStyle(-1);
        });
    }

    private void getTopBanner(){
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getPhysical_store_top();
                        adsList.addAll(bannerList);
                        loopViewPagerAdapter.setList(adsList);
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        },"physical_store_top");
    }

    private void getConfigs() {
        //showLoadDialog();
        DataManager.getInstance().getConfigs(new DefaultSingleObserver<HttpResult<ConfigDto>>() {
            @Override
            public void onSuccess(HttpResult<ConfigDto> result) {
                //dissLoadDialog();
                if (result != null) {
                  if(result.getData() != null) {
                      List<String> strLists = result.getData().getSt_search_distance();
                      for (int i = 0; i < strLists.size(); i++) {
                          SecondClassItem secondClassItem = new SecondClassItem();
                          secondClassItem.setName(strLists.get(i));
                          secondList.add(secondClassItem);
                      }
                   }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void getTagsList() {
        //showLoadDialog();
        DataManager.getInstance().getTagsList(new DefaultSingleObserver<TagBean>() {
            @Override
            public void onSuccess(TagBean result) {
                //dissLoadDialog();
                if (result != null) {
                    List<String> strLists = result.getSt();
                    for (int i = 0; i < strLists.size(); i++) {
                        FirstClassItem firstClassItem = new FirstClassItem();
                        firstClassItem.setName(strLists.get(i));
                        singleFirstList.add(firstClassItem);
                    }
                }
//                initLevelsAllPopup();
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

     private void getLocation(){
        //showLoadDialog();
        DataManager.getInstance().getLocation(new DefaultSingleObserver<HttpResult<AreaDto>>() {
            @Override
            public void onSuccess(HttpResult<AreaDto> result) {
                //dissLoadDialog();
                if(result != null){
                  if(result.getData() != null){
                      String cityName = result.getData().getName();
                      tv_location.setText(cityName);
                      sitId = String.valueOf(result.getData().getId());
                      getArea(sitId);
                   }
                }
            }

            @Override
            public void onError(Throwable throwable) {
               //dissLoadDialog();
            }
        });
     }

     private void getArea(String parendId) {
        //showLoadDialog();
        Map<String,String> map = new HashMap<String,String>();
        map.put("parent_id", parendId);
        DataManager.getInstance().getAreaList(new DefaultSingleObserver<List<AreaDto>>() {
            @Override
            public void onSuccess(List<AreaDto> areaDtos) {
                //dissLoadDialog();
                if(areaDtos != null){
                    for (int i = 0; i < areaDtos.size(); i++) {
                        FirstClassItem firstClassItem = new FirstClassItem();
                        firstClassItem.setId(areaDtos.get(i).getId());
                        firstClassItem.setName(areaDtos.get(i).getName());
                        firstClassItem.setSecondList(secondList);
                        firstList.add(firstClassItem);
                    }
                }
                initLevelsNearPopup();
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
         }, map);
       }


    private void getShopList() {
        //showLoadDialog();
        DataManager.getInstance().getShopList(new DefaultSingleObserver<HttpResult<List<RecommendListDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<RecommendListDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if(result.getData() != null) {
                        List<RecommendListDto> recommendListDtoList = result.getData();
                        storeLists.addAll(recommendListDtoList);
                        mEntityStoreAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        },"st", "1963", "1200", "22.53784", "113.96334", "distance");
    }

    private void initAdapter(){
        loopViewPagerAdapter = new LoopViewPagerAdapter(EntityStoreActivity.this, store_vp_container, store_ll_indicators);
        store_vp_container.setAdapter(loopViewPagerAdapter);
        loopViewPagerAdapter.setList(adsList);
        store_vp_container.addOnPageChangeListener(loopViewPagerAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(EntityStoreActivity.this);
        entity_store_push_recy.setLayoutManager(layoutManager);
        entity_store_push_recy.addItemDecoration(new CustomDividerItemDecoration(EntityStoreActivity.this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_f5f5f5_1));
        mEntityStoreAdapter = new EntityStoreAdapter(storeLists,this);
        entity_store_push_recy.setAdapter(mEntityStoreAdapter);
    }

    private void initLevelsAllPopup(){
//        levelsAllPopupWindow = new PopupWindow(this);
//    View view = LayoutInflater.from(this).inflate(R.layout.levels_all_popup_layout, null);
////    singleLeftLV = (ListView) view.findViewById(R.id.pop_listview_single_left);
//
//        levelsAllPopupWindow.setContentView(view);
//        levelsAllPopupWindow.setBackgroundDrawable(new PaintDrawable());
//        levelsAllPopupWindow.setFocusable(false);
//        levelsAllPopupWindow.setHeight(ScreenUtils.getScreenH(this) * 1 / 2);
//        levelsAllPopupWindow.setWidth(ScreenUtils.getScreenW(this));
//        levelsAllPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//        @Override
//        public void onDismiss() {
//            singleLeftLV.setSelection(0);
//        }
//    });
//    //加载一级分类
//    SingleFirstClassAdapter singleFirstAdapter = new SingleFirstClassAdapter(this, singleFirstList);
//        singleLeftLV.setAdapter(singleFirstAdapter);
//        singleLeftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            levelsAllPopupWindow.dismiss();
//
//        }
//    });
}


    private void initLevelsNearPopup() {
//        levelsPopupWindow = new PopupWindow(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.levels_popup_layout, null);
//        leftLV = (ListView) view.findViewById(R.id.pop_listview_left);
//        rightLV = (ListView) view.findViewById(R.id.pop_listview_right);
//
//        levelsPopupWindow.setContentView(view);
//        levelsPopupWindow.setBackgroundDrawable(new PaintDrawable());
//        levelsPopupWindow.setFocusable(false);
//        levelsPopupWindow.setHeight(ScreenUtils.getScreenH(this) * 1 / 2);
//        levelsPopupWindow.setWidth(ScreenUtils.getScreenW(this));
//        levelsPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                leftLV.setSelection(0);
//                rightLV.setSelection(0);
//            }
//        });
//        //为了方便扩展，这里的两个ListView均使用BaseAdapter.如果分类名称只显示一个字符串，建议改为ArrayAdapter.
//        //加载一级分类
//        final FirstClassAdapter firstAdapter = new FirstClassAdapter(this, firstList);
//        leftLV.setAdapter(firstAdapter);
//        //加载左侧第一行对应右侧二级分类
//        secondList = new ArrayList<SecondClassItem>();
//        secondList.addAll(firstList.get(0).getSecondList());
//        final SecondClassAdapter secondAdapter = new SecondClassAdapter(this, secondList);
//        rightLV.setAdapter(secondAdapter);
//        //左侧ListView点击事件
//        leftLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //二级数据
//                List<SecondClassItem> list2 = firstList.get(position).getSecondList();
//                //如果没有二级类，则直接跳转
//                if (list2 == null || list2.size() == 0) {
//                    levelsPopupWindow.dismiss();
//                    int firstId = firstList.get(position).getId();
//                    String selectedName = firstList.get(position).getName();
//                    handleResult(firstId, -1, selectedName);
//                    return;
//                }
//                FirstClassAdapter adapter = (FirstClassAdapter) (parent.getAdapter());
//                //如果上次点击的就是这一个item，则不进行任何操作
//                if (adapter.getSelectedPosition() == position){
//                    return;
//                }
//                //根据左侧一级分类选中情况，更新背景色
//                adapter.setSelectedPosition(position);
//                adapter.notifyDataSetChanged();
//                //显示右侧二级分类
//                updateSecondListView(list2, secondAdapter);
//            }
//        });
//
//        //右侧ListView点击事件
//        rightLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //关闭popupWindow，显示用户选择的分类
//                levelsPopupWindow.dismiss();
//
//                int firstPosition = firstAdapter.getSelectedPosition();
//                int firstId = firstList.get(firstPosition).getId();
//                int secondId = firstList.get(firstPosition).getSecondList().get(position).getId();
//                String selectedName = firstList.get(firstPosition).getSecondList().get(position)
//                        .getName();
//                handleResult(firstId, secondId, selectedName);
//            }
//        });
    }

    //刷新右侧ListView
    private void updateSecondListView(List<SecondClassItem> list2,SecondClassAdapter secondAdapter) {
        secondList.clear();
        secondList.addAll(list2);
        secondAdapter.notifyDataSetChanged();
    }

    //处理点击结果
    private void handleResult(int firstId, int secondId, String selectedName){
        //String text = "first id:" + firstId + ",second id:" + secondId;
        //Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        //mainTab1TV.setText(selectedName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GETCITY:
                    //CityDto city= (CityDto) data.getExtras().getSerializable("city");
                    //if(city!= null) {
                    //    String cityName = city.getCityName();
                    //    tv_location.setText(cityName.substring(0,cityName.length()-1));
                    //}
                    String cityName = data.getExtras().getString("city");
                    tv_location.setText(cityName);
                    break;
              }
         }
    }
}
