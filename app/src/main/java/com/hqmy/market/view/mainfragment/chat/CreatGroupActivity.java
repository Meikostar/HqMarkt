package com.hqmy.market.view.mainfragment.chat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.FriendListItemDto;
import com.hqmy.market.bean.SortBean;
import com.hqmy.market.common.PinyinComparator;
import com.hqmy.market.common.utils.PinyinUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.error.ErrorUtil;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.SortAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;

/**
 * 发起群聊
 */
public class CreatGroupActivity extends BaseActivity {
    @BindView(R.id.iv_title_back)
    ImageView    iv_title_back;
    @BindView(R.id.contacts_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_number)
    TextView     tv_number;
    @BindView(R.id.btn_confirm)
    TextView     btn_confirm;
    @BindView(R.id.et_group_name)
    EditText et_group_name;
    private SortAdapter    adapter;
    private List<SortBean> selSortBeanList = new ArrayList<>();
    private ArrayList<SortBean> sourceDate;
    //根据拼音来排列RecyclerView里面的数据类
    private PinyinComparator pinyinComparator;
    LinearLayoutManager manager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_group;
    }

    @Override
    public void initView() {
        pinyinComparator = new PinyinComparator();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        adapter = new SortAdapter(this, null);
        adapter.setSel(true);
        recyclerView.setAdapter(adapter);
        selSortBeanList.clear();
        getFriendList();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SortBean item = (SortBean) adapter.getItem(position);
                item.setSel(!item.isSel());
                adapter.getmData().set(position, item);
                adapter.notifyDataSetChanged();
                deanSelBean(item);
            }
        });

        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(btn_confirm, () -> {
            if(et_group_name.getText().toString().isEmpty()){
                ToastUtil.showToast("请先填写群组名称");
                return;
            }
            if (selSortBeanList.size() == 0) {
                ToastUtil.showToast("请选择联系人");
                return;
            }
            creGroup(et_group_name.getText().toString());
        });
    }

    /**
     * 获取用户好友信息列表
     */
    private void getFriendList() {
        showLoadDialog();
        DataManager.getInstance().getFriendList(new DefaultSingleObserver<HttpResult<ArrayList<FriendListItemDto>>>() {
            @Override
            public void onSuccess(HttpResult<ArrayList<FriendListItemDto>> result) {
                if (result != null) {
                    if (result.getData() != null) {
                        ArrayList<FriendListItemDto> fList = result.getData();
                        sourceDate = filledData(fList);
                        //根据a-z进行排序源数据
                        Collections.sort(sourceDate, pinyinComparator);
                        adapter.updateList(sourceDate);
                    }
                }
                dissLoadDialog();
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                dissLoadDialog();
            }
        });
    }

    /**
     * 封装RecyclerView数据
     * @return
     */
    private ArrayList<SortBean> filledData(ArrayList<FriendListItemDto> date) {
        ArrayList<SortBean> sortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            SortBean item = new SortBean();
            if (!TextUtils.isEmpty(date.get(i).getRemark_name())) {
                item.setName(date.get(i).getRemark_name());
            } else {
                item.setName(date.get(i).getFriend_user().getData().getName());
            }
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

    private void deanSelBean(SortBean item) {
        if (item.isSel()) {
            selSortBeanList.add(item);
        } else {
            for (int i = 0; i < selSortBeanList.size(); i++) {
                SortBean sortBean = selSortBeanList.get(i);
                if (item.getId().equals(sortBean.getId())) {
                    selSortBeanList.remove(i);
                    break;
                }
            }
        }
        tv_number.setText("(" + selSortBeanList.size() + ")");
    }

    private List<String> getSelSorBeanIDs() {
        List<String> ids = new ArrayList<>();
        for (SortBean bean : selSortBeanList) {
            ids.add(bean.getId());
        }
        return ids;
    }

    private void creGroup(String groupName) {
        showLoadDialog();
        DataManager.getInstance().creGroup(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> object) {
                dissLoadDialog();
                ToastUtil.showToast("发起群聊成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if(ApiException.getInstance().isSuccess()){
                    ToastUtil.showToast("发起群聊成功");
                    finish();
                }else {
                    ToastUtil.showToast(ErrorUtil.getInstance().getErrorMessage(throwable));
                }
            }
        }, groupName, getSelSorBeanIDs());
    }
}
