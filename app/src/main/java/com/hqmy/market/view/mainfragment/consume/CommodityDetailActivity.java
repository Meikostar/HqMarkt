package com.hqmy.market.view.mainfragment.consume;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.MapDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.WebViewUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.ShopStoreDetailActivity;
import com.hqmy.market.view.adapter.CommodityCommentImageAdapter;
import com.hqmy.market.view.adapter.ShopItemAdapter;
import com.hqmy.market.view.widgets.RegularListView;
import com.hqmy.market.view.widgets.ShopProductTypeDialog;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;

/**
 * 商品详细
 * Created by rzb on 2019/05/20
 */
public class CommodityDetailActivity extends BaseActivity implements BaseActivity.PermissionsListener {
    private static final String TAG        = CommodityDetailActivity.class.getSimpleName();
    public static final  String PRODUCT_ID = "product_id";
    public static final  String MALL_TYPE  = "mall_type";
    public static final  String FROM       = "from";
    @BindView(R.id.iv_shop)
    ImageView      ivShop;
    @BindView(R.id.tv_shop)
    TextView       tvShop;
    @BindView(R.id.iv_car)
    ImageView      ivCar;
    @BindView(R.id.tv_car)
    TextView       tvCar;
    @BindView(R.id.rl_car)
    RelativeLayout rlCar;
    @BindView(R.id.iv_service)
    ImageView      ivService;
    @BindView(R.id.tv_service)
    TextView       tvService;
    @BindView(R.id.header_commodity_detail_bottom_layout)
    LinearLayout   headerCommodityDetailBottomLayout;

    @BindView(R.id.fl_line)
    FrameLayout    flLine;
    @BindView(R.id.ll_money)
    LinearLayout   llMoney;
    @BindView(R.id.ll_commodity_comments)
    RelativeLayout llCommodityComments;
    @BindView(R.id.iv_image)
    ImageView      ivImage;
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.tv_cout)
    TextView       tvCout;
    @BindView(R.id.ll_brank)
    LinearLayout   llBrank;
    @BindView(R.id.iv_left)
    ImageView      ivLeft;
    @BindView(R.id.iv_right)
    ImageView      ivRight;
    @BindView(R.id.mall_iv_back)
    ImageView      mallIvBack;
    @BindView(R.id.topLayout)
    LinearLayout   topLayout;
    private String product_id;
    private String mall_type;

    @BindView(R.id.layout_back)
    LinearLayout   layout_back;
    @BindView(R.id.tv_commodity_info_title)
    TextView       tv_commodity_info_title;
    @BindView(R.id.tv_commodity_info_market_price)
    TextView       tv_commodity_info_market_price;
    @BindView(R.id.tv_commodity_info_price)
    TextView       tv_commodity_info_price;
    @BindView(R.id.tv_commodity_info_courier)
    TextView       tv_commodity_info_courier;
    @BindView(R.id.tv_content1)
    TextView       tv_content1;
    @BindView(R.id.tv_content2)
    TextView       tv_content2;
    @BindView(R.id.tv_content3)
    TextView       tv_content3;
    @BindView(R.id.tv_content4)
    TextView       tv_content4;
    @BindView(R.id.tv_item_consume_push_comments)
    TextView       tv_item_consume_push_comments;
    @BindView(R.id.tv_item_consume_push_comments_scale)
    TextView       tv_item_consume_push_comments_scale;
    @BindView(R.id.webView_commodity)
    WebView        webView;
    @BindView(R.id.banner)
    Banner         banner;
    @BindView(R.id.layout_service)
    RelativeLayout layout_service;
    @BindView(R.id.layout_save)
    RelativeLayout layout_save;
    @BindView(R.id.iv_save)
    ImageView      iv_save;
    @BindView(R.id.tv_save)
    TextView       tv_save;
    @BindView(R.id.layout_shop)
    RelativeLayout layout_shop;
    @BindView(R.id.tv_shop_cart_submit)
    TextView       tv_shop_cart_submit;
    @BindView(R.id.tv_add_cart)
    TextView       tv_add_cart;
    @BindView(R.id.ll_evaluate_view)
    LinearLayout   ll_evaluate_view;
    @BindView(R.id.tv_commodity_comments_name)
    TextView       tv_commodity_comments_name;
    @BindView(R.id.tv_commodity_comments_msg)
    TextView       tv_commodity_comments_msg;
    @BindView(R.id.ll_evaluate_view_notice)
    RelativeLayout  ll_evaluate_view_notice;
    @BindView(R.id.ll_no_evaluate)
    LinearLayout    ll_no_evaluate;
    @BindView(R.id.rb_evaluate)
    BaseRatingBar   rb_evaluate;
    @BindView(R.id.recyclerView_images)
    RecyclerView    recyclerViewImages;
    @BindView(R.id.tv_min_bug_num)
    TextView        tvMinBugNum;
    @BindView(R.id.layout_share_product)
    LinearLayout    layout_share_product;
    @BindView(R.id.line)
    View            line;
    @BindView(R.id.list_info)
    RegularListView list_info;

    private CommodityDetailInfoDto commodityDetailInfoDto;
    private String                 isFavorite = "false";//是否收藏
    private String                 fromStr;

    @Override
    public int getLayoutId() {
        return R.layout.ui_commodity_detail_layout;
    }
    private ShopItemAdapter mAdapter;
    @Override
    public void initView() {
        mAdapter=new ShopItemAdapter(this);
        list_info.setAdapter(mAdapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ShareUtil.getInstance().isLogin()) {
            getAddressListData();
        }

    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product_id = bundle.getString(PRODUCT_ID);
            mall_type = bundle.getString(MALL_TYPE);
            fromStr = bundle.getString(FROM);
        }
//        if (fromStr != null) {
//            if (fromStr.equals("consume_home") || fromStr.equals("gc")) {
//                layout_shop.setVisibility(View.VISIBLE);
//            } else {
//                layout_shop.setVisibility(View.GONE);
//            }
//        }
        if (ShareUtil.getInstance().isLogin()) {
            getGoodsDetailToken(mall_type, product_id);
        } else {
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
            if(commodityDetailInfoDto.getShop()!=null&&commodityDetailInfoDto.getShop().getData()!=null){
                Bundle bundle = new Bundle();
                bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID, commodityDetailInfoDto.getShop().getData().id);
                gotoActivity(ShopStoreDetailActivity.class, false, bundle);
            }

        });

        bindClickEvent(layout_service, () -> {
            HashMap<String, String> clientInfo = new HashMap<>();
            clientInfo.put("name", ShareUtil.getInstance().get(Constants.USER_NAME));
            clientInfo.put("avatar", ShareUtil.getInstance().get(Constants.USER_HEAD));
            Intent intent = new MQIntentBuilder(this)
                    .setClientInfo(clientInfo)
                    .build();
            startActivity(intent);
//            bundle.putString(ConversationActivity.TITLE, "客服");
//            bundle.putString(ConversationActivity.TARGET_ID, commodityDetailInfoDto.getSlug());
//            gotoActivity(ConversationActivity.class, true, bundle);
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

        bindClickEvent(rlCar, () -> {


            if (ShareUtil.getInstance().isLogin()) {
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 3);
                gotoActivity(MainActivity.class, true, bundle);
            } else {
                gotoActivity(LoginActivity.class);
            }
        });
        bindClickEvent(tv_add_cart, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                if(commodityDetailInfoDto==null){
                    return;
                }
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
        bindClickEvent(tv_shop_cart_submit, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                ShopProductTypeDialog dialog = new ShopProductTypeDialog(this, commodityDetailInfoDto, commodityDetailInfoDto.getAttrs(),
                        commodityDetailInfoDto.getCover(), commodityDetailInfoDto.getPrice(), commodityDetailInfoDto.getTitle(),
                        new ShopProductTypeDialog.ShopProductTypeListener() {
                            @Override
                            public void callbackSelect(String stockId, String productId, String countBuy) {
                                //if(stockId != null) {

                                Bundle bundle = new Bundle();

                                bundle.putString(ConfirmOrderActivity.MALL_TYPE, "default");
                                if(TextUtil.isNotEmpty(countBuy)){

                                    bundle.putString("countBuy", countBuy);
                                }
                                if(TextUtil.isNotEmpty(stockId)){

                                    bundle.putString("stock_id", stockId);
                                }else {
                                    bundle.putString("product_id", product_id);
                                }

                                if(defaultAddress!=null){
                                    bundle.putString("id", defaultAddress.getId()+"");
                                    bundle.putSerializable(ConfirmOrderActivity.ADDRESS_DETAIL, defaultAddress);
                                }

                                gotoActivity(ConfirmOrderActivity.class, false, bundle);
                                //}else{
                                //    ToastUtil.showToast("没有库存");
                                //}
                            }
                        }, false);
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
                    String url = "http://ax.jmlax.com/download?mall_type=" + mall_type + "&id=" + product_id;
                    String title = commodityDetailInfoDto.getTitle();
                    if (position == ShareModeDialog.SHARE_PYQ) {
                        isTimelineCb = true;
                    }
                    ShareUtil.sendToWeaChat(CommodityDetailActivity.this, isTimelineCb, title, url);
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
     *
     * @param goodsId 商品id
     */
    private void getGoodsDetail(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("include", "attrs,freight,isFavorited,brand.productsCount,shop");
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
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "default", goodsId, map);
    }
    /**
     * 获取收货地址
     */
    private List<AddressDto> mAddressDatas = new ArrayList<>();
   private AddressDto defaultAddress;
    private void getAddressListData() {
        //showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                //dissLoadDialog();
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                if (mAddressDatas.size() > 0) {
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if (mAddressDatas.get(i).getIs_default().equals("1")) {
                            defaultAddress = mAddressDatas.get(i);
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        });
    }
    /**
     * 获取商品详情(登录)
     *
     * @param goodsId 商品id
     */
    private void getGoodsDetailToken(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("include", "attrs,freight,isFavorited,brand.productsCount,shop");
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
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "default", goodsId, map);
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
                tv_save.setText("收藏");
                tv_save.setTextColor(getResources().getColor(R.color.my_color_FC6f48));
                isFavorite = "true";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                } else {
                    iv_save.setImageResource(R.mipmap.ic_product_save_red);
                    tv_save.setText("收藏");
                    tv_save.setTextColor(getResources().getColor(R.color.my_color_FC6f48));
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
                tv_save.setTextColor(getResources().getColor(R.color.my_color_f79));
                isFavorite = "false";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                } else {
                    iv_save.setImageResource(R.mipmap.ic_product_save);
                    tv_save.setTextColor(getResources().getColor(R.color.my_color_f79));
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
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "default", map);
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
                    rb_evaluate.setClickable(false);
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
            startBanner(commodityDetailDto.getImgs());
        }
        tv_commodity_info_title.setText(commodityDetailDto.getTitle());
        tv_commodity_info_price.setText( commodityDetailDto.getPrice());
        tv_commodity_info_market_price.setText("¥" + commodityDetailDto.getMarket_price());
        List<MapDto> mList = new ArrayList<>();
        MapDto mapDtos = new MapDto();
        if (commodityDetailDto.getFreight() != null) {

            if (Double.valueOf(commodityDetailDto.getFreight().getFreight())==0) {
                mapDtos.setKey("快递");
                mapDtos.setValue("包邮");
                mList.add(mapDtos);
            } else {
                mapDtos.setKey("快递");
                mapDtos.setValue("¥" + commodityDetailDto.getFreight().getFreight());
                mList.add(mapDtos);

            }
        } else {
            mapDtos.setKey("快递");
            mapDtos.setValue("包邮");
            mList.add(mapDtos);
        }


        if(commodityDetailDto.getParameter() != null){
            Map<String,String> paramMap = commodityDetailDto.getParameter();
            Set set = paramMap.keySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = (String) paramMap.get(key);
                MapDto mapDto = new MapDto();
                mapDto.setKey(key);
                mapDto.setValue(value);
                mList.add(mapDto);
            }
        }
        mAdapter.setData(mList);
//        String name = "¥" + commodityDetailDto.getMarket_price();
//        TextPaint textPaint = new TextPaint();
//        textPaint.setTextSize(12);
//        int with = (int) textPaint.measureText(name);
//        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) line.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20
//
//        linearParams.width = ScreenSizeUtil.dp2px(with - 1);// 控件的宽强制设成30
//
//        line.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        tv_commodity_info_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        if (commodityDetailDto.getFreight() != null) {
            if (Double.valueOf(commodityDetailDto.getFreight().getFreight())==0) {
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
            tv_save.setText("收藏");
            tv_save.setTextColor(getResources().getColor(R.color.my_color_FC6f48));
        } else {
            iv_save.setImageResource(R.mipmap.ic_product_save);
            tv_save.setText("收藏");
            tv_save.setTextColor(getResources().getColor(R.color.my_color_f79));
        }
        if (commodityDetailDto.labels != null && commodityDetailDto.labels.size()>0) {
            tvMinBugNum.setVisibility(View.VISIBLE);
            tvMinBugNum.setText(commodityDetailDto.labels.get(0));
        }
        WebViewUtil.setWebView(webView, Objects.requireNonNull(this));
        WebViewUtil.loadHtml(webView, commodityDetailDto.getContent());
        dissLoadDialog();

        if(commodityDetailDto.brand!=null&&commodityDetailDto.brand.data!=null){
            if(TextUtil.isNotEmpty(commodityDetailDto.brand.data.name)){
                tvName.setText(commodityDetailDto.brand.data.name);
            }
            if(TextUtil.isNotEmpty(commodityDetailDto.brand.data.productsCount)){
              tvCout.setText(commodityDetailDto.brand.data.productsCount+"件");
            }
            GlideUtils.getInstances().loadRoundCornerImg(this,ivImage,3,commodityDetailDto.brand.data.logo,R.drawable.moren_product);
            llBrank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommodityDetailActivity.this, BrandShopDetailActivity.class);
                    intent.putExtra("id",commodityDetailDto.brand.data.id);
                    startActivity(intent);
                }
            });
            llBrank.setVisibility(View.VISIBLE);
        }else {
            llBrank.setVisibility(View.GONE);
        }
    }
    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            String slidersDto = (String) path;
            String imgStr = slidersDto;
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadNormalImg(CommodityDetailActivity.this, imageView, imgStr, R.mipmap.img_default_2);
                } else {
                    GlideUtils.getInstances().loadNormalImg(CommodityDetailActivity.this, imageView, Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_2);
                }
            }


        }
    }
    private void startBanner(List<String> data) {
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
    public void onDestroy() {
        super.onDestroy();
        dissLoadDialog();
        WebViewUtil.destroyWebView(webView);
    }


}
