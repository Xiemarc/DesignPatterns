package com.xie.designpatterns.miui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/7/7.
 */

public class MiUiActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.miuiacitivity);
    }

    public void goScrollView(View view) {
        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void goRecyclerView(View view) {
        startActivity(new Intent(this, RecyclerViewLinearActivity.class));
    }

    public void goRecyclerViewGrid(View view) {
        startActivity(new Intent(this, RecyclerViewGridActivity.class));
    }
}
