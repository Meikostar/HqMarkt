package com.hqmy.market.rong.message.myimagechat;

/**
 * Created by rzb on 2019/4/28.
 */

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import io.rong.message.MessageHandler;
import io.rong.message.utils.BitmapUtil;

public class MyImageMessageHandler extends MessageHandler<MyImageMessage> {
    private static final String TAG = "MyImageMessageHandler";
    private static int COMPRESSED_SIZE = 960;
    private static int COMPRESSED_QUALITY = 85;
    private static int MAX_ORIGINAL_IMAGE_SIZE = 200;
    private static int THUMB_COMPRESSED_SIZE = 240;
    private static int THUMB_COMPRESSED_MIN_SIZE = 100;
    private static int THUMB_COMPRESSED_QUALITY = 30;
    private static final String IMAGE_LOCAL_PATH = "/image/local/";
    private static final String IMAGE_THUMBNAIL_PATH = "/image/thumbnail/";
    private static final int MAX_FILE_LENGTH = 20480;

    public MyImageMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, MyImageMessage model) {
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getUId() + ".jpg";
        if(TextUtils.isEmpty(message.getUId())) {
            name = message.getMessageId() + ".jpg";
        }

        String thumb = uri.toString() + "/image/thumbnail/";
        String local = uri.toString() + "/image/local/";
        model.setLocalUri((Uri)null);
        File localFile = new File(local + name);
        if(localFile.exists()) {
            model.setLocalUri(Uri.parse("file://" + local + name));
        }

        File thumbFile = new File(thumb + name);
        if(!TextUtils.isEmpty(model.getBase64()) && !thumbFile.exists()) {
            byte[] data = null;

            try {
                data = Base64.decode(model.getBase64(), 2);
            } catch (IllegalArgumentException var11) {
                RLog.e("MyImageMessageHandler", "afterDecodeMessage Not Base64 Content!");
                RLog.e("MyImageMessageHandler", "IllegalArgumentException ", var11);
            }

            if(!isImageFile(data)) {
                RLog.e("MyImageMessageHandler", "afterDecodeMessage Not Image File!");
                return;
            }

            FileUtils.byte2File(data, thumb, name);
        }

        model.setThumUri(Uri.parse("file://" + thumb + name));
        model.setBase64((String)null);
    }

    public void encodeMessage(Message message) {
        MyImageMessage model = (MyImageMessage)message.getContent();
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".jpg";
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Resources resources = this.getContext().getResources();

        try {
            COMPRESSED_QUALITY = resources.getInteger(resources.getIdentifier("rc_image_quality", "integer", this.getContext().getPackageName()));
            COMPRESSED_SIZE = resources.getInteger(resources.getIdentifier("rc_image_size", "integer", this.getContext().getPackageName()));
            MAX_ORIGINAL_IMAGE_SIZE = resources.getInteger(resources.getIdentifier("rc_max_original_image_size", "integer", this.getContext().getPackageName()));
        } catch (NotFoundException var22) {
            var22.printStackTrace();
        }

        File file;
        Bitmap bitmap;
        if(model.getThumUri() != null && model.getThumUri().getScheme() != null && model.getThumUri().getScheme().equals("file")) {
            file = null;
            file = new File(uri.toString() + "/image/thumbnail/" + name);
            byte[] data;
            if(file.exists()) {
                model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                data = FileUtils.file2byte(file);
                if(data != null) {
                    model.setBase64(Base64.encodeToString(data, 2));
                }
            } else {
                try {
                    String thumbPath = model.getThumUri().toString().substring(5);
                    RLog.d("MyImageMessageHandler", "beforeEncodeMessage Thumbnail not save yet! " + thumbPath);
                    BitmapFactory.decodeFile(thumbPath, options);
                    String imageFormat = options.outMimeType != null?options.outMimeType:"";
                    RLog.d("MyImageMessageHandler", "Image format:" + imageFormat);
                    if(options.outWidth <= THUMB_COMPRESSED_SIZE && options.outHeight <= THUMB_COMPRESSED_SIZE) {
                        byte var28 = -1;
                        switch(imageFormat.hashCode()) {
                            case -1487018032:
                                if(imageFormat.equals("image/webp")) {
                                    var28 = 1;
                                }
                                break;
                            case -879267568:
                                if(imageFormat.equals("image/gif")) {
                                    var28 = 0;
                                }
                        }

                        switch(var28) {
                            case 0:
                            case 1:
                                Options bmOptions = new Options();
                                bmOptions.inJustDecodeBounds = false;
                                bitmap = BitmapFactory.decodeFile(thumbPath, bmOptions);
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(CompressFormat.JPEG, 100, outputStream);
                                data = outputStream.toByteArray();
                                outputStream.close();
                                if(data != null) {
                                    model.setBase64(Base64.encodeToString(data, 2));
                                    FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                                    model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                                }

                                if(!bitmap.isRecycled()) {
                                    bitmap.recycle();
                                }
                                break;
                            default:
                                File src = new File(thumbPath);
                                long fileSize = FileUtils.getFileSize(src);
                                if(fileSize > 20480L) {
                                    int sizeLimit = options.outWidth > options.outHeight?options.outWidth:options.outHeight;
                                    Bitmap bitmapLargeFile = BitmapUtil.getThumbBitmap(this.getContext(), model.getThumUri(), sizeLimit, THUMB_COMPRESSED_MIN_SIZE);
                                    if(bitmapLargeFile != null) {
                                        ByteArrayOutputStream outputStreamLargeFile = new ByteArrayOutputStream();
                                        bitmapLargeFile.compress(CompressFormat.JPEG, THUMB_COMPRESSED_QUALITY, outputStreamLargeFile);
                                        data = outputStreamLargeFile.toByteArray();
                                        model.setBase64(Base64.encodeToString(data, 2));
                                        outputStreamLargeFile.close();
                                        FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                                        model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                                        if(!bitmapLargeFile.isRecycled()) {
                                            bitmapLargeFile.recycle();
                                        }
                                    }
                                } else {
                                    data = FileUtils.file2byte(src);
                                    if(data != null) {
                                        model.setBase64(Base64.encodeToString(data, 2));
                                        String path = uri.toString() + "/image/thumbnail/";
                                        if(FileUtils.copyFile(src, path, name) != null) {
                                            model.setThumUri(Uri.parse("file://" + path + name));
                                        }
                                    }
                                }
                        }
                    } else {
                        bitmap = BitmapUtil.getThumbBitmap(this.getContext(), model.getThumUri(), THUMB_COMPRESSED_SIZE, THUMB_COMPRESSED_MIN_SIZE);
                        if(bitmap != null) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            bitmap.compress(CompressFormat.JPEG, THUMB_COMPRESSED_QUALITY, outputStream);
                            data = outputStream.toByteArray();
                            model.setBase64(Base64.encodeToString(data, 2));
                            outputStream.close();
                            FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                            model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                            if(!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    }
                } catch (Exception var24) {
                    RLog.e("MyImageMessageHandler", "Exception ", var24);
                }
            }
        }

        if(model.getLocalUri() != null && model.getLocalUri().getScheme() != null && model.getLocalUri().getScheme().equals("file")) {
            file = new File(uri.toString() + "/image/local/" + name);
            if(file.exists()) {
                model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
            } else {
                try {
                    String localPath = model.getLocalUri().toString().substring(5);
                    BitmapFactory.decodeFile(localPath, options);
                    file = new File(localPath);
                    long fileSize = file.length() / 1024L;
                    if((options.outWidth > COMPRESSED_SIZE || options.outHeight > COMPRESSED_SIZE) && !model.isFull() && fileSize > (long)MAX_ORIGINAL_IMAGE_SIZE) {
                        bitmap = BitmapUtil.getResizedBitmap(this.getContext(), model.getLocalUri(), COMPRESSED_SIZE, COMPRESSED_SIZE);
                        if(bitmap != null) {
                            String dir = uri.toString() + "/image/local/";
                            file = new File(dir);
                            if(!file.exists()) {
                                file.mkdirs();
                            }

                            file = new File(dir + name);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            bitmap.compress(CompressFormat.JPEG, COMPRESSED_QUALITY, bos);
                            bos.close();
                            model.setLocalUri(Uri.parse("file://" + dir + name));
                            if(!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    } else if(FileUtils.copyFile(new File(localPath), uri.toString() + "/image/local/", name) != null) {
                        model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
                    }
                } catch (IOException var23) {
                    RLog.e("MyImageMessageHandler", "IOException  ", var23);
                    RLog.e("MyImageMessageHandler", "beforeEncodeMessage IOException");
                }
            }
        }

    }

    private static boolean isImageFile(byte[] data) {
        if(data != null && data.length != 0) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            return options.outWidth != -1;
        } else {
            return false;
        }
    }
}