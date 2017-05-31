package com.xie.designpatterns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xie.designpatterns.activity.MainAdapter;
import com.xie.designpatterns.behavior.CustomeBehaviorActivity;
import com.xie.designpatterns.bezier.BezierActivity;
import com.xie.designpatterns.buffer.BufferActivity;
import com.xie.designpatterns.cardview.CardViewActivity;
import com.xie.designpatterns.chinamap.ChinaMapActivity;
import com.xie.designpatterns.fb.BehaviorActivity;
import com.xie.designpatterns.fb.FabActivity;
import com.xie.designpatterns.loadtaost.LoadToastActivity;
import com.xie.designpatterns.palette.PaletteActivity;
import com.xie.designpatterns.parallel.ParalleActivity;
import com.xie.designpatterns.recyckerview.decoration.DividerItemDecoration;
import com.xie.designpatterns.recyckerview.itemtouchhelper.ItemTouchActivity;
import com.xie.designpatterns.recyckerview.wraprecyclerview.WrapActivity;
import com.xie.designpatterns.recyckerview.wraprecyclerview.WrapRecyclerView;
import com.xie.designpatterns.shader.MyGradientActivity;
import com.xie.designpatterns.snakerbar.SnakeBarActivity;
import com.xie.designpatterns.translucentscrollivew.TranslucentActivity;
import com.xie.designpatterns.wave.WaveActivity;
import com.xie.designpatterns.widget.BounchingActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn10;
    private WrapRecyclerView mRecyclerView;
    private RecyclerView.ItemDecoration decor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (WrapRecyclerView) findViewById(R.id.recyclerView);
        decor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(decor);
        List<String> mData = new ArrayList<>();
        mData.add("仿地图弹窗");
        mData.add("动画框架(贝塞尔曲线)");
        mData.add("recycleView封装");
        mData.add("波浪图");
        mData.add("recycleview滑动排序删除");
        mData.add("带动画效果的toast");
        mData.add("Palette调色板");
        mData.add("Translucent透明度变化");
        mData.add("Snackbar自定义");
        mData.add("FloatingActionBar");
        mData.add("CardView");
        mData.add("滑动隐藏");
        mData.add("Behavior滑动隐藏");
        mData.add("仿平行空间视察动画");
        mData.add("自定义behavior");
        mData.add("测试双缓冲");
        mData.add("中国地图自定义view");
        mData.add("shader实现地图扫描");
        MainAdapter adaper = new MainAdapter(mData);
        mRecyclerView.setAdapter(adaper);
        adaper.setOnItemClickListener((view,position)->{

            Intent intent = new Intent();
            switch (position) {
                case 0:
                    intent.setClass(MainActivity.this, BounchingActivity.class);
                    break;
                case 1:
                    intent.setClass(MainActivity.this, BezierActivity.class);
                    break;
                case 2:
                    intent.setClass(MainActivity.this, WrapActivity.class);
                    break;
                case 3:
                    intent.setClass(MainActivity.this, WaveActivity.class);
                    break;
                case 4:
                    intent.setClass(MainActivity.this, ItemTouchActivity.class);
                    break;
                case 5:
                    intent.setClass(MainActivity.this, LoadToastActivity.class);
                    break;
                case 6:
                    intent.setClass(MainActivity.this, PaletteActivity.class);
                    break;
                case 7:
                    intent.setClass(MainActivity.this, TranslucentActivity.class);
                    break;
                case 8:
                    intent.setClass(MainActivity.this, SnakeBarActivity.class);
                    break;
                case 9:
                    intent.setClass(MainActivity.this, FabActivity.class);
                    break;
                case 10:
                    intent.setClass(MainActivity.this, CardViewActivity.class);
                    break;
                case 11:
                    intent.setClass(MainActivity.this, FabActivity.class);
                    break;
                case 12:
                    intent.setClass(MainActivity.this, BehaviorActivity.class);
                    break;
                case 13:
                    intent.setClass(MainActivity.this, ParalleActivity.class);
                    break;
                case 14:
                    intent.setClass(MainActivity.this, CustomeBehaviorActivity.class);
                    break;
                case 15:
                    intent.setClass(MainActivity.this, BufferActivity.class);
                    break;
                case 16:
                    intent.setClass(MainActivity.this, ChinaMapActivity.class);
                    break;
                case 17:
                    intent.setClass(MainActivity.this,MyGradientActivity.class);
            }
            startActivity(intent);
        });
    }

}
