package com.xie.designpatterns.translucentscrollivew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/4/18.
 */

public class TranslucentActivity extends AppCompatActivity implements TranslucentListener {
    private Toolbar mToolbar;
    private MyScrollView scv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        scv = (MyScrollView) findViewById(R.id.scrollView);
        scv.setTranslucentListener(this);
    }

    @Override
    public void onTranlucent(float alpha) {
        mToolbar.setAlpha(alpha);
    }
}
