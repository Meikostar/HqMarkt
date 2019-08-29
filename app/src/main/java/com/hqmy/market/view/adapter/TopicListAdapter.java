package com.hqmy.market.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.ninegridimageview.ItemImageClickListener;
import com.jaeger.ninegridimageview.ItemImageLongClickListener;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.hqmy.market.R;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.PostViewHolder> {
    private LayoutInflater     mInflater;
    private List<TopicListItemDto> mPostList;
    TopListItemClick childClick;
    private Context context;

    public TopicListAdapter(Context context,TopListItemClick childClick,List<TopicListItemDto> mPostList) {
        super();
        this.context = context;
        this.childClick = childClick;
        this.mPostList = mPostList;
        mInflater = LayoutInflater.from(context);
    }

    public void setNewData(List<TopicListItemDto> mPostList) {
        this.mPostList = mPostList;
        notifyDataSetChanged();
    }

    public void addData(List<TopicListItemDto> mPostList) {
        if( this.mPostList == null){
            this.mPostList = new ArrayList<>();
        }
        if(mPostList != null){
            this.mPostList.addAll(mPostList);
            notifyDataSetChanged();
        }
    }

    public List<TopicListItemDto> getmPostList() {
        return mPostList;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bind(mPostList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mPostList != null ? mPostList.size() : 0;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostViewHolder(mInflater.inflate(R.layout.topic_item_post_grid_style, parent, false));
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout              layout_topic_item;
        private NineGridImageView<String> mNglContent;
        private ImageView                 img_user_header;
        private TextView                  tv_topic_follow;
        private TextView                  tv_nickname;
        private TextView                  tv_site;
        private TextView                  tv_time;
        private TextView                  tv_content;
        private TextView                  tv_reward_num;
        private TextView                  tv_share_num;
        private TextView                  tv_comment_num;
        private TextView                  tv_praise_num;
        private TextView                  tv_save_num;
        private LinearLayout              layout_reward;
        private LinearLayout              layout_share;
        private LinearLayout              layout_comment;
        private LinearLayout              layout_praise;
        private LinearLayout              layout_save;

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

        public PostViewHolder(View itemView) {
            super(itemView);
            layout_topic_item = itemView.findViewById(R.id.layout_topic_item);
            tv_nickname = (TextView) itemView.findViewById(R.id.tv_topic_name);
            tv_site = (TextView) itemView.findViewById(R.id.tv_topic_site);
            mNglContent = (NineGridImageView<String>) itemView.findViewById(R.id.ngl_images);
            tv_time = (TextView) itemView.findViewById(R.id.tv_topic_time);
            tv_content = itemView.findViewById(R.id.tv_topic_content);
            img_user_header = (ImageView) itemView.findViewById(R.id.img_user_header);
            tv_topic_follow = itemView.findViewById(R.id.tv_topic_follow);
            tv_site = (TextView) itemView.findViewById(R.id.tv_topic_site);
            tv_reward_num = (TextView) itemView.findViewById(R.id.tv_reward_num);
            tv_share_num = (TextView) itemView.findViewById(R.id.tv_share_num);
            tv_comment_num = (TextView) itemView.findViewById(R.id.tv_comment_num);
            tv_praise_num = (TextView) itemView.findViewById(R.id.tv_praise_num);
            tv_save_num = (TextView) itemView.findViewById(R.id.tv_save_num);
            layout_reward = (LinearLayout)itemView.findViewById(R.id.layout_reward);
            layout_share =(LinearLayout) itemView.findViewById(R.id.layout_share);
            layout_comment =(LinearLayout) itemView.findViewById(R.id.layout_comment);
            layout_praise =(LinearLayout) itemView.findViewById(R.id.layout_praise);
            layout_save = (LinearLayout)itemView.findViewById(R.id.layout_save);


            mNglContent.setAdapter(mAdapter);
            mNglContent.setItemImageClickListener(new ItemImageClickListener<String>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    Log.d("onItemImageClick", list.get(index));
                }
            });
            mNglContent.setItemImageLongClickListener(new ItemImageLongClickListener<String>() {
                @Override
                public boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
                    Log.d("onItemImageLongClick", list.get(index));
                    return true;
                }
            });
        }

        public void bind(TopicListItemDto post, int position) {
            mNglContent.setImagesData(post.getImg(), NineGridImageView.NOSPAN);
            tv_content.setText(post.getContent());
            tv_nickname.setText(post.getUser().getData().getName());
            if(post.getAddress() != null) {
                tv_site.setVisibility(View.VISIBLE);
                tv_site.setText(post.getAddress());
            }else{
                tv_site.setVisibility(View.GONE);
            }
            tv_time.setText(post.getCreated_at());
            tv_reward_num.setText(post.getFavoriters_count());
            tv_share_num.setText(post.getShares());
            tv_comment_num.setText(post.getComments_count());
            tv_praise_num.setText(post.getClick());
            tv_save_num.setText(post.getLikers_count());

            GlideUtils.getInstances().loadRoundImg(context, img_user_header, Constants.WEB_IMG_URL_UPLOADS + post.getUser().getData().getAvatar());

            tv_topic_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (childClick != null) {
                        childClick.share(post, position);
                    }
                }
            });

            layout_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (childClick != null) {
                        childClick.share(post, position);
                    }
                }
            });
            layout_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (childClick != null) {
                        childClick.comment(post, position);
                    }
                }
            });
            tv_praise_num.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (childClick != null) {
                        childClick.thumbUpClick(post, position);
                    }
                }
            });

            layout_topic_item.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(childClick != null){
                        childClick.onItemClick(post, position);
                    }
                }
            });
        }
    }

    public interface TopListItemClick {
        /*关注*/
        public void follow(TopicListItemDto post, int position);
        /*分享*/
        public void share(TopicListItemDto post, int position);
        /*评论*/
        public void comment(TopicListItemDto post, int position);
        /*点赞*/
        public void thumbUpClick(TopicListItemDto post, int position);
        /*整个item项*/
        public void onItemClick(TopicListItemDto post, int position);
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}

