package com.hqmy.market.view.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.InviteFriendBean;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.InviteFiendAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class InviteFriendsActivity extends BaseActivity {
    @BindView(R.id.recy_friend)
    RecyclerView recyFriend;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;

    InviteFiendAdapter mAdapter;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void initView() {
        tvTitleText.setText("邀请好友列表");
        recyFriend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new InviteFiendAdapter();
        recyFriend.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        getUserChildren();
    }

    private void getUserChildren() {
        DataManager.getInstance().getUserChildren(new DefaultSingleObserver<HttpResult<List<InviteFriendBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<InviteFriendBean>> result) {
                super.onSuccess(result);
                mAdapter.setNewData(result.getData());
            }
        });
    }

    @OnClick({R.id.iv_title_back})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }

    }
}
