package com.xie.designpatterns.design.besiz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/21.
 */

public class WaveActivity extends AppCompatActivity {

    private PathSearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_besi_wave);
        WaveView waveView  = (WaveView) findViewById(R.id.wave);
        waveView.startAnimation();
        searchView = (PathSearchView) findViewById(R.id.search);
    }

    public void start(View v){
        searchView.startAnimator();
    }
}
