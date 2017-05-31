package com.xie.designpatterns.chinamap;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xie.designpatterns.R;
import com.xie.designpatterns.utils.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 2017/5/4.
 */

public class MapSVGManager {
    private static final String TAG = "MapSVGManager";
    private Context mContext;
    /**
     * 线程锁
     */
    private static final Object Lock = new Object();
    /**
     * 省份路径集合  使用volatile，多线程中使用
     */
    private volatile List<ProvincePath> mProvincePathList;
    /**
     * 总的矩形
     */
    private RectF mTotalRect;
    /**
     * 单例
     */
    private static MapSVGManager instance;
    //主线程
    private Handler mMainHandler;


    private MapSVGManager(Context context) {
        this.mContext = context.getApplicationContext();
        mMainHandler = new Handler(Looper.getMainLooper());
        init();
    }

    public static MapSVGManager getInstance(Context context) {
        if (instance == null) {
            synchronized (MapSVGManager.class) {
                //使用类锁
                if (instance == null) {
                    instance = new MapSVGManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 初始化  xml解析svg文件封装ProvincePath信息
     */
    private void init() {
        new Thread(
                () -> {
                    try {
                        if (mProvincePathList == null) {
                            long startTime = System.currentTimeMillis();
                            if (mProvincePathList == null) {
                                List<ProvincePath> list = new ArrayList<>();
                                InputStream inputStream = mContext.getResources().openRawResource(R.raw.china);
                                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                                parser.setInput(inputStream, "utf-8");
                                int eventType;
                                //默认矩形的 左上右下
                                float left = -1;
                                float top = -1;
                                float right = -1;
                                float bottom = -1;
                                while ((eventType = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                                    //不是xml文件的结尾
                                    if (eventType == XmlPullParser.START_TAG) {
                                        //xml的开始标签
                                        String name = parser.getName();
                                        if ("path".equals(name)) {
                                            //拿到path节点
                                            String id = parser.getAttributeValue(null, "id");
                                            String title = parser.getAttributeValue(null, "title");
                                            String pathData = parser.getAttributeValue(null, "d");
                                            //封装ProvincePath
                                            ProvincePath provincePath = new ProvincePath(Integer.valueOf(id), title, pathData);
                                            Path path = provincePath.getPath();

                                            RectF rect = new RectF();
                                            path.computeBounds(rect, true);
                                            //给左上右下赋值
                                            left = left == -1 ? rect.left : Math.min(left, rect.left);
                                            top = top == -1 ? rect.top : Math.min(top, rect.top);
                                            right = right == -1 ? rect.right : Math.max(right, rect.right);
                                            bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                                            list.add(provincePath);
                                        }
                                    }
                                    parser.next();
                                }
                                //地图所在的矩形范围
                                mTotalRect = new RectF(left, top, right, bottom);
                                mProvincePathList = list;
                                Logger.i(list.size()+"");
                            }
                            Log.i(TAG, "初始化结束->" + (System.currentTimeMillis() - startTime));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    synchronized (Lock) {
                        Lock.notifyAll();
                    }
                }
        ).start();
    }

    /**
     * 异步获取到ProvincePath信息并且设置回调
     *
     * @param callback
     */
    public void getProvincePathListAsync(Callback callback) {
        if (mProvincePathList == null) {
            new Thread(
                    () -> {
                        try {
                            synchronized (Lock) {
                                if (mProvincePathList == null) {
                                    Lock.wait();
                                }
                            }
                            //主线程发送 数据
                            mMainHandler.post(() -> callback.onResult(mProvincePathList, mTotalRect));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        }else {
            mMainHandler.post(() -> callback.onResult(mProvincePathList, mTotalRect));
        }
    }


    interface Callback {
        /**
         * 回调方法
         *
         * @param provincePathList
         * @param rectF
         */
        void onResult(List<ProvincePath> provincePathList, RectF rectF);
    }

}
