package com.hqmy.market.view.mainfragment.community;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.labels.LabelsView;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.bean.TagBean;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.Compressor;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.view.adapter.UploadPicAdapter;

import io.reactivex.functions.Consumer;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 发布话题
 * Created by rzb on 2019/5/13.
 */
public class TopicPublishActivity extends BaseActivity {
    @BindView(R.id.layout_top)
    RelativeLayout layout_top;
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_text)
    TextView  mTitleText;
    @BindView(R.id.tv_title_right)
    TextView  mTitleRight;
    @BindView(R.id.recy_view)
    RecyclerView recyPic;
    @BindView(R.id.lv_topic_tag)
    LabelsView  lv_topic_tag;
    @BindView(R.id.ed_content)
    EditText  ed_content;
    @BindView(R.id.layout_topic_site)
    LinearLayout layout_topic_site;
    @BindView(R.id.tv_topic_site)
    TextView tv_topic_site;

    private List<String> tagList = new ArrayList<String>();
    private String labelStr = null;

    private int upPicPosition;
    private UploadPicAdapter mAdapter;
    private List<String> imgArr = new ArrayList<>();

    private String address = "深圳";
    private String lat = "22.30000";
    private String lng = "113.51667";

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_publish;
    }

    @Override
    public void initView() {
        layout_top.setBackgroundColor(getResources().getColor(R.color.my_color_white));
        mTitleText.setText("发布话题");
        mTitleRight.setVisibility(View.VISIBLE);
        mTitleRight.setText("发布");
        mTitleRight.setTextColor(getResources().getColor(R.color.my_color_d60029));

        Bundle bundle = getIntent().getExtras();
        List<ImageBean> list = null;
        if (bundle != null) {
            list = bundle.getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
        }
        initAdapter(list);
    }

    @Override
    public void initData() {
        getTagList();
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(mTitleRight, () -> {
            if(TextUtils.isEmpty(labelStr)){
                ToastUtil.showToast("请选择话题类型...");
                return;
            }
            if (TextUtils.isEmpty(ed_content.getText().toString().trim())) {
                ToastUtil.showToast("请输入想说什么...");
                return;
            }
            initUploadData();
        });

        lv_topic_tag.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(TextView label, Object data, int position) {
                //label 是被点击的标签，data 是标签所对应的数据，position 是标签的位置
                labelStr = (String)data;
            }
        });

        bindClickEvent(layout_topic_site, () -> {
            //gotoActivity(LocationSearchActivity.class);
        });
    }

    private void initAdapter(List<ImageBean> list) {
        mAdapter = new UploadPicAdapter(list, 9, "evaluate_pic");
        mAdapter.refresh();
        recyPic.setLayoutManager(new GridLayoutManager(this, 3));
        recyPic.setHasFixedSize(true);
        recyPic.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (Constants.IMAGEITEM_DEFAULT_ADD.equals(mAdapter.getItem(position).getImagePath())) {
                    //打开选择
                    new RxPermissions(TopicPublishActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                                            .start(TopicPublishActivity.this, Constants.INTENT_REQUESTCODE_VERIFIED_IMG1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.INTENT_REQUESTCODE_VERIFIED_IMG1 && data != null) {
            ArrayList<ImageBean> resultList = data.getExtras().getParcelableArrayList(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null && resultList.size() > 0) {
                if (mAdapter.getData().size() > 0) {
                    List<ImageBean> newList = mAdapter.getData();
                    newList.addAll(mAdapter.getData().size() - 1,resultList);
                    mAdapter.setNewData(newList);
                } else {
                    mAdapter.addData(0, resultList);
                }
                mAdapter.refresh();
            }
        }
    }

    private void initUploadData() {
        showLoadDialog();
        upPicPosition = 0;
        imgArr.clear();
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
            String content = ed_content.getText().toString().trim();
            publishDynamic("", content, 1, imgArr, labelStr, address, lat, lng);
        }
    }

    //获取话题标签
    private void getTagList() {
        DataManager.getInstance().getTagList(new DefaultSingleObserver<TagBean>() {
            @Override
            public void onSuccess(TagBean tagBean) {
                dissLoadDialog();
                if(tagBean != null) {
                    List<String> tagList = tagBean.getPost();
                    if (tagList != null) {
                        if (tagList.size() > 0) {
                            labelStr = tagList.get(0);
                            lv_topic_tag.setSelectType(LabelsView.SelectType.SINGLE_IRREVOCABLY);
                            lv_topic_tag.setLabels(tagList, new LabelsView.LabelTextProvider<String>() {
                                @Override
                                public CharSequence getLabelText(TextView label, int position, String data) {
                                    return data;
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        });
    }

    //发布动态
    private void publishDynamic(String title, String content, int type, List<String> imgArr, String tags, String address, String lat, String lng) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("content",content);
        map.put("type", type);
        if (imgArr.size() > 0) {
            for (int i = 0; i < imgArr.size(); i++) {
                map.put("img[" + i + "]", imgArr.get(i));
            }
        }
        map.put("tags", tags);
        map.put("address", address);
        map.put("lat", lat);
        map.put("lng", lng);
        DataManager.getInstance().publishDynamic(new DefaultSingleObserver<HttpResult<Object>>() {
            @Override
            public void onSuccess(HttpResult<Object> httpResult) {
                dissLoadDialog();
                ToastUtil.showToast("话题发布成功");
                finish();
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
            }
        }, map);
    }

    private void uploadImg(String imgPath) {
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
            ToastUtil.showToast("上传话题图片失败");
            return;
        }
        //showLoadDialog();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), compressedImage);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        DataManager.getInstance().uploadFiles(new DefaultSingleObserver<UploadFilesDto>() {
            @Override
            public void onSuccess(UploadFilesDto object) {
                if (object != null) {
                    imgArr.add(String.valueOf(object.getPath()));
                    dealUpload();
                } else {
                    dissLoadDialog();
                    ToastUtil.showToast("上传话题图片失败");
                }
            }
            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.toast(ApiException.getInstance().getErrorMsg());
            }
        }, "image", part);
    }
}
