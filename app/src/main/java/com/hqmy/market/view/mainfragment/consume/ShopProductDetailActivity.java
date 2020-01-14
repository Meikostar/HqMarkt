package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.ShopProductsAapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 店铺产品详情
 */
public class ShopProductDetailActivity extends BaseActivity {
    @BindView(R.id.iv_shop_banner)
    ImageView ivShopBanner;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.rb_shop)
    BaseRatingBar rbShop;
    @BindView(R.id.tv_shop_follow_number)
    TextView tvShopFollowNumber;
    @BindView(R.id.tv_shop_content)
    TextView tvShopContent;
    @BindView(R.id.shop_detail_top_layout)
    RelativeLayout shopDetailTopLayout;
    @BindView(R.id.tv_service_title)
    TextView tvServiceTitle;
    @BindView(R.id.iv_st_logo)
    ImageView ivStLogo;
    @BindView(R.id.tv_st_product_name)
    TextView tvStProductName;
    @BindView(R.id.tv_st_brief)
    TextView tvStBrief;
    @BindView(R.id.tv_st_label_one)
    TextView tvStLabelOne;
    @BindView(R.id.tv_st_label_two)
    TextView tvStLabelTwo;
    @BindView(R.id.tv_st_price)
    TextView tvStPrice;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.layout_entity_store_item)
    RelativeLayout layoutEntityStoreItem;
    @BindView(R.id.rl_comment)
    RelativeLayout rlComment;
    @BindView(R.id.tv_product_more_title)
    TextView tvProductMoreTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.shop_layout_back)
    LinearLayout shopLayoutBack;
    @BindView(R.id.shop_layout_share)
    LinearLayout shopLayoutShare;
    @BindView(R.id.tv_to_order)
    TextView tvToOrder;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private String shopId, mall_type, productId;
    private ShopProductsAapter shopProductsAapter;
    private CommodityDetailInfoDto commodityDetailInfoDto;
    @Override
    public void initListener() {
        bindClickEvent(shopLayoutBack, () -> {
            finish();
        });
        bindClickEvent(shopLayoutShare, () -> {
            ShareModeDialog dialog = new ShareModeDialog(this, new ShareModeDialog.DialogListener() {
                @Override
                public void sureItem(int position) {
                    boolean isTimelineCb = false;
                    String url = "http://app.b-market.shop/download?mall_type=" + mall_type + "&id=" + productId + "&from=ShopDetailActivity";
                    String title = "实体店铺分享";
                    if (position == ShareModeDialog.SHARE_PYQ) {
                        isTimelineCb = true;
                    }
                    ShareUtil.sendToWeaChat(ShopProductDetailActivity.this, isTimelineCb, title, url);
                }
            });
            dialog.show();
        });
        bindClickEvent(rlComment, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("shopId", shopId);
            if(commodityDetailInfoDto != null && commodityDetailInfoDto.getShop() != null && commodityDetailInfoDto.getShop().getData() != null){
                bundle.putString("shopName", commodityDetailInfoDto.getShop().getData().getShop_name());
                bundle.putString("followCount", commodityDetailInfoDto.getShop().getData().getFollow_count());
                bundle.putFloat("averageScore", commodityDetailInfoDto.getShop().getData().getComment_average_score());
                bundle.putString("content", commodityDetailInfoDto.getContent());
            }
            gotoActivity(ShopCommentActivity.class,false,bundle);
        });
        bindClickEvent(tvToOrder, () -> {
            if(TextUtils.isEmpty(ShareUtil.getInstance().getString(Constants.USER_TOKEN, ""))){
                gotoActivity(LoginActivity.class);
                return;
            }
            //立即预定
            Bundle bundle = new Bundle();
            bundle.putString("productId", productId);
            bundle.putString("mall_type", mall_type);
            bundle.putString("shopId",shopId);
            gotoActivity(ShopProductCommitOrderActivity.class,false,bundle);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_product_detail;
    }

    @Override
    public void initView() {
        shopId = getIntent().getStringExtra("shopId");
        mall_type = getIntent().getStringExtra("mall_type");
        productId = getIntent().getStringExtra("productId");
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        shopProductsAapter = new ShopProductsAapter();
        recyclerView.setAdapter(shopProductsAapter);
    }

    @Override
    public void initData() {
        getGoodsDetail();
        getProductList();
    }


    private void getGoodsDetail() {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("extra_include", "follow_count,is_follow");
        map.put("include", "shop");
        DataManager.getInstance().getGoodsDetail(new DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<CommodityDetailInfoDto> result) {
                if (result != null && result.getData() != null) {
                    commodityDetailInfoDto = result.getData();
                    if (commodityDetailInfoDto.getShop() != null && commodityDetailInfoDto.getShop().getData() != null) {
                        GlideUtils.getInstances().loadNormalImg(ShopProductDetailActivity.this, ivShopBanner, Constants.WEB_IMG_URL_UPLOADS + commodityDetailInfoDto.getShop().getData().getBackground_img());
                        tvShopName.setText(commodityDetailInfoDto.getShop().getData().getShop_name());
                        rbShop.setRating(commodityDetailInfoDto.getShop().getData().getComment_average_score());
                        tvShopFollowNumber.setText("关注量：" + commodityDetailInfoDto.getShop().getData().getFollow_count());
                    }
                    tvStProductName.setText(commodityDetailInfoDto.getTitle());
                    tvShopContent.setText(Html.fromHtml(commodityDetailInfoDto.getContent()));
                    if (commodityDetailInfoDto.getParameter() != null){
                        StringBuilder builder = new StringBuilder();
                        for(Map.Entry<String, String> entry : commodityDetailInfoDto.getParameter().entrySet()){
                            String mapKey = entry.getKey();
                            String mapValue = entry.getValue();
                            builder.append(mapKey+":"+mapValue);
                            builder.append("\r\n");
                        }
                        tvStBrief.setText(builder.toString());
                    }else {
                        tvStBrief.setText("");
                    }
                    if(commodityDetailInfoDto.getLabels() != null) {
                        tvStLabelOne.setText(commodityDetailInfoDto.getLabels().get(0));
                        tvStLabelTwo.setText(commodityDetailInfoDto.getLabels().get(1));
                    }
                    tvStPrice.setText("¥"+commodityDetailInfoDto.getPrice());
                    tvTotalPrice.setText("¥"+commodityDetailInfoDto.getPrice());
                    GlideUtils.getInstances().loadNormalImg(ShopProductDetailActivity.this, ivStLogo, Constants.WEB_IMG_URL_UPLOADS + commodityDetailInfoDto.getCover());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.i(TAG, "--RxLog-Thread: onError()");
                dissLoadDialog();
            }
        }, mall_type, productId, map);
    }

    private void getProductList() {
        showLoadDialog();
        Map<String, String> map = new HashMap<>();
        map.put("filter[shop_id]", shopId);
//        map.put("page", "1");
        map.put("sort[]", "-sales_count");

        DataManager.getInstance().getFamilyLikeList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if (result != null && result.getData() != null && result.getData().size() > 0) {
                    shopProductsAapter.setNewData(result.getData());
                }else {
                    shopProductsAapter.setEmptyView(new EmptyView(ShopProductDetailActivity.this));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, mall_type, map);
    }
}
