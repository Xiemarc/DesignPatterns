package com.xie.designpatterns.recyckerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xie.designpatterns.R;

import java.util.ArrayList;

/**
 * des:
 * author: marc
 * date:  2017/2/12 21:11
 * email：aliali_ha@yeah.net
 */

public class RecycleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> list;
    private MyRecyclerAdapter adapter;
    private RecyclerView.ItemDecoration decor;
    boolean isGrid = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add("tiem" + i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.hori_recyclerview);
        adapter = new MyRecyclerAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        decor = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        recyclerView.addItemDecoration(decor);
    }

    public void addItem(View v) {
        adapter.addData(3);
    }
}
