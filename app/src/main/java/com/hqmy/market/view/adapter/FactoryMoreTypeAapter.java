package com.hqmy.market.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.CategoryListdto;

import java.util.List;

/**
 * 爱心工厂类别适配器
 * Created by rzb on 2019/4/20
 */
public class FactoryMoreTypeAapter extends BaseQuickAdapter<CategoryListdto, BaseViewHolder> {

    public FactoryMoreTypeAapter(List<CategoryListdto> cateLists, Context context) {
        super(R.layout.item_more_factory_type, cateLists);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryListdto item) {
        if(item != null) {
            helper.setText(R.id.tv_factoty_type_home, item.getTitle());
        }
    }
}
