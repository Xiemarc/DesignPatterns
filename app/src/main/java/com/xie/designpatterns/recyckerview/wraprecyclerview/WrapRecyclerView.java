package com.xie.designpatterns.recyckerview.wraprecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * des：
 * author：Xie
 */
public class WrapRecyclerView extends RecyclerView {
    //头部view 集合
    private ArrayList<View> mHeaderViewInfos = new ArrayList<View>();
    //脚布局集合
    private ArrayList<View> mFooterViewInfos = new ArrayList<View>();
    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 添加头部布局
     * @param v
     */
    public void addHeaderView(View v) {
        mHeaderViewInfos.add(v);
    }


    @Override
    public void setAdapter(Adapter adapter) {

//        super.setAdapter(adapter);
    }
}
