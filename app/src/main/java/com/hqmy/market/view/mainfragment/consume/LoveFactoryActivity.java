package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.FactoryTypeAapter;
import com.hqmy.market.view.adapter.FactotyBrandListAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

/**
 * Created by rzb on 2019/4/22.
 * 爱心厂家
 */
public class LoveFactoryActivity extends BaseActivity {
    @BindView(R.id.factory_iv_back)
    ImageView  factory_iv_back;
    @BindView(R.id.iv_banner_factory)
    ImageView iv_banner_factory;
    @BindView(R.id.recycle_factory_type)
    RecyclerView recycle_factory_type;
    @BindView(R.id.recycle_brand)
    NoScrollListView recycle_brand;
    @BindView(R.id.recycle_consume_goods)
    RecyclerView recycle_consume_goods;
    @BindView(R.id.factory_iv_cart)
    ImageView factory_iv_cart;
    @BindView(R.id.factory_tv_find)
    TextView factory_tv_find;
    @BindView(R.id.factory_consume_srl)
    SmartRefreshLayout factory_consume_srl;

    private FactoryTypeAapter ftAdapter;
    private List<CategoryListdto> categoryLists = new ArrayList<CategoryListdto>();
    private FactotyBrandListAdapter fbListAdapter;
    private List<RecommendListDto> brandList = new ArrayList<RecommendListDto>();
    private ConsumePushAdapter mConsumePushAdapter;
    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();
    private List<CategoryListdto> categoryListdtoList = null;
    private ArrayList<CategoryListdto> allCateList = new ArrayList<>();
    private int mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_love_factory;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        getBanner();
        getFamilyTypes();
        getRecommendList();
        findGoodLists();
    }

    @Override
    public void initListener() {
        bindClickEvent(factory_iv_back, () -> {
            finish();
        });

        bindClickEvent(factory_tv_find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "gc");
            gotoActivity(ProductSearchActivity.class,false, bundle);
        });

        bindClickEvent(factory_iv_cart, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(ShopCartActivity.MALL_TYPE, "gc");
            gotoActivity(ShopCartActivity.class, false, bundle);
        });

        factory_consume_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                findGoodLists();
            }
        });

        factory_consume_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                findGoodLists();
            }
        });
    }

    private void initAdapter(){
        ftAdapter = new FactoryTypeAapter(categoryLists, allCateList, LoveFactoryActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LoveFactoryActivity.this,LinearLayoutManager.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        recycle_factory_type.setLayoutManager(layoutManager);
        recycle_factory_type.setAdapter(ftAdapter);

        fbListAdapter = new FactotyBrandListAdapter(this, brandList);
        recycle_brand.setAdapter(fbListAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mConsumePushAdapter = new ConsumePushAdapter(goodsLists,this);
        recycle_consume_goods.addItemDecoration(new RecyclerItemDecoration(3, 2));
        recycle_consume_goods.setLayoutManager(gridLayoutManager);
        recycle_consume_goods.setAdapter(mConsumePushAdapter);
    }

    private void getBanner(){
        showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getUnion_mall_top();
                        if(bannerList != null) {
                            GlideUtils.getInstances().loadNormalImg(LoveFactoryActivity.this, iv_banner_factory,
                                    Constants.WEB_IMG_URL_UPLOADS + bannerList.get(0).getPath());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        },"love_factory_top");
    }

    private void getFamilyTypes(){
        showLoadDialog();
        DataManager.getInstance().getCategoryList(new DefaultSingleObserver<HttpResult<List<CategoryListdto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CategoryListdto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        CategoryListdto clHomeDto = new CategoryListdto();
                        clHomeDto.setTitle("首页");
                        CategoryListdto clMoreDto = new CategoryListdto();
                        clMoreDto.setTitle("更多");
                        categoryLists.add(clHomeDto);
                        categoryListdtoList = result.getData();
                        if(categoryListdtoList.size() > 5){
                            categoryLists.addAll(categoryListdtoList.subList(0,5));
                        }else{
                            categoryLists.addAll(result.getData());
                        }
                        categoryLists.add(clMoreDto);
                        allCateList.addAll(categoryListdtoList);
                        ftAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "gc");
    }

    private void getRecommendList(){
        showLoadDialog();
        DataManager.getInstance().getRecommendList(new DefaultSingleObserver<HttpResult<List<RecommendListDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<RecommendListDto>> result) {
                dissLoadDialog();
                if(result != null){
                  if(result.getData() != null){
                      brandList.addAll(result.getData());
                      fbListAdapter.notifyDataSetChanged();
                  }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "gc");
    }

    private void findGoodLists(){
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("page", mPage + "");
        DataManager.getInstance().findGoodLists(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                setData(result);
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "gc", map);
    }

    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        if (mPage <= 1) {
            goodsLists.clear();
            goodsLists.addAll(httpResult.getData());
            mConsumePushAdapter.notifyDataSetChanged();
            if(httpResult.getData() == null || httpResult.getData().size() == 0); {
            }
            factory_consume_srl.finishRefresh();
            factory_consume_srl.setEnableLoadMore(true);
        } else {
            factory_consume_srl.finishLoadMore();
            factory_consume_srl.setEnableRefresh(true);
            goodsLists.addAll(httpResult.getData());
            mConsumePushAdapter.notifyDataSetChanged();
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                factory_consume_srl.finishLoadMoreWithNoMoreData();
            }
        }
    }
}
