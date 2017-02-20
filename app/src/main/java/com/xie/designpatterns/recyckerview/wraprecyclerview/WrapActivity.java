package com.xie.designpatterns.recyckerview.wraprecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xie.designpatterns.R;
import com.xie.designpatterns.recyckerview.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * des:
 * author: marc
 * date:  2017/2/13 22:19
 * email：aliali_ha@yeah.net
 */

public class WrapActivity extends AppCompatActivity {
    private WrapRecyclerView mRecyclerView;
    private RecyclerView.ItemDecoration decor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warp_recycler);
        mRecyclerView = (WrapRecyclerView) findViewById(R.id.recyclerView);
        decor = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        TextView headerView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        headerView.setGravity(Gravity.CENTER);
        headerView.setTextSize(20);
        headerView.setLayoutParams(params);
        headerView.setText("我是头布局");
        mRecyclerView.addHeaderView(headerView);

        TextView footerView = new TextView(this);
        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        footerView.setLayoutParams(params);
        footerView.setText("我是根布局");
        mRecyclerView.addFooterView(footerView);
        mRecyclerView.setVerticalScrollbarPosition(0);
        mRecyclerView.addItemDecoration(decor);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("item:" + i);
        }
        MyAdapter adapter = new MyAdapter(list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(adapter);
    }
}
