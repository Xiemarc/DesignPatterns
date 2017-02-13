package com.xie.designpatterns.chapter1;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.xie.designpatterns.logger.Logger;

/**
 * des:imageview缓存类
 * author: marc
 * date:  2017/1/13 20:58
 * email：aliali_ha@yeah.net
 */

public class ImageCache {

    //图片缓存
    LruCache<String, Bitmap> mImageCache;

    public ImageCache() {
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内容村
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //取1/4作为缓存
        final int cacheSize = maxMemory / 4;
        mImageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    /**
     * 把图片放进韩村
     *
     * @param url
     * @param bitmap
     */
    public void put(String url, Bitmap bitmap) {
        Logger.i("放入缓存");
        mImageCache.put(url, bitmap);
    }

    /**
     * 得到bitmap
     */
    public Bitmap get(String url) {
        Logger.i("取出缓存");
        return mImageCache.get(url);
    }
}
