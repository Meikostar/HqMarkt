package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.ProductDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.TypeFactoryAdapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;
import com.hqmy.market.view.widgets.autoview.SortingLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 产品搜索结果页面
 * Created by rzb on 2019/5/22.
 */
public class ProductSearchResultActivity extends BaseActivity {
    public static final String SEARCH_KEY_STR = "search_key_str";
    public static final String SEARCH_MALL_TYPE = "search_mall_type";
    @BindView(R.id.search_result_iv_back)
    ImageView  search_result_iv_back;
    @BindView(R.id.search_result_tv_title)
    TextView   search_result_tv_title;
    @BindView(R.id.sorting_layout)
    SortingLayout  sorting_layout;
    @BindView(R.id.recycle_search_result)
    MaxRecyclerView recycle_search_result;
    private String strTitle = null;
    private TypeFactoryAdapter mTypeFactoryAdapter;
    private ArrayList<ProductDto> pList = new ArrayList<>();
    private String mall_type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_result;
    }

    @Override
    public void initView() {
        initAdapter();
    }

    @Override
    public void initData() {
        Bundle  bundle = getIntent().getExtras();
        strTitle = bundle.getString(SEARCH_KEY_STR);
        mall_type = bundle.getString(SEARCH_MALL_TYPE);
        search_result_tv_title.setText(strTitle);
        getSearchResultProductList(strTitle);
    }

    @Override
    public void initListener() {
        bindClickEvent(search_result_iv_back, () -> {
            finish();
        });
    }

    private void getSearchResultProductList(String titleStr){
        showLoadDialog();
        DataManager.getInstance().getSearchResultProductList(new DefaultSingleObserver<HttpResult<List<ProductDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<ProductDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if (result.getData() != null) {
                        if (result.getData().size() > 0) {
                            mTypeFactoryAdapter.setNewData(result.getData());
                        } else {
                            mTypeFactoryAdapter.setNewData(result.getData());
                            EmptyView emptyView = new EmptyView(ProductSearchResultActivity.this);
                            emptyView.setTvEmptyTip("暂无数据");
                            mTypeFactoryAdapter.setEmptyView(emptyView);
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, mall_type, titleStr);
    }

    private void initAdapter(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTypeFactoryAdapter = new TypeFactoryAdapter(pList,this);
        recycle_search_result.addItemDecoration(new RecyclerItemDecoration(3, 2));
        recycle_search_result.setLayoutManager(gridLayoutManager);
        recycle_search_result.setAdapter(mTypeFactoryAdapter);
    }
}
