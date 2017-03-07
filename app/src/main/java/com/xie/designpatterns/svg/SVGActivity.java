package com.xie.designpatterns.svg;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.TextView;

import com.xie.designpatterns.R;

/**
 * des:
 * author: marc
 * date:  2017/2/23 08:17
 * email：aliali_ha@yeah.net
 */

public class SVGActivity extends AppCompatActivity {
    TextView imageView;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        imageView = (TextView) findViewById(R.id.imageview);
        tv = (TextView) findViewById(R.id.textview);
    }

    public void move(View view) {

        Drawable[] compoundDrawables = imageView.getCompoundDrawables();
        for (Drawable dr :compoundDrawables) {
            if(dr instanceof Animatable){
                ((Animatable) dr).start();
            }
        }
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
