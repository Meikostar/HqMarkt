package com.hqmy.market.view.mainfragment.consume;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.view.adapter.FactoryMoreTypeAapter;
import com.hqmy.market.view.widgets.RecyclerItemDecoration;

import java.util.ArrayList;
import butterknife.BindView;

/**
 * 爱心厂家(更多)
 * Created by rzb on 2019/6/24
 */
public class LoveFactoryMoreActivity extends BaseActivity {
    public static final String MORE_TYPE_LIST = "more_type_list";
    @BindView(R.id.iv_love_factory_more_back)
    ImageView    iv_love_factory_more_back;
    @BindView(R.id.recycle_love_factory_type_more)
    RecyclerView recycle_love_factory_type_more;
    private FactoryMoreTypeAapter fmtAdapter;
    private ArrayList<CategoryListdto> categoryLists = new ArrayList<CategoryListdto>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_love_factory_more;
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(LoveFactoryMoreActivity.this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recycle_love_factory_type_more.addItemDecoration(new RecyclerItemDecoration(10, 4));
        recycle_love_factory_type_more.setLayoutManager(gridLayoutManager);
        fmtAdapter = new FactoryMoreTypeAapter(categoryLists,LoveFactoryMoreActivity.this);
        recycle_love_factory_type_more.setAdapter(fmtAdapter);
    }

    @Override
    public void initData() {
        ArrayList<CategoryListdto> categoryListdtoList = (ArrayList<CategoryListdto>) getIntent().getSerializableExtra(MORE_TYPE_LIST);
        categoryLists.addAll(categoryListdtoList);
        fmtAdapter.notifyDataSetChanged();
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_love_factory_more_back, () -> {
            finish();
        });

        fmtAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategoryListdto categoryListdto = fmtAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(LoveFactoryTypeActivity.TYPE_TITLE, categoryListdto.getTitle());
                bundle.putSerializable(LoveFactoryTypeActivity.CATE_ID, categoryListdto.getId());
                gotoActivity(LoveFactoryTypeActivity.class, false, bundle);
            }
        });
    }
}
