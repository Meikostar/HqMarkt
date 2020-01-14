package com.hqmy.market.view.mainfragment.community;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.bean.CommentListBean;
import com.hqmy.market.bean.NewsDetailDto;
import com.hqmy.market.bean.NewsOtherListItemDto;
import com.hqmy.market.bean.NewsRecommendListItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.WebViewUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.ArticleRequest;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.RxKeyboardTool;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.adapter.CommentsListAdapter;
import com.hqmy.market.view.adapter.NewsAdAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration_NoFirstLast;
import com.hqmy.market.view.widgets.autoview.MaxRecyclerView;

import android.webkit.WebView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新闻详情
 */
public class HuoDonDetailActivity extends BaseActivity {
    public static final String HUODON = "huodon";
    private TextView tv_detail_title;
    private TextView tv_name;
    private TextView txt_follow;
    private TextView  tv_detail_time;
    private WebView webView_huodon;
    @BindView(R.id.refreshlayout_hd)
    SmartRefreshLayout refreshlayout_hd;
    @BindView(R.id.recyclerview_hd)
    RecyclerView  recyclerview_hd;
    @BindView(R.id.et_comment)
    EditText et_comment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;

    private MaxRecyclerView recycle_ad_article;
    private TextView        tv_article_all_comment;
    private TextView        tv_article_no_comment;
    private ImageView       iv_wx;
    private ImageView       iv_pyq;
    private LinearLayout    mCommentContainer;

    private NewsRecommendListItemDto mNewsRecommendListItemDto;
    private NewsOtherListItemDto     mNewsOtherListItemDto;
    private CommentsListAdapter      mCommentsListAdapter;

    private NewsAdAdapter   mNewsAdAdapter;
    private List<BannerItemDto> adLists = new ArrayList<>();
    private String from;
    private int  mPage = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_huodon_detail;
    }

    @Override
    public void initView() {
        refreshlayout_hd.setEnableRefresh(false);
        initRecyclerView();
        initAdapter();
    }

    @Override
    public void initData() {
         from = getIntent().getExtras().getString("from");
        if(TextUtils.isEmpty(from)){
              mNewsRecommendListItemDto = (NewsRecommendListItemDto) getIntent().getExtras().getSerializable(HUODON);
              getNewsDetail(mNewsRecommendListItemDto.getType(), mNewsRecommendListItemDto.getId());
              getCommentsList("SMG\\Page\\PageModel", mNewsRecommendListItemDto.getId());
        }else{
           if("other".equals(from)){
               mNewsOtherListItemDto = (NewsOtherListItemDto)getIntent().getExtras().getSerializable(HUODON);
               getNewsDetail("news", mNewsOtherListItemDto.getId());
               getCommentsList("SMG\\Page\\PageModel", mNewsOtherListItemDto.getId());
           }
        }
        getNewsMiddleBanner();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview_hd.setLayoutManager(layoutManager);
        CustomDividerItemDecoration_NoFirstLast itemDecoration = new CustomDividerItemDecoration_NoFirstLast(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_dddddd_1);
        itemDecoration.setOffsetLeft(DensityUtil.dp2px(16));
        itemDecoration.setOffsetRight(DensityUtil.dp2px(25));
        recyclerview_hd.addItemDecoration(itemDecoration);
        mCommentsListAdapter = new CommentsListAdapter(this);
        recyclerview_hd.setAdapter(mCommentsListAdapter);

        initHeaderView();
        mCommentsListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_praise://点赞
                        CommentDto commentListBean = mCommentsListAdapter.getData().get(position);
                        if(commentListBean != null){
                            if(commentListBean.getIs_liked().equals("0")){
                                commentThumsUp(mCommentsListAdapter, view, position);
                            } else{
                                deteleCommentThumsUp(mCommentsListAdapter, view, position);
                            }
                        }
                        break;
                 }
             }
        });
    }

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_hd_detail_head_view, null);
        tv_detail_title = mHeaderView.findViewById(R.id.tv_detail_title);
        tv_name = mHeaderView.findViewById(R.id.tv_name);
        txt_follow = mHeaderView.findViewById(R.id.txt_follow);
        tv_detail_time = mHeaderView.findViewById(R.id.tv_detail_time);
        iv_wx = mHeaderView.findViewById(R.id.iv_wx);
        iv_pyq = mHeaderView.findViewById(R.id.iv_pyq);
        tv_article_all_comment = mHeaderView.findViewById(R.id.tv_article_all_comment);
        tv_article_no_comment = mHeaderView.findViewById(R.id.tv_article_no_comment);
        mCommentContainer = mHeaderView.findViewById(R.id.ll_comment_container);
        webView_huodon = mHeaderView.findViewById(R.id.webView_huodon);
        recycle_ad_article = mHeaderView.findViewById(R.id.recycle_ad_article);
        mCommentsListAdapter.addHeaderView(mHeaderView);
    }


    private void getNewsDetail(String type, String  id) {
        DataManager.getInstance().getNewsDetail(new DefaultSingleObserver<HttpResult<NewsDetailDto>>() {
            @Override
            public void onSuccess(HttpResult<NewsDetailDto> httpResult) {
                super.onSuccess(httpResult);
                setData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, type, id);
    }

    /**
     * 新闻详情中间广告
     */
    private void getNewsMiddleBanner(){
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                if(result != null) {
                    if (result.getData() != null) {
                        List<BannerItemDto> bannerList = result.getData().getNews_detail_middle();
                        adLists.addAll(bannerList);
                        mNewsAdAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
            }
        },"news_detail_middle");
    }


    private void getCommentsList(String commented_type, String commented_id) {
        DataManager.getInstance().getCommentsList(new DefaultSingleObserver<HttpResult<List<CommentDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentDto>> httpResult) {
                if(httpResult != null) {
                    if(httpResult.getData() != null) {
                        setCommentListData(httpResult);
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, commented_type, commented_id);
    }

    /**
     * 设置评论列表数据
     * @param httpResult
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
            refreshlayout_hd.finishRefresh();
            refreshlayout_hd.setEnableLoadMore(true);
        } else {
            refreshlayout_hd.finishLoadMore();
            refreshlayout_hd.setEnableRefresh(true);
            mCommentsListAdapter.addData(httpResult.getData());
        }

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                refreshlayout_hd.finishLoadMoreWithNoMoreData();
            }
        }
    }

    private void setData(HttpResult<NewsDetailDto> httpResult) {
        if (httpResult == null || httpResult.getData() == null) {
            return;
        }
        NewsDetailDto bean = httpResult.getData();
        tv_detail_title.setText(bean.getTitle());
        tv_name.setText(bean.getAuthor());
        txt_follow.setText("点击量: " + bean.getClick());
        tv_detail_time.setText(bean.getCreated_at());
        WebViewUtil.setWebView(webView_huodon, Objects.requireNonNull(this));
        WebViewUtil.loadHtml(webView_huodon, bean.getContent());
    }

    private void initAdapter() {
        recycle_ad_article.setLayoutManager(new LinearLayoutManager(HuoDonDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        mNewsAdAdapter = new NewsAdAdapter(adLists, HuoDonDetailActivity.this);
        recycle_ad_article.setAdapter(mNewsAdAdapter);
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_wx, () -> {
            if(TextUtils.isEmpty(from)) {
                boolean isTimelineCb = false;
                String url = "http://app.b-market.shop/download?id=" + mNewsRecommendListItemDto.getId();
                String title = mNewsRecommendListItemDto.getTitle();
                ShareUtil.sendToWeaChat(HuoDonDetailActivity.this, isTimelineCb, title, url);
            }else{
                if("other".equals(from)){
                    boolean isTimelineCb = false;
                    String url = "http://app.b-market.shop/download?id=" + mNewsOtherListItemDto.getId();
                    String title = mNewsOtherListItemDto.getTitle();
                    ShareUtil.sendToWeaChat(HuoDonDetailActivity.this, isTimelineCb, title, url);
                }
            }
        });

        bindClickEvent(iv_pyq, () -> {
            if(TextUtils.isEmpty(from)) {
                boolean isTimelineCb = true;
                String url = "http://app.b-market.shop/download?id=" + mNewsRecommendListItemDto.getId();
                String title = mNewsRecommendListItemDto.getTitle();
                ShareUtil.sendToWeaChat(HuoDonDetailActivity.this, isTimelineCb, title, url);
            }else{
                if("other".equals(from)){
                    boolean isTimelineCb = true;
                    String url = "http://app.b-market.shop/download?id=" + mNewsOtherListItemDto.getId();
                    String title = mNewsOtherListItemDto.getTitle();
                    ShareUtil.sendToWeaChat(HuoDonDetailActivity.this, isTimelineCb, title, url);
                }
            }
        });

        bindClickEvent(tv_send_comment, () -> {
            sendComment();
        });

        refreshlayout_hd.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                if(TextUtils.isEmpty(from)){
                    getCommentsList("SMG\\Page\\PageModel", mNewsRecommendListItemDto.getId());
                }else{
                    if("other".equals(from)){
                        getCommentsList("SMG\\Page\\PageModel", mNewsOtherListItemDto.getId());
                    }
                }
            }
        });

        refreshlayout_hd.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                if(TextUtils.isEmpty(from)){
                    getCommentsList("SMG\\Page\\PageModel", mNewsRecommendListItemDto.getId());
                }else{
                    if("other".equals(from)){
                        getCommentsList("SMG\\Page\\PageModel", mNewsOtherListItemDto.getId());
                    }
                }
            }
        });
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
        articleRequest.setObject("SMG\\Page\\PageModel");
        if(TextUtils.isEmpty(from)){
            articleRequest.setId(mNewsRecommendListItemDto.getId());
        }else{
            if("other".equals(from)){
                articleRequest.setId(mNewsOtherListItemDto.getId());
            }
        }
        articleRequest.setComment(comment);
        DataManager.getInstance().articleComment(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                scroll();
                ToastUtil.showToast("发送成功");
                et_comment.setText("");
                mPage = 1;
                if(TextUtils.isEmpty(from)){
                    getCommentsList("SMG\\Page\\PageModel", mNewsRecommendListItemDto.getId());
                }else{
                    if("other".equals(from)){
                        getCommentsList("SMG\\Page\\PageModel", mNewsOtherListItemDto.getId());
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    scroll();
                    ToastUtil.showToast("发送成功");
                    et_comment.setText("");
                    mPage = 1;
                    if(TextUtils.isEmpty(from)){
                        getCommentsList("SMG\\Page\\PageModel", mNewsRecommendListItemDto.getId());
                    }else{
                        if("other".equals(from)){
                            getCommentsList("SMG\\Page\\PageModel", mNewsOtherListItemDto.getId());
                        }
                    }
                }
            }
        }, articleRequest);
    }

    private void scroll() {
        int y = getCommentContainerY();
        if (y > 0) {
            int scrollToY = y - DensityUtil.dp2px(79);
            recyclerview_hd.smoothScrollBy(0, scrollToY);
        } else {
            recyclerview_hd.smoothScrollToPosition(1);
        }
    }

    private int getCommentContainerY() {
        int[] location = new int[2];
        mCommentContainer.getLocationOnScreen(location);
        int y = location[1];
        return y;
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
     * 点赞评论
     */
    private void commentThumsUp(BaseQuickAdapter adapter, View view, int position) {
        CommentListBean commentData = (CommentListBean) adapter.getItem(position);
        Map<String, Object> map = new HashMap<>();
        map.put("object", "SMG\\Comment\\Comment");
        map.put("id", commentData.getId());
        DataManager.getInstance().addPraise(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                super.onSuccess(result);
                commentData.setIs_liked(1);
                int thumsUpNum = commentData.getLikers_count();
                commentData.setLikers_count(++thumsUpNum);
                adapter.notifyItemChanged(position+1);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    commentData.setIs_liked(1);
                    int thumsUpNum = commentData.getLikers_count();
                    commentData.setLikers_count(++thumsUpNum);
                    adapter.notifyItemChanged(position+1);
                }
            }
        }, map);
    }

    /**
     * 删除点赞评论
     */
    private void deteleCommentThumsUp(BaseQuickAdapter adapter, View view, int position) {
        CommentListBean commentData = (CommentListBean) adapter.getItem(position);
        Map<String, Object> map = new HashMap<>();
        map.put("object", "SMG\\Comment\\Comment");
        map.put("id", commentData.getId());
        DataManager.getInstance().canclePraise(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                super.onSuccess(result);
                commentData.setIs_liked(0);
                int thumsUpNum = commentData.getLikers_count();
                commentData.setLikers_count(--thumsUpNum);
                adapter.notifyItemChanged(position+1);
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                commentData.setIs_liked(0);
                int thumsUpNum = commentData.getLikers_count();
                commentData.setLikers_count(--thumsUpNum);
                adapter.notifyItemChanged(position+1);
            }
        }, map);
    }

}
