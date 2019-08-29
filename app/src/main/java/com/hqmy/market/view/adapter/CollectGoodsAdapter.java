package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class CollectGoodsAdapter extends BaseQuickAdapter<TopicListItemDto, BaseViewHolder> {
    public CollectGoodsAdapter() {
        super(R.layout.collect_good_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicListItemDto item) {
        if (item.getImgs() != null && item.getImgs().size()>0){
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_collect_goods_icon), Constants.WEB_IMG_URL_UPLOADS + item.getImgs().get(0));
        }else {
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_collect_goods_icon), R.drawable.glide_default_picture);
        }
        if (item.isFlag2()){
            //积分商品
            helper.setText(R.id.tv_collect_goods_price, item.getScore()+"积分");
        }else {
            helper.setText(R.id.tv_collect_goods_price, "￥" + item.getPrice());
        }
        helper .setText(R.id.tv_collect_goods_title, item.getTitle());
        if (item.getShop() != null && item.getShop().getData() != null) {
            helper.setText(R.id.tv_collect_goods_store, item.getShop().getData().getShop_name());
        } else {
            helper.setText(R.id.tv_collect_goods_store, "联盟商场自营");
        }
        helper.addOnClickListener(R.id.content)
                .addOnClickListener(R.id.tv_deleted);
    }
}
