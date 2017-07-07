package com.xie.designpatterns.miui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;

import com.xie.designpatterns.R;
import com.xie.designpatterns.miui.adapter.ItemAdapter;
import com.xie.designpatterns.miui.custom.ParallaxRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用recycleview做仿miui效果
 * Created by marc on 2017/7/7.
 */

public class RecyclerViewGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridrecycle);
        init();
    }

    private void init() {
        ParallaxRecyclerView parallaxRecyclerView = (ParallaxRecyclerView) findViewById(R.id.parallax_recycler_view_grid);
        parallaxRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ItemAdapter adapter = new ItemAdapter(itemList());
        parallaxRecyclerView.setAdapter(adapter);
    }


    private List<Item> itemList() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            if (i % 2 == 0) {
                items.add(new Item(R.drawable.girl, "赵丽颖"));
            } else {
                items.add(new Item(R.drawable.girl_1, "ZhaoLiying"));
            }
        }
        return items;
    }
}
