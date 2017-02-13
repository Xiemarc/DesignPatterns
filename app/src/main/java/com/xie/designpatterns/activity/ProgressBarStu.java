package com.xie.designpatterns.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.xie.designpatterns.R;

/**
 * des:
 * author: marc
 * date:  2017/1/21 10:09
 * email：aliali_ha@yeah.net
 */

public class ProgressBarStu extends AppCompatActivity {
    ProgressBar mProgressBar;
    Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int max = mProgressBar.getMax();
                try {
                    while (max != mProgressBar.getProgress()) {
                        int stepprogress = max / 10;
                        int currentProgress = mProgressBar.getProgress();
                        mProgressBar.setProgress(stepprogress + currentProgress);
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        handler.post(new Runnable() {
            @Override
            public void run() {
                //在handler绑定的线程执行
            }
        });
    }


}
