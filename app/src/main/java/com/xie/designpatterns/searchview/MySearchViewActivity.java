package com.xie.designpatterns.searchview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/12.
 */

public class MySearchViewActivity extends AppCompatActivity {
    private MySearchView mySearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);
        mySearchView = (MySearchView) findViewById(R.id.sv);
        mySearchView.setController(new MyController());
    }

    public void start(View v){
        mySearchView.startAnimation();
    }
    public void reset(View v){
        mySearchView.resetAnimation();
    }
}
