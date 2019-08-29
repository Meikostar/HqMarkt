package com.hqmy.market.view.mainfragment.consume;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.WebViewUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.CommodityCommentImageAdapter;
import com.hqmy.market.view.adapter.LoopViewPagerAdapter;
import com.hqmy.market.view.mainfragment.chat.ConversationActivity;
import com.hqmy.market.view.widgets.ShopProductTypeDialog;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;

/**
 * 商品详细
 * Created by rzb on 2019/05/20
 */
public class CommodityDetailActivity extends BaseActivity implements BaseActivity.PermissionsListener {
    private static final String TAG = CommodityDetailActivity.class.getSimpleName();
    public static final String PRODUCT_ID = "product_id";
    public static final String MALL_TYPE = "mall_type";
    public static final String FROM = "from";
    private String product_id;
    private String mall_type;

    @BindView(R.id.layout_back)
    LinearLayout layout_back;
    @BindView(R.id.tv_commodity_info_title)
    TextView tv_commodity_info_title;
    @BindView(R.id.tv_commodity_info_market_price)
    TextView tv_commodity_info_market_price;
    @BindView(R.id.tv_commodity_info_price)
    TextView tv_commodity_info_price;
    @BindView(R.id.tv_commodity_info_courier)
    TextView tv_commodity_info_courier;
    @BindView(R.id.tv_commodity_info_from)
    TextView tv_commodity_info_from;
    @BindView(R.id.tv_commodity_info_weight)
    TextView tv_commodity_info_weight;
    @BindView(R.id.tv_item_consume_push_comments)
    TextView tv_item_consume_push_comments;
    @BindView(R.id.tv_item_consume_push_comments_scale)
    TextView tv_item_consume_push_comments_scale;
    @BindView(R.id.webView_commodity)
    WebView webView;
    @BindView(R.id.commodity_detail_banner_vp_container)
    ViewPager banner_vp_container;
    @BindView(R.id.commodity_detail_banner_ll_indicators)
    LinearLayout banner_ll_indicators;
    @BindView(R.id.layout_service)
    RelativeLayout layout_service;
    @BindView(R.id.layout_save)
    RelativeLayout layout_save;
    @BindView(R.id.iv_save)
    ImageView iv_save;
    @BindView(R.id.tv_save)
    TextView tv_save;
    @BindView(R.id.layout_shop)
    RelativeLayout layout_shop;
    @BindView(R.id.tv_shop_cart_submit)
    TextView tv_shop_cart_submit;
    @BindView(R.id.ll_evaluate_view)
    LinearLayout ll_evaluate_view;
    @BindView(R.id.tv_commodity_comments_name)
    TextView tv_commodity_comments_name;
    @BindView(R.id.tv_commodity_comments_msg)
    TextView tv_commodity_comments_msg;
    @BindView(R.id.ll_evaluate_view_notice)
    RelativeLayout ll_evaluate_view_notice;
    @BindView(R.id.ll_no_evaluate)
    LinearLayout ll_no_evaluate;
    @BindView(R.id.rb_evaluate)
    BaseRatingBar rb_evaluate;
    @BindView(R.id.recyclerView_images)
    RecyclerView recyclerViewImages;
    @BindView(R.id.tv_min_bug_num)
    TextView tvMinBugNum;
    @BindView(R.id.layout_share_product)
    LinearLayout layout_share_product;
    private CommodityDetailInfoDto commodityDetailInfoDto;
    private String isFavorite = "false";//是否收藏
    private String fromStr;

    @Override
    public int getLayoutId() {
        return R.layout.ui_commodity_detail_layout;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_id = bundle.getString(PRODUCT_ID);
            mall_type = bundle.getString(MALL_TYPE);
            fromStr = bundle.getString(FROM);
        }
        if (fromStr != null) {
            if (fromStr.equals("consume_home") || fromStr.equals("gc")) {
                layout_shop.setVisibility(View.VISIBLE);
            } else {
                layout_shop.setVisibility(View.GONE);
            }
        }
        if(ShareUtil.getInstance().isLogin()) {
            getGoodsDetailToken(mall_type, product_id);
        }else{
            getGoodsDetail(mall_type, product_id);
        }
        getCommentsList();
    }

    @Override
    public void initListener() {
        bindClickEvent(layout_back, () -> {
            finish();
        });

        bindClickEvent(layout_shop, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(BrandShopDetailActivity.MALL_TYPE, commodityDetailInfoDto.getType());
            bundle.putString(BrandShopDetailActivity.SHOP_DETAIL_ID, commodityDetailInfoDto.getId());
            gotoActivity(BrandShopDetailActivity.class, false, bundle);
        });

        bindClickEvent(layout_service, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(ConversationActivity.TITLE, "客服");
            bundle.putString(ConversationActivity.TARGET_ID, commodityDetailInfoDto.getSlug());
            gotoActivity(ConversationActivity.class, true, bundle);
        });

        bindClickEvent(layout_save, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                if (isFavorite.equals("false")) {
                    addFavorites(product_id);
                } else {
                    cancleFavorites(product_id);
                }
            } else {
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(tv_shop_cart_submit, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                ShopProductTypeDialog dialog = new ShopProductTypeDialog(this, commodityDetailInfoDto, commodityDetailInfoDto.getAttrs(),
                        commodityDetailInfoDto.getCover(), commodityDetailInfoDto.getPrice(), commodityDetailInfoDto.getTitle(),
                        new ShopProductTypeDialog.ShopProductTypeListener() {
                            @Override
                            public void callbackSelect(String stockId, String productId, String countBuy) {
                                //if(stockId != null) {
                                addShoppingCart(commodityDetailInfoDto.getType(), stockId, productId, countBuy);
                                //}else{
                                //    ToastUtil.showToast("没有库存");
                                //}
                            }
                        }, true);
                dialog.show();
            } else {
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(ll_evaluate_view_notice, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", product_id);
            if (commodityDetailInfoDto != null) {
                bundle.putString("commentCount", commodityDetailInfoDto.getComment_count());
                bundle.putString("commentRate", commodityDetailInfoDto.getComment_good_rate());
            }
            gotoActivity(CommodityCommentActivity.class, false, bundle);
        });

        bindClickEvent(layout_share_product, () -> {
            ShareModeDialog dialog = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                @Override
                public void sureItem(int position) {
                    boolean isTimelineCb = false;
                    String url = "http://ax.jmlax.com/download?mall_type="+mall_type+"&id="+product_id;
                    String title = commodityDetailInfoDto.getTitle();
                    if(position == ShareModeDialog.SHARE_PYQ){
                        isTimelineCb = true;
                    }
                    ShareUtil.sendToWeaChat(CommodityDetailActivity.this,isTimelineCb,title,url);
                }
            });
            dialog.show();
        });
    }



    public CommodityDetailInfoDto getCommodityDetailInfoDto() {
        return commodityDetailInfoDto;
    }

    public void setCommodityDetailInfoDto(CommodityDetailInfoDto commodityDetailInfoDto) {
        this.commodityDetailInfoDto = commodityDetailInfoDto;
    }

    /**
     * 获取商品详情(未登录)
     * @param goodsId 商品id
     */
    private void getGoodsDetail(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String,String> map = new HashMap<>();
        map.put("include","user,attrs,freight,isFavorited");
        DataManager.getInstance().getGoodsDetail(new DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<CommodityDetailInfoDto> result) {
                LogUtil.i(TAG, "--RxLog-Thread: onSuccess()");
                if (result != null) {
                    if (result.getData() != null) {
                        commodityDetailInfoDto = result.getData();
                        notifyData(commodityDetailInfoDto);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.i(TAG, "--RxLog-Thread: onError()");
                dissLoadDialog();
            }
        }, mType, goodsId,map);
    }

    /**
     * 获取商品详情(登录)
     * @param goodsId 商品id
     */
    private void getGoodsDetailToken(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String,String> map = new HashMap<>();
        map.put("include","user,attrs,freight,isFavorited");
        DataManager.getInstance().getGoodsDetailToken(new DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<CommodityDetailInfoDto> result) {
                LogUtil.i(TAG, "--RxLog-Thread: onSuccess()");
                if (result != null) {
                    if (result.getData() != null) {
                        commodityDetailInfoDto = result.getData();
                        notifyData(commodityDetailInfoDto);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.i(TAG, "--RxLog-Thread: onError()");
                dissLoadDialog();
            }
        }, mType, goodsId,map);
    }

    /**
     * 添加收藏
     */
    private void addFavorites(String product_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "SMG\\Mall\\Models\\MallProduct");
        map.put("id", product_id);
        DataManager.getInstance().addProductFavorites(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                iv_save.setImageResource(R.mipmap.ic_product_save_red);
                tv_save.setText("取消收藏");
                isFavorite = "true";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                } else {
                    iv_save.setImageResource(R.mipmap.ic_product_save_red);
                    tv_save.setText("取消收藏");
                    isFavorite = "true";
                }
            }
        }, map);
    }

    /**
     * 取消收藏
     */
    private void cancleFavorites(String product_id) {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "SMG\\Mall\\Models\\MallProduct");
        map.put("id", product_id);
        DataManager.getInstance().cancleProductFavorites(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                ToastUtil.showToast("取消收藏成功");
                iv_save.setImageResource(R.mipmap.ic_product_save);
                tv_save.setText("收藏");
                isFavorite = "false";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                } else {
                    iv_save.setImageResource(R.mipmap.ic_product_save);
                    tv_save.setText("收藏");
                    isFavorite = "false";
                }
            }
        }, map);
    }


    /**
     * 添加购物车
     *
     * @param stockId
     */
    private void addShoppingCart(String mall_type, String stockId, String productId, String countBuy) {
        showLoadDialog();
        HashMap<String, Object> map = new HashMap<String, Object>();
        if (stockId != null) {
            map.put("stock_id", stockId);
        } else if (productId != null) {
            map.put("product_id", productId);
        }
        map.put("qty", countBuy);
        DataManager.getInstance().addShoppingCart(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.toast("加入购物车成功");
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, mall_type, map);
    }

    private void getCommentsList() {
        String commented_type = "SMG\\Mall\\Models\\MallProduct";
        DataManager.getInstance().getCommentsList(new DefaultSingleObserver<HttpResult<List<CommentDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentDto>> httpResult) {
                if (httpResult != null && httpResult.getData() != null && httpResult.getData().size() > 0) {
                    ll_evaluate_view.setVisibility(View.VISIBLE);
                    ll_no_evaluate.setVisibility(View.GONE);
                    CommentDto commentDto = httpResult.getData().get(0);
                    rb_evaluate.setRating(Integer.valueOf(commentDto.getScore()));
                    if (commentDto.getUser() != null && commentDto.getUser().getData() != null) {
                        tv_commodity_comments_name.setText(commentDto.getUser().getData().getName());
                    }
                    tv_commodity_comments_msg.setText(commentDto.getComment());
                    if (commentDto.getImages() != null && commentDto.getImages().size() > 0) {
                        recyclerViewImages.setVisibility(View.VISIBLE);
                        recyclerViewImages.setLayoutManager(new GridLayoutManager(CommodityDetailActivity.this, 3));
                        CommodityCommentImageAdapter mAdapter = new CommodityCommentImageAdapter(commentDto.getImages());
                        recyclerViewImages.setAdapter(mAdapter);
                    }
                } else {
                    ll_no_evaluate.setVisibility(View.VISIBLE);
                    ll_evaluate_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, commented_type, product_id);
    }

    @Override
    public void callbackPermissions(String permissions, boolean isSuccess) {

    }

    /**
     * 刷新商品详细数据
     *
     * @param commodityDetailDto
     */
    private void notifyData(CommodityDetailInfoDto commodityDetailDto) {
        if (commodityDetailDto.getImgs() != null) {
            onBannerSuccess(commodityDetailDto.getImgs());
        }
        tv_commodity_info_title.setText("¥" + commodityDetailDto.getTitle());
        tv_commodity_info_price.setText("¥" + commodityDetailDto.getPrice());
        tv_commodity_info_market_price.setText(commodityDetailDto.getMarket_price());
        tv_commodity_info_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        if (commodityDetailDto.getFreight() != null) {
            if (commodityDetailDto.getFreight().getFreight().equals("0")) {
                tv_commodity_info_courier.setText("包邮");
            } else {
                tv_commodity_info_courier.setText(commodityDetailDto.getFreight().getFreight());
            }
        } else {
            tv_commodity_info_courier.setText("包邮");
        }
        tv_item_consume_push_comments.setText("评价" + "(" + commodityDetailDto.getComment_count() + ")");
        tv_item_consume_push_comments_scale.setText(commodityDetailDto.getComment_good_rate() + "%");
        isFavorite = commodityDetailDto.getIsFavorited();
        if (isFavorite.equals("true")) {
            iv_save.setImageResource(R.mipmap.ic_product_save_red);
            tv_save.setText("取消收藏");
        }else {
            iv_save.setImageResource(R.mipmap.ic_product_save);
            tv_save.setText("收藏");
        }
        if (commodityDetailDto.getExt() != null && !TextUtils.isEmpty(commodityDetailDto.getExt().getMin_buy())) {
            tvMinBugNum.setVisibility(View.VISIBLE);
            tvMinBugNum.setText(commodityDetailDto.getExt().getMin_buy() + "个起批");
        }
        WebViewUtil.setWebView(webView, Objects.requireNonNull(this));
        WebViewUtil.loadHtml(webView, commodityDetailDto.getContent());
        dissLoadDialog();
    }


    /**
     * 顶部海报
     */
    public void onBannerSuccess(List<String> bannerList) {
        List<BannerItemDto> slidersDtos = new ArrayList<>();
        for (String item : bannerList) {
            BannerItemDto slidersDto = new BannerItemDto();
            slidersDto.setPath(item);
            slidersDtos.add(slidersDto);
        }
        LoopViewPagerAdapter loopViewPagerAdapter = new LoopViewPagerAdapter(this, banner_vp_container, banner_ll_indicators);
        banner_vp_container.setAdapter(loopViewPagerAdapter);
        loopViewPagerAdapter.setList(slidersDtos);
        banner_vp_container.addOnPageChangeListener(loopViewPagerAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dissLoadDialog();
        WebViewUtil.destroyWebView(webView);
    }

}
