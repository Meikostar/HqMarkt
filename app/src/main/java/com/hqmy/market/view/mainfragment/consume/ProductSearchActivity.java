package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.HotSearchInfo;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.ProductListActivity;
import com.hqmy.market.view.adapter.RecentSearchProductAdapter;
import com.hqmy.market.view.widgets.RecommendViewGroup;
import com.hqmy.market.view.widgets.autoview.ClearEditText;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

public class ProductSearchActivity extends BaseActivity implements TextView.OnEditorActionListener{
    @BindView(R.id.et_search_procdut)
    ClearEditText et_search_procdut;
    @BindView(R.id.recommendViewGroup)
    RecommendViewGroup recommendViewGroup;
    @BindView(R.id.tv_clear_all)
    TextView tvClearAll;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecentSearchProductAdapter recentSearchAdapter;
    private List<String> productSearchData = new ArrayList<>();
    private String strInclude = null;
    private String mall_type = null;

    @Override
    public void initListener() {
        et_search_procdut.setOnEditorActionListener(this);
        et_search_procdut.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())){

                }else{

                }
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable edit) {

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.product_search_layout;
    }

    @Override
    public void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            strInclude = bundle.getString("includeStr");
        }
        if(strInclude.equals("consume_index")){
            mall_type = "gc";
        }else{
            mall_type = strInclude;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recentSearchAdapter = new RecentSearchProductAdapter();
        recyclerView.setAdapter(recentSearchAdapter);
        recentSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String strSearch = recentSearchAdapter.getItem(position);
                productSearchData.add(strSearch);
                ShareUtil.getInstance().save("productSearchData", new Gson().toJson(productSearchData));
                Bundle bundle = new Bundle();
                bundle.putString(ProductSearchResultActivity.SEARCH_KEY_STR, strSearch);
                gotoActivity(ProductListActivity.class, false, bundle);
            }
        });
    }

    @Override
    public void initData() {
        getHotSearch(strInclude);
    }

    private void getHotSearch(String includeStr) {
        showLoadDialog();
        DataManager.getInstance().getHotSearch(new DefaultSingleObserver<HttpResult<HotSearchInfo>>() {
            @Override
            public void onSuccess(HttpResult<HotSearchInfo> result) {
                dissLoadDialog();
                recommendViewGroup.removeAllViews();
                if (result != null && result.getData() != null && result.getData().getHot_search() != null) {
                    List<String> items = new ArrayList<>();
                    if (result.getData().getHot_search().mdefault != null && result.getData().getHot_search().mdefault.size() > 0) {
                        items.addAll(result.getData().getHot_search().mdefault);

//                        if(strInclude != null){
//                            if(strInclude.equals("consume_index")){
//                                items.addAll(result.getData().getHot_search().getConsume_index());
//                            }else if(strInclude.equals("gc")){
//                                items.addAll(result.getData().getHot_search().getGc());
//                            }else if(strInclude.equals("lm")){
//                                items.addAll(result.getData().getHot_search().getLm());
//                            }else if(strInclude.equals("ax")){
//                                items.addAll(result.getData().getHot_search().getAx());
//                            }else if(strInclude.equals("st")){
//                                items.addAll(result.getData().getHot_search().getSt());
//                            }else if(strInclude.equals("default")){
//
//                            }
//                        }
                    }
                    for (int i = 0; i < items.size(); i++) {
                        View view = getLayoutInflater().inflate(R.layout.hot_search_item, null);
                        TextView textView = view.findViewById(R.id.tv_hot_recent_text);
                        textView.setText(items.get(i));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productSearchData.add(textView.getText().toString());
                                ShareUtil.getInstance().save("productSearchData", new Gson().toJson(productSearchData));
                                Bundle bundle = new Bundle();
                                bundle.putString(ProductSearchResultActivity.SEARCH_KEY_STR, textView.getText().toString());
                                bundle.putString(ProductSearchResultActivity.SEARCH_MALL_TYPE, mall_type);
                                gotoActivity(ProductListActivity.class, false, bundle);
                            }
                        });
                        recommendViewGroup.addView(view);
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "default");
    }

    @OnClick({R.id.iv_search_back, R.id.tv_clear_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_back:
                finish();
                break;
            case R.id.tv_clear_all:
                //清空
                productSearchData.clear();
                ShareUtil.getInstance().save("productSearchData", "");
                recentSearchAdapter.setNewData(null);
                ToastUtil.showToast("清空成功");
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    String searchStr = et_search_procdut.getText().toString();
                    if (TextUtils.isEmpty(searchStr)) {
                        ToastUtil.showToast("请输入搜索内容");
                        return false;
                    }
                    if(productSearchData != null){
                        if(productSearchData.size() > 0){
                            for(int i=0; i<productSearchData.size(); i++){
                                if(searchStr.equals(productSearchData.get(i))){
                                    productSearchData.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    productSearchData.add(et_search_procdut.getText().toString());
                    ShareUtil.getInstance().save("productSearchData", new Gson().toJson(productSearchData));
                    Bundle bundle = new Bundle();
                    bundle.putString(ProductSearchResultActivity.SEARCH_KEY_STR, et_search_procdut.getText().toString());
                    bundle.putString(ProductSearchResultActivity.SEARCH_MALL_TYPE, mall_type);
                    gotoActivity(ProductListActivity.class, false, bundle);
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        String value = ShareUtil.getInstance().get("productSearchData");
        if (value != null && value != "") {
            productSearchData = new Gson().fromJson(value, new TypeToken<List<String>>() {
            }.getType());
            recentSearchAdapter.setNewData(productSearchData);
        }
    }
}
