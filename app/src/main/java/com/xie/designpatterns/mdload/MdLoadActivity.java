package com.xie.designpatterns.mdload;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * Created by marc on 2017/6/28.
 */

public class MdLoadActivity extends AppCompatActivity {
    private LoadView loadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashView splashView = new SplashView(MdLoadActivity.this);
        ContentView contentView = new ContentView(MdLoadActivity.this);
        loadView = new LoadView(this);
        FrameLayout fl = new FrameLayout(this);
//        fl.addView(splashView);
        fl.addView(contentView);
        fl.addView(loadView);
        setContentView(fl);
        handler.postDelayed(() -> loadView.splashAndDisappear(), 2000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
