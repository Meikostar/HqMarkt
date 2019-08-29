package com.hqmy.market.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lwkandroid.imagepicker.data.ImageBean;

import com.hqmy.market.R;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;


public class UploadPicAdapter extends BaseQuickAdapter<ImageBean, BaseViewHolder> {
    private int maxImgCount;
    private String mType;

    public UploadPicAdapter(List<ImageBean> list, int maxImgCount, String type) {
        super(R.layout.item_reason_upload_pic_item, list);
        this.maxImgCount = maxImgCount;
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {
        if (!Constants.IMAGEITEM_DEFAULT_ADD.equals(item.getImagePath())) {
            helper.setGone(R.id.ll_del, true)
                    .addOnClickListener(R.id.ll_del);
            GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.iv_pic), item.getImagePath());
        } else {
            helper.setGone(R.id.ll_del, false);
            helper.setImageResource(R.id.iv_pic, R.mipmap.upload_pic_default);
        }
    }

    public void refresh() {
        if (getItemCount() > maxImgCount && Constants.IMAGEITEM_DEFAULT_ADD.equals(getData().get(getItemCount() - 1).getImagePath())) {
            //del last
            getData().remove(getItemCount() - 1);
        }else if (getItemCount() < maxImgCount) {
            if (getItemCount() == 0 || (!Constants.IMAGEITEM_DEFAULT_ADD.equals(getData().get(getItemCount() - 1).getImagePath()) && getItemCount() < maxImgCount)) {
                ImageBean imageItem = new ImageBean();
                imageItem.setImagePath(Constants.IMAGEITEM_DEFAULT_ADD);
                getData().add(imageItem);
            }
        }
        notifyDataSetChanged();
    }
}
