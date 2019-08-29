package com.hqmy.market.view.mainfragment.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.common.utils.InputUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.AddFriendAdapter;
import java.util.List;
import butterknife.BindView;

/**
 * 添加好友
 * Created by rzb on 2019/6/20
 */
public class AddFriendActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private static final String TAG = AddFriendActivity.class.getSimpleName();
    @BindView(R.id.iv_title_back)
    ImageView    iv_title_back;
    @BindView(R.id.add_friend_title)
    TextView     add_friend_title;
    @BindView(R.id.tv_title_right)
    TextView     tv_title_right;
    @BindView(R.id.layout_add_friend)
    RelativeLayout layout_add_friend;
    @BindView(R.id.tv_search_contacts_view)
    TextView     tv_search_contacts_view;
    @BindView(R.id.et_contacts_search_str)
    EditText     et_search_str;
    @BindView(R.id.add_recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private AddFriendAdapter  mAddFriendAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    public void initView() {
        manager = new LinearLayoutManager(AddFriendActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        mAddFriendAdapter = new AddFriendAdapter(AddFriendActivity.this, null);
        recyclerView.setAdapter(mAddFriendAdapter);
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(layout_add_friend, () -> {
            gotoActivity(NewFriendListActivity.class);
        });

        et_search_str.setOnEditorActionListener(this);
        et_search_str.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())){
                    tv_search_contacts_view.setVisibility(View.VISIBLE);
                }else{
                    tv_search_contacts_view.setVisibility(View.GONE);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable edit) {
            }
        });
        bindClickEvent(tv_title_right, () -> {
            finish();
        });
    }

    private void queryUserInfoByPhone(String phone) {
        showLoadDialog();
        DataManager.getInstance().queryUserListByPhone(new DefaultSingleObserver<HttpResult<List<UserInfoDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<UserInfoDto>> result) {
                dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        List<UserInfoDto> uifList = result.getData();
                        if (uifList!= null) {
                            mAddFriendAdapter.setNewData(uifList);
                            mAddFriendAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, phone);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    String seachStr = et_search_str.getText().toString().trim();
                    queryUserInfoByPhone(seachStr);
                    InputUtil.HideKeyboard(et_search_str);
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }
}
