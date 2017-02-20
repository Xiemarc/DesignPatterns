package com.xie.designpatterns.recyckerview.itemtouchhelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xie.designpatterns.R;
import com.xie.designpatterns.recyckerview.decoration.DividerItemDecoration;
import com.xie.designpatterns.recyckerview.wraprecyclerview.WrapRecyclerView;

import java.util.List;

import static com.xie.designpatterns.R.id.recyclerView;

/**
 * des:
 * author: marc
 * date:  2017/2/20 15:36
 * email：aliali_ha@yeah.net
 */

public class ItemTouchActivity extends AppCompatActivity implements StartDragListener{



    private WrapRecyclerView mRecyclerView;
    private RecyclerView.ItemDecoration decor;
    private ItemTouchHelper itemTouchHelper;
    private  MyItemTouchHelperCallback mCallback;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warp_recycler);
        mRecyclerView = (WrapRecyclerView) findViewById(recyclerView);
        decor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        List<QQMessage> list = DataUtils.init();
        QQAdapter adapter = new QQAdapter(list,this);

        mRecyclerView.addItemDecoration(decor);
        mRecyclerView.setAdapter(adapter);
        //recycelveview条目触摸辅助类
        mCallback = new MyItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
