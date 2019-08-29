package com.hqmy.market.view.mainfragment.consume;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.CommodityCommentAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;

import java.util.List;
import butterknife.BindView;

/**
 * 商品评价
 */
public class CommodityCommentActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_item_consume_push_comments)
    TextView tvItemConsumePushComments;
    @BindView(R.id.tv_item_consume_push_comments_scale)
    TextView tvItemConsumePushCommentsScale;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CommodityCommentAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.ui_commodity_comment_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("全部评价");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CommodityCommentAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        String commentCount = getIntent().getStringExtra("commentCount");
        tvItemConsumePushComments.setText("评价(" + commentCount + ")");
        tvItemConsumePushCommentsScale.setText(getIntent().getStringExtra("commentRate")+"%");
        String id = getIntent().getStringExtra("id");
        getCommentsList(id);
    }

    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });
    }

    private void getCommentsList(String commented_id) {
        String commented_type = "SMG\\Mall\\Models\\MallProduct";
        DataManager.getInstance().getCommentsList(new DefaultSingleObserver<HttpResult<List<CommentDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentDto>> httpResult) {
                if (httpResult != null && httpResult.getData() != null && httpResult.getData().size() > 0) {
                    mAdapter.setNewData(httpResult.getData());
                } else {
                    mAdapter.setEmptyView(new EmptyView(CommodityCommentActivity.this));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, commented_type, commented_id);
    }
}
