package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.BrandListItemDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.UrlInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.MyCommonWebView;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.MallBrandListAapter;
import com.hqmy.market.view.adapter.MallLikeListAdapter;
import com.hqmy.market.view.adapter.MallNewListAapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.NoScrollGridView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 联盟商城
 * Created by rzb on 2019/6/22.
 */
public class AllianceMallActivity extends BaseActivity {
    @BindView(R.id.mall_iv_back)
    ImageView  mall_iv_back;
    @BindView(R.id.iv_banner_mall)
    ImageView iv_banner_mall;
    @BindView(R.id.mall_iv_cart)
    ImageView mall_iv_cart;
    @BindView(R.id.recycle_mall_push)
    NoScrollGridView recycle_mall_push;
    @BindView(R.id.recycle_mall_new)
    NoScrollGridView recycle_mall_new;
    @BindView(R.id.recycle_mall_like)
    MaxRecyclerView  recycle_mall_like;
    @BindView(R.id.mall_tv_find)
    TextView mall_tv_find;

    private MallBrandListAapter mMallBrandListAapter;
    private MallNewListAapter   mMallNewListAapter;
    private MallLikeListAdapter mMallLikeListAdapter;
    private List<BrandListItemDto> brandList  = new ArrayList<BrandListItemDto>();
    private List<NewListItemDto>   newList = new ArrayList<NewListItemDto>();
    private List<NewListItemDto> likeLists = new ArrayList<NewListItemDto>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_alliance_mall;
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycle_mall_like.addItemDecoration(new RecyclerItemDecoration(3, 2));
        recycle_mall_like.setLayoutManager(gridLayoutManager);
        initAdapter();
    }

    @Override
    public void initData() {
        getBanner();
        getMallsBrandList();
        getMallsNewList();
        getMallsLikeList();
    }

    @Override
    public void initListener() {
        bindClickEvent(mall_iv_back, () -> {
            finish();
        });

        bindClickEvent(mall_tv_find, () -> {
            Bundle bundle = new Bundle();
            bundle.putString("includeStr", "lm");
            gotoActivity(ProductSearchActivity.class,false, bundle);
        });

        bindClickEvent(mall_iv_cart, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(ShopCartActivity.MALL_TYPE, "lm");
            gotoActivity(ShopCartActivity.class, false, bundle);
        });

        recycle_mall_push.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                    gotoActivity(LoginActivity.class);
                }else{
                    BrandListItemDto brandItem = (BrandListItemDto) mMallBrandListAapter.getItem(position);
                    getMallsBrandUrl(brandItem.getId());
                }
            }
        });

        recycle_mall_new.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(CommodityDetailActivity.PRODUCT_ID, mMallNewListAapter.getItem(position).getId());
                bundle.putString(CommodityDetailActivity.MALL_TYPE, "lm");
                gotoActivity(CommodityDetailActivity.class,false, bundle);
            }
        });
    }

    private void getBanner(){
        showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getUnion_mall_top();
                        if(bannerList != null) {
                            GlideUtils.getInstances().loadNormalImg(AllianceMallActivity.this, iv_banner_mall,
                                    Constants.WEB_IMG_URL_UPLOADS + bannerList.get(0).getPath());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
         },"union_mall_top");
    }

    private void getMallsBrandList(){
        showLoadDialog();
        DataManager.getInstance().getMallsBrandList(new DefaultSingleObserver<HttpResult<List<BrandListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<BrandListItemDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        List<BrandListItemDto> bList= result.getData();
                        if (bList != null) {
                            brandList.addAll(bList);
                            mMallBrandListAapter.notifyDataSetChanged();
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

    private void getMallsBrandUrl(String id){
        showLoadDialog();
        DataManager.getInstance().getMallsBrandUrl(new DefaultSingleObserver<HttpResult<UrlInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<UrlInfoDto> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        UrlInfoDto urlDto = result.getData();
                        if (urlDto != null) {
                            String wUrl = urlDto.getUrl();
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.INTENT_WEB_URL, wUrl);
                            gotoActivity(MyCommonWebView.class, false, bundle);
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, id);
    }

    private void getMallsNewList(){
        showLoadDialog();
        DataManager.getInstance().getNewList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null){
                        List<NewListItemDto>  nliList =  result.getData();
                        if (nliList != null) {
                            newList.addAll(nliList);
                            mMallNewListAapter.notifyDataSetChanged();
                       }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        },"lm");
    }

    private void getMallsLikeList(){
        showLoadDialog();
        DataManager.getInstance().getLikeList(new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> result) {
                dissLoadDialog();
                if(result != null){
                    if(result.getData() != null) {
                        List<NewListItemDto> likeList = result.getData();
                        if (likeList != null) {
                            likeLists.addAll(likeList);
                            mMallLikeListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "lm");
    }

    private void initAdapter(){
        mMallBrandListAapter = new MallBrandListAapter(this,brandList);
        recycle_mall_push.setAdapter(mMallBrandListAapter);

        mMallNewListAapter = new MallNewListAapter(this,newList);
        recycle_mall_new.setAdapter(mMallNewListAapter);

        mMallLikeListAdapter = new MallLikeListAdapter(likeLists,this);
        recycle_mall_like.setAdapter(mMallLikeListAdapter);

    }
}
