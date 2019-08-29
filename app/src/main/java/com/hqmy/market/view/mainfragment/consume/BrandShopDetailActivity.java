package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.MallLikeListAdapter;
import com.hqmy.market.view.adapter.MallShopNewListAapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.SortingLayout;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 品牌店铺详情
 * Created by rzb on 2019/4/22.
 */
public class BrandShopDetailActivity extends BaseActivity {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String MALL_TYPE = "mall_type";

    @BindView(R.id.brand_shop_detail_iv_back)
    ImageView brand_shop_detail_iv_back;
    @BindView(R.id.iv_brand_shop_detail)
    ImageView iv_brand_shop_detail;
    @BindView(R.id.iv_brand_shop_detail_logo)
    ImageView iv_brand_shop_detail_logo;
    @BindView(R.id.tv_shop_name)
    TextView tv_shop_name;
    @BindView(R.id.tv_shop_follow_number)
    TextView tv_shop_follow_number;
    @BindView(R.id.gv_brand_shop_detail_new)
    RecyclerView gv_brand_shop_detail_new;
    @BindView(R.id.brand_shop_detail_srl)
    SmartRefreshLayout brand_shop_detail_srl;
    @BindView(R.id.rb_brand_shop)
    BaseRatingBar rbBrandShop;
    @BindView(R.id.tv_shop_follow)
    TextView tvShopFollow;
    @BindView(R.id.recycle_mall_like)
    MaxRecyclerView recycle_mall_like;
    @BindView(R.id.brand_shop_detail_tv_find)
    TextView brand_shop_detail_tv_find;
    @BindView(R.id.sorting_layout)
    SortingLayout sorting_layout;

    private MallShopNewListAapter mMallNewListAapter;
    private MallLikeListAdapter mallLikeListAdapter;
    private List<NewListItemDto> newList = new ArrayList<NewListItemDto>();
    private String id;
    private String mallType;
    private int mPage = 1;
    private boolean isFollow;
    private String sortStr;

    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_shop_detail;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString(SHOP_DETAIL_ID);
            mallType = getIntent().getExtras().getString(MALL_TYPE);
        }
        getBrandShopDetail(id);
        findGoodLists(mallType);
        getStProductList();
    }

    @Override
    public void initListener() {
        bindClickEvent(brand_shop_detail_iv_back, () -> {
            finish();
        });

        brand_shop_detail_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getStProductList();
            }
        });

        brand_shop_detail_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getStProductList();
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
        bindClickEvent(brand_shop_detail_tv_find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", mallType);
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });
        sorting_layout.setListener(new SortingLayout.ClickListener() {
            @Override
            public void all() {

            }

            @Override
            public void saleDesc() {
                sortStr = "-sales_count";
                mPage = 1;
                getStProductList();
            }

            @Override
            public void saleAsc() {
                sortStr = "sales_count";
                mPage = 1;
                getStProductList();
            }

            @Override
            public void priceDesc() {
                sortStr = "-price";
                mPage = 1;
                getStProductList();
            }

            @Override
            public void priceAsc() {
                sortStr = "price";
                mPage = 1;
                getStProductList();
            }
        });
    }

    private void initAdapter() {
        gv_brand_shop_detail_new.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMallNewListAapter = new MallShopNewListAapter();
        gv_brand_shop_detail_new.setAdapter(mMallNewListAapter);
        mMallNewListAapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, mMallNewListAapter.getItem(position).getId());
                bundle.putString(CommodityDetailActivity.MALL_TYPE, mallType);
                gotoActivity(CommodityDetailActivity.class, false, bundle);
            }
        });
        recycle_mall_like.setLayoutManager(new GridLayoutManager(this, 2));
        mallLikeListAdapter = new MallLikeListAdapter(null, this);
        recycle_mall_like.setAdapter(mallLikeListAdapter);
    }


    private void getBrandShopDetail(String id) {
        showLoadDialog();
        DataManager.getInstance().getBrandShopDetail(new DefaultSingleObserver<HttpResult<RecommendListDto>>() {
            @Override
            public void onSuccess(HttpResult<RecommendListDto> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        setDetailView(result.getData());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, id);
    }

    private void findGoodLists(String mallType) {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("filter[shop_id]", id);
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
        }, mallType, map);
    }

    private void setData(HttpResult<List<NewListItemDto>> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        if (mPage <= 1) {
            mMallNewListAapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) ;
            {
                mMallNewListAapter.setEmptyView(new EmptyView(BrandShopDetailActivity.this));
            }
            brand_shop_detail_srl.finishRefresh();
            brand_shop_detail_srl.setEnableLoadMore(true);
        } else {
            brand_shop_detail_srl.finishLoadMore();
            brand_shop_detail_srl.setEnableRefresh(true);
            mMallNewListAapter.addData(httpResult.getData());
        }

        if (httpResult.getMeta() != null && httpResult.getMeta().getPagination() != null) {
            if (httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()) {
                brand_shop_detail_srl.finishLoadMoreWithNoMoreData();
            }
        }
    }

    private void setDetailView(RecommendListDto recommendListDto) {
        GlideUtils.getInstances().loadNormalImg(BrandShopDetailActivity.this, iv_brand_shop_detail, Constants.WEB_IMG_URL_UPLOADS + recommendListDto.getBackground_img());
        GlideUtils.getInstances().loadRoundImg(BrandShopDetailActivity.this, iv_brand_shop_detail_logo, Constants.WEB_IMG_URL_UPLOADS + recommendListDto.getLogo());
        tv_shop_name.setText(recommendListDto.getShop_name());
        tv_shop_follow_number.setText("关注量：" + recommendListDto.getFollow_count());
        rbBrandShop.setRating(Float.valueOf(recommendListDto.getComment_average_score()));
        isFollow = recommendListDto.isIs_follow();
        if (recommendListDto.isIs_follow()) {
            tvShopFollow.setText("已关注");
        } else {
            tvShopFollow.setText("+关注店铺");
        }
    }

    private void postAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                isFollow = true;
                tvShopFollow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isFollow = true;
                    tvShopFollow.setText("已关注");
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("object", "SMG\\Seller\\Seller");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                isFollow = false;
                tvShopFollow.setText("+关注店铺");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isFollow = false;
                    tvShopFollow.setText("+关注店铺");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    private void getStProductList() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[shop_id]", id);
        map.put("page", mPage + "");
        if (!TextUtils.isEmpty(sortStr)) {
            map.put("sort[]", sortStr);
        }
        DataManager.getInstance().getStProductList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                brand_shop_detail_srl.finishRefresh();
                brand_shop_detail_srl.finishLoadMore();
                if (mPage == 1) {
                    if (result != null && result.getData() != null) {
                        mallLikeListAdapter.setNewData(result.getData());
                    } else {
                        mallLikeListAdapter.setNewData(null);
                        mallLikeListAdapter.setEmptyView(new EmptyView(BrandShopDetailActivity.this));
                    }
                } else {
                    mallLikeListAdapter.addData(result.getData());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                brand_shop_detail_srl.finishRefresh();
                brand_shop_detail_srl.finishLoadMore();
            }
        }, mallType, map);
    }
}
