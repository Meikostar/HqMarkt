package com.hqmy.market.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
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
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EnterprisePermissionActivity extends BaseActivity {

    UploadFilesDto uploadFilesDto;
    @BindView(R.id.iv_title_back)
    ImageView      ivTitleBack;
    @BindView(R.id.tv_title_text)
    TextView       tvTitleText;
    @BindView(R.id.tv_title_right)
    TextView       tvTitleRight;
    @BindView(R.id.layout_top)
    RelativeLayout layoutTop;
    @BindView(R.id.et_content1)
    EditText       etContent1;
    @BindView(R.id.et_name)
    EditText       etName;
    @BindView(R.id.iv_img11)
    ImageView      ivImg11;

    @BindView(R.id.iv_img22)
    ImageView      ivImg22;

    @BindView(R.id.iv_img33)
    ImageView      ivImg33;

    @BindView(R.id.iv_img44)
    ImageView      ivImg44;

    @BindView(R.id.iv_img55)
    ImageView      ivImg55;

    @BindView(R.id.tv_submit)
    TextView       tvSubmit;
    @BindView(R.id.cb_register_check_box)
    CheckBox       cbRegisterCheckBox;


    @Override
    public int getLayoutId() {
        return R.layout.activity_enterprise;
    }

    @Override
    public void initView() {
        tvTitleText.setText("企业认证");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        ivTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({
            R.id.tv_fw,
            R.id.tv_ys,
            R.id.iv_img11,
            R.id.iv_img22,
            R.id.iv_img33,
            R.id.iv_img44,
            R.id.iv_img55,
            R.id.tv_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fw:

                Intent intent = new Intent(EnterprisePermissionActivity.this, WebUtilsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.tv_ys:

                Intent intent1 = new Intent(EnterprisePermissionActivity.this, WebUtilsActivity.class);
                intent1.putExtra("type",1);
                startActivity(intent1);
                break;
            case R.id.iv_img11:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(1);
                break;
            case R.id.iv_img22:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(2);
                break;
            case R.id.iv_img33:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(3);
                break;
            case R.id.iv_img44:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(4);
                break;
            case R.id.iv_img55:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(5);
                break;
            case R.id.tv_submit:
                upSellers();
                break;
        }

    }

    private int code;
    public void selectPictur(int poition){
        if(poition==1){
            code=Constants.INTENT_REQUESTCODE_VERIFIED_IMG1;
        }else if(poition==2){
            code=Constants.INTENT_REQUESTCODE_VERIFIED_IMG2;
        }else if(poition==3){
            code=Constants.INTENT_REQUESTCODE_VERIFIED_IMG3;
        }else if(poition==4){
            code=Constants.INTENT_REQUESTCODE_VERIFIED_IMG4;
        }else if(poition==5){
            code=Constants.INTENT_REQUESTCODE_VERIFIED_IMG5;
        }

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
                                    .start(EnterprisePermissionActivity.this, code);
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK  && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            String    imageUrl="";
            if (resultList != null && resultList.size() > 0) {
                 imageUrl = resultList.get(0).getImagePath();

            }
            if(requestCode==Constants.INTENT_REQUESTCODE_VERIFIED_IMG1){
                uploadImg(imageUrl,1);
            }else if(requestCode==Constants.INTENT_REQUESTCODE_VERIFIED_IMG2){
                uploadImg(imageUrl,2);
            }else if(requestCode==Constants.INTENT_REQUESTCODE_VERIFIED_IMG3){
                uploadImg(imageUrl,3);
            }else if(requestCode==Constants.INTENT_REQUESTCODE_VERIFIED_IMG4){
                uploadImg(imageUrl,4);
            }else if(requestCode==Constants.INTENT_REQUESTCODE_VERIFIED_IMG5){
                uploadImg(imageUrl,5);
            }

        }
    }
    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    private void uploadImg(String imgPath,final int poition) {
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
            ToastUtil.showToast("上传失败");
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
                    if(poition==1){
                        img1=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg11, Constants.BASE_URL +img1);

                    }else if(poition==2){
                        img2=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg22, Constants.BASE_URL +img2);
                    }else if(poition==3){
                        img3=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg33, Constants.BASE_URL +img3);
                    }else if(poition==4){
                        img4=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg44, Constants.BASE_URL +img4);
                    }else if(poition==5){
                        img5=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg55, Constants.BASE_URL +img5);
                    }

                } else {
                    if(poition==1){
                        img1=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg11, R.drawable.icon_cer_1);

                    }else if(poition==2){
                        img2=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg22, R.drawable.icon_cer_2);
                    }else if(poition==3){
                        img3=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg33, R.drawable.icon_cer_3);
                    }else if(poition==4){
                        img4=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg44, R.drawable.icon_cer_4);
                    }else if(poition==5){
                        img5=object.getUrl();
                        GlideUtils.getInstances().loadNormalImg(EnterprisePermissionActivity.this, ivImg55, R.drawable.icon_cer_5);
                    }
                    ToastUtil.showToast("上传图片失败");
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
//                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, "seller", part);
    }
    private List<String> imgs=new ArrayList<>();
    private String content;
    public void upSellers() {

        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            ToastUtil.showToast("请输入营业执照上真实填写");
            return;
        }

        if (TextUtils.isEmpty(etContent1.getText().toString().trim())) {
            ToastUtil.showToast("请输入营业执照上的全称");
            return;
        }
        if (TextUtils.isEmpty(img1)) {
            ToastUtil.showToast("请上传法人身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(img2)) {
            ToastUtil.showToast("请上传法人身份证背面照");
            return;
        }
        if (TextUtils.isEmpty(img3)) {
            ToastUtil.showToast("请上传营业执照");
            return;
        }
        if (TextUtils.isEmpty(img4)) {
            ToastUtil.showToast("请上传店铺照");
            return;
        }
        if (TextUtils.isEmpty(img5)) {
            ToastUtil.showToast("请上传logo");
            return;
        }

        if (!cbRegisterCheckBox.isChecked()) {
            ToastUtil.showToast("请同意相关条款");
            return;
        }
        showLoadDialog();
        content="";
        imgs.add(img1);
        imgs.add(img2);
        Gson g = new Gson();
        content = g.toJson(imgs);
        HashMap<String,String> map = new HashMap<>();
        map.put("type","default");
        map.put("shop_name",etContent1.getText().toString().trim());
        map.put("name",etName.getText().toString().trim());
        map.put("logo",img5);
        map.put("id_cards",content);
        map.put("credentials",img3);
        map.put("photos",img4);
        map.put("logo",img5);
        DataManager.getInstance().upSellers(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                ToastUtil.showToast("提交成功，请等待审核");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                finish();
            }
        }, map);
    }

}
