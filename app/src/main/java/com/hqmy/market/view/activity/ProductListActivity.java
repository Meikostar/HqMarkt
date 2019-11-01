package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.ExtDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ScreenSizeUtil;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.common.utils.SwipeRefreshLayoutUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.db.bean.ProductListType;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.adapter.BrandImgAdapter;
import com.hqmy.market.view.adapter.ProductLabeAdapter;
import com.hqmy.market.view.adapter.ProductListAdapter;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;
import com.hqmy.market.view.widgets.autoview.ClearEditText;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.autoview.SuperSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品列表
 * Created by dahai on 2019/01/18.
 */
public class ProductListActivity extends BaseActivity {
    private static final String TAG = ProductListActivity.class.getSimpleName();

    @BindView(R.id.refreshLayout)
    SuperSwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_shop_product_1)
    TextView                tv_shop_product_1;
    @BindView(R.id.tv_search)
    TextView                tv_search;


    @BindView(R.id.tv_shop_product_2)
    TextView tv_shop_product_2;

    @BindView(R.id.ll_shop_product_3)
    LinearLayout ll_shop_product_3;

    @BindView(R.id.tv_shop_product_3)
    TextView tv_shop_product_3;
    @BindView(R.id.iv_labe)
    ImageView iv_labe;
    @BindView(R.id.iv_shop_product_3)
    ImageView iv_shop_product_3;

    @BindView(R.id.ll_shop_product_4)
    LinearLayout ll_shop_product_4;

    @BindView(R.id.tv_shop_product_4)
    TextView tv_shop_product_4;

    @BindView(R.id.iv_shop_product_4)
    ImageView iv_shop_product_4;

    @BindView(R.id.recy_shop_product_list)
    RecyclerView recyclerView;


    public static final String ACTION_SEARCH_KEY = "search_key_str"; //调用者传参名字
    public static final String ACTION_SEARCH_ID = "search_key_id"; //调用者传参名字
    @BindView(R.id.acb_status_bar)
    ImageView            acbStatusBar;
    @BindView(R.id.actionbar_back)
    ImageView            actionbarBack;
    @BindView(R.id.et_search)
    ClearEditText        etSearch;
    @BindView(R.id.grid)
    GridView             gridView;
    @BindView(R.id.h_scroll)
    HorizontalScrollView hScroll;
    private             String                  searchKey;
    private             String                  id;
    public static final String                  PRODUCT_TYPE = "product_type";//调用者传递的名字
    private             int                     mCategoryId  = 1;//产品类型:
    private             ProductListAdapter      mAdapter;
    private             ProductLabeAdapter      mLabAdapter;
    private             BrandImgAdapter         adapter;
    private             HashMap<String, String> mParamsMaps;
    private             int                     loadDataType = ProductListType.synthesize.getType(); //加载数据类型
    private             int                     mCurrentPage = 1;
    private             SwipeRefreshLayoutUtil  mSwipeRefreshLayoutUtil;


    @Override
    public void initView() {

        StatusBarUtils.StatusBarLightMode(this);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(0, 2));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new ProductListAdapter();
        mLabAdapter = new ProductLabeAdapter(this);
        adapter = new BrandImgAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        searchKey = getIntent().getStringExtra(ACTION_SEARCH_KEY);
        id = getIntent().getStringExtra(ACTION_SEARCH_ID);
        mCategoryId = getIntent().getIntExtra(PRODUCT_TYPE, 0);
        mParamsMaps = new HashMap<>();
        mParamsMaps.put("rows", Constants.PAGE_SIZE + "");
       if(TextUtil.isNotEmpty(searchKey)){
           etSearch.setText(searchKey);
       }
        setTopTitlesView(tv_shop_product_1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_product_list_layout;
    }


    private void iniGridView(final List<String> list) {

        int length = 78;  //定义一个长度
        int size = 0;  //得到集合长度
        //获得屏幕分辨路
        DisplayMetrics dm = new DisplayMetrics();
        if(dm==null||this==null){
            return;
        }
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int gridviewWidth = (int) (list.size() * (length + 2) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, 0, 0);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(ScreenSizeUtil.dp2px(3)); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(list.size()); // 设置列数量=列表集合数
        adapter.setData(list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ProductListActivity.this, BrandShopDetailActivity.class);
                intent.putExtra("id",lists.get(position));
                startActivity(intent);

            }
        });
    }
    private List<String> list=new ArrayList<>();
    private List<String> lists=new ArrayList<>();
    private void getProductListData() {
        showLoadDialog();
        mParamsMaps.put("page", mCurrentPage + "");
        mParamsMaps.put("include_products_brands", 1 + "");
        mParamsMaps.put("include", "brand.category");

        if (TextUtil.isNotEmpty(searchKey)) {
            mParamsMaps.put("filter[scopeSearch]", searchKey);
        } else {
            mParamsMaps.remove("filter[scopeSearch]");
        }
        if (TextUtil.isNotEmpty(id)) {
            mParamsMaps.put("filter[category_id]", id);
        } else {
            mParamsMaps.remove("filter[category_id]");
        }


        DataManager.getInstance().findGoodsList(mParamsMaps, new DefaultSingleObserver<HttpResult<List<NewListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NewListItemDto>> data) {
                dissLoadDialog();
                list.clear();
                lists.clear();
                if (null != data.getData() && data.getData().size() > 0) {

                    if (mCurrentPage == 1) {
                        datas.clear();
                        datas.addAll(data.getData());
                        mAdapter.setNewData(data.getData());
                        refreshLayout.setRefreshing(false);
                    } else {
                        datas.addAll(data.getData());
                        mAdapter.addData(data.getData());
                        refreshLayout.setLoadMore(false);
                    }
                    for(ExtDto dto:data.getMeta().brand){
                        if(TextUtil.isNotEmpty(dto.logo)){
                            list.add(dto.logo);
                            lists.add(dto.id);
                        }
                    }
                    if(list.size()>0){
                        hScroll.setVisibility(View.VISIBLE);
                        iniGridView(list);
                    }else {
                        hScroll.setVisibility(View.GONE);
                    }
                } else {
                    EmptyView emptyView = new EmptyView(ProductListActivity.this);
                    hScroll.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(searchKey)) {
                        mAdapter.setNewData(data.getData());
                        emptyView.setTvEmptyTip(String.format("没搜索到%s相关数据", searchKey));
                    } else {
                        emptyView.setTvEmptyTip("暂无商品数据");
                    }
                    mAdapter.setEmptyView(emptyView);


                }
                mSwipeRefreshLayoutUtil.isMoreDate(mCurrentPage, Constants.PAGE_SIZE, data.getMeta().getPagination().getTotal());
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (mCurrentPage == 1) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refreshLayout.setLoadMore(false);
                }
            }
        });
    }
    private List<NewListItemDto> datas=new ArrayList<>();
    private boolean isLabe;
    @Override
    public void initListener() {
        iv_labe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLabe){
                    isLabe=false;
                    iv_labe.setImageResource(R.drawable.lb_s);
                    recyclerView.addItemDecoration(new RecyclerItemDecoration(0, 2));
                    recyclerView.setLayoutManager(new GridLayoutManager(ProductListActivity.this, 2));

//                    List<NewListItemDto> data = mLabAdapter.getData();
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setNewData(datas);
                }else {
//                    List<NewListItemDto> data = mAdapter.getData();
                    recyclerView.addItemDecoration(new RecyclerItemDecoration(0, 1));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ProductListActivity.this);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(mLabAdapter);
                    mLabAdapter.setNewData(datas);
                    iv_labe.setImageResource(R.drawable.lb_h);
                    isLabe=true;
                }
            }
        });
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtil.isNotEmpty(searchKey)) {
                    getProductListData();
                } else {
                    ToastUtil.showToast("请输入关键词");
                }

            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    getProductListData();
                    closeKeyBoard();

                    return true;
                }
                return false;
            }
        });
        bindClickEvent(tv_shop_product_1, () -> {
            setTopTitlesView(tv_shop_product_1);
        });
        bindClickEvent(actionbarBack, () -> {
            finish();
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
                    getProductListData();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bindClickEvent(tv_shop_product_2, () -> {
            setTopTitlesView(tv_shop_product_2);
        });
        bindClickEvent(ll_shop_product_3, () -> {
            setTopTitlesView(ll_shop_product_3);
            setTopTitles3View();
        });
        bindClickEvent(ll_shop_product_4, () -> {
            //            setTopTitlesView(ll_shop_product_4);

        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommodityDetailInfoDto item = (CommodityDetailInfoDto) adapter.getItem(position);
                startActivityCommodityDetail(item.getId());
            }
        });
        mSwipeRefreshLayoutUtil = new SwipeRefreshLayoutUtil();
        mSwipeRefreshLayoutUtil.setSwipeRefreshView(refreshLayout, new SwipeRefreshLayoutUtil.OnRefreshAndLoadMoreListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                getProductListData();
            }

            @Override
            public void onLoadMore() {
                // mSwipeRefreshLayoutUtil.setCanLoadMore(true);
                mCurrentPage++;
                getProductListData();
            }
        });
    }


    private void setTopTitlesView(View view) {
        mCurrentPage = 1;

        switch (view.getId()) {
            case R.id.tv_shop_product_1:
                loadDataType = ProductListType.synthesize.getType();
                tv_shop_product_1.setSelected(true);
                tv_shop_product_2.setSelected(false);
                tv_shop_product_3.setSelected(false);
                tv_shop_product_4.setSelected(false);
                iv_shop_product_3.setTag("priceSelect");
                iv_shop_product_3.setImageResource(R.mipmap.shop_product_price_litre);

                mParamsMaps.put("sort", "");
                getProductListData();
                break;
            case R.id.tv_shop_product_2:
                loadDataType = ProductListType.sales.getType();
                tv_shop_product_1.setSelected(false);
                tv_shop_product_2.setSelected(true);
                tv_shop_product_3.setSelected(false);
                tv_shop_product_4.setSelected(false);
                iv_shop_product_3.setTag("priceSelect");
                iv_shop_product_3.setImageResource(R.mipmap.shop_product_price_litre);

                getProductListData();
                break;
            case R.id.ll_shop_product_3:
                tv_shop_product_1.setSelected(false);
                tv_shop_product_2.setSelected(false);
                tv_shop_product_3.setSelected(true);
                tv_shop_product_4.setSelected(false);
                break;
            case R.id.ll_shop_product_4:
                loadDataType = ProductListType.screening.getType();
                tv_shop_product_1.setSelected(false);
                tv_shop_product_2.setSelected(false);
                tv_shop_product_3.setSelected(false);
                tv_shop_product_4.setSelected(true);
                iv_shop_product_3.setTag("priceSelect");
                iv_shop_product_3.setImageResource(R.mipmap.shop_product_price_litre);
                break;
        }
    }

    private void setTopTitles3View() {
        mCurrentPage = 1;
        mParamsMaps.put("prop", "gd_price");
        if (iv_shop_product_3.getTag().equals("priceSelect")) {
            loadDataType = ProductListType.priceLitre.getType();
            iv_shop_product_3.setTag("unPriceSelect");
            iv_shop_product_3.setImageResource(R.mipmap.shop_product_price_drop);
            mParamsMaps.put("sort", "price");
        } else {
            loadDataType = ProductListType.priceDrop.getType();
            iv_shop_product_3.setTag("priceSelect");
            iv_shop_product_3.setImageResource(R.mipmap.shop_product_price_litre);
            mParamsMaps.put("sort", "-price");
        }
        getProductListData();
    }

    /**
     * 启动商品详细
     *
     * @param product_id 商品ID
     */
    private void startActivityCommodityDetail(String product_id) {
        Bundle bundle = new Bundle();
        bundle.getString(CommodityDetailActivity.PRODUCT_ID, product_id);
        gotoActivity(CommodityDetailActivity.class, false, bundle);
    }



}
