package com.hqmy.market.view.mainfragment.learn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.PPT;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.common.utils.ToastUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

public class CourseWarehousePPTFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private CourseWarehousePPTAdapter pptAdapter;
    VideoBean videoBean;
    BaseDownloadTask fileDownloader;

    public static CourseWarehousePPTFragment newInstance(VideoBean videoBean) {
        CourseWarehousePPTFragment fragment = new CourseWarehousePPTFragment();
        fragment.videoBean = videoBean;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.course_warehouse_ppt_fragment;
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        pptAdapter = new CourseWarehousePPTAdapter();
        recyclerView.setAdapter(pptAdapter);
        pptAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new RxPermissions(getActivity()).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    PPT ppt = pptAdapter.getItem(position);
                                    openPPT(Constants.WEB_IMG_URL_UPLOADS + ppt.getPpt_file());
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void initData() {
        if (videoBean != null && videoBean.getCourse_info() != null && videoBean.getCourse_info().getData() != null && videoBean.getCourse_info().getData().getPpt() != null) {
            pptAdapter.setNewData(videoBean.getCourse_info().getData().getPpt());
        }
    }

    @Override
    protected void initListener() {

    }

    private void openPPT(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        showLoadDialog();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/love";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String pathName = url.substring(url.lastIndexOf("/"), url.length());
        final String pathFile = path + pathName;
        fileDownloader = FileDownloader.getImpl().create(url)
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
                        LogUtil.d("down", "error");
                        ToastUtil.showToast("打开失败");
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
                        LogUtil.d("down", "warn");
                        dissLoadDialog();
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        LogUtil.d("down", "completed");
                        dissLoadDialog();
                        Intent intent = getWordFileIntent(pathFile);
                        startActivity(intent);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);
                        LogUtil.d("down", "warn");
                    }
                });
        fileDownloader.start();
    }

    private Intent getWordFileIntent(String Path) {
        File file = new File(Path);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type = getMIMEType(file);
        if (type.contains("pdf") || type.contains("vnd.ms-powerpoint") || type.contains("vnd.ms-word") || type.contains("vnd.ms-excel") || type.contains("text/plain") || type.contains("text/html")) {
            if (isInstall(getActivity(), "cn.wps.moffice_eng")) {
                intent.setClassName("cn.wps.moffice_eng",
                        "cn.wps.moffice.documentmanager.PreStartActivity2");
                setUri(intent, file, type);
            } else {
                setUri(intent, file, type);
            }
        } else {

            setUri(intent, file, type);
        }
        return intent;
    }

    private void setUri(Intent intent, File file, String type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.hqmy.market.FileProvider", file);
            intent.setDataAndType(contentUri, type);
        } else {
            Uri uri = Uri.fromFile(file);
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(uri);
            intent.setDataAndType(uri, type);
        }

    }

    private boolean isInstall(Context context, String packageName) {//判断是否安装某个app
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("pdf")) {
            type = "application/pdf";
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image/*";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else if (end.equals("pptx") || end.equals("ppt")) {
            type = "application/vnd.ms-powerpoint";
        } else if (end.equals("docx") || end.equals("doc")) {
            type = "application/vnd.ms-word";
        } else if (end.equals("xlsx") || end.equals("xls")) {
            type = "application/vnd.ms-excel";
        } else if (end.equals("txt")) {
            type = "text/plain";
        } else if (end.equals("html") || end.equals("htm")) {
            type = "text/html";
        } else {
            //如果无法直接打开，就跳出软件列表给用户选择
            type = "*/*";
        }
        return type;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fileDownloader != null) {
            fileDownloader.pause();
        }
    }

    class CourseWarehousePPTAdapter extends BaseQuickAdapter<PPT, BaseViewHolder> {

        public CourseWarehousePPTAdapter() {
            super(R.layout.course_warehouse_ppt_item, null);
        }

        @Override
        protected void convert(BaseViewHolder helper, PPT item) {
            GlideUtils.getInstances().loadNormalImg(getActivity(), helper.getView(R.id.iv_icon), Constants.WEB_IMG_URL_UPLOADS + item.getPpt_cover());
        }
    }
}
