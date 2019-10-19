package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.NoticeDto;
import com.hqmy.market.view.adapter.NewFriendNoticeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView       mTitleText;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.ll_xt)
    LinearLayout   llXt;
    @BindView(R.id.ll_dd)
    LinearLayout   llDd;
    private NewFriendNoticeAdapter mAdapter;
    private List<NoticeDto>        data = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public void initView() {
        mTitleText.setText("消息中心");
    }

    @Override
    public void initData() {
        //        initRecyclerView();
        //        getNoticeList();
    }

    @Override
    public void initListener() {
        llDd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageCenterActivity.this,MessageOrderActivity.class));
            }
        });
        llXt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageCenterActivity.this,MessageXtActivity.class));
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //        mRecyclerView.setLayoutManager(layoutManager);
        //        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        //        mAdapter = new NewFriendNoticeAdapter(data,this);
        //        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick({R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
