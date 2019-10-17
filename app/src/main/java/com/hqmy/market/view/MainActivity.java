package com.hqmy.market.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.qiniu.chatroom.ChatroomKit;
import com.hqmy.market.view.mainfragment.ShopClassFragment;
import com.hqmy.market.view.mainfragment.learn.OnLineLiveFragment;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.common.utils.ToastUtil;

import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.mainfragment.ConsumeFragment;
import com.hqmy.market.view.mainfragment.LearnFragment;
import com.hqmy.market.view.mainfragment.MeFragment;
import com.hqmy.market.view.mainfragment.community.TopicPublishActivity;
import com.hqmy.market.view.widgets.autoview.AutoViewPager;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import com.hqmy.market.view.widgets.dialog.MorePopWindow;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.rong.imlib.RongIMClient;


public class MainActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private AutoViewPager mViewPager;
    private Context mContext;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //会话列表的fragment


    public static final String APP_EXIT = "app_exit";//退出应用

    @BindView(R.id.consume_layout)
    RelativeLayout consume_layout;
    @BindView(R.id.chat_layout)
    RelativeLayout chat_layout;
    @BindView(R.id.community_layout)
    RelativeLayout community_layout;
    @BindView(R.id.learn_layout)
    RelativeLayout learn_layout;
    @BindView(R.id.me_layout)
    RelativeLayout me_layout;

    @BindView(R.id.tab_img_consume)
    ImageView tab_img_consume;
    @BindView(R.id.tab_img_chat)
    ImageView tab_img_chat;
    @BindView(R.id.tab_img_community)
    ImageView tab_img_community;
    @BindView(R.id.tab_img_learn)
    ImageView tab_img_learn;
    @BindView(R.id.tab_img_me)
    ImageView tab_img_me;


    @BindView(R.id.ll_headview_top_message_view)
    View ll_message_view_top;//消息顶部View
    @BindView(R.id.iv_message_actionbar_add)
    ImageView iv_message_actionbar_add;
    @BindView(R.id.iv_message_contacts)
    ImageView iv_message_contacts;
    @BindView(R.id.ll_search_friend_view)
    RelativeLayout ll_search_friend_view;

    private long firstClick = 0;
    private long secondClick = 0;
    private long clickTime;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    private int page_index=0;
    public static final String PAGE_INDEX = "page_index";//调用者传递的名字
    @Override
    public void initView() {
        mContext = this;
        checkPermissioin();
        StatusBarUtils.StatusBarLightMode(this);
        mViewPager = findViewById(R.id.main_viewpager);
        page_index = getIntent().getIntExtra(PAGE_INDEX, 0);
        changeTextViewColor();
        initMainViewPager();
        mViewPager.setCurrentItem(page_index,false);
        changeSelectedTabState(page_index);
    }

    @Override
    public void initData() {
        String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, null);
        if (!TextUtils.isEmpty(cacheToken)) {
            ChatroomKit.connect(cacheToken, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    LogUtil.d(TAG, "ConnectCallback connect onTokenIncorrect");
                    ShareUtil.getInstance().save(Constants.APP_USER_KEY, "");
                }

                @Override
                public void onSuccess(String userId) {
                    LogUtil.d(TAG, "--ConnectCallback connect onSuccess");
                    ShareUtil.getInstance().save(Constants.USER_ID, userId);
                }

                @Override
                public void onError(final RongIMClient.ErrorCode e) {
                    ToastUtil.showToast("" + e.getMessage());
                    LogUtil.d(TAG, "--ConnectCallback connect onError-ErrorCode=" + e.getMessage());
                }
            });

            RongIMClient.getInstance().setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus status) {
                    switch (status) {

                        case CONNECTED://连接成功。
                            Log.i(TAG, "连接成功");
                            break;
                        case DISCONNECTED://断开连接。
                            Log.i(TAG, "断开连接");
                            break;
                        case CONNECTING://连接中。
                            Log.i(TAG, "连接中");
                            break;
                        case NETWORK_UNAVAILABLE://网络不可用。
                            Log.i(TAG, "网络不可用");
                            break;
                        case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                            Log.i(TAG, "用户账户在其他设备登录");
                            break;
                    }
                }
            });
        } else {
            //ToastUtil.showToast("当前用户token凭证为空");
        }

        getConversationPush();// 获取 push 的 id 和 target
    }

    @Override
    public void initListener() {
        bindClickEvent(consume_layout, () -> {
            if (mViewPager.getCurrentItem() == 0) {
                if (firstClick == 0) {
                    firstClick = System.currentTimeMillis();
                } else {
                    secondClick = System.currentTimeMillis();
                }

                if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {

                    firstClick = 0;
                    secondClick = 0;
                } else if (firstClick != 0 && secondClick != 0) {
                    firstClick = 0;
                    secondClick = 0;
                }
            }
            mViewPager.setCurrentItem(0, false);
        });
        bindClickEvent(community_layout, () -> {
            mViewPager.setCurrentItem(1, false);
        });
        bindClickEvent(chat_layout, () -> {
            if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
                gotoActivity(LoginActivity.class);
            }else {
                mViewPager.setCurrentItem(2, false);
            }
         });
         bindClickEvent(learn_layout, () -> {
            mViewPager.setCurrentItem(3, false);
         });
         bindClickEvent(me_layout, () -> {
            mViewPager.setCurrentItem(4, false);
         });
         bindClickEvent(iv_message_contacts, () -> {
//           gotoActivity(ContactsActivity.class);
         });
         bindClickEvent(iv_message_actionbar_add, () -> {
            MorePopWindow morePopWindow = new MorePopWindow(this);
            morePopWindow.showPopupWindow(iv_message_actionbar_add);
         });
         //设置会话列表界面item操作的监听器

         BroadcastManager.getInstance(mContext).addAction(APP_EXIT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                moveTaskToBack(false);
                finishAll();
            }
        });
    }

    private void initMainViewPager() {
        fragments.add(new ConsumeFragment());
        fragments.add(new ShopClassFragment());
        fragments.add(new OnLineLiveFragment());
        fragments.add(new LearnFragment());
        fragments.add(new MeFragment());
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setScanScroll(false);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTextViewColor() {
        tab_img_consume.setBackgroundResource(R.mipmap.ic_xiaofei_def);
        tab_img_chat.setBackgroundResource(R.mipmap.ic_chat_def);
        tab_img_community.setBackgroundResource(R.mipmap.ic_shequ_def);
        tab_img_learn.setBackgroundResource(R.mipmap.ic_xuexi_def);
        tab_img_me.setBackgroundResource(R.mipmap.ic_my_def);
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:

                tab_img_consume.setBackgroundResource(R.mipmap.ic_xiaofei_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 1:

                tab_img_community.setBackgroundResource(R.mipmap.ic_shequ_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 2:

                tab_img_chat.setBackgroundResource(R.mipmap.ic_chat_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 3:

                tab_img_learn.setBackgroundResource(R.mipmap.ic_xuexi_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
            case 4:

                tab_img_me.setBackgroundResource(R.mipmap.ic_my_sel);
                ll_message_view_top.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_PUBLISH && data != null) {
            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ImagePicker.INTENT_RESULT_DATA, resultList);
                gotoActivity(TopicPublishActivity.class, false, bundle, Constants.INTENT_ADD_FRIEND);
            }
        }
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.HOME_REQUEST_CODE_SCAN && data != null){

        }
        if(resultCode == Activity.RESULT_OK && requestCode == Constants.CHAT_REQUEST_CODE_SCAN && data != null){

        }
    }



    private void getConversationPush() {

    }



    private void showLoginHintDialog() {
        ConfirmDialog dialog = new ConfirmDialog((Activity) mContext);
        dialog.setTitle("登录提示");
        dialog.setMessage("该帐号已经在其他设备登录,是否重新登录.");
        dialog.setYesOnclickListener("确定", new BaseDialog.OnYesClickListener() {
            @Override
            public void onYesClick() {
            }
        });
        dialog.setCancleClickListener("取消", new BaseDialog.OnCloseClickListener() {
            @Override
            public void onCloseClick() {
                String cacheToken = ShareUtil.getInstance().getString(Constants.APP_USER_KEY, "");

            }
        });
        dialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(2, false);
        }
    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - clickTime < 2000) {
            moveTaskToBack(false);
            finishAll();
        } else {
            clickTime = System.currentTimeMillis();
            ToastUtil.toast("再按一次退出应用");
        }
    }
}

