package com.xie.designpatterns.miui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.xie.designpatterns.R;
import com.xie.designpatterns.miui.adapter.ItemAdapter;
import com.xie.designpatterns.miui.custom.ParallaxRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 2017/7/7.
 */

public class RecyclerViewLinearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_linear);
        initView();
    }

    private List<Item> initDatas() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (i % 2 == 0) {
                items.add(new Item(R.drawable.girl, "赵丽颖"));
            } else {
                items.add(new Item(R.drawable.girl_1, "赵丽颖"));
            }
        }
        return items;
    }

    private void initView() {
        ParallaxRecyclerView recyclerView = (ParallaxRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemAdapter adapter = new ItemAdapter(initDatas());
        recyclerView.setAdapter(adapter);
    }
}
