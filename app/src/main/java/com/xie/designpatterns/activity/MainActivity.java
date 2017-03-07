package com.xie.designpatterns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xie.designpatterns.R;
import com.xie.designpatterns.bezier.BezierActivity;
import com.xie.designpatterns.recyckerview.itemtouchhelper.ItemTouchActivity;
import com.xie.designpatterns.wave.WaveActivity;
import com.xie.designpatterns.widget.BounchingActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn1,btn2,btn3,btn4,btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
//        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.btn1:
                intent.setClass(MainActivity.this, BounchingActivity.class);
                break;

            case R.id.btn2:
                intent.setClass(MainActivity.this, BezierActivity.class);
                break;

            case R.id.btn3:
//                intent.setClass(MainActivity.this, WrapActivity.class);
                break;
            case R.id.btn4:
                intent.setClass(MainActivity.this, WaveActivity.class);
                break;
            case R.id.btn5:
                intent.setClass(MainActivity.this, ItemTouchActivity.class);
                break;
        }
        startActivity(intent);
    }
}
