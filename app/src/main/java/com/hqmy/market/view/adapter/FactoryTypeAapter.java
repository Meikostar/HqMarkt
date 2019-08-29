package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.view.mainfragment.consume.LoveFactoryMoreActivity;
import com.hqmy.market.view.mainfragment.consume.LoveFactoryTypeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 爱心工厂类别适配器
 * Created by rzb on 2019/4/20
 */
public class FactoryTypeAapter extends BaseQuickAdapter<CategoryListdto, BaseViewHolder> {
    private ArrayList<CategoryListdto> cList = null;

    public FactoryTypeAapter(List<CategoryListdto> cateLists, ArrayList<CategoryListdto> allCateLists, Context context) {
        super(R.layout.item_factory_type, cateLists);
        this.mContext = context;
        this.cList = allCateLists;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryListdto item) {
        if(item != null) {
            helper.setText(R.id.tv_factoty_type_home, item.getTitle());
        }

        helper.getView(R.id.layout_item_factory_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!item.getTitle().equals("首页")){
                    if(item.getTitle().equals("更多")){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(LoveFactoryMoreActivity.MORE_TYPE_LIST, cList);
                        gotoActivity(LoveFactoryMoreActivity.class, bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putString(LoveFactoryTypeActivity.TYPE_TITLE, item.getTitle());
                        bundle.putSerializable(LoveFactoryTypeActivity.CATE_ID, item.getId());
                        gotoActivity(LoveFactoryTypeActivity.class, bundle);
                    }
                }
            }
        });
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}
