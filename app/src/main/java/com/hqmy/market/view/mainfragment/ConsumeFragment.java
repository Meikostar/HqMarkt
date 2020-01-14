package com.hqmy.market.view.mainfragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CategorieBean;
import com.hqmy.market.bean.HeadLineDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.activity.BrandsActivity;
import com.hqmy.market.view.activity.ConturyActivity;
import com.hqmy.market.view.activity.HotActivity;
import com.hqmy.market.view.activity.MessageCenterActivity;
import com.hqmy.market.view.activity.SpikeActivity;
import com.hqmy.market.view.activity.WebUtilsActivity;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.adapter.Homedapter;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.mainfragment.consume.ProductSearchActivity;
import com.hqmy.market.view.mainfragment.consume.SelectCityActivity;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 消费
 * Created by rzb on 2019/04/18.
 */
public class ConsumeFragment extends BaseFragment {
    @BindView(R.id.topLayout)
    LinearLayout       topLayout;
    @BindView(R.id.tv_location)
    TextView           tv_location;
    @BindView(R.id.find)
    TextView           find;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.consume_scrollView)
    CustomView         consume_scrollView;
    @BindView(R.id.header)
    MaterialHeader     header;
    @BindView(R.id.banner)
    Banner             banner;

    Unbinder unbinder;
    @BindView(R.id.grid_content)
    NoScrollGridView gridContent;
    @BindView(R.id.card)
    CardView         card;
    @BindView(R.id.rl_vp_container)
    RelativeLayout   rlVpContainer;
    @BindView(R.id.iv_one)
    ImageView        ivOne;
    @BindView(R.id.iv_two)
    ImageView        ivTwo;
    @BindView(R.id.iv_three)
    ImageView        ivThree;
    @BindView(R.id.iv_four)
    ImageView        ivFour;
    @BindView(R.id.iv_five)
    ImageView        ivFive;
    @BindView(R.id.consume_push_recy)
    RecyclerView     consumePushRecy;
    @BindView(R.id.iv_location)
    ImageView        ivLocation;
    @BindView(R.id.iv_scan)
    ImageView        ivScan;
    @BindView(R.id.iv_message)
    ImageView        ivMessage;


    private List<RecommendListDto> brandList    = new ArrayList<RecommendListDto>();
    private ConsumePushAdapter     mConsumePushAdapter;
    private List<NewListItemDto>   goodsLists   = new ArrayList<NewListItemDto>();
    private ArrayList<HeadLineDto> lineLists    = new ArrayList<HeadLineDto>();
    private List<String>           lineStrLists = new ArrayList<String>();
    private int                    mPage        = 1;
    private int                    hPage        = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_consume;
    }

    @Override
    protected void initView() {

        header.setColorSchemeResources(R.color.my_color_head1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mHomedapter=new Homedapter(getActivity());
        gridContent.setAdapter(mHomedapter);

    }
    private Homedapter mHomedapter;
    @Override
    protected void initData() {



        refreshLayout.autoRefresh();
        initViewPager();
        getLocation();
        getHomeCategorie();
        getTopBanner();
        getFourBanner();
        getHeadLines();
//        getHomeRecommendList();
//        findGoodLists();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        bindClickEvent(tv_location, () -> {

            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStore");
            gotoActivity(SelectCityActivity.class, false, bundle);
        });
        bindClickEvent(ivLocation, () -> {

            Bundle bundle = new Bundle();
            bundle.putString("from", "entityStore");
            gotoActivity(SelectCityActivity.class, false, bundle);
        });

        bindClickEvent(find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "consume_index");
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });

        //        bindClickEvent(ll_add, () -> {
        //            HomeMorePopWindow morePopWindow = new HomeMorePopWindow(this.getActivity());
        //            morePopWindow.showPopupWindow(ll_add);
        //        });
        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });

        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SpikeActivity.class));
            }
        });
        ivFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),HotActivity.class));
            }
        });
        ivThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ConturyActivity.class));
            }
        });
        ivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),BrandsActivity.class));
            }
        });

        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 启动扫一扫
                 */

                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                ZxingConfig config = new ZxingConfig();
                config.setReactColor(R.color.my_color_009AFF);//设置扫描框四个角的颜色 默认为白色
                config.setScanLineColor(R.color.my_color_009AFF);//设置扫描线的颜色 默认白色
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                config.setShowbottomLayout(false);
                startActivityForResult(intent, Constants.HOME_REQUEST_CODE_SCAN);

            }
        });
        ;


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                findGoodLists();
            }
        });


        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                findGoodLists();
            }
        });

        /**
         * 滑动时标题栏渐变
         */
        consume_scrollView.setScrollViewListener(new CustomView.ScrollViewListener() {
            @Override
            public void onScrollChanged(CustomView customView, int x, int y, int oldx, int oldy) {
                int toolbarHeight = topLayout.getHeight();
                if (y <= 0) {
                    topLayout.setBackgroundColor(Color.argb((int) 0, 26, 47, 176));//AGB由相关工具获得，或者美工提供
                } else if (y > 0 && y <= toolbarHeight) {
                    //获取ScrollView向下滑动图片消失的比例
                    float scale = (float) y / toolbarHeight;
                    //更加这个比例,让标题颜色由浅入深
                    float alpha = (255 * scale);
                    // 只是layout背景透明
                    topLayout.setBackgroundColor(Color.argb((int) alpha, 26, 47, 176));
                }
            }
        });


        BroadcastManager.getInstance(getActivity()).addAction(Constants.CHOICE_CITY, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String cName = intent.getStringExtra("String");
                    tv_location.setText(cName);
                }
            }
        });

        getProductTopBanner();
    }

    private void initViewPager() {


        mConsumePushAdapter = new ConsumePushAdapter(goodsLists, getActivity());
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(6, 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mConsumePushAdapter);

    }


    private void getLocation() {
        //showLoadDialog();
        DataManager.getInstance().getLocation(new DefaultSingleObserver<HttpResult<AreaDto>>() {
            @Override
            public void onSuccess(HttpResult<AreaDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        String cityName = result.getData().getName();
                        tv_location.setText(cityName);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }
    private void getHomeCategorie() {
        //showLoadDialog();
        DataManager.getInstance().banCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();
                if (result != null&&result.getData()!=null) {
                  mHomedapter.setData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();

            }
        });
    }
    private void getTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getIndex_top();
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_top");
    }
    private void getProductTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().index_product_list_top;
                        if(bannerList!=null){
                            ban=bannerList.get(0);
                            String imgStr = ban.getPath();
                            if (imgStr != null) {
                                if (imgStr.contains("http://")) {
                                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivFive, 3,imgStr, R.mipmap.img_default_2);
                                } else {
                                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivFive, 3,Constants.WEB_IMG_URL_UPLOADS + imgStr, R.drawable.home_5);
                                }
                            }
                            ivFive.setOnClickListener(new View.OnClickListener() {
                                //                product_default:商品
                                //                seller_default:商家
                                @Override
                                public void onClick(View v) {
                                    //                    Intent intent = new Intent(getActivity(), WebUtilsActivity.class);
                                    //                    intent.putExtra("weburl","http://pszlgl.zhinf.net/guangming/#/pages/authorize/index");
                                    //                    intent.putExtra("webtitle","webtitle");
                                    //                    startActivity(intent);
                                    if(ban.getClick_event_type().equals("product_default")){
                                        Bundle bundle = new Bundle();
                                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, ban.getClick_event_value());
                                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                                        Intent intent = new Intent(getActivity(), CommodityDetailActivity.class);
                                        if (bundle != null) {
                                            intent.putExtras(bundle);
                                        }
                                        startActivity(intent);
                                    }else if(ban.getClick_event_type().equals("seller_default")){
                                        Intent intent = new Intent(getActivity(), BrandShopDetailActivity.class);
                                        intent.putExtra("id",ban.getClick_event_value());
                                        startActivity(intent);
                                    }
                                }
                            });
                        }


                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_product_list_top");
    }
    private BannerItemDto ban;
    private void startBanner(List<BannerItemDto> data) {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(data);
        //设置banner动画效果
        //        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerItemDto slidersDto = (BannerItemDto) path;
            String imgStr = slidersDto.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadNormalImg(getActivity(), imageView, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(getActivity(), imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_2);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), WebUtilsActivity.class);
//                    intent.putExtra("weburl","http://pszlgl.zhinf.net/guangming/#/pages/authorize/index");
//                    intent.putExtra("webtitle","webtitle");
//                    startActivity(intent);
                    if(slidersDto.getClick_event_type().equals("product_default")){
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        Intent intent = new Intent(context, CommodityDetailActivity.class);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                    }else if(slidersDto.getClick_event_type().equals("seller_default")){
                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                        intent.putExtra("id",slidersDto.getClick_event_value());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    private void getMiddleBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getIndex_middle();

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_middle");
    }
    private void getFourBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getIndex_middle();
                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivOne, 0f, Constants.WEB_IMG_URL_UPLOADS+result.getData().index_after_category_1.get(0).getPath(),R.drawable.home_1);
                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivTwo, 0f,Constants.WEB_IMG_URL_UPLOADS+ result.getData().index_after_category_2_left.get(0).getPath(),R.drawable.home_2);
                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivThree, 0f,Constants.WEB_IMG_URL_UPLOADS+ result.getData().index_after_category_2_right_top.get(0).getPath(),R.drawable.home_3);
                        GlideUtils.getInstances().loadRoundCornerImg(getActivity(), ivFour, 0f, Constants.WEB_IMG_URL_UPLOADS+result.getData().index_after_category_2_right_bottom.get(0).getPath(),R.drawable.home_4);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "index_after_category_1,index_after_category_2_left,index_after_category_2_right_top,index_after_category_2_right_bottom");
    }
    private void getHeadLines() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("page", hPage + "");
        DataManager.getInstance().getHeadLines(new DefaultSingleObserver<HttpResult<List<HeadLineDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<HeadLineDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        lineLists.addAll(result.getData());
                        for (int j = 0; j < lineLists.size(); j++) {
                            lineStrLists.add(lineLists.get(j).getTitle());
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }



    private void findGoodLists() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include",  "brand.category");
        map.put("page", mPage + "");
        DataManager.getInstance().findHomeGoodLists(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                setData(result);
                refreshLayout.finishLoadMore();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "default", map);
    }


    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }


        if (mPage <= 1) {
            mConsumePushAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mConsumePushAdapter.setEmptyView(new EmptyView(getActivity()));
            }
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
            refreshLayout.setEnableRefresh(true);
            mConsumePushAdapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                refreshLayout.finishLoadMoreWithNoMoreData();
            }
        }


    }

    @Override
    protected void dissLoadDialog() {
        super.dissLoadDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(getActivity()).destroy(Constants.CHOICE_CITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
