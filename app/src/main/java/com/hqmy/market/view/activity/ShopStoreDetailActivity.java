package com.hqmy.market.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.ShopInfoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
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
 * 品牌店铺详情
 * Created by rzb on 2019/4/22.
 */
public class ShopStoreDetailActivity extends BaseActivity {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String MALL_TYPE      = "mall_type";


    @BindView(R.id.super_recycle_view)
    SuperRecyclerView mSuperRecyclerView;
    @BindView(R.id.banner)
    Banner            banner;
    @BindView(R.id.acb_status_bar)
    ImageView         acbStatusBar;
    @BindView(R.id.actionbar_back)
    ImageView         actionbarBack;
    @BindView(R.id.et_search)
    EditText          etSearch;
    @BindView(R.id.iv_labe)
    ImageView         ivLabe;
    @BindView(R.id.iv_img)
    ImageView         ivImg;
    @BindView(R.id.tv_title)
    TextView          tvTitle;
    @BindView(R.id.tv_content)
    TextView          tvContent;
    @BindView(R.id.tv_attention)
    TextView          tvShopFollow;
    @BindView(R.id.tv_detail)
    TextView          tv_detail;
    @BindView(R.id.tv_shop_product_1)
    TextView          tvShopProduct1;
    @BindView(R.id.tv_shop_product_2)
    TextView          tvShopProduct2;
    @BindView(R.id.tv_shop_product_3)
    TextView          tvShopProduct3;
    @BindView(R.id.iv_shop_product_3)
    ImageView         ivShopProduct3;
    @BindView(R.id.ll_shop_product_3)
    LinearLayout      llShopProduct3;
    @BindView(R.id.tv_shop_product_4)
    TextView          tvShopProduct4;
    @BindView(R.id.iv_shop_product_4)
    ImageView         ivShopProduct4;
    @BindView(R.id.ll_shop_product_4)
    LinearLayout      llShopProduct4;
    @BindView(R.id.ll_bg)
    LinearLayout      llBg;
    private SwipeRefreshLayoutUtil mSwipeRefreshLayoutUtil;
    private String                 id;


    private LinearLayoutManager layoutManager;

    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_store_detail;
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        id = getIntent().getStringExtra("shop_detail_id");
        initAdapter();
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        mAdapter = new ConsumePushAdapter(goodsLists, this);
        mSuperRecyclerView.addItemDecoration(new RecyclerItemDecoration(6, 2));
        mSuperRecyclerView.setLayoutManager(gridLayoutManager2);
        //        layoutManager = new LinearLayoutManager(this);
        //        mSuperRecyclerView.setLayoutManager(layoutManager);
        //        mSuperRecyclerView.addItemDecoration(new DivItemDecoration(2, true));

        mSuperRecyclerView.getMoreProgressView().getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;


        mSuperRecyclerView.setAdapter(mAdapter);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //  mSuperRecyclerView.showMoreProgress();
                getStProductList(TYPE_PULL_REFRESH);
                if (mSuperRecyclerView != null) {
                    mSuperRecyclerView.hideMoreProgress();
                }
                closeKeyBoard();

            }
        };

        mSuperRecyclerView.setRefreshListener(refreshListener);


    }


    public void refresh() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }

    @Override
    public void initData() {

        reflash();
        if (!TextUtil.isEmpty(id)) {
            getStProductList(TYPE_PULL_REFRESH);
            getShopInfo();
        }

    }

    private void reflash() {
        if (mSuperRecyclerView != null) {
            //实现自动下拉刷新功能
            mSuperRecyclerView.getSwipeToRefresh().post(new Runnable() {
                @Override
                public void run() {
                    mSuperRecyclerView.setRefreshing(true);//执行下拉刷新的动画
                    refreshListener.onRefresh();//执行数据加载操作
                }
            });
        }
    }

    private int mCurrentPage = 1;

    private void setTopTitlesView(int poition) {
        mCurrentPage = 1;

        switch (poition) {
            case 1:
                sortStr = "";
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(true);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList(TYPE_PULL_REFRESH);
                break;
            case 2:
                sortStr = "";
                isNew = 1;
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(true);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList(TYPE_PULL_REFRESH);

                break;
            case 3:
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(true);
                setTopTitles3View();
                break;

        }
    }


    private final int TYPE_PULL_REFRESH = 888;
    private final int TYPE_PULL_MORE    = 889;
    private       int currpage          = 1;//第几页

    private ShopInfoBean         data;
    private List<BannerItemDto>  lists = new ArrayList<>();
    private List<NewListItemDto> dats  = new ArrayList<>();

    public void onDataLoaded(int loadType, final boolean haveNext, List<NewListItemDto> list) {

        if (loadType == TYPE_PULL_REFRESH) {
            currpage = 1;
            dats.clear();
            for (NewListItemDto info : list) {
                dats.add(info);
            }
        } else {
            for (NewListItemDto info : list) {
                dats.add(info);
            }
        }

        mAdapter.setNewData(dats);

        mAdapter.notifyDataSetChanged();
        if (mSuperRecyclerView != null) {
            mSuperRecyclerView.hideMoreProgress();
        }


        if (haveNext) {
            mSuperRecyclerView.setupMoreListener(new OnMoreListener() {
                @Override
                public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
                    currpage++;
                    mSuperRecyclerView.showMoreProgress();

                    if (haveNext)
                        getStProductList(TYPE_PULL_MORE);
                    mSuperRecyclerView.hideMoreProgress();

                }
            }, 1);
        } else {
            if (mSuperRecyclerView != null) {
                mSuperRecyclerView.removeMoreListener();
                mSuperRecyclerView.hideMoreProgress();
            }


        }


    }

    private void getShopInfo() {
        DataManager.getInstance().following_shops_detai(new DefaultSingleObserver<HttpResult<ShopInfoBean>>() {
            @Override
            public void onSuccess(HttpResult<ShopInfoBean> countOrderBean) {
                if (countOrderBean != null && countOrderBean.seller != null) {
                    data = countOrderBean.seller;
                    if (data != null) {
                        lists.clear();
                        GlideUtils.getInstances().loadRoundCornerImg(ShopStoreDetailActivity.this, ivImg, 3, data.logo, R.drawable.moren_product);
                        if (data.photos != null) {
                            //                            for(String url:data.ext.imgs){
                            BannerItemDto dto = new BannerItemDto();
                            dto.setPath(data.photos);
                            lists.add(dto);
                            //                            }
                        }
                        startBanner(lists);
                        if (TextUtil.isNotEmpty(data.shop_name)) {
                            tvTitle.setText(data.shop_name);

                        }
                        String attion = "";
                        if (TextUtil.isNotEmpty(data.followings_count)) {
                            attion = data.followings_count;
                        } else {
                            attion = "0";
                        }

                        tvShopFollow.setVisibility(View.VISIBLE);
                        if (data.is_following) {

                            tvShopFollow.setText("已关注");

                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_FC6B00));
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_orgin_14);
                        } else {
                            tvShopFollow.setText(data.shop_name);
                            tvShopFollow.setText("+关注");
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_14);
                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_333333));
                        }
                        if (data.ext != null && data.ext.instructions != null) {
                            tv_detail.setText(data.ext.instructions);
                            tv_detail.setVisibility(View.VISIBLE);
                            //                            }
                        } else {
                            tv_detail.setVisibility(View.VISIBLE);
                            //
                        }


                    }

                }


            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {

                    //                    tvShopFollow.setText("已关注");
                } else {
                    ToastUtil.toast("");
                }

            }
        }, id);
    }

    @Override
    public void initListener() {

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                //点击搜索要做的操作
                if (TextUtil.isNotEmpty(etSearch.getText().toString())) {

                    searchKey = etSearch.getText().toString();

                } else {
                    searchKey = "";


                }

                getStProductList(TYPE_PULL_REFRESH);
                return false;
            }
        });
        tvShopProduct1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(1);
            }
        });
        tvShopProduct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(2);
            }
        });
        llShopProduct3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopTitlesView(3);
            }
        });
        ivLabe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(MessageCenterActivity.class);
            }
        });
        actionbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        bindClickEvent(tvShopFollow, () -> {
            //关注和取消关注
            if (isFollow) {
                deleteAttention();
            } else {
                postAttention();
            }
        });


    }

    private List<NewListItemDto> goodsLists = new ArrayList<NewListItemDto>();

    private void initAdapter() {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerItemDto slidersDto = (BannerItemDto) path;
            String imgStr = slidersDto.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadNormalImg(ShopStoreDetailActivity.this, imageView, imgStr, R.drawable.img_default_three);
                } else {
                    GlideUtils.getInstances().loadNormalImg(ShopStoreDetailActivity.this, imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.drawable.img_default_three);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                //                product_default:商品
                //                seller_default:商家
                @Override
                public void onClick(View v) {
                    //                    if(slidersDto.getClick_event_type().equals("product_default")){
                    //                        Bundle bundle = new Bundle();
                    //                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                    //                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                    //                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                    //                        Intent intent = new Intent(context, CommodityDetailActivity.class);
                    //                        if (bundle != null) {
                    //                            intent.putExtras(bundle);
                    //                        }
                    //                        startActivity(intent);
                    //                    }else if(slidersDto.getClick_event_type().equals("seller_default")){
                    //                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                    //                        intent.putExtra("id",slidersDto.getClick_event_value());
                    //                        context.startActivity(intent);
                    //                    }
                }
            });

        }
    }

    private void setTopTitles3View() {
        mCurrentPage = 1;

        if (ivShopProduct3.getTag().equals("select")) {

            ivShopProduct3.setTag("unselect");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_drop);
            sortStr = "-price";
        } else {

            ivShopProduct3.setTag("select");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_litre);
            sortStr = "price";
        }
        getStProductList(TYPE_PULL_REFRESH);
    }

    private ConsumePushAdapter mAdapter;

    private void postAttention() {

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                getShopInfo();
                isFollow = true;
                //                tvShopFollow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {
                getShopInfo();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isFollow = true;
                    //                    tvShopFollow.setText("已关注");
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private boolean isFollow;

    private void deleteAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                isFollow = false;
                //                tvShopFollow.setText("+关注店铺");
                getShopInfo();
            }

            @Override
            public void onError(Throwable throwable) {
                getShopInfo();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isFollow = false;
                    //                    tvShopFollow.setText("+关注店铺");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    private String sortStr;
    private String scopeSearch;
    private int    mPage;
    private int    isNew;

    private void getStProductList(int loadtype) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[shop_id]", id);
        map.put("page", currpage + "");


        if (isNew != 0) {
            isNew = 0;
            map.put("filter[is_new]", 1 + "");
        }
        if (!TextUtils.isEmpty(sortStr)) {
            map.put("sort", sortStr);
        }
        if (!TextUtils.isEmpty(searchKey)) {
            map.put("filter[scopeSearch]", searchKey);
        }

        DataManager.getInstance().getStProductList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {

                dissLoadDialog();
                onDataLoaded(loadtype, result.getData().size() == Constants.PAGE_SIZE, result.getData());
                //                if (null != result.getData() && result.getData().size() > 0) {
                //
                //                    if (mCurrentPage == 1) {
                //
                //                        mAdapter.setNewData(result.getData());
                //                        refreshLayout.setRefreshing(false);
                //                    } else {
                //
                //                        mAdapter.addData(result.getData());
                //                        refreshLayout.setLoadMore(false);
                //                    }
                //
                //                } else {
                //                    EmptyView emptyView = new EmptyView(ShopStoreDetailActivity.this);
                //                    if (!TextUtils.isEmpty(searchKey)) {
                //                        mAdapter.setNewData(result.getData());
                //                        emptyView.setTvEmptyTip(String.format("没搜索到%s相关数据", searchKey));
                //                    } else {
                //                        emptyView.setTvEmptyTip("暂无商品数据");
                //                    }
                //                    mAdapter.setEmptyView(emptyView);
                //
                //
                //                }
                //                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, "", map);
    }

    private String searchKey;
}
