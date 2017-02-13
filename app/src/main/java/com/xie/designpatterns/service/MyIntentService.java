package com.xie.designpatterns.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.widget.Toast;

import com.xie.designpatterns.logger.Logger;

/**
 * des:
 * author: marc
 * date:  2017/1/18 21:42
 * email：aliali_ha@yeah.net
 */

public class MyIntentService extends IntentService {
    private String ex = "";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            Logger.i("在哪个现成");
            Toast.makeText(MyIntentService.this, "-e" + ex, Toast.LENGTH_SHORT).show();
        }
    };

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ex = intent.getStringExtra("start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Logger.i("在哪个现成");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.sendEmptyMessage(0);
        SystemClock.sleep(1000);
    }
}
