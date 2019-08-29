package com.hqmy.market.view.mainfragment.consume;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ProductTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布宝贝商品类型
 */
public class ProductTypeActivity extends BaseActivity {
    public static final int CHOICE_PRODUCT_TYPE = 4001;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.product_type_listview)
    ListView  product_type_listview;
    private ProductTypeAdapter mAdapter;
    private List<CategoryListdto> cateList = new ArrayList<CategoryListdto>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_type;
    }

    @Override
    public void initView() {
        mTitleText.setText("选择类型");
        mAdapter = new ProductTypeAdapter(ProductTypeActivity.this,cateList);
        product_type_listview.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        getFamilyTypes();
    }
    @Override
    public void initListener() {
        product_type_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("catedto",(CategoryListdto)mAdapter.getItem(position));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void getFamilyTypes(){
        showLoadDialog();
        DataManager.getInstance().getCategoryList(new DefaultSingleObserver<HttpResult<List<CategoryListdto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CategoryListdto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        cateList.addAll(result.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, "ax");
    }

    @OnClick({R.id.iv_title_back,R.id.tv_title_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
