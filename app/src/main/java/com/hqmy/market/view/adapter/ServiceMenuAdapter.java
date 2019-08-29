package com.hqmy.market.view.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.ServiceMenuBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class ServiceMenuAdapter extends BaseQuickAdapter<ServiceMenuBean, BaseViewHolder> {
    public ServiceMenuAdapter() {
        super(R.layout.item_service_menu);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceMenuBean item) {
        helper.setText(R.id.tv_title,item.getTitle());
        GlideUtils.getInstances().loadNormalImg(mContext,(ImageView) helper.getView(R.id.img_logo), Constants.WEB_IMG_URL_UPLOADS+item.getImages());
    }
}
