package com.hqmy.market.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.view.MainActivity;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaySuccessActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.img_qrcode)
    ImageView      imgQrcode;
    @BindView(R.id.tv_state)
    TextView       tvState;
    @BindView(R.id.btn_save_qcode)
    TextView       btnSaveQcode;
    @BindView(R.id.ll_look)
    LinearLayout   llLook;
    @BindView(R.id.ll_back)
    LinearLayout   llBack;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_state;
    }

    @Override
    public void initView() {
        tvTitleText.setText("支付成功");
    }

    @Override
    public void initData() {


    }






    @OnClick({R.id.iv_title_back, R.id.ll_look, R.id.ll_back})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            //            case R.id.btn_friend:
            //                gotoActivity(InviteFriendsActivity.class);
            //                break;
            case R.id.ll_look:
                Intent intent = new Intent(PaySuccessActivity.this, OrderActivity.class);
                intent.putExtra("page", 2);
                startActivity(intent);
                break;
            case R.id.ll_back:
                finishAll();
                Bundle bundle = new Bundle();
                bundle.putInt(MainActivity.PAGE_INDEX, 0);
                gotoActivity(MainActivity.class, true, bundle);
                break;

        }

    }



}
