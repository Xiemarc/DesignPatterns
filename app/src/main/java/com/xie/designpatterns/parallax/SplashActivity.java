package com.xie.designpatterns.parallax;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xie.designpatterns.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ParallaxContainer container = (ParallaxContainer) findViewById(R.id.parallax_container);
        container.setUp(new int[]{
                R.layout.view_intro_1,
                R.layout.view_intro_2,
                R.layout.view_intro_3,
                R.layout.view_intro_4,
                R.layout.view_intro_5,
                R.layout.view_login
        });

        //设置动画
        ImageView iv_man = (ImageView) findViewById(R.id.iv_man);
        container.setIv_man(iv_man);
//		iv_man.setBackgroundResource(R.anim.man_run);
//		container.setIv_man(iv_man);

    }


}
