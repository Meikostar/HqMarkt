package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.ProductDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.TypeFactoryAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.SortingLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 爱心工厂分类
 * Created by rzb on 2019/5/22.
 */
public class LoveFactoryTypeActivity extends BaseActivity {
    public static final String CATE_ID = "cate_id";
    public static final String TYPE_TITLE = "type_title";
    @BindView(R.id.type_iv_back)
    ImageView  type_iv_back;
    @BindView(R.id.type_tv_title)
    TextView   type_tv_title;
    @BindView(R.id.type_factory_banner)
    ImageView type_factory_banner;
    @BindView(R.id.sorting_layout)
    SortingLayout  sorting_layout;
    @BindView(R.id.recycle_type_factory)
    MaxRecyclerView recycle_type_factory;
    private String cateId = null;
    private String strTitle = null;
    private TypeFactoryAdapter mTypeFactoryAdapter;
    private ArrayList<ProductDto> pList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_factory_type;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        Bundle  bundle = getIntent().getExtras();
        cateId = bundle.getString(CATE_ID);
        strTitle = bundle.getString(TYPE_TITLE);
        type_tv_title.setText(strTitle + "会场");
        getBanner();
        getStProductList(cateId);
    }

    @Override
    public void initListener() {
        bindClickEvent(type_iv_back, () -> {
            finish();
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
                        List<BannerItemDto> bannerList = result.getData().getLove_factory_categories_top();
                        if(bannerList != null) {
                            GlideUtils.getInstances().loadNormalImg(LoveFactoryTypeActivity.this, type_factory_banner,
                                    Constants.WEB_IMG_URL_UPLOADS + bannerList.get(0).getPath());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        },"love_factory_categories_top");
    }

    private void getStProductList(String cateId){
        showLoadDialog();
        DataManager.getInstance().getGcTypeProductList(new DefaultSingleObserver<HttpResult<List<ProductDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<ProductDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        //setDetailView(result.getData());
                        mTypeFactoryAdapter.setNewData(result.getData());
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "gc", cateId);
    }

    private void initAdapter(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTypeFactoryAdapter = new TypeFactoryAdapter(pList,this);
        recycle_type_factory.addItemDecoration(new RecyclerItemDecoration(3, 2));
        recycle_type_factory.setLayoutManager(gridLayoutManager);
        recycle_type_factory.setAdapter(mTypeFactoryAdapter);
    }
}
