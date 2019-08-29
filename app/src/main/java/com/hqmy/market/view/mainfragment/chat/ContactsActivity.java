package com.hqmy.market.view.mainfragment.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.FriendListItemDto;
import com.hqmy.market.bean.SortBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.InputUtil;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.PinyinUtils;
import com.hqmy.market.common.PinyinComparator;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.rong.SealAppContext;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.adapter.SortAdapter;
import com.hqmy.market.view.widgets.autoview.SideBarView;
import com.hqmy.market.view.widgets.dialog.MorePopWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 通讯录
 * Created by rzb on 2019/5/18.
 */
public class ContactsActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private static final String TAG = ContactsActivity.class.getSimpleName();
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.iv_contacts_actionbar_add)
    ImageView   iv_contacts_actionbar_add;
    @BindView(R.id.tv_search_contacts_view)
    TextView    tv_search_contacts_view;
    @BindView(R.id.et_contacts_search_str)
    EditText    et_search_str;
    @BindView(R.id.contacts_side_bar)
    SideBarView sideBar;
    @BindView(R.id.contacts_tag_dialog)
    TextView    dialog;
    @BindView(R.id.rl_contacts_item_scan)
    RelativeLayout rl_contacts_item_scan;
    @BindView(R.id.contacts_recycler_view)
    RecyclerView   recyclerView;
    private SortAdapter  adapter;
    private ArrayList<SortBean> sourceDate;
    //根据拼音来排列RecyclerView里面的数据类
    private PinyinComparator    pinyinComparator;
    LinearLayoutManager manager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_contacts_layout;
    }

    @Override
    public void initView() {
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(dialog);
        manager = new LinearLayoutManager(ContactsActivity.this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        adapter = new SortAdapter(ContactsActivity.this, null);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        et_search_str.setOnEditorActionListener(this);

        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(iv_contacts_actionbar_add, () -> {
            MorePopWindow morePopWindow = new MorePopWindow(ContactsActivity.this);
            morePopWindow.showPopupWindow(iv_contacts_actionbar_add);
        });

        bindClickEvent(rl_contacts_item_scan, () -> {
            gotoActivity(GroupListActivity.class);
        });

        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SortBean item = (SortBean) adapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.USER_ID, item.getId());
                gotoActivity(UserDetailActivity.class, false, bundle);
            }
        });
        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBarView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        //根据输入框输入值的改变来过滤搜索
        et_search_str.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                    }
            @Override
            public void afterTextChanged(Editable edit) {
                    }
        });
        BroadcastManager.getInstance(ContactsActivity.this).addAction(SealAppContext.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                LogUtil.i(TAG, "BroadcastManager Action=" + command);
                if (!TextUtils.isEmpty(command)) {
                    //iv_contacts_item_new_tag.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        queryWaitAcceptReqHongDiandian();
//        String userId = Constants.getInstance().getString(Constants.USER_ID, "");
//        int newSize = Constants.getInstance().getInt(Constants.TAG_SIZE + userId,0);
//        int flagSize = Constants.getInstance().getInt(Constants.FLAG_SIZE + userId,0);
//        if(newSize > flagSize) {
//            iv_new_tag_notice.setVisibility(View.VISIBLE);
//        }else{
//            iv_new_tag_notice.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_RED_DOT);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //防止两次发送请求
        if (actionId == EditorInfo.IME_ACTION_SEND ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_UP:
                    String seachStr = et_search_str.getText().toString().trim();
                    //getUserList(seachStr);
                    InputUtil.HideKeyboard(et_search_str);
                    return true;
                default:
                    return true;
            }
        }
        return false;
    }

    /**
     * 获取用户好友信息列表
     */
    private void getFriendList() {
        showLoadDialog();
        DataManager.getInstance().getFriendList(new DefaultSingleObserver<HttpResult<ArrayList<FriendListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FriendListItemDto>> result) {
                LogUtil.i(TAG, "--RxLog-Thread: getFriendList onSuccess() = " + result.getData().size());
                if (result != null) {
                    if(result.getData() != null) {
                        ArrayList<FriendListItemDto> friendList = result.getData();
                        if (friendList != null) {
                            if (friendList.size() > 0) {
                                sourceDate = filledData(friendList);
                                // 根据a-z进行排序源数据
                                Collections.sort(sourceDate, pinyinComparator);
                                adapter.updateList(sourceDate);
                                for (int j = 0; j < friendList.size(); j++) {
                                    if(friendList.get(j).getRemark_name() != null) {
                                        if (!friendList.get(j).getRemark_name().isEmpty()) {
                                            Uri RongHeadImg = Uri.parse(Constants.WEB_IMG_URL_UPLOADS + friendList.get(j).getFriend_user().getData().getAvatar());
                                            String uId = friendList.get(j).getFriend_user_id();
                                            String nickName = friendList.get(j).getFriend_user().getData().getName();
                                            UserInfo userInfo = new UserInfo(uId, nickName, RongHeadImg);
                                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    adapter.updateList(null);
                    sideBar.setVisibility(View.GONE);
                }
                dissLoadDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                LogUtil.i(TAG, "--RxLog-Thread: getFriendList onError() = " + throwable);
                //ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                String errMsg = ErrorUtil.getInstance().getErrorMessage(throwable);
                if(errMsg != null) {
                    if (errMsg.equals("appUserKey过期")) {
                        ToastUtil.showToast("appUserKey已过期，请重新登录");
                        ShareUtil.getInstance().cleanUserInfo();
                        gotoActivity(LoginActivity.class, true, null);
                    }
                }
                dissLoadDialog();
            }
        });
    }

    /**
     * 封装RecyclerView数据
     * @param date
     * @return
     */
    private ArrayList<SortBean> filledData(ArrayList<FriendListItemDto> date) {
        ArrayList<SortBean> sortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortBean item = new SortBean();
            item.setRemarkName(date.get(i).getRemark_name());
            item.setName(date.get(i).getFriend_user().getData().getName());
            item.setIcon(date.get(i).getFriend_user().getData().getAvatar());
            item.setId(date.get(i).getFriend_user_id());
            item.setPhone(date.get(i).getFriend_user().getData().getPhone());
            //汉字转换成拼音
            String pinyin = PinyinUtils.getPingYin(TextUtils.isEmpty(item.getName()) ? "*" : item.getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                item.setLetters(sortString.toUpperCase());
            } else {
                item.setLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }


    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortBean> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            tv_search_contacts_view.setVisibility(View.VISIBLE);
            filterDateList = sourceDate;
        } else {
            tv_search_contacts_view.setVisibility(View.GONE);
            filterDateList.clear();
            if (sourceDate != null && !sourceDate.isEmpty()) {
                for (SortBean item : sourceDate) {
                    String phone = item.getPhone() == null ? "*" : item.getPhone();
                    String name = item.getName() == null ? "*" : item.getName();
                    if (phone.indexOf(filterStr.toString()) != -1 || name.indexOf(filterStr.toString()) != -1 ||
                            PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                            //不区分大小写
                            || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                            || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                            ) {
                        filterDateList.add(item);
                    }
                }
            }
        }

        // 根据a-z进行排序
        if(filterDateList != null) {
            if(filterDateList.size() > 0) {
                Collections.sort(filterDateList, pinyinComparator);
                adapter.updateList(filterDateList);
            }
        }
    }

    public void queryWaitAcceptReqHongDiandian() {
        getFriendList();
//        DataManager.getInstance().queryWaitAcceptReqHongDiandian(new DefaultSingleObserver<Boolean>() {
//            @Override
//            public void onSuccess(Boolean aBoolean) {
//                if (aBoolean) {
//                    iv_new_tag.setVisibility(View.VISIBLE);
//                } else {
//                    iv_new_tag.setVisibility(View.GONE);
//                }
//            }
//        });
    }
}
