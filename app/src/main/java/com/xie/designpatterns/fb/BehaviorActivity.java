package com.xie.designpatterns.fb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.xie.designpatterns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义behavior
 * Created by marc on 2017/4/19.
 */

public class BehaviorActivity extends AppCompatActivity{

    private RecyclerView recyclerview;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior_1);

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("动脑学院");
//		recyclerview.setOnScrollListener(null);
//		recyclerview.addOnScrollListener(new FabScrollListener(this));
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("Item"+i);
        }
        RecyclerView.Adapter adapter = new FabRecyclerAdapter(list );
        recyclerview.setAdapter(adapter );
    }
}
