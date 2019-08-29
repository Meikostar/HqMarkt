package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.view.adapter.AreaAdapter;
import com.hqmy.market.view.widgets.autoview.ActionbarView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AreaListActivity extends BaseActivity {
    public static final String TAG = AreaListActivity.class.getName();
    @BindView(R.id.recy_area)
    RecyclerView recyclerView;
    @BindView(R.id.tv_area_parent_name)
    TextView     parentName;
    AreaAdapter mAdapter;
    int         parendId = -1;
    String      name;
    @BindView(R.id.custom_action_bar)
    ActionbarView customActionBar;
    private String allName = "";

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.ui_area_list_layout;
    }

    @Override
    public void initView() {

        customActionBar.setTitle("区域选择");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            allName = bundle.getString(Constants.INTENT_AREA_NAME);
            Serializable serializable = bundle.getSerializable(Constants.INTENT_DATA);
            if (serializable != null) {
                AreaDto areaDto = (AreaDto) serializable;
                name = areaDto.getName();
                parendId = areaDto.getId();
            }
        }
        if (TextUtils.isEmpty(name)) {
            name = "中国";
        }
        parentName.setText(name);
        initAdapter();
    }

    @Override
    public void initData() {

        if (parendId == -1) {
            getArea("");
        } else {
            getArea(parendId + "");
        }

    }

    private void initAdapter() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AreaAdapter(this, name);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Object object = adapter.getItem(position);
                AreaDto areaDto = null;
                if (object != null) {
                    areaDto = (AreaDto) object;
                    if (areaDto.getDepth() == 2) {
                        allName = allName + " " + areaDto.getName();
                        /*AddGoodsAddressActivity.areaName = allName;
                        AddGoodsAddressActivity.isChangeArea = true;
                        AddGoodsAddressActivity.areaId = areaDto.getId() + "";*/
                        Intent intent = new Intent();
                        intent.putExtra("areaName", allName);
                        intent.putExtra("areaId", areaDto.getId() + "");
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } else {
                        allName = allName + " " + areaDto.getName();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.INTENT_DATA, areaDto);
                        bundle.putString(Constants.INTENT_AREA_NAME, allName);
                        Intent intent = new Intent(AreaListActivity.this, AreaListActivity.class);
                        intent.putExtras(bundle);

                        startActivityForResult(intent, Constants.INTENT_REQUESTCODE_AREA);
                    }

                }

            }
        });
    }

    private void getArea(String parendId) {
        showLoadDialog();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("parent_id", parendId);
        DataManager.getInstance().getAreaList(new DefaultSingleObserver<List<AreaDto>>() {
            @Override
            public void onSuccess(List<AreaDto> areaDtos) {
                dissLoadDialog();
                LogUtil.i(TAG, "--getArea: onSuccess() = " + areaDtos.size());
                mAdapter.setNewData(areaDtos);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_AREA) {
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }



}
