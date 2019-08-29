package com.hqmy.market.rong.message.myimagechat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hqmy.market.common.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.assist.FailReason;
import io.rong.imageloader.core.assist.ImageScaleType;
import io.rong.imageloader.core.assist.ImageSize;
import io.rong.imageloader.core.listener.ImageLoadingListener;
import io.rong.imageloader.core.listener.ImageLoadingProgressListener;
import io.rong.imkit.RongBaseNoActionbarActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.image.HackyViewPager;
import io.rong.imkit.utilities.OptionsPopupDialog;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imkit.utilities.RongUtils;
import io.rong.imkit.utils.ImageDownloadManager;
import io.rong.imlib.RongCommonDefine;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.destruct.DestructionTaskManager;
import io.rong.imlib.destruct.MessageBufferPool;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.DestructionCmdMessage;
import io.rong.subscaleview.ImageSource;
import io.rong.subscaleview.SubsamplingScaleImageView;

/**
 * Created by rzb on 2019/4/27.
 */
public class MyPicturePagerActivity extends RongBaseNoActionbarActivity implements View.OnLongClickListener {
    private static final String TAG = MyPicturePagerActivity.class.getSimpleName();
    private static final int IMAGE_MESSAGE_COUNT = 10;
    private HackyViewPager                mViewPager;
    private MyImageMessage                mCurrentImageMessage;
    private Message                       mMessage;
    private Conversation.ConversationType mConversationType;
    private int                           mCurrentMessageId;
    private String mTargetId = null;
    private int mCurrentIndex = 0;
    private ImageAdapter mImageAdapter;
    private boolean                        isFirstTime         = false;
    private File                           toFile              = null;
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        public void onPageSelected(int position) {
            RLog.i("MyPicturePagerActivity", "onPageSelected. position:" + position);
            MyPicturePagerActivity.this.mCurrentIndex = position;
            View view = MyPicturePagerActivity.this.mViewPager.findViewById(position);
            if(view != null) {
                MyPicturePagerActivity.this.mImageAdapter.updatePhotoView(position, view);
            }

            if(position == MyPicturePagerActivity.this.mImageAdapter.getCount() - 1) {
                MyPicturePagerActivity.this.getConversationImageUris(MyPicturePagerActivity.this.mImageAdapter.getItem(position).getMessageId().getMessageId(), RongCommonDefine.GetMessageDirection.BEHIND);
            } else if(position == 0) {
                MyPicturePagerActivity.this.getConversationImageUris(MyPicturePagerActivity.this.mImageAdapter.getItem(position).getMessageId().getMessageId(), RongCommonDefine.GetMessageDirection.FRONT);
            }
        }

        public void onPageScrollStateChanged(int state) {
        }
    };

    public MyPicturePagerActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(io.rong.imkit.R.layout.rc_fr_photo);
        Message currentMessage = (Message)this.getIntent().getParcelableExtra("message");
        this.mMessage = currentMessage;
        this.mCurrentImageMessage = (MyImageMessage)currentMessage.getContent();
        this.mConversationType = currentMessage.getConversationType();
        this.mCurrentMessageId = currentMessage.getMessageId();
        this.mTargetId = currentMessage.getTargetId();
        this.mViewPager = (HackyViewPager)this.findViewById(io.rong.imkit.R.id.viewpager);
        this.mViewPager.setOnPageChangeListener(this.mPageChangeListener);
        this.mImageAdapter = new ImageAdapter(null);
        this.isFirstTime = true;
        this.getConversationImageUris(this.mCurrentMessageId, RongCommonDefine.GetMessageDirection.FRONT);
        this.getConversationImageUris(this.mCurrentMessageId, RongCommonDefine.GetMessageDirection.BEHIND);
    }

    private void getConversationImageUris(int mesageId, final RongCommonDefine.GetMessageDirection direction) {
        if(this.mConversationType != null && !TextUtils.isEmpty(this.mTargetId)) {
            RongIMClient.getInstance().getHistoryMessages(this.mConversationType, this.mTargetId, "RC:ImgMsg", mesageId, 10, direction, new RongIMClient.ResultCallback<List<Message>>() {
                public void onSuccess(List<Message> messages) {
                    ArrayList<ImageInfo> lists = new ArrayList();
                    if(messages != null) {
                        if(direction.equals(RongCommonDefine.GetMessageDirection.FRONT)) {
                            Collections.reverse(messages);
                        }

                        for(int i = 0; i < messages.size(); ++i) {
                            Message message = (Message)messages.get(i);
                            if(message.getContent() instanceof MyImageMessage) {
                                MyImageMessage imageMessage = (MyImageMessage)message.getContent();
                                Uri largeImageUri = imageMessage.getLocalUri() == null?imageMessage.getRemoteUri():imageMessage.getLocalUri();
                                if(imageMessage.getThumUri() != null && largeImageUri != null) {
                                    lists.add(MyPicturePagerActivity.this.new ImageInfo(message, imageMessage.getThumUri(), largeImageUri));
                                }
                            }
                        }
                    }

                    if(direction.equals(RongCommonDefine.GetMessageDirection.FRONT) && MyPicturePagerActivity.this.isFirstTime) {
                        lists.add(MyPicturePagerActivity.this.new ImageInfo(MyPicturePagerActivity.this.mMessage, MyPicturePagerActivity.this.mCurrentImageMessage.getThumUri(), MyPicturePagerActivity.this.mCurrentImageMessage.getLocalUri() == null? MyPicturePagerActivity.this.mCurrentImageMessage.getRemoteUri(): MyPicturePagerActivity.this.mCurrentImageMessage.getLocalUri()));
                        MyPicturePagerActivity.this.mImageAdapter.addData(lists, direction.equals(RongCommonDefine.GetMessageDirection.FRONT));
                        MyPicturePagerActivity.this.mViewPager.setAdapter(MyPicturePagerActivity.this.mImageAdapter);
                        MyPicturePagerActivity.this.isFirstTime = false;
                        MyPicturePagerActivity.this.mViewPager.setCurrentItem(lists.size() - 1);
                        MyPicturePagerActivity.this.mCurrentIndex = lists.size() - 1;
                    } else if(lists.size() > 0) {
                        MyPicturePagerActivity.this.mImageAdapter.addData(lists, direction.equals(RongCommonDefine.GetMessageDirection.FRONT));
                        MyPicturePagerActivity.this.mImageAdapter.notifyDataSetChanged();
                        if(direction.equals(RongCommonDefine.GetMessageDirection.FRONT)) {
                            MyPicturePagerActivity.this.mViewPager.setCurrentItem(lists.size());
                            MyPicturePagerActivity.this.mCurrentIndex = lists.size();
                        }
                    }

                }

                public void onError(RongIMClient.ErrorCode e) {
                }
            });
        }

    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        DestructionTaskManager.getInstance().removeListeners("PicturePagerActivity");
    }

    public boolean onPictureLongClick(View v, Uri thumbUri, Uri largeImageUri) {
        return false;
    }

    public boolean onLongClick(View v) {
        ImageInfo imageInfo = this.mImageAdapter.getImageInfo(this.mCurrentIndex);
        if(imageInfo != null) {
            Uri thumbUri = imageInfo.getThumbUri();
            Uri largeImageUri = imageInfo.getLargeImageUri();
            if(this.onPictureLongClick(v, thumbUri, largeImageUri)) {
                return true;
            }
            if(largeImageUri == null) {
                return false;
            }
            final File file;
            if(!largeImageUri.getScheme().startsWith("http") && !largeImageUri.getScheme().startsWith("https")) {
                file = new File(largeImageUri.getPath());
            } else {
                file = ImageLoader.getInstance().getDiskCache().get(largeImageUri.toString());
            }

            String[] items = new String[]{"保存图片","编辑图片"};
            OptionsPopupDialog.newInstance(this, items).setOptionsPopupDialogListener(new OptionsPopupDialog.OnOptionsItemClickedListener() {
                public void onOptionsItemClicked(int which) {
                    if(which == 0) {
                        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        if(!PermissionCheckUtil.requestPermissions(MyPicturePagerActivity.this, permissions)) {
                            return;
                        }
                        String saveImagePath = RongUtils.getImageSavePath(MyPicturePagerActivity.this);
                        if(file != null && file.exists()) {
                            String name = System.currentTimeMillis() + ".jpg";
                            FileUtils.copyFile(file, saveImagePath + File.separator, name);
                            MediaScannerConnection.scanFile(MyPicturePagerActivity.this, new String[]{saveImagePath + File.separator + name}, (String[])null, (MediaScannerConnection.OnScanCompletedListener)null);
                            ToastUtil.showToast(getString(io.rong.imkit.R.string.rc_save_picture_at));
                        } else {
                            ToastUtil.showToast(getString(io.rong.imkit.R.string.rc_src_file_not_found));
                        }
                    }else if(which == 1){

                        String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                        if(!PermissionCheckUtil.requestPermissions(MyPicturePagerActivity.this, permissions)) {
                            return;
                        }
                        String saveImagePath = RongUtils.getImageSavePath(MyPicturePagerActivity.this);
                        if(file != null && file.exists()) {
                            String name = System.currentTimeMillis() + ".jpg";
                            toFile = FileUtils.copyFile(file, saveImagePath + File.separator, name);
                            MediaScannerConnection.scanFile(MyPicturePagerActivity.this, new String[]{saveImagePath + File.separator + name}, (String[])null, (MediaScannerConnection.OnScanCompletedListener)null);
                        }
                        //Intent intent = new Intent(MyPicturePagerActivity.this, IMGEditActivity.class);
                        //Uri imageUrl = Uri.fromFile(toFile);
                        //intent.putExtra(IMGEditActivity.EXTRA_IMAGE_URI,imageUrl);
                        //intent.putExtra(IMGEditActivity.EXTRA_IMAGE_SAVE_PATH,imageUrl.getPath());
                        //MyPicturePagerActivity.this.startActivity(intent);
                    }
                }
            }).show();
        }
        return true;
    }

    private class ImageInfo {
        private Message message;
        private Uri     thumbUri;
        private Uri     largeImageUri;

        ImageInfo(Message message, Uri thumbnail, Uri largeImageUri) {
            this.message = message;
            this.thumbUri = thumbnail;
            this.largeImageUri = largeImageUri;
        }

        public Message getMessageId() {
            return this.message;
        }

        public Uri getLargeImageUri() {
            return this.largeImageUri;
        }

        public Uri getThumbUri() {
            return this.thumbUri;
        }
    }

    private class ImageAdapter extends PagerAdapter {
        private ArrayList<ImageInfo> mImageList;

        private ImageAdapter(Object o) {
            this.mImageList = new ArrayList();
        }

        private View newView(Context context, ImageInfo imageInfo) {
            View result = LayoutInflater.from(context).inflate(io.rong.imkit.R.layout.rc_fr_image, (ViewGroup)null);
            ImageAdapter.ViewHolder holder = new ImageAdapter.ViewHolder();
            holder.progressBar = (ProgressBar)result.findViewById(io.rong.imkit.R.id.rc_progress);
            holder.progressText = (TextView)result.findViewById(io.rong.imkit.R.id.rc_txt);
            holder.photoView = (SubsamplingScaleImageView)result.findViewById(io.rong.imkit.R.id.rc_photoView);
            holder.mCountDownView = (TextView)result.findViewById(io.rong.imkit.R.id.rc_count_down);
            holder.photoView.setOnLongClickListener(MyPicturePagerActivity.this);
            holder.photoView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MyPicturePagerActivity.this.finish();
                }
            });
            result.setTag(holder);
            return result;
        }

        public void addData(ArrayList<ImageInfo> newImages, boolean direction) {
            if(newImages != null && newImages.size() != 0) {
                if(this.mImageList.size() == 0) {
                    this.mImageList.addAll(newImages);
                } else if(direction && !MyPicturePagerActivity.this.isFirstTime && !this.isDuplicate(((ImageInfo)newImages.get(0)).getMessageId().getMessageId())) {
                    ArrayList<ImageInfo> temp = new ArrayList();
                    temp.addAll(this.mImageList);
                    this.mImageList.clear();
                    this.mImageList.addAll(newImages);
                    this.mImageList.addAll(this.mImageList.size(), temp);
                } else if(!MyPicturePagerActivity.this.isFirstTime && !this.isDuplicate(((ImageInfo)newImages.get(0)).getMessageId().getMessageId())) {
                    this.mImageList.addAll(this.mImageList.size(), newImages);
                }
            }
        }

        private boolean isDuplicate(int messageId) {
            Iterator var2 = this.mImageList.iterator();

            ImageInfo info;
            do {
                if(!var2.hasNext()) {
                    return false;
                }

                info = (ImageInfo)var2.next();
            } while(info.getMessageId().getMessageId() != messageId);

            return true;
        }

        public ImageInfo getItem(int index) {
            return (ImageInfo)this.mImageList.get(index);
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public int getCount() {
            return this.mImageList.size();
        }

        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            RLog.i("MyPicturePagerActivity", "instantiateItem.position:" + position);
            View imageView = this.newView(container.getContext(), (ImageInfo)this.mImageList.get(position));
            this.updatePhotoView(position, imageView);
            imageView.setId(position);
            container.addView(imageView);
            return imageView;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            RLog.i("MyPicturePagerActivity", "destroyItem.position:" + position);
            container.removeView((View)object);
        }

        private void updatePhotoView(final int position, View view) {
            final ImageAdapter.ViewHolder holder = (ImageAdapter.ViewHolder)view.getTag();
            Uri originalUri = ((ImageInfo)this.mImageList.get(position)).getLargeImageUri();
            final Uri thumbUri = ((ImageInfo)this.mImageList.get(position)).getThumbUri();
            if(originalUri != null && thumbUri != null) {
                File file = ImageLoader.getInstance().getDiskCache().get(originalUri.toString());
                if(file != null && file.exists()) {
                    Uri resultUri = Uri.fromFile(file);
                    if(!resultUri.equals(holder.photoView.getUri())) {
                        holder.photoView.setImage(ImageSource.uri(resultUri));
                    }
                } else {
                    DisplayImageOptions options = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).build();
                    ImageLoader.getInstance().loadImage(originalUri.toString(), (ImageSize)null, options, new ImageLoadingListener() {
                        public void onLoadingStarted(String imageUri, View view) {
                            holder.photoView.setImage(ImageSource.uri(thumbUri));
                            holder.progressText.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.VISIBLE);
                            holder.progressText.setText("0%");
                        }
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            if(imageUri.startsWith("file://")) {
                                holder.progressText.setVisibility(View.GONE);
                                holder.progressBar.setVisibility(View.GONE);
                            } else {
                                ImageDownloadManager.getInstance().downloadImage(imageUri, new ImageDownloadManager.DownloadStatusListener() {
                                    public void downloadSuccess(String localPath, Bitmap bitmap) {
                                        holder.photoView.setImage(ImageSource.uri(localPath));
                                        holder.progressText.setVisibility(View.GONE);
                                        holder.progressBar.setVisibility(View.GONE);
                                    }

                                    public void downloadFailed(ImageDownloadManager.DownloadStatusError error) {
                                        holder.progressText.setVisibility(View.GONE);
                                        holder.progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            MyPicturePagerActivity.ImageAdapter.this.sendDestructingMsg(MyPicturePagerActivity.this.mMessage);
                            MyPicturePagerActivity.ImageAdapter.this.handleDestructionImage(position, holder);
                            holder.progressText.setVisibility(View.GONE);
                            holder.progressBar.setVisibility(View.GONE);
                            File file = ImageLoader.getInstance().getDiskCache().get(imageUri);
                            Uri resultUri = null;
                            if(file != null) {
                                resultUri = Uri.fromFile(file);
                            }
                            holder.photoView.setBitmapAndFileUri(loadedImage, resultUri);
                            View inPagerView = MyPicturePagerActivity.this.mViewPager.findViewById(position);
                            if(inPagerView != null) {
                                MyPicturePagerActivity.ImageAdapter.ViewHolder inPagerHolder = (MyPicturePagerActivity.ImageAdapter.ViewHolder)inPagerView.getTag();
                                if(inPagerHolder != holder) {
                                    inPagerHolder.progressText.setVisibility(View.GONE);
                                    inPagerHolder.progressBar.setVisibility(View.GONE);
                                    MyPicturePagerActivity.this.mImageAdapter.updatePhotoView(position, inPagerView);
                                }
                            }
                        }
                        public void onLoadingCancelled(String imageUri, View view) {
                            holder.progressText.setVisibility(View.GONE);
                            holder.progressText.setVisibility(View.GONE);
                        }
                    }, new ImageLoadingProgressListener() {
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            holder.progressText.setText(current * 100 / total + "%");
                            if(current == total) {
                                holder.progressText.setVisibility(View.GONE);
                                holder.progressBar.setVisibility(View.GONE);
                            } else {
                                holder.progressText.setVisibility(View.VISIBLE);
                                holder.progressBar.setVisibility(View.VISIBLE);
                            }

                        }
                    });
                    this.handleDestructionImage(position, holder);
                }
            } else {
                RLog.e("MyPicturePagerActivity", "large uri and thumbnail uri of the image should not be null.");
            }
        }

        private void sendDestructingMsg(Message message) {
            if(message.getContent().isDestruct() && message.getMessageDirection() == Message.MessageDirection.RECEIVE && message.getReadTime() <= 0L && !TextUtils.isEmpty(message.getUId())) {
                long currentTimeMillis = System.currentTimeMillis();
                RongIMClient.getInstance().setMessageReadTime((long)message.getMessageId(), currentTimeMillis, (RongIMClient.OperationCallback)null);
                message.setReadTime(currentTimeMillis);
                DestructionCmdMessage destructionCmdMessage = new DestructionCmdMessage();
                destructionCmdMessage.addBurnMessageUId(message.getUId());
                MessageBufferPool.getInstance().putMessageInBuffer(Message.obtain(message.getTargetId(), message.getConversationType(), destructionCmdMessage));
                EventBus.getDefault().post(message);
            }
        }

        private void handleDestructionImage(int position, final MyPicturePagerActivity.ImageAdapter.ViewHolder holder) {
            final Message message = ((ImageInfo)this.mImageList.get(position)).message;
            if(message.getContent().isDestruct() && message.getReadTime() > 0L) {
                holder.mCountDownView.setVisibility(View.VISIBLE);
                RongIM.getInstance().createDestructionTask(MyPicturePagerActivity.this.mMessage, new DestructionTaskManager.OnOverTimeChangeListener() {
                    public void onOverTimeChanged(final int messageId, final long leftTime) {
                        holder.mCountDownView.post(new Runnable() {
                            public void run() {
                                if(messageId == message.getMessageId()) {
                                    if(leftTime <= 30L) {
                                        holder.mCountDownView.setBackgroundResource(io.rong.imkit.R.drawable.rc_count_down_preview_count);
                                        holder.mCountDownView.setText(MyPicturePagerActivity.this.getResources().getString(io.rong.imkit.R.string.rc_time_count_down, new Object[]{Long.valueOf(leftTime)}));
                                    } else {
                                        holder.mCountDownView.setBackgroundResource(io.rong.imkit.R.drawable.rc_count_down_preview_no_count);
                                    }
                                    if(leftTime <= 0L) {
                                        String toast = MyPicturePagerActivity.this.getResources().getString(io.rong.imkit.R.string.rc_toast_message_destruct);
                                        ToastUtil.showToast(toast);
                                        MyPicturePagerActivity.this.finish();
                                    }

                                }
                            }
                        });
                    }

                    public void onMessageDestruct(int messageId) {
                    }
                }, "MyPicturePagerActivity");
            } else {
                holder.mCountDownView.setVisibility(View.GONE);
            }

        }

        public ImageInfo getImageInfo(int position) {
            return (ImageInfo)this.mImageList.get(position);
        }

        public class ViewHolder {
            ProgressBar               progressBar;
            TextView                  progressText;
            SubsamplingScaleImageView photoView;
            TextView                  mCountDownView;

            public ViewHolder() {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
