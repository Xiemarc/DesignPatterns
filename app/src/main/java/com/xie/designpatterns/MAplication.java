package com.xie.designpatterns;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.xie.designpatterns.logger.Logger;

/**
 * des:
 * author: marc
 * date:  2017/1/13 21:21
 * email：aliali_ha@yeah.net
 */

public class MAplication extends Application{
    private static MAplication application;
    private static int mainTid;
    private static Handler handler;
    private static final String TAG = "marc";
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(TAG);
        application=this;
        mainTid = android.os.Process.myTid();
        handler=new Handler();
    }
    public static Context getApplication() {
        return application;
    }
    public static int getMainTid() {
        return mainTid;
    }
    public static Handler getHandler() {
        return handler;
    }
}
