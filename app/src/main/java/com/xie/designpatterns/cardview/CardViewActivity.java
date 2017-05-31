package com.xie.designpatterns.cardview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xie.designpatterns.R;

/**cardview 的activity
 * Created by marc on 2017/4/19.
 */

public class CardViewActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);
    }
}
