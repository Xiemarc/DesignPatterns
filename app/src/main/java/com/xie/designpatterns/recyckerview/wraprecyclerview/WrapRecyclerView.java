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
    private ArrayList<View> mHeaderViewInfos = new ArrayList<>();
    //脚布局集合
    private ArrayList<View> mFooterViewInfos = new ArrayList<>();
    private Adapter mAdapter;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 添加头部布局
     *
     * @param v
     */
    public void addHeaderView(View v) {
        mHeaderViewInfos.add(v);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, mAdapter);
            }
        }
    }

    public void addFooterView(View v) {
        mFooterViewInfos.add(v);
        if (mAdapter != null) {
            if (!(mAdapter instanceof HeaderViewRecyclerAdapter)) {
                mAdapter = new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, mAdapter);
            }
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            //有头布局或者根布局
            mAdapter = new HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        } else {
                mAdapter = adapter;
        }
        super.setAdapter(mAdapter);
    }
}
