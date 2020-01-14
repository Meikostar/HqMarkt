package com.hqmy.market.view.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.OnlineLiveFinishBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.qiniu.AVStreamingActivity;
import com.hqmy.market.utils.ShareUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by winder on 2019/7/14.
 * 直播关闭
 */

public class OnlineLiveFinishActivity extends BaseActivity {
    @BindView(R.id.img_user_header)
    ImageView imgUserHeader;
    @BindView(R.id.tv_home_number)
    TextView tvHomeNumber;
    @BindView(R.id.tv_dian_zan)
    TextView tvDianZan;
    @BindView(R.id.tv_people_number)
    TextView tvPeopleNumber;
    @BindView(R.id.tv_play_time)
    TextView tvPlayTime;
    @Override
    public int getLayoutId() {
        return R.layout.ui_live_finish;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        liveVideosCloseSuccess();
    }

    @Override
    public void initListener() {

    }

    /**
     * 获取首页直播列表数据
     */
    private void liveVideosCloseSuccess() {
        String id = getIntent().getStringExtra("id");
        DataManager.getInstance().liveVideosCloseSuccess(new DefaultSingleObserver<HttpResult<OnlineLiveFinishBean>>() {
            @Override
            public void onSuccess(HttpResult<OnlineLiveFinishBean> result) {
                if (result != null && result.getData() !=null){
                    GlideUtils.getInstances().loadRoundImg(OnlineLiveFinishActivity.this, imgUserHeader, Constants.WEB_IMG_URL_UPLOADS + ShareUtil.getInstance().get(Constants.USER_HEAD),R.drawable.moren_ren);

//                    GlideUtils.getInstances().loadRoundImg(OnlineLiveFinishActivity.this,imgUserHeader,Constants.WEB_IMG_URL_STORAGE+result.getData().getImages(),R.drawable.moren_ren);
                    tvHomeNumber.setText("房间号："+result.getData().getLive_title());
                    tvDianZan.setText(result.getData().getFloolwers_count());
                    tvPeopleNumber.setText(result.getData().getPlay_count());
                    tvPlayTime.setText(result.getData().getSpace_time());
                }

            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, id);
    }

    @OnClick(R.id.btn_go_home)
    public void toOnclick() {
        finish();
    }
}
