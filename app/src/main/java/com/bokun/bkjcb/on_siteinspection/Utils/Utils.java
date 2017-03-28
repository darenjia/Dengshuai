package com.bokun.bkjcb.on_siteinspection.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by BKJCB on 2017/3/24.
 */

public class Utils {

    public static Bitmap compressBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        int reqHeight = 800;
        int reqWidth = 480;
        if (height < reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        Bitmap bm;
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        int length = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight() : bitmap.getWidth();
//        int length = 150;
        LogUtil.logI("width:" + length);
        bm = Bitmap.createBitmap(bitmap, 0, 0, length,
                length, matrix, true);
        return bm;
    }

    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        bitmap = Utils.compressBitmap(bitmap);
        return bitmap;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                if (oldfile.isFile()) {
                    InputStream inStream = new FileInputStream(oldPath); //读入原文件
                    FileOutputStream fs = new FileOutputStream(newPath);
                    byte[] buffer = new byte[4 * 1024];
                    int length;
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; //字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    inStream.close();
                }
            }
        } catch (Exception e) {
            System.out.println("文件操作出错");
            e.printStackTrace();

        }

    }

}

