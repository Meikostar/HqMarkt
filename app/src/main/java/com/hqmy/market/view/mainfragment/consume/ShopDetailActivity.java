package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.CouponBean;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.WebViewUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.RechargeWebActivity;
import com.hqmy.market.view.adapter.ShopCouponAdapter;
import com.hqmy.market.view.adapter.StProductsAapter;
import com.hqmy.market.view.widgets.autoview.NoScrollListView;
import com.hqmy.market.view.widgets.autoview.NoScrollWebView;
import com.hqmy.market.view.widgets.autoview.SortingLayout;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 实体店铺详情
 * Created by rzb on 2019/5/22.
 */
public class ShopDetailActivity extends BaseActivity implements SortingLayout.ClickListener {
    public static final String SHOP_DETAIL_ID = "shop_detail_id";
    public static final String SHOP_DETAIL_LAT = "shop_detail_lat";
    public static final String SHOP_DETAIL_LNG = "shop_detail_lng";

    @BindView(R.id.iv_shop_banner)
    ImageView iv_shop_banner;
    @BindView(R.id.iv_shop_detail)
    ImageView iv_shop_detail;
    @BindView(R.id.tv_shop_name)
    TextView tv_shop_name;
    @BindView(R.id.tv_shop_follow_number)
    TextView tv_shop_follow_number;
    @BindView(R.id.shop_layout_back)
    LinearLayout shop_layout_back;
    @BindView(R.id.iv_shop_detail_location)
    ImageView iv_shop_detail_location;
    @BindView(R.id.iv_shop_detail_phone)
    ImageView iv_shop_detail_phone;
    @BindView(R.id.tv_shop_location)
    TextView tv_shop_location;
    @BindView(R.id.tv_shop_distance)
    TextView tv_shop_distance;
    @BindView(R.id.tv_shop_yingye_time)
    TextView tv_shop_yingye_time;
    @BindView(R.id.tv_shop_have)
    TextView tv_shop_have;
    @BindView(R.id.sorting_layout)
    SortingLayout sorting_layout;
    @BindView(R.id.shop_detail_webView)
    NoScrollWebView shop_detail_webView;
    @BindView(R.id.shop_detail_notice)
    LinearLayout shop_detail_notice;
    @BindView(R.id.shop_detail_listView)
    NoScrollListView shop_detail_listView;
    @BindView(R.id.tv_shop_follow)
    TextView tv_shop_follow;
    @BindView(R.id.recyclerView_coupon)
    NoScrollListView recyclerView_coupon;
    @BindView(R.id.shop_detail_srl)
    SmartRefreshLayout shop_detail_srl;
    @BindView(R.id.shop_layout_share)
    View shop_layout_share;
    @BindView(R.id.find)
    TextView find;

    private StProductsAapter mStProductsAapter;
    private List<NewListItemDto> pList = new ArrayList<NewListItemDto>();

    private String id;
    private String lat;
    private String lng;
    private boolean isFollow = false;
    private String type;
    private ShopCouponAdapter shopCouponAdapter;
    private int page = 1;
    private String sortStr;

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        id = getIntent().getExtras().getString(SHOP_DETAIL_ID);
        lat = getIntent().getExtras().getString(SHOP_DETAIL_LAT);
        lng = getIntent().getExtras().getString(SHOP_DETAIL_LNG);
        getBanner();
        getShopDetail(id, lat, lng);
        getShopCoupons();
    }

    @Override
    public void initListener() {
        bindClickEvent(shop_layout_back, () -> {
            finish();
        });
        bindClickEvent(tv_shop_follow, () -> {
            //关注和取消关注
            if (isFollow) {
                deleteAttention();
            } else {
                postAttention();
            }
        });
        shop_detail_srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getStProductList();
            }
        });
        shop_detail_srl.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getStProductList();
            }
        });
        bindClickEvent(shop_layout_share, () -> {
            ShareModeDialog dialog = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                @Override
                public void sureItem(int position) {
                    boolean isTimelineCb = false;
                    String url = "http://ax.jmlax.com/download?mall_type=" + type + "&id=" + id + "&from=ShopDetailActivity";
                    String title = "实体店铺分享";
                    if (position == ShareModeDialog.SHARE_PYQ) {
                        isTimelineCb = true;
                    }
                    ShareUtil.sendToWeaChat(ShopDetailActivity.this, isTimelineCb, title, url);
                }
            });
            dialog.show();
        });
        bindClickEvent(find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", type);
            gotoActivity(ProductSearchActivity.class, false, bundle);
        });
        sorting_layout.setListener(new SortingLayout.ClickListener() {
            @Override
            public void all() {

            }

            @Override
            public void saleDesc() {
                sortStr = "-sales_count";
                page = 1;
                getStProductList();
            }

            @Override
            public void saleAsc() {
                sortStr = "sales_count";
                page = 1;
                getStProductList();
            }

            @Override
            public void priceDesc() {
                sortStr = "-price";
                page = 1;
                getStProductList();
            }

            @Override
            public void priceAsc() {
                sortStr = "price";
                page = 1;
                getStProductList();
            }
        });
    }

    private void getBanner() {
        showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getPhysical_store_top();
                        if (bannerList != null) {
                            GlideUtils.getInstances().loadNormalImg(ShopDetailActivity.this, iv_shop_banner,
                                    Constants.WEB_IMG_URL_UPLOADS + bannerList.get(0).getPath());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "physical_store_top");
    }

    private void getShopDetail(String id, String lat, String lng) {
        showLoadDialog();
        DataManager.getInstance().getShopDetail(new DefaultSingleObserver<HttpResult<RecommendListDto>>() {
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
        }, id, lat, lng);
    }

    private void getShopCoupons() {
        Map<String, Object> map = new HashMap<>();
        map.put("filter[shop_id]", id);
        DataManager.getInstance().getShopCoupons(new DefaultSingleObserver<HttpResult<List<CouponBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<CouponBean>> httpResult) {
                dissLoadDialog();
                if (httpResult != null && httpResult.getData() != null && httpResult.getData().size() > 0) {
                    recyclerView_coupon.setVisibility(View.VISIBLE);
                    shopCouponAdapter = new ShopCouponAdapter(ShopDetailActivity.this, httpResult.getData());
                    recyclerView_coupon.setAdapter(shopCouponAdapter);
                } else {
                    recyclerView_coupon.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void getStProductList() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("filter[shop_id]", id);
        map.put("page", page + "");
        if (!TextUtils.isEmpty(sortStr)) {
            map.put("sort[]", sortStr);
        }
        DataManager.getInstance().getStProductList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                shop_detail_srl.finishRefresh();
                shop_detail_srl.finishLoadMore();
                if (result != null && result.getData() != null) {
                    if (page == 1) {
                        pList.clear();
                    }
                    pList.addAll(result.getData());
                    mStProductsAapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                shop_detail_srl.finishRefresh();
                shop_detail_srl.finishLoadMore();
            }
        }, type, map);
    }

    private void setDetailView(RecommendListDto recommendListDto) {
        type = recommendListDto.getType();
        getStProductList();
        tv_shop_location.setText(recommendListDto.getAddress());
        tv_shop_distance.setText(recommendListDto.getDistance());
        GlideUtils.getInstances().loadNormalImg(ShopDetailActivity.this, iv_shop_banner, Constants.WEB_IMG_URL_UPLOADS + recommendListDto.getBackground_img());
        GlideUtils.getInstances().loadRoundImg(ShopDetailActivity.this, iv_shop_detail, Constants.WEB_IMG_URL_UPLOADS + recommendListDto.getLogo());
        tv_shop_name.setText(recommendListDto.getShop_name());
        tv_shop_follow_number.setText("关注量：" + recommendListDto.getFollow_count());
        isFollow = recommendListDto.isIs_follow();
        if (recommendListDto.isIs_follow()) {
            tv_shop_follow.setText("已关注");
        } else {
            tv_shop_follow.setText("+关注店铺");
        }
        tv_shop_distance.setText(recommendListDto.getDistance());
        if (recommendListDto.getExt() != null) {
            tv_shop_yingye_time.setText(recommendListDto.getExt().getOpen_time());
            tv_shop_have.setText(recommendListDto.getExt().getServices());
        }
        if (recommendListDto.getTips() != null) {
            shop_detail_notice.setVisibility(View.VISIBLE);
            WebViewUtil.loadHtml(shop_detail_webView, recommendListDto.getTips());
        } else {
            shop_detail_notice.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        mStProductsAapter = new StProductsAapter(ShopDetailActivity.this, pList);
        shop_detail_listView.setAdapter(mStProductsAapter);
        shop_detail_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("productId", mStProductsAapter.getItem(i).getId());
                bundle.putString("mall_type", type);
                bundle.putString("shopId", id);
                gotoActivity(ShopProductDetailActivity.class, false, bundle);
            }
        });
        recyclerView_coupon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("2".equals(shopCouponAdapter.getItem(i).getCoupon_type())) {
                    checkOutOrder(shopCouponAdapter.getItem(i).getId());
                } else {
                    postShopCoupons(shopCouponAdapter.getItem(i).getId());
                }
            }
        });
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
                tv_shop_follow.setText("已关注");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isFollow = true;
                    tv_shop_follow.setText("已关注");
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
                tv_shop_follow.setText("+关注店铺");
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isFollow = false;
                    tv_shop_follow.setText("+关注店铺");
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    /**
     * 領取優惠券
     */
    private void postShopCoupons(String id) {
        showLoadDialog();
        DataManager.getInstance().postShopCoupons(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                dissLoadDialog();
                ToastUtil.toast("领取成功");
                getShopCoupons();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("领取成功");
                    getShopCoupons();
                } else {
                    ToastUtil.toast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, id);
    }

    private void checkOutOrder(String id) {
        String couponType = "coupon_";
        if (!TextUtils.isEmpty(type)) {
            couponType = couponType.concat(type);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", id);
        map.put("use_slug", "1");
        DataManager.getInstance().checkOutOrder(new DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>>() {
            @Override
            public void onSuccess(HttpResult<List<CheckOutOrderResult>> result) {
                ToastUtil.showToast("领取成功");
                getShopCoupons();
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("领取成功");
                    getShopCoupons();
                } else {
                    String errorMsg = ApiException.getHttpExceptionMessage(throwable);
                    if (!TextUtils.isEmpty(errorMsg) && errorMsg.indexOf("您有未完成的购买优惠券的订单") != -1) {
                        String orderId = "";
                        if (errorMsg.indexOf("_") != -1) {
                            orderId = errorMsg.substring(errorMsg.indexOf("_") + 1, errorMsg.length());
                            toPay(orderId);
                        }
                    } else {
                        ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                    }

                }
            }
        }, couponType, map);
    }

    private void toPay(String orderId) {
        if (!TextUtils.isEmpty(orderId)) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.INTENT_WEB_URL, Constants.BASE_URL + "to_pay?order_id=" + orderId);
            bundle.putString(Constants.INTENT_WEB_TITLE, "支付");
            gotoActivity(RechargeWebActivity.class, true, bundle);
        }
    }

    @Override
    public void all() {
    }

    @Override
    public void saleDesc() {
    }

    @Override
    public void saleAsc() {
    }

    @Override
    public void priceDesc() {
    }

    @Override
    public void priceAsc() {
    }
}
