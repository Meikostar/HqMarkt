package com.hqmy.market.view.activity;

import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MyQRcodeActivity extends BaseActivity {
    @BindView(R.id.img_qrcode)
    ImageView imgQrcode;
    @BindView(R.id.btn_save_qcode)
    TextView btnSaveQcode;
    @BindView(R.id.btn_friend)
    Button btnFriend;
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;

    String shareUrl ;

    @Override
    public void initListener() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    public void initView() {
        tvTitleText.setText("我的推广");
        shareUrl = Constants.BASE_URL+"api/package/user/invitation_img?user_id="+ ShareUtil.getInstance().getString(Constants.USER_ID,"");
        GlideUtils.getInstances().loadNormalImg(this,imgQrcode, Constants.BASE_URL+"api/package/user/invitation_img?user_id="+ ShareUtil.getInstance().getString(Constants.USER_ID,""));
    }

    @Override
    public void initData() {
        getUserCount();
    }

    public void getUserCount() {
        DataManager.getInstance().getUserCount(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> result) {
                btnFriend.setText("已邀请好友 " + result.getData() + "人>");
            }
        });
    }

    @OnClick({R.id.iv_title_back,R.id.btn_friend,R.id.btn_save_qcode})
    public void toClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.btn_friend:
                gotoActivity(InviteFriendsActivity.class);
                break;
            case R.id.btn_save_qcode:
                downImg(shareUrl);
                break;
        }

    }

    private void downImg(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        showLoadDialog();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/love/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        final String pathFile = path + System.currentTimeMillis() + ".png";
        FileDownloader.getImpl().create(url)
                .setPath(pathFile, false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "pending");
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);
                        LogUtil.d("down", "progress" + soFarBytes + "totalBytes=" + totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        ToastUtil.showToast("保存失败");
                        dissLoadDialog();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        LogUtil.d("down", "connected");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        dissLoadDialog();
                        ToastUtil.showToast("保存成功");

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        LogUtil.d("down", "warn");
                    }
                }).start();
    }
}
