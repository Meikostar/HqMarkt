package com.hqmy.market.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.hqmy.market.view.mainfragment.ShopClassFragment;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.StatusBarUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.rong.SealAppContext;
import com.hqmy.market.rong.adapter.ConversationListAdapterEx;
import com.hqmy.market.rong.widget.DragPointView;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.mainfragment.CommunityFragment;
import com.hqmy.market.view.mainfragment.ConsumeFragment;
import com.hqmy.market.view.mainfragment.LearnFragment;
import com.hqmy.market.view.mainfragment.MeFragment;
import com.hqmy.market.view.mainfragment.chat.ContactsActivity;
import com.hqmy.market.view.mainfragment.chat.ConversationActivity;
import com.hqmy.market.view.mainfragment.chat.NewFriendListActivity;
import com.hqmy.market.view.mainfragment.community.TopicPublishActivity;
import com.hqmy.market.view.widgets.autoview.AutoViewPager;
import com.hqmy.market.view.widgets.dialog.BaseDialog;
import com.hqmy.market.view.widgets.dialog.ConfirmDialog;
import com.hqmy.market.view.widgets.dialog.MorePopWindow;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;


public class MainActivity extends BaseActivity implements
        ViewPager.OnPageChangeListener,
        DragPointView.OnDragListencer,
        IUnReadMessageObserver {
    private static final String TAG = MainActivity.class.getSimpleName();
    private AutoViewPager mViewPager;
    private Context mContext;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //会话列表的fragment
    private ConversationListFragment mConversationListFragment = null;
    private Conversation.ConversationType[] mConversationsTypes = null;
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
            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
        } else {
            //ToastUtil.showToast("当前用户token凭证为空");
        }
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP,
                Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
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
                RLog.i("MainActivity", "time = " + (secondClick - firstClick));
                if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                    mConversationListFragment.focusUnreadItem();
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
           gotoActivity(ContactsActivity.class);
         });
         bindClickEvent(iv_message_actionbar_add, () -> {
            MorePopWindow morePopWindow = new MorePopWindow(this);
            morePopWindow.showPopupWindow(iv_message_actionbar_add);
         });
         //设置会话列表界面item操作的监听器
         RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
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
        fragments.add(setConversationList());
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
                ll_message_view_top.setVisibility(View.VISIBLE);
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

    /**
     * 设置会话类别数据
     * @return
     */
    private Fragment setConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return mConversationListFragment;
        } else{
            return mConversationListFragment;
        }
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {
            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");
            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation != null) {
                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            //startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }
                @Override
                public void onError(RongIMClient.ErrorCode e) {
                }
            });
        }
    }


    @Override
    public void onCountChanged(int i) {

    }

    @Override
    public void onDragOut() {

    }

    private class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {
        /**
         * 当点击会话头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param targetId         被点击的用户id。
         * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String targetId) {

            return true;
        }

        /**
         * 当长按会话头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param targetId         被点击的用户id。
         * @return 如果用户自己处理了点击后的逻辑处理，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String targetId) {
            return false;
        }

        /**
         * 长按会话列表中的 item 时执行。
         *
         * @param context        上下文。
         * @param view           触发点击的 View。
         * @param uiConversation 长按时的会话条目。
         * @return 如果用户自己处理了长按会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
            return false;
        }

        /**
         * 点击会话列表中的 item 时执行。
         *
         * @param context        上下文。
         * @param view           触发点击的 View。
         * @param uiConversation 会话条目。
         * @return 如果用户自己处理了点击会话后的逻辑处理，则返回 true， 否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
            String conversationTargetId = uiConversation.getConversationTargetId();
            LogUtil.i(TAG, "--conversationTargetId=" + conversationTargetId);
            if (conversationTargetId.equals("1")) {
                // Intent i = new Intent(getActivity(), SystemMessageActivity.class);
                // com.zhangjiajie.art.ui.activity.MainActivity.mainActivity.startActivityIn(i, getActivity());
                return true;
            } else if (conversationTargetId.equals("2")) {
                // Intent i = new Intent(getActivity(), OrderMessageActivity.class);
                // com.zhangjiajie.art.ui.activity.MainActivity.mainActivity.startActivityIn(i, getActivity());
                return true;
            } else if (conversationTargetId.equals("10000")) {
                // Intent i = new Intent(getActivity(), OrderMessageActivity.class);
                // com.zhangjiajie.art.ui.activity.MainActivity.mainActivity.startActivityIn(i, getActivity());
                return true;
            }
            else if (!TextUtils.isEmpty(uiConversation.getConversationContent()) && uiConversation.getConversationContent().toString().contains(Constants.ADD_FRIEND_MESSAGE)) {
                gotoActivity(NewFriendListActivity.class);
                return true;
            } else if (Conversation.ConversationType.SYSTEM == uiConversation.getConversationType()) {
                RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM , uiConversation.getConversationTargetId(), null);
                //gotoActivity(SysPushMessageActivity.class);
                return true;
            }else {
                Bundle bundle = new Bundle();
                bundle.putString(ConversationActivity.TARGET_ID, uiConversation.getConversationTargetId());
                bundle.putInt(ConversationActivity.CONVERSATION_TYPE, uiConversation.getConversationType().getValue());
                bundle.putString(ConversationActivity.TITLE, uiConversation.getUIConversationTitle());
                gotoActivity(ConversationActivity.class, false, bundle);
                return true;
            }
        }
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
                if (!TextUtils.isEmpty(cacheToken)) {
                    RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
                } else {
                    Log.e("seal", "token is empty, can not reconnect");
                }
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

