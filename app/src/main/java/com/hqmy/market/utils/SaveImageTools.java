package com.hqmy.market.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2017/12/12.
 */

public class SaveImageTools {

    public static void saveFile(Bitmap bm, String fileName, Context context) {

        try {
            File file = new File(context.getExternalCacheDir() + "/putImg/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File myCaptureFile = new File(context.getExternalCacheDir() + "/putImg/", fileName);
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 50, bos); // 把bitmap50%高质量压缩 到 output对象里
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getImageByte(String basePath, String filePath) {


        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(basePath + filePath);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    private void saveImageFile(byte[] data, String photoName, Context context) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(context.getExternalCacheDir() + "/putImg/");
            if (!file.exists()) {
                file.mkdirs();
            }
            File myCaptureFile = new File(context.getExternalCacheDir() + "/putImg/", photoName);
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(myCaptureFile);
            fileOutputStream.write(data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void initFileFolder(Context context) {
        File file = new File(context.getExternalCacheDir() + "/putImg/");
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static byte[] putImageByte(String filePath) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        Socket socket;
        try {
            socket = new Socket("192.168.1.203", 40000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            File file = new File(filePath);
            if (!file.exists()) {
                return null;
            }
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
            out.write(buffer);
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }
}
