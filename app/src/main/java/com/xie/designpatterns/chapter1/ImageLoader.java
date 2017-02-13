package com.xie.designpatterns.chapter1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.xie.designpatterns.logger.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * des:图片加载类
 * author: marc
 * date:  2017/1/13 20:46
 * email：aliali_ha@yeah.net
 */

public class ImageLoader {
    //图片缓存类
    //线程池，线程数量为CPU的数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    ImageCache mImageCache = new ImageCache();
    //    DiskCache mDiskCache = new DiskCache();
    //是否使用sd卡缓存
    boolean isUserDiskCache = false;

    /**
     * 展示图片
     *
     * @param url
     * @param imageView
     */
    public void displayImage(final String url, final ImageView imageView) {
        Bitmap bitmap = mImageCache.get(url);
//        final Bitmap bitmap = isUserDiskCache ? mDiskCache.get(url) : mImageCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        imageView.setTag(url);
        //开启个子线程下载图【片
        Logger.i("开始显示");
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitma = downloadImage(url);
                Logger.i("开始down");
                if (null == bitma) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitma);
                }
                mImageCache.put(url, bitma);
            }
        });
    }

    public void userDiskCache(boolean isUserDiskCache) {
        this.isUserDiskCache = isUserDiskCache;
    }

    /**
     * 现在图片
     *
     * @param imageUrl 图片地址
     * @return
     */
    public Bitmap downloadImage(String imageUrl) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
