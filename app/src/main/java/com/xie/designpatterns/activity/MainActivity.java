package com.xie.designpatterns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.xie.designpatterns.R;
import com.xie.designpatterns.chapter1.ImageLoader;
import com.xie.designpatterns.service.MyIntentService;

public class MainActivity extends AppCompatActivity {
    String url = "http://192.168.1.106:8080/tomcat-power.gif";
    ImageLoader loader;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new ImageLoader();
//        loader.userDiskCache(true);
        mImageView = (ImageView) findViewById(R.id.image);
        ListView listView = new ListView(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滚动状态发生变化
                
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动时
            }
        });
    }

    public void showImage(View view) {
//        loader.displayImage(url, mImageView);
        Intent intent = new Intent(this, MyIntentService.class);
        intent.putExtra("start", "MyIntentService");
        startService(intent);
    }
}
