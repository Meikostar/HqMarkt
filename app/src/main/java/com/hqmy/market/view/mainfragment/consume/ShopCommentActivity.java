package com.hqmy.market.view.mainfragment.consume;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ShopCommentAdapter;
import com.hqmy.market.view.widgets.autoview.EmptyView;
import com.hqmy.market.view.widgets.ratingbar.BaseRatingBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;

public class ShopCommentActivity extends BaseActivity {
    @BindView(R.id.recy_layout)
    RecyclerView recyclerView;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.rb_shop)
    BaseRatingBar rbShop;
    @BindView(R.id.tv_shop_follow_number)
    TextView tvShopFollowNumber;
    @BindView(R.id.tv_shop_content)
    TextView tvShopContent;
    @BindView(R.id.shop_detail_top_layout)
    RelativeLayout shopDetailTopLayout;
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.fl_all)
    FrameLayout flAll;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.fl_good)
    FrameLayout flGood;
    @BindView(R.id.tv_bad)
    TextView tvBad;
    @BindView(R.id.fl_bad)
    FrameLayout flBad;
    @BindView(R.id.tv_has_imags)
    TextView tvHasImags;
    @BindView(R.id.fl_has_imags)
    FrameLayout flHasImags;

    private String id;
    ShopCommentAdapter mAdapter;
    private int position = 0;

    @Override
    public void initListener() {
        bindClickEvent(ivTitleBack, () -> {
            finish();
        });
        bindClickEvent(flAll, () -> {
            //全部
            position = 0;
            tvAll.setSelected(true);
            tvGood.setSelected(false);
            tvBad.setSelected(false);
            tvHasImags.setSelected(false);
            getData();
        });
        bindClickEvent(flGood, () -> {
            //好评
            position = 1;
            tvAll.setSelected(false);
            tvGood.setSelected(true);
            tvBad.setSelected(false);
            tvHasImags.setSelected(false);
            getData();
        });
        bindClickEvent(flBad, () -> {
            //差评
            position = 2;
            tvAll.setSelected(false);
            tvGood.setSelected(false);
            tvBad.setSelected(true);
            tvHasImags.setSelected(false);
            getData();
        });
        bindClickEvent(flHasImags, () -> {
            //有图
            position = 3;
            tvAll.setSelected(false);
            tvGood.setSelected(false);
            tvBad.setSelected(false);
            tvHasImags.setSelected(true);
            getData();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_shop_comment;
    }

    @Override
    public void initView() {
        tvTitleText.setText("点评");
        tvAll.setSelected(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ShopCommentAdapter();
        recyclerView.setAdapter(mAdapter);
        id = getIntent().getStringExtra("shopId");
        tvShopName.setText(getIntent().getStringExtra("shopName"));
        tvShopFollowNumber.setText("关注量：" + getIntent().getStringExtra("followCount"));
        tvShopContent.setText(Html.fromHtml(getIntent().getStringExtra("content")));
        rbShop.setRating(getIntent().getFloatExtra("averageScore", 0));
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {
        Map map = new HashMap<String, String>();
        map.put("filter[commented_type]", "SMG\\Mall\\Models\\MallProduct");
        map.put("filter[scopeInShopId]", id);
        map.put("include", "user");
        map.put("page", "1");
        if (position == 1) {
            map.put("filter[scopeGood]", "1");
        }
        if (position == 2) {
            map.put("filter[scopeBad]", "1");
        }
        if (position == 2) {
            map.put("filter[scopeHaveImage]", "1");
        }
        DataManager.getInstance().getCommentsList(new DefaultSingleObserver<HttpResult<List<CommentDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<CommentDto>> httpResult) {
                if (httpResult != null && httpResult.getData() != null && httpResult.getData().size() > 0) {
                    mAdapter.setNewData(httpResult.getData());
                } else {
                    mAdapter.setNewData(null);
                    mAdapter.setEmptyView(new EmptyView(ShopCommentActivity.this));
                }
            }
        }, map);
    }
}
