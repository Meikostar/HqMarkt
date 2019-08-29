package com.hqmy.market.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.hqmy.market.R;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.utils.ShareUtil;

import java.util.List;

/**
 * 收藏 社区
 */
public class CollectCommunityAdapter extends BaseQuickAdapter<TopicListItemDto, BaseViewHolder> {
    public CollectCommunityAdapter() {
        super(R.layout.collect_community_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopicListItemDto item) {
        GlideUtils.getInstances().loadRoundImg(mContext, helper.getView(R.id.img_user_header), Constants.WEB_IMG_URL_UPLOADS + item.getUser().getData().getAvatar());
        helper.setText(R.id.tv_topic_name, item.getUser().getData().getName())
                .setText(R.id.tv_topic_site, item.getAddress())
                .setText(R.id.tv_topic_time, item.getCreated_at())
                .setText(R.id.tv_topic_content, item.getContent())
                .setText(R.id.tv_reward_num, item.getFavoriters_count())
                .setText(R.id.tv_share_num, item.getShares())
                .setText(R.id.tv_comment_num, item.getComments_count())
                .setText(R.id.tv_praise_num, item.getFavoritersCount())
                .setText(R.id.tv_save_num, item.getLikersCount());
        helper.setGone(R.id.tv_topic_follow, false);
        if (item.getUser() != null && item.getUser().getData() != null) {
            if (!ShareUtil.getInstance().getString(Constants.USER_ID, "").equals(item.getUser().getData().getId()) && item.getUser().getData().isFollowing()) {
                helper.setVisible(R.id.tv_topic_follow, true);
                helper.setText(R.id.tv_topic_follow, "已关注");
            } else if (!ShareUtil.getInstance().getString(Constants.USER_ID, "").equals(item.getUser().getData().getId()) && !item.getUser().getData().isFollowing()) {
                helper.setVisible(R.id.tv_topic_follow, true);
                helper.setText(R.id.tv_topic_follow, "+关注");
            }
        }
        NineGridImageView mNglContent = helper.getView(R.id.ngl_images);
        mNglContent.setAdapter(mAdapter);
        mNglContent.setImagesData(item.getImg(), NineGridImageView.NOSPAN);
        helper.addOnClickListener(R.id.content)
                .addOnClickListener(R.id.tv_deleted);
        if(item.getFavorited()){
           helper.setImageResource(R.id.img_praise,R.mipmap.ic_praise_red);
        }else {
            helper.setImageResource(R.id.img_praise,R.mipmap.ic_praise);
        }
        if("true".equals(item.getIsLiked())){
            helper.setImageResource(R.id.img_save,R.mipmap.ic_save_red);
        }else {
            helper.setImageResource(R.id.img_save,R.mipmap.ic_save);
        }
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
//              Bundle bundle = new Bundle();
//              bundle.putStringArrayList(ImageContants.INTENT_KEY_DATA, (ArrayList) list);
//              bundle.putInt(ImageContants.INTENT_KEY_START_POSITION, index);
//              Intent intent = new Intent(context,ImagePreviewActivity.class);
//              intent.putExtras(bundle);
//              context.startActivity(intent);
        }

        @Override
        protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
            return true;
        }
    };
}
