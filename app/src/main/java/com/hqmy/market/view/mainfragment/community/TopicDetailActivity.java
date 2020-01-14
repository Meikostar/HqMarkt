package com.hqmy.market.view.mainfragment.community;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.bean.TopicDetailDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.ArticleRequest;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.RxKeyboardTool;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.CommentsListAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration_NoFirstLast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 话题详情
 */
public class TopicDetailActivity extends BaseActivity {
    public static final String TOPIC_ID = "topic_id";
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    private ImageView img_user_header;
    private TextView tv_topic_name;
    private TextView tv_topic_site;
    private TextView tv_detail_time;
    private TextView tv_topic_content;
    private NineGridImageView ngl_images;
    private ImageView iv_topic_follow;
    private ImageView iv_topic_follow_alr;
    private LinearLayout layout_reward;
    private TextView tv_reward_num;
    private ImageView img_reward;
    private LinearLayout layout_praise;
    private TextView tv_praise_num;
    private ImageView img_praise;
    private LinearLayout layout_save;
    private TextView tv_save_num;
    private ImageView img_save;
    private ImageView iv_wx;
    private ImageView iv_pyq;
    private TextView tv_article_all_comment;
    private TextView tv_article_no_comment;
    private LinearLayout ll_comment_container;
    @BindView(R.id.recyle_detail_comments)
    RecyclerView recyle_detail_comments;
    @BindView(R.id.refreshlayout_td)
    SmartRefreshLayout refreshlayout_td;
    @BindView(R.id.et_comment)
    TextView et_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    private CommentsListAdapter mCommentsListAdapter;
    private List<CommentDto> commentsList = new ArrayList<CommentDto>();
    private String topicId = null;
    private TopicDetailDto bean;
    private String isFollow = "false";
    private String userId = null;
    private int mPage = 1;
    private String isFavorite = "false";
    private String isLike = "false";

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("热门话题正文");
        initRecyclerView();
        ngl_images.setAdapter(nivAdapter);
    }

    @Override
    public void initData() {
        topicId = getIntent().getExtras().getString(TOPIC_ID);
        getTopicDetail(topicId);
        getCommentsList("Modules\\Project\\Entities\\Post", topicId);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyle_detail_comments.setLayoutManager(layoutManager);
        CustomDividerItemDecoration_NoFirstLast itemDecoration = new CustomDividerItemDecoration_NoFirstLast(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_dddddd_1);
        itemDecoration.setOffsetLeft(DensityUtil.dp2px(16));
        itemDecoration.setOffsetRight(DensityUtil.dp2px(25));
        recyle_detail_comments.addItemDecoration(itemDecoration);
        mCommentsListAdapter = new CommentsListAdapter(this);
        recyle_detail_comments.setAdapter(mCommentsListAdapter);

        initHeaderView();
        mCommentsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_praise://点赞
                        CommentDto commentDto = mCommentsListAdapter.getData().get(position);
                        if(commentDto != null){
                            if(commentDto.getIs_liked().equals("0")){
                                //commentThumsUp(mCommentsListAdapter, view, position);
                            } else{
                                //deteleCommentThumsUp(mCommentsListAdapter, view, position);
                            }
                        }
                        break;
                    }
                }
         });
    }

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_td_detail_head_view, null);
        img_user_header = mHeaderView.findViewById(R.id.img_user_header);
        tv_topic_name =  mHeaderView.findViewById(R.id.tv_topic_name);
        tv_topic_site = mHeaderView.findViewById(R.id.tv_topic_site);
        tv_detail_time = mHeaderView.findViewById(R.id.tv_detail_time);
        tv_topic_content = mHeaderView.findViewById(R.id.tv_topic_content);
        ngl_images = mHeaderView.findViewById(R.id.ngl_images);
        layout_reward = mHeaderView.findViewById(R.id.layout_reward);
        tv_reward_num = mHeaderView.findViewById(R.id.tv_reward_num);
        img_reward = mHeaderView.findViewById(R.id.img_reward);
        layout_praise = mHeaderView.findViewById(R.id.layout_praise);
        tv_praise_num = mHeaderView.findViewById(R.id.tv_praise_num);
        img_praise = mHeaderView.findViewById(R.id.img_praise);
        layout_save = mHeaderView.findViewById(R.id.layout_save);
        tv_save_num = mHeaderView.findViewById(R.id.tv_save_num);
        img_save = mHeaderView.findViewById(R.id.img_save);
        iv_topic_follow = mHeaderView.findViewById(R.id.iv_topic_follow);
        iv_topic_follow_alr = mHeaderView.findViewById(R.id.iv_topic_follow_alr);
        iv_wx = mHeaderView.findViewById(R.id.iv_wx);
        iv_pyq = mHeaderView.findViewById(R.id.iv_pyq);
        tv_article_all_comment = mHeaderView.findViewById(R.id.tv_article_all_comment);
        tv_article_no_comment = mHeaderView.findViewById(R.id.tv_article_no_comment);
        ll_comment_container = mHeaderView.findViewById(R.id.ll_comment_container);
        mCommentsListAdapter.addHeaderView(mHeaderView);
    }

    private void getTopicDetail(String  id) {
        DataManager.getInstance().getTopicDetail(new DefaultSingleObserver<HttpResult<TopicDetailDto>>() {
            @Override
            public void onSuccess(HttpResult<TopicDetailDto> httpResult) {
                super.onSuccess(httpResult);
                setData(httpResult);
            }
            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, id);
    }

    private void getCommentsList(String commented_type, String commented_id) {
        DataManager.getInstance().getCommentsList(new DefaultSingleObserver<HttpResult<List<CommentDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentDto>> httpResult) {
                if(httpResult != null) {
                    setCommentListData(httpResult);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, commented_type, commented_id);
    }


    private void setData(HttpResult<TopicDetailDto> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        bean = httpResult.getData();
        GlideUtils.getInstances().loadRoundImg(TopicDetailActivity.this, img_user_header,
                Constants.WEB_IMG_URL_UPLOADS + bean.getUser().getData().getAvatar());
        tv_topic_name.setText(bean.getUser().getData().getName());
        tv_topic_site.setText(bean.getAddress());
        tv_detail_time.setText(bean.getCreated_at());
        tv_topic_content.setText(bean.getContent());
        ngl_images.setImagesData(bean.getImg(), NineGridImageView.NOSPAN);

        userId= bean.getUser_id();
        if(userId.equals(ShareUtil.getInstance().get(Constants.USER_ID))){
            iv_topic_follow.setVisibility(View.GONE);
        }else{
            iv_topic_follow.setVisibility(View.VISIBLE);
        }

        isLike = bean.getIsLiked();
        if(bean.getIsLiked().equals("true")){
            img_praise.setImageResource(R.mipmap.ic_praise_red);
        }else{
            img_praise.setImageResource(R.mipmap.ic_praise);
        }

        isFollow = bean.getUser().getData().getIsFollowing();
        if (isFollow.equals("true")) {
            iv_topic_follow.setVisibility(View.GONE);
            iv_topic_follow_alr.setVisibility(View.VISIBLE);
        } else {
            iv_topic_follow.setVisibility(View.VISIBLE);
            iv_topic_follow_alr.setVisibility(View.GONE);
        }

        isFavorite = bean.getIsFavorited();
        if(isFavorite.equals("true")){
            img_save.setImageResource(R.mipmap.ic_save_red);
        }else{
            img_save.setImageResource(R.mipmap.ic_save);
        }
    }

    /**
     * 设置评论列表数据
     */
    private void setCommentListData(HttpResult<List<CommentDto>> httpResult) {
        if(httpResult == null || httpResult.getData() == null){
            return;
        }
        if (mPage <= 1) {
            mCommentsListAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                tv_article_all_comment.setVisibility(View.GONE);
                tv_article_no_comment.setVisibility(View.VISIBLE);
            }else{
                tv_article_all_comment.setVisibility(View.VISIBLE);
                tv_article_no_comment.setVisibility(View.GONE);
            }
            refreshlayout_td.finishRefresh();
            refreshlayout_td.setEnableLoadMore(true);
        } else {
            refreshlayout_td.finishLoadMore();
            refreshlayout_td.setEnableRefresh(true);
            mCommentsListAdapter.addData(httpResult.getData());
        }

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                refreshlayout_td.finishLoadMoreWithNoMoreData();
            }
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_topic_follow, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                if(isFollow.equals("false")) {
                    postAttention();
                }
            } else {
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(iv_topic_follow_alr, () -> {
            if (ShareUtil.getInstance().isLogin()) {
                if(isFollow.equals("true")) {
                    deleteAttention();
                }
            } else {
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(layout_reward, () -> {
            if(ShareUtil.getInstance().isLogin()) {
                showRewardDialog(TopicDetailActivity.this);
            }else{
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(layout_praise, () -> {
            if(ShareUtil.getInstance().isLogin()) {
                if (isLike.equals("false")) {
                    postThumsUp();
                } else {
                    detelePostThumsUp();
                }
            }else{
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(layout_save, () -> {
            if(ShareUtil.getInstance().isLogin()) {
                if (isFavorite.equals("false")) {
                    addFavorites();
                } else {
                    cancleFavorites();
                }
            }else{
                gotoActivity(LoginActivity.class);
            }
        });

        bindClickEvent(iv_wx, () -> {
            boolean isTimelineCb = false;
            String url = "http://app.b-market.shop/download?id=" + bean.getId();
            String title = bean.getContent();
            ShareUtil.sendToWeaChat(TopicDetailActivity.this, isTimelineCb, title, url);
        });

        bindClickEvent(iv_pyq, () -> {
            boolean isTimelineCb = true;
            String url = "http://app.b-market.shop/download?id=" + bean.getId();
            String title = bean.getContent();
            ShareUtil.sendToWeaChat(TopicDetailActivity.this, isTimelineCb, title, url);
        });

        refreshlayout_td.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getCommentsList("Modules\\Project\\Entities\\Post", topicId);
            }
        });

        refreshlayout_td.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getCommentsList("Modules\\Project\\Entities\\Post", topicId);
            }
        });

        bindClickEvent(tv_send_comment, () -> {
            sendComment();
        });

    }

    @OnClick({R.id.iv_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 九宫格适配器
     */
    private NineGridImageViewAdapter<String> nivAdapter = new NineGridImageViewAdapter<String>() {
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
            //  Bundle bundle = new Bundle();
            //  bundle.putStringArrayList(ImageContants.INTENT_KEY_DATA, (ArrayList) list);
            //  bundle.putInt(ImageContants.INTENT_KEY_START_POSITION, index);
            //  Intent intent = new Intent(context,ImagePreviewActivity.class);
            //  intent.putExtras(bundle);
            //  context.startActivity(intent);
        }
        @Override
        protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<String> list) {
            return true;
        }
    };

    /**
     * 打赏对话框
     */
    public static void showRewardDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reward_dialog, null);

        TextView tv_money_num = view.findViewById(R.id.tv_money_num);
        ImageView iv_reward_close = view.findViewById(R.id.iv_reward_close);
        iv_reward_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView tvNo = view.findViewById(R.id.tv_sj_num);
        tvNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int num = optRandom();
                tv_money_num.setText(String.valueOf(num));
            }
        });

        TextView tv_reward_opt = view.findViewById(R.id.tv_reward_opt);
        tv_reward_opt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager wm = ((Activity)(context)).getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.width = (int) (d.getWidth()*0.8) ; // 宽度设置为屏幕的0.6
        dialogWindow.setAttributes(p);
        dialog.show();
    }

    private static int optRandom(){
        List<Integer> list=new ArrayList<Integer>();
        list.add(5);
        list.add(10);
        list.add(20);
        list.add(50);
        list.add(100);
        list.add(200);
        Random random = new Random();
        int index = random.nextInt(list.size());
        int ranInt =  list.get(index);
        return ranInt;
    }


    private void postAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", userId);
        map.put("object", "Modules\\Base\\Entities\\User");
        DataManager.getInstance().postAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("关注成功");
                isFollow = "true";
                iv_topic_follow.setVisibility(View.GONE);
                iv_topic_follow_alr.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("关注成功");
                    isFollow = "true";
                    iv_topic_follow.setVisibility(View.GONE);
                    iv_topic_follow_alr.setVisibility(View.VISIBLE);
                } else {
                    ToastUtil.toast("关注失败");
                }
            }
        }, map);
    }

    private void deleteAttention() {
        Map<String, String> map = new HashMap<>();
        map.put("id", userId);
        map.put("object", "Modules\\Base\\Entities\\User");
        DataManager.getInstance().deleteAttention(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> o) {
                ToastUtil.toast("取消关注成功");
                isFollow = "false";
                iv_topic_follow.setVisibility(View.VISIBLE);
                iv_topic_follow_alr.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable throwable) {
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast("取消关注成功");
                    isFollow = "false";
                    iv_topic_follow.setVisibility(View.VISIBLE);
                    iv_topic_follow_alr.setVisibility(View.GONE);
                } else {
                    ToastUtil.toast("取消关注失败");
                }
            }
        }, map);
    }

    /**
     * 删除点赞话题
     */
    private void detelePostThumsUp() {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", bean.getId());
        DataManager.getInstance().canclePraise(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                super.onSuccess(result);
                img_praise.setImageResource(R.mipmap.ic_praise);
                isLike = "false";
                //int thumsUpNum = bean.get();
                //tv_praise_num.setText();
                //adapter.notifyItemChanged(position+1);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                img_praise.setImageResource(R.mipmap.ic_praise);
                isLike = "false";
                //int thumsUpNum = commentData.getLikers_count();
                //commentData.setLikers_count(--thumsUpNum);
                //adapter.notifyItemChanged(position+1);
            }
        }, map);
    }

    /**
     * 点赞话题
     */
    private void postThumsUp() {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", bean.getId());
        DataManager.getInstance().addPraise(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                super.onSuccess(result);
                //int thumsUpNum = commentData.getLikers_count();
                //commentData.setLikers_count(++thumsUpNum);
                 img_praise.setImageResource(R.mipmap.ic_praise_red);
                 isLike = "true";
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    img_praise.setImageResource(R.mipmap.ic_praise_red);
                    isLike = "true";
                }
            }
        }, map);
    }

    /**
     * 添加收藏
     */
    private void addFavorites() {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", bean.getId());
        DataManager.getInstance().addProductFavorites(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                img_save.setImageResource(R.mipmap.ic_save_red);
                isFavorite = "true";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    isFavorite = "true";
                } else {
                    img_save.setImageResource(R.mipmap.ic_save_red);
                }
            }
        }, map);
    }

    /**
     * 取消收藏
     */
    private void cancleFavorites() {
        Map<String, Object> map = new HashMap<>();
        map.put("object", "Modules\\Project\\Entities\\Post");
        map.put("id", bean.getId());
        DataManager.getInstance().cancleProductFavorites(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                ToastUtil.showToast("取消收藏成功");
                img_save.setImageResource(R.mipmap.ic_save);
                isFavorite = "false";
            }

            @Override
            public void onError(Throwable throwable) {
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    isFavorite = "false";
                } else {
                    img_save.setImageResource(R.mipmap.ic_save);
                }
            }
        }, map);
    }

    /**
     * 发送评论
     */
    private void sendComment() {
        String comment = et_comment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            ToastUtil.showToast("请输入评论内容");
            return;
        }
        RxKeyboardTool.hideKeyboard(this, et_comment);
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setObject("Modules\\Project\\Entities\\Post");
        articleRequest.setId(bean.getId());
        articleRequest.setComment(comment);
        DataManager.getInstance().articleComment(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                scroll();
                ToastUtil.showToast("发送成功");
                et_comment.setText("");
                mPage = 1;
                getCommentsList("Modules\\Project\\Entities\\Post", bean.getId());
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    scroll();
                    ToastUtil.showToast("发送成功");
                    et_comment.setText("");
                    mPage = 1;
                    getCommentsList("Modules\\Project\\Entities\\Post", bean.getId());
                }
            }
        }, articleRequest);
    }

    private void scroll() {
        int y = getCommentContainerY();
        if (y > 0) {
            int scrollToY = y - DensityUtil.dp2px(79);
            recyle_detail_comments.smoothScrollBy(0, scrollToY);
        } else {
            recyle_detail_comments.smoothScrollToPosition(1);
        }
    }

    private int getCommentContainerY() {
        int[] location = new int[2];
        ll_comment_container.getLocationOnScreen(location);
        int y = location[1];
        return y;
    }

}
