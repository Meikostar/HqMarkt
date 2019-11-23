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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.Param;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.StringUtil;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.MainActivity;
import com.hqmy.market.view.widgets.MCheckBox;
import com.hqmy.market.view.widgets.dialog.ShopTypeWindow;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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

public class AfterLoginActivity extends BaseActivity {


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
    @BindView(R.id.cb_nan)
    MCheckBox      cbNan;
    @BindView(R.id.tv_nan)
    TextView       tvNan;
    @BindView(R.id.cb_nv)
    MCheckBox      cbNv;
    @BindView(R.id.tv_nv)
    TextView       tvNv;
    @BindView(R.id.et_content2)
    EditText       etContent2;
    @BindView(R.id.cb_qy)
    MCheckBox      cbQy;
    @BindView(R.id.tv_qy)
    TextView       tvQy;
    @BindView(R.id.cb_gr)
    MCheckBox      cbGr;
    @BindView(R.id.tv_gr)
    TextView       tvGr;
    @BindView(R.id.et_content3)
    EditText       etContent3;
    @BindView(R.id.et_content4)
    EditText       etContent4;
    @BindView(R.id.iv_img11)
    ImageView      ivImg11;
    @BindView(R.id.ll_qy)
    LinearLayout   llQy;
    @BindView(R.id.et_content1s)
    EditText       etContent1s;
    @BindView(R.id.et_content2s)
    EditText       etContent2s;
    @BindView(R.id.et_contents3)
    EditText       etContents3;
    @BindView(R.id.iv_imgs11)
    ImageView      ivImgs11;
    @BindView(R.id.iv_imgs22)
    ImageView      ivImgs22;
    @BindView(R.id.ll_gr)
    LinearLayout   llGr;
    @BindView(R.id.et_contents)
    TextView       etContents;
    @BindView(R.id.et_content5)
    EditText       etContent5;
    @BindView(R.id.iv_img22)
    ImageView      ivImg22;
    @BindView(R.id.et_contentss)
    TextView       etContentss;
    @BindView(R.id.iv_img33)
    ImageView      ivImg33;
    @BindView(R.id.tv_submit)
    TextView       tvSubmit;
    @BindView(R.id.cb_register_check_box)
    CheckBox       cbRegisterCheckBox;
    @BindView(R.id.tv_fw)
    TextView       tvFw;
    @BindView(R.id.tv_ys)
    TextView       tvYs;
    @BindView(R.id.ll_nan)
    LinearLayout   llNan;
    @BindView(R.id.ll_nv)
    LinearLayout   ll_nv;
    @BindView(R.id.ll_cqy)
    LinearLayout   llCqy;
    @BindView(R.id.ll_cgr)
    LinearLayout   llCgr;
    @BindView(R.id.ll_pt)
    LinearLayout   llPt;
    @BindView(R.id.ll_lx)
    LinearLayout   ll_lx;

    @Override
    public int getLayoutId() {
        return R.layout.activity_after_login;
    }
    private String phone;
    @Override
    public void initView() {
        tvTitleText.setText("完善资料");
        phone=getIntent().getStringExtra("phone");
    }

    @Override
    public void initData() {
        getTags();
        showSelectType();
    }
    private boolean isMan;
    private boolean isEnsity;
    @Override
    public void initListener() {
        ll_lx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoptypeWindow2.showPopView(ll_lx);
            }
        });

        llPt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoptypeWindow.showPopView(llPt);
            }
        });
        llCgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isMan=false;
                cbQy.setChecked(false);
                cbGr.setChecked(true);
                llGr.setVisibility(View.VISIBLE);
                llQy.setVisibility(View.GONE);


            }
        });
        llCqy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isMan=true;
                cbQy.setChecked(true);
                cbGr.setChecked(false);
                llGr.setVisibility(View.GONE);
                llQy.setVisibility(View.VISIBLE);

            }
        });
        ll_nv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cbNan.setChecked(false);
                    cbNv.setChecked(true);

            }
        });
        llNan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    cbNan.setChecked(true);
                    cbNv.setChecked(false);
            }
        });

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
            R.id.iv_imgs11,
            R.id.iv_imgs22,
            R.id.tv_submit
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_fw:
                Intent intent1 = new Intent(AfterLoginActivity.this, WebUtilsActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);

                break;
            case R.id.tv_ys:
                Intent intent = new Intent(AfterLoginActivity.this, WebUtilsActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);

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
            case R.id.iv_imgs11:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(4);
                break;
            case R.id.iv_imgs22:
                //                ToastUtil.showToast("上传身份证正面照");
                selectPictur(5);
                break;
            case R.id.tv_submit:
                upSellers();
                break;
        }

    }

    private int code;

    public void selectPictur(int poition) {
        if (poition == 1) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG1;
        } else if (poition == 2) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG2;
        } else if (poition == 3) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG3;
        } else if (poition == 4) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG4;
        } else if (poition == 5) {
            code = Constants.INTENT_REQUESTCODE_VERIFIED_IMG5;
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
                                    .start(AfterLoginActivity.this, code);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            String imageUrl = "";
            if (resultList != null && resultList.size() > 0) {
                imageUrl = resultList.get(0).getImagePath();

            }
            if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1) {
                uploadImg(imageUrl, 1);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG2) {
                uploadImg(imageUrl, 2);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG3) {
                uploadImg(imageUrl, 3);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG4) {
                uploadImg(imageUrl, 4);
            } else if (requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG5) {
                uploadImg(imageUrl, 5);
            }

        }
    }

    private String img1;
    private String img2;
    private String img3;
    private String img4;
    private String img5;
    UploadFilesDto uploadFilesDto;

    private void uploadImg(String imgPath, final int poition) {
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
                    if (poition == 1) {
                        img1 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg11, Constants.WEB_IMG_URL_UPLOADS + img1);

                    } else if (poition == 2) {
                        img2 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg22, Constants.WEB_IMG_URL_UPLOADS + img2);
                    } else if (poition == 3) {
                        img3 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg33, Constants.WEB_IMG_URL_UPLOADS + img3);
                    } else if (poition == 4) {
                        img4 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImgs11, Constants.WEB_IMG_URL_UPLOADS + img4);
                    } else if (poition == 5) {
                        img5 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImgs22, Constants.WEB_IMG_URL_UPLOADS + img5);
                    }

                } else {
                    if (poition == 1) {
                        img1 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg11, R.drawable.adds);

                    } else if (poition == 2) {
                        img2 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg22, R.drawable.adds);
                    } else if (poition == 3) {
                        img3 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImg33, R.drawable.adds);
                    } else if (poition == 4) {
                        img4 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImgs11, R.drawable.card1);
                    } else if (poition == 5) {
                        img5 = object.getPath();
                        GlideUtils.getInstances().loadNormalImg(AfterLoginActivity.this, ivImgs22, R.drawable.card2);
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


    private ShopTypeWindow shoptypeWindow;
    private ShopTypeWindow shoptypeWindow2;


    private void showSelectType() {

        shoptypeWindow = new ShopTypeWindow(this);
        shoptypeWindow.setSureListener(new ShopTypeWindow.ClickListener() {
            @Override
            public void clickListener(String name) {
                etContents.setText(name);
            }
        });
        shoptypeWindow2 = new ShopTypeWindow(this);

        shoptypeWindow2.setSureListener(new ShopTypeWindow.ClickListener() {
            @Override
            public void clickListener(String name) {
                etContentss.setText(name);
            }
        });

    }

    public void getTags(){
        DataManager.getInstance().getTags(new DefaultSingleObserver<Param>() {
            @Override
            public void onSuccess(Param result) {
                dissLoadDialog();
                if(result!=null){
                    if(result.shop_platform!=null){
                        shoptypeWindow.selectData(result.shop_platform,"销售平台");
                    }
                    if(result.shop_category!=null){
                        shoptypeWindow2.selectData(result.shop_category,"店铺类型");
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {

                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        });

    }
    private List<String> imgs = new ArrayList<>();
    private String       content;
    private int          type = 1;

    public void upSellers() {
        boolean isPerson=cbGr.isCheck();

        if (TextUtils.isEmpty(etContent1.getText().toString().trim())) {
            ToastUtil.showToast("请输入登录名");
            return;
        }
        if (TextUtils.isEmpty(etContent2.getText().toString().trim())) {
            ToastUtil.showToast("请输入手机号码");
            return;
        }
        if(isPerson){
            if (TextUtils.isEmpty(etContent1s.getText().toString().trim())) {
                ToastUtil.showToast("请输入真实姓名");
                return;
            }
            if (TextUtils.isEmpty(etContent2s.getText().toString().trim())) {
                ToastUtil.showToast("请输入身份证号码");
                return;
            }
            try {
                if (!StringUtil.IDCardValidate(etContent2s.getText().toString().trim())) {
                    ToastUtil.showToast("请输入正确的身份证");
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(etContents3.getText().toString().trim())) {
                ToastUtil.showToast("请输入地址");
                return;
            }
            if (TextUtils.isEmpty(img4)) {
                ToastUtil.showToast("请上传身份证正面照");
                return;
            }
            if (TextUtils.isEmpty(img5)) {
                ToastUtil.showToast("请上传身份证背面照");
                return;
            }
        }else {
            if (TextUtils.isEmpty(etContent3.getText().toString().trim())) {
                ToastUtil.showToast("请输入公司名称");
                return;
            }
            if (TextUtils.isEmpty(etContent4.getText().toString().trim())) {
                ToastUtil.showToast("请输入地址");
                return;
            }
            if (TextUtils.isEmpty(img1)) {
                ToastUtil.showToast("请上传营业执照");
                return;
            }

        }
        if (TextUtils.isEmpty(img2)) {
            ToastUtil.showToast("请上传网店管理后台截图");
            return;
        }
        if (TextUtils.isEmpty(img3)) {
            ToastUtil.showToast("请上传网店管理后台截图");
            return;
        }
        if (TextUtils.isEmpty(etContents.getText().toString())) {
            ToastUtil.showToast("请选择请选择销售平台");
            return;
        }
        if (TextUtils.isEmpty(etContentss.getText().toString())) {
            ToastUtil.showToast("请选择请选择店铺类型");
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


        if (!cbRegisterCheckBox.isChecked()) {
            ToastUtil.showToast("请同意相关条款");
            return;
        }
        showLoadDialog();
        content = "";
        imgs.add(img1);
        imgs.add(img2);
        Gson g = new Gson();
        content = g.toJson(imgs);
        HashMap<String, String> map = new HashMap<>();


        map.put("name", etContent1.getText().toString().trim());
        map.put("sex", cbNan.isCheck()?"1":"2");
        map.put("individual", cbQy.isCheck()?"0":"1");
        map.put("mobile", etContent2.getText().toString());

        map.put("identity_name", isPerson?etContent1s.getText().toString():etContent3.getText().toString());
        map.put("address", isPerson?etContents3.getText().toString():etContent4.getText().toString());
        if(isPerson){
            map.put("id_card_no", etContent2s.getText().toString());
            map.put("id_card_img_a", img4);
            map.put("id_card_img_b", img5);
        }else {
            map.put("business_license_img", img1);
        }

        map.put("online_shop_platform", etContents.getText().toString());
        map.put("online_shop_site", etContent5.getText().toString());


        map.put("online_shop_admin_panel_screenshot", img2);
        map.put("shop_category", etContentss.getText().toString());
        map.put("shop_photo", img3);

        DataManager.getInstance().afterLogin(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> result) {
                dissLoadDialog();
                if(TextUtil.isNotEmpty(phone)){
                    gotoActivity(MainActivity.class, false);
                }

                ToastUtil.showToast("提交成功，请等待审核");
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    if(TextUtil.isNotEmpty(phone)){
                        gotoActivity(MainActivity.class, false);
                    }
                    ToastUtil.showToast("提交成功，请等待审核");
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, map);
    }


}
