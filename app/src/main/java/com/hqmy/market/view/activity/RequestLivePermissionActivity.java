package com.hqmy.market.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RequestLivePermissionActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;
    @BindView(R.id.iv_id_card)
    ImageView iv_id_card;
    UploadFilesDto uploadFilesDto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_request_live_permission;
    }

    @Override
    public void initView() {
        mTitleText.setText("我要开播");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back,
            R.id.tv_submit,
            R.id.iv_id_card
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.iv_id_card:
//                ToastUtil.showToast("上传身份证正面照");
                new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted && Manifest.permission.CAMERA.equals(permission.name)) {
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.SINGLE)//设置选取类型(拍照、单选、多选)
                                            .maxNum(1)//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
//                                             .doCrop(1, 1, 300, 300)
                                            .start(RequestLivePermissionActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            }
                        });
                break;
            case R.id.tv_submit:
                if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    ToastUtil.showToast("请输入姓名");
                    return;
                }
                if (TextUtils.isEmpty(et_id_card.getText().toString().trim())) {
                    ToastUtil.showToast("请输入身份证");
                    return;
                }
                if (uploadFilesDto == null) {
                    ToastUtil.showToast("请上传身份证");
                    return;
                }
                HashMap<String,String> map = new HashMap<>();
                map.put("realname",et_name.getText().toString().trim());
                map.put("identity",et_id_card.getText().toString().trim());
                map.put("identity_image",uploadFilesDto.getUrl());
                applyLive(map);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                String imageUrl = resultList.get(0).getImagePath();
                uploadImg(imageUrl);
            }
        }
    }

    private void uploadImg(String imgPath) {
        uploadFilesDto = null;
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(BaseApplication.getInstance()).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }
        } else {
            ToastUtil.showToast("上传身份证失败");
            return;
        }
        showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                dissLoadDialog();
                if (object != null) {
                    uploadFilesDto = object;
                    GlideUtils.getInstances().loadNormalImg(RequestLivePermissionActivity.this, iv_id_card, imgPath);
                } else {
                    ToastUtil.showToast("上传身份证失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.toast(ApiException.getInstance().getErrorMsg());

            }
        }, "live", part);
    }
    public void applyLive(HashMap<String, String> map) {
        showLoadDialog();
        DataManager.getInstance().applyLive(new DefaultSingleObserver<HttpResult<AnchorInfo>>() {
            @Override
            public void onSuccess(HttpResult<AnchorInfo> result) {
                dissLoadDialog();
                gotoActivity(LiveCheckingActivity.class,true);
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, map);
    }
}
