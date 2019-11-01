package com.hqmy.market.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.BaseDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.Areadapter;
import com.hqmy.market.view.adapter.ConturyAdapter;
import com.hqmy.market.view.adapter.ConturyCagoriadapter;
import com.hqmy.market.view.adapter.ConturyProcutAdapter;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.AutoLocateHorizontalView;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.ActionbarView;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ConturyActivity extends BaseActivity {


    @BindView(R.id.custom_action_bar)
    ActionbarView            customActionBar;
    @BindView(R.id.banner)
    Banner                   banner;
    @BindView(R.id.grid_content)
    NoScrollGridView         gridContent;
    @BindView(R.id.iv_one)
    ImageView                ivOne;
    @BindView(R.id.grid_content1)
    NoScrollGridView         gridContent1;
    @BindView(R.id.auto_scroll)
    AutoLocateHorizontalView autoScroll;
    @BindView(R.id.consume_push_recy)
    RecyclerView             consumePushRecy;
    @BindView(R.id.consume_scrollView)
    CustomView               consumeScrollView;
    @BindView(R.id.consume_srl)
    SmartRefreshLayout       refreshLayout;
    @BindView(R.id.ll_bg)
    LinearLayout             llBg;

    @Override
    public void initListener() {

    }

    private int state;

    @Override
    public int getLayoutId() {
        return R.layout.ui_contury_layout;
    }

    //    private SpikeChooseTimeAdapter testAdapter;
    private ConturyAdapter testAdapter;
    private boolean        isShow;
    private BaseDto        spikeDto;
    private Areadapter     mHomedapter;

    @Override
    public void initView() {
        actionbar.setImgStatusBar(R.color.my_color_white);
        actionbar.setTitle("国家地区馆");
        actionbar.setRightImageAction(R.mipmap.black_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });

        mHomedapter = new Areadapter(this);
        gridContent.setAdapter(mHomedapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setHasFixedSize(true);
        autoScroll.setLayoutManager(linearLayoutManager);
        autoScroll.setOnSelectedPositionChangedListener(new AutoLocateHorizontalView.OnSelectedPositionChangedListener() {
            @Override
            public void selectedPositionChanged(int pos) {
//                                viewpagerMain.setCurrentItem(pos, false);

                                if(datas!=null&&pos>=0){
                                    showLoadDialog();
                                    getConturyProducts(datas.get(pos).id+"");
                                }


            }
        });

        testAdapter = new ConturyAdapter();
        mProcutAdapter = new ConturyProcutAdapter(lists, this);
        mCagoriadapter = new ConturyCagoriadapter(this);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(3, 3));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mProcutAdapter);
        gridContent1.setAdapter(mCagoriadapter);
        testAdapter.setItemClick(new ConturyAdapter.ItemClickListener() {
            @Override
            public void itemClick(int poition, AreaDto data) {
                autoScroll.moveToPosition(poition);
            }
        });
        autoScroll.setInitPos(0);
        autoScroll.setItemCount(5);
        autoScroll.setAdapter(testAdapter);
        getTopBanner();
        getBottenBanner();
        getCategorisContury();
        getConturyProduct();
    }

    private List<AreaDto>        datas = new ArrayList<>();
    private List<NewListItemDto> lists = new ArrayList<>();
    private BannerItemDto        ban;


    private void getConturyProduct() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "category");
        map.put("filter[is_recommend]", 1 + "");
        DataManager.getInstance().getConturyProduct(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {

                        mCagoriadapter.setData(result.getData());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, map);
    }

    private ConturyProcutAdapter mProcutAdapter;
    private ConturyCagoriadapter mCagoriadapter;

    private void getConturyProducts(String id) {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("include", "category");
        map.put("filter[category_id]", id + "");
        DataManager.getInstance().getConturyProduct(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
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
        }, map);
    }

    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }

        mProcutAdapter.setNewData(httpResult.getData());
        if (httpResult.getData() == null || httpResult.getData().size() == 0) {
            mProcutAdapter.setEmptyView(new EmptyView(this));
        }
        refreshLayout.finishRefresh();


    }

    private void getCategorisContury() {
        //showLoadDialog();
        DataManager.getInstance().getCategorisContury(new DefaultSingleObserver<HttpResult<List<AreaDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AreaDto>> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        datas.clear();
                        datas.addAll(result.getData());
                        mHomedapter.setData(datas);
                        AreaDto liveCatesBean = new AreaDto();
                        liveCatesBean.title = "";
                        liveCatesBean.setId(-2);
                        datas.add(liveCatesBean);
                        testAdapter.setDatas(datas);
                        testAdapter.notifyDataSetChanged();
                        if (datas.size() > 0) {
                            getConturyProducts(datas.get(0).id + "");
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

    private void getTopBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().country_list_top;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "country_list_top");
    }

    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null && result.getData().country_list_below_country.size() > 0) {
                        List<BannerItemDto> bannerList = result.getData().country_list_below_country;
                        if (bannerList != null) {
                            ban = bannerList.get(0);
                            GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, ivOne, bannerList.get(0).getPath(), R.drawable.moren_fldb);
                            ivOne.setOnClickListener(new View.OnClickListener() {
                                //                product_default:商品
                                //                seller_default:商家
                                @Override
                                public void onClick(View v) {
                                    //                    Intent intent = new Intent(getActivity(), WebUtilsActivity.class);
                                    //                    intent.putExtra("weburl","http://pszlgl.zhinf.net/guangming/#/pages/authorize/index");
                                    //                    intent.putExtra("webtitle","webtitle");
                                    //                    startActivity(intent);
                                    if (ban.getClick_event_type().equals("product_default")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, ban.getClick_event_value());
                                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                                        Intent intent = new Intent(ConturyActivity.this, CommodityDetailActivity.class);
                                        if (bundle != null) {
                                            intent.putExtras(bundle);
                                        }
                                        startActivity(intent);
                                    } else if (ban.getClick_event_type().equals("seller_default")) {
                                        Intent intent = new Intent(ConturyActivity.this, BrandShopDetailActivity.class);
                                        intent.putExtra("id", ban.getClick_event_value());
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
        }, "country_list_below_country");
    }

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
                    GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, imageView, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(ConturyActivity.this, imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_2);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    if (slidersDto.getClick_event_type().equals("product_default")) {
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        Intent intent = new Intent(ConturyActivity.this, CommodityDetailActivity.class);
                        if (bundle != null) {
                            intent.putExtras(bundle);
                        }
                        startActivity(intent);
                    } else if (slidersDto.getClick_event_type().equals("seller_default")) {
                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                        intent.putExtra("id", slidersDto.getClick_event_value());
                        context.startActivity(intent);
                    }
                }
            });

        }
    }

    @Override
    public void initData() {

        getHomeCategorie();
        llBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(GlobeConturyActivity.class);
            }
        });

    }

    private List<BannerDto> data = new ArrayList<>();

    private void getHomeCategorie() {
        //showLoadDialog();
        Map<String, String> map = new HashMap<>();
        //        map.put("filter[is_hot]","1");
        DataManager.getInstance().AllCategorie(new DefaultSingleObserver<HttpResult<List<BannerDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BannerDto>> result) {
                dissLoadDialog();


            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, map);
    }


}
