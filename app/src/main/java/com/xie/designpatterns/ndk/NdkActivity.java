package com.xie.designpatterns.ndk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/5/15.
 */

public class NdkActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ndk);
    }

    /**
     * 加密
     * @param btn
     */
    public void mCrypt(View btn) {

    }

    /**
     * 解密
     * @param btn
     */
    public void mDecrypt(View btn) {

    }
}
