package com.xie.designpatterns.miui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/7/7.
 */

public class ScrollViewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
    }

    public void img(View view) {
        Toast.makeText(this, "赵丽颖", Toast.LENGTH_SHORT).show();
    }
}
