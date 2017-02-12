package com.xie.designpatterns.chapter1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.xie.designpatterns.utils.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * des:SD卡缓存
 * author: marc
 * date:  2017/1/13 21:52
 * email：aliali_ha@yeah.net
 */

public class DiskCache {

    static String cacheDir = FileUtils.getPath("imageview");
    /**
     * 从缓存图片中获取图片
     *
     * @param url
     * @return
     */
    public Bitmap get(String url) {
        return BitmapFactory.decodeFile(cacheDir + url);
    }

    /**
     * 保存图片到缓存中
     */
    public void put(String url, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(cacheDir + url);
            //图片压缩一下
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static boolean isSDAvailable() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
