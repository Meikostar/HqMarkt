package com.hqmy.market.view.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.ReasonUploadPicAdapter;
import com.hqmy.market.view.widgets.dialog.ReturnReasonDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyOrderReturnActivity extends BaseActivity {
    @BindView(R.id.tv_title_text)
    TextView tvTitleText;
    @BindView(R.id.iv_order_goods_icon)
    ImageView ivOrderGoodsIcon;
    @BindView(R.id.tv_order_goods_name)
    TextView tvOrderGoodsName;
    @BindView(R.id.tv_order_goods_price)
    TextView tvOrderGoodsPrice;
    @BindView(R.id.tv_order_goods_num)
    TextView tvOrderGoodsNum;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_return_money)
    TextView tvReturnMoney;
    @BindView(R.id.ed_return_des)
    EditText edReturnDes;
    @BindView(R.id.recy_return)
    RecyclerView recyReturn;
    private ReturnReasonDialog dialog;
    private ReasonUploadPicAdapter mAdapter;
    private String orderId,type;
    private List<String> areadUploadImg = new ArrayList<>();
    private int upPicPosition = 0;
    private List<String> reasons = new ArrayList<>();

    @Override
    public void initListener() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.ui_myorder_return_layout;
    }

    @Override
    public void initView() {
        tvTitleText.setText("申请退款");
        Intent intent = getIntent();
        if (intent != null) {
            orderId = intent.getStringExtra(Constants.INTENT_ID);
            type = intent.getStringExtra(Constants.TYPE);
            GlideUtils.getInstances().loadNormalImg(this, ivOrderGoodsIcon, Constants.WEB_IMG_URL_UPLOADS + intent.getStringExtra("cover"));
            tvOrderGoodsName.setText(intent.getStringExtra("title"));
            if (getIntent().getBooleanExtra("isFlag2",false)){
                tvOrderGoodsPrice.setText(intent.getStringExtra("score")+"积分");
                tvReturnMoney.setText(intent.getStringExtra("score")+"积分");
            }else {
                tvOrderGoodsPrice.setText("¥" + intent.getStringExtra("price"));
                tvReturnMoney.setText("¥" + intent.getStringExtra("price"));
            }
            tvOrderGoodsNum.setText("x" + intent.getStringExtra("num"));

        }
        initPicAdapter();
    }

    @Override
    public void initData() {
        refundReason();
    }

    private void initPicAdapter() {
        mAdapter = new ReasonUploadPicAdapter(null, 6, "return_upload_pic");
        mAdapter.refresh();
        recyReturn.setLayoutManager(new GridLayoutManager(this, 3));
        recyReturn.setHasFixedSize(true);
        recyReturn.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {
                    //打开选择
                    new RxPermissions(MyOrderReturnActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean aBoolean) throws Exception {
                                    if (!aBoolean) {
                                        return;
                                    }
                                    // 用户已经同意该权限
                                    new ImagePicker()
                                            .pickType(ImagePickType.MULTI)//设置选取类型(拍照、单选、多选)
                                            .maxNum(7 - mAdapter.getItemCount())//设置最大选择数量(拍照和单选都是1，修改后也无效)
                                            .needCamera(true)//是否需要在界面中显示相机入口(类似微信)
                                            .displayer(new GlideImagePickerDisplayer())//自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                                            .start(MyOrderReturnActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
                                }
                            });
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_del:
                        mAdapter.remove(position);
                        mAdapter.refresh();
                        break;
                }
            }
        });
    }

    private void showReasonDialog() {
        if (dialog == null) {
            dialog = new ReturnReasonDialog(this, new ReturnReasonDialog.DialogListener() {
                @Override
                public void sureItem(int position, String name) {
                    tvReason.setText(name);
                }
            },reasons);
        }
        dialog.show();
    }

    private void refundReason() {
        DataManager.getInstance().refundReasons(new DefaultSingleObserver<HttpResult<List<String>>>() {
            @Override
            public void onSuccess(HttpResult<List<String>> httpResult) {
                dissLoadDialog();
                reasons.clear();
                if (httpResult != null && httpResult.getData() != null) {
                    reasons.addAll(httpResult.getData());
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

    private void refundOrder() {
        Map<String, Object> map = new HashMap<>();

        if (!TextUtils.isEmpty(edReturnDes.getText().toString())){
            map.put("describe", edReturnDes.getText().toString());
        }
        if (!TextUtils.isEmpty(tvReason.getText().toString())){
            map.put("reason", tvReason.getText().toString());
        }

        if (areadUploadImg.size() > 0) {
            map.put("imgs", areadUploadImg);
        }
        DataManager.getInstance().refundOrder(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object o) {
                dissLoadDialog();
                ToastUtil.showToast("成功");
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                if (ApiException.getInstance().isSuccess()) {
                    ToastUtil.showToast("成功");
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }

            }
        }, orderId, map,type);
    }

    private void initUploadData() {
        showLoadDialog();
        upPicPosition = 0;
        areadUploadImg.clear();
        dealUpload();
    }

    private void dealUpload() {
        List<ImageBean> imageBeanList = mAdapter.getData();
        if (imageBeanList != null && upPicPosition < imageBeanList.size()) {
            String imgPath = imageBeanList.get(upPicPosition).getImagePath();
            upPicPosition = upPicPosition + 1;
            if (!Constants.IMAGEITEM_DEFAULT_ADD.equals(imgPath)) {
                uploadImg(imgPath);
            } else {//添加图片
                dealUpload();
            }

        } else {
            upLoadFinish();
        }

    }

    private void upLoadFinish() {
        refundOrder();
    }

    private void uploadImg(String imgPath) {
        File file = new File(imgPath);
        File compressedImage = null;
        if (file.exists()) {
            //压缩文件
            try {
                compressedImage = new Compressor(this).compressToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (compressedImage == null) {
                compressedImage = file;
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
                @Override
                public void onSuccess(UploadFilesDto object) {
                    if (object != null) {
                        Log.d("onSuccess() = ", "UploadFilesDto==" + object.getName());
                        areadUploadImg.add(object.getPath() + "");
                    }
                    dealUpload();
                }

                @Override
                public void onError(Throwable throwable) {
                    dissLoadDialog();
//                    ToastUtil.toast(ApiException.getInstance().getErrorMsg());
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }, "image", part);
        } else {
            dealUpload();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            List<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                mAdapter.addData(0, resultList);
                mAdapter.refresh();
            }
        }

    }

    @OnClick({R.id.iv_title_back, R.id.ll_reason, R.id.btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.ll_reason:
                showReasonDialog();
                break;
            case R.id.btn_sure:

                if (TextUtils.isEmpty(tvReason.getText().toString())||tvReason.getText().toString().equals("请选择")){
                ToastUtil.showToast("请选择退款原因");
                return;
                }
                if (TextUtils.isEmpty(edReturnDes.getText().toString())){
                    ToastUtil.showToast("请填写退款说明");
                    return;
                }
                initUploadData();
                break;
        }
    }
}
