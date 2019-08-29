package com.hqmy.market.view.mainfragment.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.FriendPageDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import butterknife.BindView;

/**
 * 用户详细
 * Created by rzb on 2019/6/20
 */
public class UserDetailActivity extends BaseActivity {
    private static final String TAG = UserDetailActivity.class.getSimpleName();
    public static final String ITEM_USER = "item_user";
    public static final String SEARCH_RESULT = "SearchResult";//调用者传递的名字
    private boolean isSearchResult;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.ta_home_title)
    TextView  ta_home_title;
    @BindView(R.id.iv_ta_home_user_icon)
    ImageView iv_ta_home_user_icon;
    @BindView(R.id.iv_ta_home_user_name)
    TextView  iv_ta_home_user_name;
    @BindView(R.id.iv_ta_home_user_mname)
    TextView  iv_ta_home_user_mname;
    @BindView(R.id.iv_ta_home_user_phone)
    TextView  iv_ta_home_user_phone;
    @BindView(R.id.iv_ta_home_update_tag)
    RelativeLayout  iv_ta_home_update_tag;
    @BindView(R.id.but_ta_home_user_send)
    RelativeLayout but_ta_home_user_send;
    @BindView(R.id.but_ta_home_user_add)
    RelativeLayout  but_ta_home_user_add;
    @BindView(R.id.but_ta_home_user_del)
    RelativeLayout  but_ta_home_user_del;
    @BindView(R.id.ta_home_line)
    View ta_home_line;
    private String userId;
    private String userHeader;
    private String nickName;

    @Override
    public int getLayoutId() {
        return R.layout.ui_ta_home_layout;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        Bundle userBundle = getIntent().getExtras();
        UserInfoDto userInfoDto = (UserInfoDto) userBundle.getSerializable(ITEM_USER);
        if(userInfoDto != null){
            initDataView(userInfoDto);
        }else{
            userId = getIntent().getExtras().getString(Constants.USER_ID);
            if(userId != null) {
                queryUserInfoPage(userId);
            }
        }
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(iv_ta_home_update_tag, () -> {
            //Bundle bundle = new Bundle();
            //bundle.putInt(Constants.TYPE, UserNameActivity.TYPE_FRIEND_NICK);
            //bundle.putString(Constants.TIP, "好友备注");
            //bundle.putString(Constants.FRIEND_ID, userId);
            //bundle.putString(Constants.FRIEND_NAME,nickName);
            //bundle.putString(Constants.FRIEND_HEAD,userHeader);
            //gotoActivity(UserNameActivity.class, false, bundle, UserNameActivity.TYPE_FRIEND_NICK);
        });

        bindClickEvent(but_ta_home_user_send, () -> {
            Bundle bundle = new Bundle();
            bundle.putString(ConversationActivity.TITLE, nickName);
            bundle.putString(ConversationActivity.TARGET_ID, userId);
            gotoActivity(ConversationActivity.class, true, bundle);
        });

        bindClickEvent(but_ta_home_user_add, () -> {
            addFriend(userId,nickName,"");
        });

        bindClickEvent(but_ta_home_user_del, () -> {
            ConfirmDialog confirmDialog = new ConfirmDialog(this);
            confirmDialog.setCancelText("取消");
            confirmDialog.setTitle("温馨提示");
            confirmDialog.setMessage("是否删除好友" + (nickName == null ? "" : nickName));
            confirmDialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
                @Override
                public void onYesClick() {
                    confirmDialog.hide();
                    delFriend(userId);
                }
            });
            confirmDialog.show();
        });
    }

    /**
     * 发送请求添加好友
     */
    private void addFriend(String friendId, String remark_name, String msg) {
        showLoadDialog();
        DataManager.getInstance().addFriend(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("请求发送成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        }, friendId, remark_name, msg);
    }

    private void queryUserInfoPage(String userId) {
         showLoadDialog();
         DataManager.getInstance().queryUserInfoPage(new DefaultSingleObserver<FriendPageDto>() {
            @Override
            public void onSuccess(FriendPageDto friendPageDto) {
                dissLoadDialog();
                if(friendPageDto != null) {
                    initFriendDataView(friendPageDto);
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, userId);
    }

    private void initFriendDataView(FriendPageDto friendPageDto) {
        if (friendPageDto != null) {
            if (friendPageDto.getUser() != null) {
                userHeader = friendPageDto.getUser().getAvatar();
                nickName =  friendPageDto.getUser().getFriend().getRemark_name();
                iv_ta_home_user_name.setText(friendPageDto.getUser().getFriend().getRemark_name());
                iv_ta_home_user_mname.setText(friendPageDto.getUser().getName());
                iv_ta_home_user_phone.setText(friendPageDto.getUser().getPhone());
                GlideUtils.getInstances().loadUserRoundImg(this, iv_ta_home_user_icon, Constants.WEB_IMG_URL_UPLOADS + userHeader);
            }
                but_ta_home_user_send.setVisibility(View.VISIBLE);
                but_ta_home_user_add.setVisibility(View.GONE);
                but_ta_home_user_del.setVisibility(View.VISIBLE);
                iv_ta_home_update_tag.setVisibility(View.VISIBLE);
        }
    }

    private void initDataView(UserInfoDto userInfoDto) {
        if (userInfoDto != null) {
            userId = userInfoDto.getId();
            nickName = userInfoDto.getName();
            userHeader = userInfoDto.getAvatar();
            iv_ta_home_user_name.setText(userInfoDto.getName());
            iv_ta_home_user_phone.setText(userInfoDto.getPhone());
            GlideUtils.getInstances().loadUserRoundImg(this, iv_ta_home_user_icon, Constants.WEB_IMG_URL_UPLOADS + userHeader);

            but_ta_home_user_send.setVisibility(View.GONE);
            but_ta_home_user_add.setVisibility(View.VISIBLE);
            but_ta_home_user_del.setVisibility(View.GONE);
            iv_ta_home_update_tag.setVisibility(View.GONE);
        }
    }

    /**
     * 删除好友
     */
    private void delFriend(String friendId) {
        showLoadDialog();
        DataManager.getInstance().delFriend(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                finish();
                //ToastUtil.showToast("删除好友成功");
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("删除好友成功");
                }else{
                    ToastUtil.showToast(ApiException.getInstance().getErrorMsg());
                }
                finish();
            }
        }, friendId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == UserNameActivity.TYPE_FRIEND_NICK && resultCode == Activity.RESULT_OK && data != null) {
//            iv_ta_home_user_name.setText(data.getStringExtra("name"));
//        }
    }
}
