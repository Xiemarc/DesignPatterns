package com.xie.designpatterns.snakerbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/4/18.
 */

public class SnakeBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaker);
    }

    public void showCustomToast(View v) {
        Toast toast = new Toast(SnakeBarActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custome_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText("我是自定义的哟");
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showSnackbar(View v) {
        Snackbar snackbar = Snackbar.make(v, "会否开启加速模式", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showCustomToast(null);
            }
        });
        //不能设置多个action，会被覆盖
//        snackbar.setAction("取消", new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                showCustomToast(null);
//            }
//        });
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
//                showCustomToast(null);
                super.onDismissed(snackbar, event);
            }

            @Override
            public void onShown(Snackbar snackbar) {
                Log.i("marc","onShown");
                super.onShown(snackbar);
            }
        });
        snackbar.setActionTextColor(Color.GREEN);
        snackbar.show();
    }
}
