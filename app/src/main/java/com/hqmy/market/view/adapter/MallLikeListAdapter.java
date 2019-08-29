package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import java.util.List;

/**
 * 联盟商城猜你喜欢列表适配器
 * Created by rzb on 2019/5/20
 */
public class MallLikeListAdapter extends BaseQuickAdapter<NewListItemDto, BaseViewHolder> {
    private Context mContext;

    public MallLikeListAdapter(List<NewListItemDto> data, Context mContext) {
        super(R.layout.item_mall_like_list, data);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewListItemDto item) {
        if (item != null) {
            helper.setText(R.id.tv_item_mall_like_title, item.getTitle());
            helper.setText(R.id.tv_mall_like_price, "¥" + item.getPrice());
            String labelStr = "";
            List<String> labLists = item.getLabels();
            if(labLists != null && labLists.size() > 0) {
                for (int i = 0; i < labLists.size(); i++) {
                    if(i==0){
                        labelStr = labelStr + labLists.get(i);
                    }else {
                        labelStr = labelStr + "," + labLists.get(i);
                    }
                }
            }
            if(labelStr != "") {
                helper.setVisible(R.id.tv_mall_like_type, true);
                helper.setText(R.id.tv_mall_like_type, labelStr);
            }else{
                helper.setVisible(R.id.tv_mall_like_type, false);
            }
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_item_mall_like_img), Constants.WEB_IMG_URL_UPLOADS + item.getImgs().get(0));
            helper.getView(R.id.iv_item_mall_like).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CommodityDetailActivity.PRODUCT_ID, item.getId());
                    bundle.putString(CommodityDetailActivity.MALL_TYPE, item.getType());
                    gotoActivity(CommodityDetailActivity.class, bundle);
                }
            });
        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }
}