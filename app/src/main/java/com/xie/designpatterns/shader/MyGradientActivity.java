package com.xie.designpatterns.shader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/5/22.
 */

public class MyGradientActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);
    }
}
