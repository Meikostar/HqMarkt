package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.ShippingAddressActivity;
import com.hqmy.market.view.adapter.BabyImageAapter;
import com.hqmy.market.view.widgets.autoview.NoScrollListView;
import com.hqmy.market.view.widgets.dialog.ShareModeDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 宝贝详情
 * Created by rzb on 2019/05/20
 */
public class BabyDetailActivity extends BaseActivity {
    private static final String TAG = BabyDetailActivity.class.getSimpleName();
    public static final String PRODUCT_ID = "product_id";
    public static final String MALL_TYPE = "mall_type";
    private String product_id;
    private String mall_type;

    @BindView(R.id.layout_back)
    LinearLayout   layout_back;
    @BindView(R.id.layout_share_product)
    LinearLayout   layout_share_product;
    @BindView(R.id.img_user_header)
    ImageView        img_user_header;
    @BindView(R.id.tv_baby_name)
    TextView         tv_baby_name;
    @BindView(R.id.tv_baby_time)
    TextView         tv_baby_time;
    @BindView(R.id.tv_baby_price)
    TextView         tv_baby_price;
    @BindView(R.id.tv_baby_content)
    TextView         tv_baby_content;
    @BindView(R.id.layout_save)
    RelativeLayout   layout_save;
    @BindView(R.id.iv_save)
    ImageView        iv_save;
    @BindView(R.id.tv_save)
    TextView         tv_save;
    @BindView(R.id.tv_baby_service)
    TextView         tv_baby_service;
    @BindView(R.id.tv_baby_submit)
    TextView         tv_baby_submit;
    @BindView(R.id.baby_detail_listView)
    NoScrollListView baby_detail_listView;
    private BabyImageAapter mBabyImageAapter;
    private List<String> imgLists = new ArrayList<>();
    private List<AddressDto> mAddressDatas = new ArrayList<>();
    private AddressDto defaultAddress = null;
    private CommodityDetailInfoDto commodityDetailInfoDto;
    private String isFavorite = "false";//是否收藏

    @Override
    public int getLayoutId() {
        return R.layout.ui_baby_detail_layout;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        Bundle  bundle = getIntent().getExtras();
        if(bundle != null) {
            product_id = bundle.getString(PRODUCT_ID);
            mall_type = bundle.getString(MALL_TYPE);
        }
        getAddressListData();
        if(ShareUtil.getInstance().isLogin()) {
            getGoodsDetailToken(mall_type, product_id);
        }else{
            getGoodsDetail(mall_type, product_id);
        }
    }

    @Override
    public void initListener() {

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
                    ShareUtil.sendToWeaChat(BabyDetailActivity.this,isTimelineCb,title,url);
                }
            });
            dialog.show();
        });

          bindClickEvent(layout_back, () -> {
            finish();
          });

          bindClickEvent(tv_baby_service, () -> {
            Bundle bundle = new Bundle();
//            bundle.putString(ConversationActivity.TITLE, commodityDetailInfoDto.getUser().getData().getName());
//            bundle.putString(ConversationActivity.TARGET_ID, commodityDetailInfoDto.getUser().getData().getId());
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

        bindClickEvent(tv_baby_submit, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                if(defaultAddress != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(BuyBabyActivity.PRODUCT_ID, commodityDetailInfoDto.getId());
                    bundle.putSerializable(BuyBabyActivity.ADDRESS_DETAIL, defaultAddress);
                    bundle.putString(BuyBabyActivity.MALL_TYPE, "ax");
                    gotoActivity(BuyBabyActivity.class, true, bundle);
                }else{
                    gotoActivity(ShippingAddressActivity.class);
                }
            } else {
                gotoActivity(LoginActivity.class);
           }
        });
    }

    private void initAdapter(){
        mBabyImageAapter = new BabyImageAapter(BabyDetailActivity.this, imgLists, BabyDetailActivity.this);
        baby_detail_listView.setAdapter(mBabyImageAapter);
    }

    public CommodityDetailInfoDto getCommodityDetailInfoDto() {
        return commodityDetailInfoDto;
    }

    public void setCommodityDetailInfoDto(CommodityDetailInfoDto commodityDetailInfoDto) {
        this.commodityDetailInfoDto = commodityDetailInfoDto;
    }

    /**
     * 获取商品详情(未登录)
     */
    private void getGoodsDetail(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String,String> map = new HashMap<>();
        map.put("include","user,attrs,freight,isFavorited");
        DataManager.getInstance().getGoodsDetail(new DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<CommodityDetailInfoDto> result) {
                LogUtil.i(TAG, "--RxLog-Thread: onSuccess()");
                if(result != null){
                    if(result.getData() != null) {
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
     */
    private void getGoodsDetailToken(String mType, String goodsId) {
        showLoadDialog();
        HashMap<String,String> map = new HashMap<>();
        map.put("include","attrs,freight,isFavorited,brand.productsCount");
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
     * 获取收货地址
     */
    private void getAddressListData() {
        showLoadDialog();
        DataManager.getInstance().getAddressesList(new DefaultSingleObserver<List<AddressDto>>() {
            @Override
            public void onSuccess(List<AddressDto> addressDtos) {
                dissLoadDialog();
                mAddressDatas.clear();
                mAddressDatas.addAll(addressDtos);
                if(mAddressDatas.size() > 0) {
                    for (int i = 0; i < mAddressDatas.size(); i++) {
                        if (mAddressDatas.get(i).getIs_default().equals("1")) {
                            defaultAddress = mAddressDatas.get(i);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    /**
     * 刷新商品详细数据
     */
    private void notifyData(CommodityDetailInfoDto commodityDetailDto) {
        dissLoadDialog();
        if (commodityDetailDto.getImgs()!= null) {
            imgLists.addAll(commodityDetailDto.getImgs());
            mBabyImageAapter.notifyDataSetChanged();
        }
        tv_baby_name.setText(commodityDetailDto.getUser().getData().getName());
        tv_baby_time.setText("发布于 " + commodityDetailDto.getCreated_at());
        tv_baby_price.setText(commodityDetailDto.getScore() + "积分");
        tv_baby_content.setText(commodityDetailDto.getContent());
        GlideUtils.getInstances().loadRoundImg(BabyDetailActivity.this, img_user_header,
                Constants.WEB_IMG_URL_UPLOADS + commodityDetailDto.getUser().getData().getAvatar());
        isFavorite = commodityDetailDto.getIsFavorited();
        if (isFavorite.equals("true")) {
            iv_save.setImageResource(R.mipmap.ic_product_save_red);
            tv_save.setText("取消收藏");
        }else {
            iv_save.setImageResource(R.mipmap.ic_product_save);
            tv_save.setText("收藏");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        dissLoadDialog();
    }
}
