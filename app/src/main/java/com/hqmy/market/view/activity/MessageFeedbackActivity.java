package com.hqmy.market.view.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.MessageFeedbackTypeBean;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.view.adapter.MessageFeedbackGridAdapter;
import com.hqmy.market.view.widgets.NoScrollGridView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 留言反馈
 */
public class MessageFeedbackActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView         mTitleText;
    @BindView(R.id.gv_feedback_type)
    NoScrollGridView mGridView;
    @BindView(R.id.tv_feedback_content_num)
    TextView         mContentNum;
    @BindView(R.id.et_feedback_content)
    EditText         mContent;
    private MessageFeedbackGridAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_feedback;
    }

    @Override
    public void initView() {
        mTitleText.setText("留言反馈");
    }

    @Override
    public void initData() {
        initGridView();
    }

    private void initGridView() {
        ArrayList<MessageFeedbackTypeBean> lists = new ArrayList<>();
        MessageFeedbackTypeBean bean1 = new MessageFeedbackTypeBean();
        bean1.setType("投诉销售");
        bean1.setSelect(true);
        MessageFeedbackTypeBean bean2 = new MessageFeedbackTypeBean();
        bean2.setType("投诉客服");
        bean2.setSelect(false);
        MessageFeedbackTypeBean bean3 = new MessageFeedbackTypeBean();
        bean3.setType("商品问题");
        bean3.setSelect(false);
        MessageFeedbackTypeBean bean4 = new MessageFeedbackTypeBean();
        bean4.setType("新品需求");
        bean4.setSelect(false);
        MessageFeedbackTypeBean bean5 = new MessageFeedbackTypeBean();
        bean5.setType("其他");
        bean5.setSelect(false);
        lists.add(bean1);
        lists.add(bean2);
        lists.add(bean3);
        lists.add(bean4);
        lists.add(bean5);
        mAdapter = new MessageFeedbackGridAdapter(this, lists);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    public void initListener() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.RefreshData(position);
                MessageFeedbackTypeBean bean = (MessageFeedbackTypeBean) mAdapter.getItem(position);
                LogUtil.e("tea","----"+bean.getType());
            }
        });

        mContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mContentNum.setText("0/400");
                } else {
                    mContentNum.setText(s.toString().length() + "/400");
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
