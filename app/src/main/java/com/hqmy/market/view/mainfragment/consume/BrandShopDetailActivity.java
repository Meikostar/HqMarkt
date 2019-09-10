package com.hqmy.market.view.mainfragment.consume;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.ExtDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.bean.ShopInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.db.bean.ProductListType;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.activity.ConturyActivity;
import com.hqmy.market.view.activity.MessageCenterActivity;
import com.hqmy.market.view.activity.ProductListActivity;
import com.hqmy.market.view.adapter.ConsumePushAdapter;
import com.hqmy.market.view.mainfragment.ConsumeFragment;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.ClearEditText;
import com.hqmy.market.view.widgets.autoview.CustomView;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;
import com.scwang.smartrefresh.header.MaterialHeader;
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
 * 品牌店铺详情
 * Created by rzb on 2019/4/22.
 */
public class BrandShopDetailActivity extends BaseActivity {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String MALL_TYPE      = "mall_type";

    @BindView(R.id.banner)
    Banner                  banner;
    @BindView(R.id.acb_status_bar)
    ImageView               acbStatusBar;
    @BindView(R.id.actionbar_back)
    ImageView               actionbarBack;
    @BindView(R.id.et_search)
    ClearEditText           etSearch;
    @BindView(R.id.iv_labe)
    ImageView               ivLabe;
    @BindView(R.id.iv_img)
    ImageView               ivImg;
    @BindView(R.id.tv_title)
    TextView                tvTitle;
    @BindView(R.id.tv_detail)
    TextView                tv_detail;

    @BindView(R.id.tv_content)
    TextView                tvContent;
    @BindView(R.id.tv_attention)
    TextView                tvShopFollow;
    @BindView(R.id.tv_shop_product_1)
    TextView                tvShopProduct1;
    @BindView(R.id.tv_shop_product_2)
    TextView                tvShopProduct2;
    @BindView(R.id.tv_shop_product_3)
    TextView                tvShopProduct3;
    @BindView(R.id.iv_shop_product_3)
    ImageView               ivShopProduct3;
    @BindView(R.id.ll_shop_product_3)
    LinearLayout            llShopProduct3;
    @BindView(R.id.tv_shop_product_4)
    TextView                tvShopProduct4;
    @BindView(R.id.iv_shop_product_4)
    ImageView               ivShopProduct4;
    @BindView(R.id.ll_shop_product_4)
    LinearLayout            llShopProduct4;
    @BindView(R.id.recycle_mall_like)
    RecyclerView            consumePushRecy;
    @BindView(R.id.brand_shop_detail_scrollView)
    CustomView              brandShopDetailScrollView;
    @BindView(R.id.brand_shop_detail_srl)
    SuperSwipeRefreshLayout      refreshLayout;

    private             SwipeRefreshLayoutUtil  mSwipeRefreshLayoutUtil;
    private String id;
    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_shop_detail;
    }

    @Override
    public void initView() {
        id=getIntent().getStringExtra("id");
        initAdapter();
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(refreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                getStProductList();
            }

            @Override
            public void onLoadMore() {
                // mSwipeRefreshLayoutUtil.setCanLoadMore(true);
                mCurrentPage++;
                getStProductList();
            }
        });
        tvShopProduct1.setSelected(true);
        tvShopProduct2.setSelected(false);
        tvShopProduct3.setSelected(false);
    }

    @Override
    public void initData() {

        getStProductList();
        getShopDetailInfo();
    }
    private    int      mCurrentPage = 1;
    private void setTopTitlesView(int poition) {
        mCurrentPage = 1;

        switch (poition) {
            case 1:
                sortStr="";
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(true);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList();
                break;
            case 2:
                sortStr="";
                isNew=1;
                ivShopProduct3.setTag("select");
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(true);
                tvShopProduct3.setSelected(false);
                ivShopProduct3.setImageResource(R.mipmap.arrow_defaut);
                getStProductList();

                break;
            case 3:
                tvShopProduct1.setSelected(false);
                tvShopProduct2.setSelected(false);
                tvShopProduct3.setSelected(true);
                setTopTitles3View();
                break;

        }
    }



    private   ShopInfoDto data;
    private List<BannerItemDto> lists=new ArrayList<>();
    private void getShopDetailInfo() {


        DataManager.getInstance().getBrandInfo(new DefaultSingleObserver<HttpResult<ShopInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<ShopInfoDto> countOrderBean) {

                if (countOrderBean != null && countOrderBean.getData() != null) {
                    data = countOrderBean.getData();
                    if(data!=null){
                        lists.clear();
                        GlideUtils.getInstances().loadRoundCornerImg(BrandShopDetailActivity.this,ivImg,3,data.logo,R.drawable.moren_product);
                        if(data.ext!=null&&data.ext.imgs!=null&&data.ext.imgs.size()>0){
                            for(String url:data.ext.imgs){
                                BannerItemDto dto=new BannerItemDto();
                                dto.setPath(url);
                                lists.add(dto);
                            }
                        }
                        startBanner(lists);
                        if(TextUtil.isNotEmpty(data.name)){
                            tvTitle.setText(data.name);

                        }
                        String attion="";
                        if(TextUtil.isNotEmpty(data.followersCount)){
                            attion=data.followersCount;
                        }else {
                            attion="0";
                        }
                        if(data.category!=null&&data.category.data!=null&&TextUtil.isNotEmpty(data.category.data.title)){
                            tvContent.setText(data.category.data.title+" | "+attion+"关注");
                        }else {
                            tvContent.setText(attion+"关注");
                        }

                        if(data.isFollowed){

                            tvShopFollow.setText("已关注");
                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_FC6B00));
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_orgin_14);
                        }else {
                            tvShopFollow.setText(data.shop_name);
                            tvShopFollow.setText("+关注");
                            tvShopFollow.setBackgroundResource(R.drawable.shape_radius_14);
                            tvShopFollow.setTextColor(getResources().getColor(R.color.my_color_333333));
                        }


                            if(TextUtil.isNotEmpty(data.description)){
                                tv_detail.setText(data.description);
                                tv_detail.setVisibility(View.VISIBLE);
                            }else {
                                tv_detail.setVisibility(View.VISIBLE);
                            }




                    }

                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, id,"followersCount,isFollowed,category");
    }


    @Override
    public void initListener() {

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
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtil.isNotEmpty(charSequence.toString())) {

                    searchKey = charSequence.toString();
                } else {
                    searchKey = "";
                    getStProductList();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    private List<NewListItemDto>   goodsLists   = new ArrayList<NewListItemDto>();
    private void initAdapter() {
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mAdapter = new ConsumePushAdapter(goodsLists, this);
        consumePushRecy.addItemDecoration(new RecyclerItemDecoration(6, 2));
        consumePushRecy.setLayoutManager(gridLayoutManager2);
        consumePushRecy.setAdapter(mAdapter);

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
                    GlideUtils.getInstances().loadNormalImg(BrandShopDetailActivity.this, imageView, imgStr, R.drawable.img_default_three);
                } else {
                    GlideUtils.getInstances().loadNormalImg(BrandShopDetailActivity.this, imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr,  R.drawable.img_default_three);
                }
            }


        }
    }
    private void setTopTitles3View() {
        mCurrentPage = 1;

        if (ivShopProduct3.getTag().equals("select")) {

            ivShopProduct3.setTag("unselect");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_drop);
            sortStr="-sales_count";
        } else {

            ivShopProduct3.setTag("select");
            ivShopProduct3.setImageResource(R.mipmap.shop_product_price_litre);
            sortStr="sales_count";
        }
        getStProductList();
    }

    private ConsumePushAdapter mAdapter;

    private void postAttention() {

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Mall\\Models\\MallBrand");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                getShopDetailInfo();
                isFollow = true;
                //                tvShopFollow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {
                getShopDetailInfo();
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
        map.put("object", "SMG\\Mall\\Models\\MallBrand");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                isFollow = false;
                //                tvShopFollow.setText("+关注店铺");
                getShopDetailInfo();
            }

            @Override
            public void onError(Throwable throwable) {
                getShopDetailInfo();
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
    private int mPage;
    private int isNew;
    private void getStProductList() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[brand_id]", id);
        map.put("page", mPage + "");


        if(isNew!=0){
            isNew=0;
            map.put("filter[is_new]", 1 + "");
        }
        if (!TextUtils.isEmpty(sortStr)) {
            map.put("sort[]", sortStr);
        }
        DataManager.getInstance().getStProductList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if (null != result.getData() && result.getData().size() > 0) {

                    if (mCurrentPage == 1) {

                        mAdapter.setNewData(result.getData());
                        refreshLayout.setRefreshing(false);
                    } else {

                        mAdapter.addData(result.getData());
                        refreshLayout.setLoadMore(false);
                    }

                } else {
                    EmptyView emptyView = new EmptyView(BrandShopDetailActivity.this);
                    if (!TextUtils.isEmpty(searchKey)) {
                        mAdapter.setNewData(result.getData());
                        emptyView.setTvEmptyTip(String.format("没搜索到%s相关数据", searchKey));
                    } else {
                        emptyView.setTvEmptyTip("暂无商品数据");
                    }
                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, result.getMeta().getPagination().getTotal());

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();

            }
        }, "", map);
    }
    private String searchKey;
}
