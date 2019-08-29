package com.hqmy.market.view.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.NoticeDto;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.NewFriendNoticeAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView     mTitleText;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private NewFriendNoticeAdapter mAdapter;
    private List<NoticeDto> data = new ArrayList<>();

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
        initRecyclerView();
        getNoticeList();
    }

    @Override
    public void initListener() {

    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new NewFriendNoticeAdapter(data,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void getNoticeList(){
        showLoadDialog();
        DataManager.getInstance().getNoticeList(new DefaultSingleObserver<HttpResult<List<NoticeDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<NoticeDto>> result) {
                dissLoadDialog();
                if(result != null) {
//                    if(result.getData() != null){
//                        List<NoticeDto>  nliList =  result.getData();
//                        if (nliList != null) {
//                            mAdapter.setNewData(nliList);
//                        }
//                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    @OnClick({ R.id.iv_title_back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
        }
    }
}
