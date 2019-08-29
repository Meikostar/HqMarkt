package com.hqmy.market.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.hqmy.market.R;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.List;

public class ShopCommentAdapter extends BaseQuickAdapter<CommentDto, BaseViewHolder> {
    public ShopCommentAdapter() {
        super(R.layout.item_shop_comment);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentDto item) {
        helper.setText(R.id.tv_topic_name, item.getUser().getData().getName())
                .setText(R.id.tv_topic_time, item.getCreated_at())
                .setText(R.id.tv_topic_content, item.getComment());
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.img_user_header), Constants.WEB_IMG_URL_UPLOADS + item.getUser().getData().getAvatar());
        NineGridImageView mNglContent = helper.getView(R.id.ngl_images);
        mNglContent.setAdapter(mAdapter);
        mNglContent.setImagesData(item.getImages(), NineGridImageView.NOSPAN);
        helper.setText(R.id.tv_share_num, item.getShare_count())
                .setText(R.id.tv_comment_num, item.getComments_count())
                .setText(R.id.tv_praise_num, item.getLikers_count());
    }

    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            GlideUtils.getInstances().loadNormalImg(context, imageView, Constants.WEB_IMG_URL_UPLOADS + s);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
        }

        @Override
        protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
            return true;
        }
    };
}
