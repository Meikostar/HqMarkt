package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import java.util.List;

public class NewsAdAdapter extends BaseQuickAdapter<BannerItemDto, BaseViewHolder> {

    public NewsAdAdapter(List<BannerItemDto> data, Context mContext) {
        super(R.layout.item_news_ads_list_layout, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, BannerItemDto item) {
        helper.setText(R.id.tv_news_ad, item.getExtend().getExtra1())
                .setText(R.id.tv_new_ad_des, item.getExtend().getExtra2())
                .setText(R.id.tv_new_ad_num, item.getExtend().getExtra3());
        GlideUtils.getInstances().loadNormalImg(mContext,helper.getView(R.id.iv_news_ad),
                Constants.WEB_IMG_URL_UPLOADS + item.getPath());
        helper.getView(R.id.layout_news_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle = new Bundle();
                //bundle.putSerializable(HuoDonDetailActivity.HUODON, item);
                //gotoActivity(HuoDonDetailActivity.class, bundle);
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
