package com.hqmy.market.view.mainfragment.consume;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
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
import com.hqmy.market.bean.CommentListBean;
import com.hqmy.market.bean.HeadLineDetailDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.common.utils.WebViewUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.request.ArticleRequest;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.RxKeyboardTool;
import com.hqmy.market.view.adapter.ArticleDetailCommentAdapter;
import com.hqmy.market.view.widgets.CustomDividerItemDecoration_NoFirstLast;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import butterknife.BindView;

/**
 * 爱心头条详情
 */
public class HeadLineDetailActivity extends BaseActivity {
    public static final String ARTICLE_ID = "article_id";
    @BindView(R.id.tv_title_text)
    TextView           mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView  iv_title_back;
    @BindView(R.id.et_comment)
    EditText           mEditTextComment;
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView       mRecyclerView;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    WebView mWebView;
    private ArticleDetailCommentAdapter mAdapter;
    private String          mArticleId;
    private int             mPage = 1;
    private TextView            mArticleTitle;
    private TextView            mArticleAuthor;
    private TextView            mArticleTime;
    private TextView            mArticleClick;
    private TextView            mArticleNoComment;
    private TextView            mArticleAllComment;
    private LinearLayout        mCommentContainer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_head_line_detail;
    }

    @Override
    public void initView() {
        mTitleText.setText("爱心详情");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mArticleId = bundle.getString(ARTICLE_ID);
        }
    }

    @Override
    public void initData() {
        mRefreshLayout.setEnableRefresh(false);
        initRecyclerView();
        getHeadLineDetail(mArticleId);
        getArticleCommontListData();
    }


    @Override
    public void initListener() {

        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(tv_send_comment, () -> {
            sendComment();
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                getArticleCommontListData();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                ++mPage;
                getArticleCommontListData();
            }
        });
    }

    /**
     * 获取文章评论列表
     */
    private void getArticleCommontListData() {
        Map<String, String> map = new HashMap<>();
        map.put("object", "SMG\\Page\\PageModel");
        map.put("id", mArticleId);
        map.put("include", "user");
        map.put("page", mPage + "");

        DataManager.getInstance().getDynamicCommentList(new DefaultSingleObserver<HttpResult<List<CommentListBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentListBean>> httpResult){
                setCommentListData(httpResult);
            }

            @Override
            public void onError(Throwable throwable) {
            }
        }, map);
    }

    /**
     * 设置评论列表数据
     * @param httpResult
     */
    private void setCommentListData(HttpResult<List<CommentListBean>> httpResult) {
        if(httpResult == null || httpResult.getData() == null){
            return;
        }
        if (mPage <= 1) {
            mAdapter.setNewData(httpResult.getData());
            if (httpResult.getData() == null || httpResult.getData().size() == 0) {
                mArticleAllComment.setVisibility(View.GONE);
                mArticleNoComment.setVisibility(View.VISIBLE);
            }else{
                mArticleAllComment.setVisibility(View.VISIBLE);
                mArticleNoComment.setVisibility(View.GONE);
            }
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.finishLoadMore();
            mRefreshLayout.setEnableRefresh(true);
            mAdapter.addData(httpResult.getData());
        }

        if(httpResult.getMeta()!=null && httpResult.getMeta().getPagination()!=null){
            if(httpResult.getMeta().getPagination().getTotal_pages() == httpResult.getMeta().getPagination().getCurrent_page()){
                mRefreshLayout.finishLoadMoreWithNoMoreData();
            }
        }
    }

    /**
     * 获取头条详情
     */
    private void getHeadLineDetail(String id) {
        DataManager.getInstance().getHeadLineDetail(new DefaultSingleObserver<HttpResult<HeadLineDetailDto>>() {
            @Override
            public void onSuccess(HttpResult<HeadLineDetailDto> result) {
                if(result.getData() != null){
                    HeadLineDetailDto  hdDto = result.getData();
                    setDetailData(hdDto);
                }
            }
            @Override
            public void onError(Throwable throwable) {
            }
        }, id);
    }

    /**
     * 设置文章详情数据
     */
     private void setDetailData(HeadLineDetailDto headLineDetailDto) {
         mArticleTitle.setText(headLineDetailDto.getTitle());
         mArticleAuthor.setText(headLineDetailDto.getAuthor());
         mArticleTime.setText(headLineDetailDto.getCreated_at());
         mArticleClick.setText(headLineDetailDto.getClick());
         WebViewUtil.setWebView(mWebView, Objects.requireNonNull(this));
         WebViewUtil.loadHtml(mWebView, headLineDetailDto.getContent());
     }

    /**
     * 发送评论
     */
    private void sendComment() {
        String comment = mEditTextComment.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            ToastUtil.showToast("请输入评论内容");
            return;
        }
        RxKeyboardTool.hideKeyboard(this, mEditTextComment);
        ArticleRequest articleRequest = new ArticleRequest();
        articleRequest.setObject("SMG\\Page\\PageModel");
        articleRequest.setId(mArticleId);
        articleRequest.setComment(comment);
        DataManager.getInstance().articleComment(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                scroll();
                ToastUtil.showToast("发送成功");
                mEditTextComment.setText("");
                mPage = 1;
                getArticleCommontListData();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if (ApiException.getInstance().isSuccess()) {
                    scroll();
                    ToastUtil.showToast("发送成功");
                    mEditTextComment.setText("");
                    mPage = 1;
                    getArticleCommontListData();
                }
            }
        }, articleRequest);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        CustomDividerItemDecoration_NoFirstLast itemDecoration = new CustomDividerItemDecoration_NoFirstLast(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider_dddddd_1);
        itemDecoration.setOffsetLeft(DensityUtil.dp2px(16));
        itemDecoration.setOffsetRight(DensityUtil.dp2px(25));
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new ArticleDetailCommentAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        initHeaderView();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_praise://点赞,取消点赞
                        CommentListBean commentListBean = mAdapter.getData().get(position);
                        if(commentListBean != null){
                           if(commentListBean.getIs_liked() == 0){
                               commentThumsUp(mAdapter, view, position);
                           } else{
                               deteleCommentThumsUp(mAdapter, view, position);
                           }
                        }
                    break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initHeaderView() {
        View mHeaderView = View.inflate(this, R.layout.layout_article_detail_head_view, null);
        mArticleTitle = mHeaderView.findViewById(R.id.tv_article_title);
        mArticleAuthor = mHeaderView.findViewById(R.id.tv_article_author);
        mArticleTime = mHeaderView.findViewById(R.id.tv_article_time);
        mArticleClick = mHeaderView.findViewById(R.id.tv_article_click);
        mArticleAllComment = mHeaderView.findViewById(R.id.tv_article_all_comment);
        mArticleNoComment = mHeaderView.findViewById(R.id.tv_article_no_comment);
        mCommentContainer = mHeaderView.findViewById(R.id.ll_comment_container);
        mWebView = mHeaderView.findViewById(R.id.webView);
        mAdapter.addHeaderView(mHeaderView);
    }

    private int getCommentContainerY() {
            int[] location = new int[2];
            mCommentContainer.getLocationOnScreen(location);
            int y = location[1];
            return y;
    }

    private void scroll() {
        int y = getCommentContainerY();
        if (y > 0) {
            int scrollToY = y - DensityUtil.dp2px(79);
            mRecyclerView.smoothScrollBy(0, scrollToY);
        } else {
            mRecyclerView.smoothScrollToPosition(1);
        }
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            WebViewUtil.destroyWebView(mWebView);
        }
    }
}
