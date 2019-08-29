package com.hqmy.market.view.mainfragment.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.FriendListItemDto;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.NewFriendMonthAdapter;
import com.hqmy.market.view.adapter.NewFriendThreeDayAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * 新的朋友
 * Created by rzb on 2019/6/19.
 */
public class NewFriendListActivity extends BaseActivity {
    @BindView(R.id.iv_new_title_back)
    ImageView iv_new_title_back;
    @BindView(R.id.recycler_new_friend_list_month)
    RecyclerView recyclerViewMonth;
    private NewFriendMonthAdapter adapterMonth;
    @BindView(R.id.tv_new_friend_list_three_month)
    TextView tv_new_friend_list_three_month;

    @BindView(R.id.recycler_new_friend_list_three_day)
    RecyclerView recyclerViewThreeDay;
    private NewFriendThreeDayAdapter adapterThreeDay;
    @BindView(R.id.tv_new_friend_list_three_day)
    TextView tv_new_friend_list_three_day;
    private List<FriendListItemDto> monthLists = new ArrayList<>();
    private List<FriendListItemDto> threeLists = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.ui_new_friend_list_layout;
    }

    @Override
    public void initView() {
        recyclerViewMonth.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewThreeDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));    }

    @Override
    public void initData() {
        adapterMonth = new NewFriendMonthAdapter(null, this);
        recyclerViewMonth.setAdapter(adapterMonth);
        adapterThreeDay = new NewFriendThreeDayAdapter(null, this);
        recyclerViewThreeDay.setAdapter(adapterThreeDay);
        getFriendLists();
    }

    @Override
    public void initListener() {

        bindClickEvent(iv_new_title_back, () -> {
            finish();
        });

        adapterMonth.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FriendListItemDto item = adapterMonth.getItem(position);
                   switch (view.getId()) {
                       case R.id.but_new_friend_user_status:
                           verifyFriend(item.getFriend_user_id(), item.getRemark_name());
                           break;
                   }
            }
        });

        adapterThreeDay.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FriendListItemDto item = adapterThreeDay.getItem(position);
                switch (view.getId()) {
                    case R.id.but_new_friend_user_status:
                        verifyFriend(item.getFriend_user_id(), item.getRemark_name());
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取朋友验证消息列表
     */
    private void getFriendLists() {
        showLoadDialog();
        DataManager.getInstance().getNewFriendLists(new DefaultSingleObserver<HttpResult<List<FriendListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<FriendListItemDto>> result) {
                dissLoadDialog();
                if(result != null){
                    if(result.getData() != null){
                        List<FriendListItemDto> fLists = result.getData();
                        for(int i=0; i<fLists.size(); i++){
                           if(fLists.get(i).getIs_before_three_day().equals("false")){
                               if(monthLists != null){
                                   monthLists.add(fLists.get(i));
                               }
                               monthLists.add(fLists.get(i));
                           }else if(fLists.get(i).getIs_before_three_day().equals("true")){
                              if(threeLists != null) {
                                  threeLists.add(fLists.get(i));
                              }
                           }
                        }
                        if(monthLists.size() > 0){
                            tv_new_friend_list_three_month.setVisibility(View.VISIBLE);
                        }else{
                            tv_new_friend_list_three_month.setVisibility(View.GONE);
                        }
                        if(threeLists.size() > 0){
                            tv_new_friend_list_three_day.setVisibility(View.VISIBLE);
                        }else{
                            tv_new_friend_list_three_day.setVisibility(View.GONE);
                        }
                        adapterMonth.setNewData(monthLists);
                        adapterThreeDay.setNewData(threeLists);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                dissLoadDialog();
            }
        });
    }

    /**
     * 确认添加好友
     */
    private void verifyFriend(String friendId, String remark_name) {
        showLoadDialog();
        DataManager.getInstance().verifyFriend(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("添加好友成功");
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (!ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }else{
                    ToastUtil.showToast("添加好友成功");
                    if(monthLists.size() > 0){
                      monthLists.clear();
                    }
                    if(threeLists.size() > 0){
                       threeLists.clear();
                    }
                    getFriendLists();
                }
            }
        }, friendId, remark_name);
    }
}
