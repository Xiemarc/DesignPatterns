package com.xie.designpatterns.recyckerview.wraprecyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * des：
 * author：Xie
 */
public class HeaderViewRecyclerAdapter extends Adapter {
    private Adapter mAdapter;
    /**
     * 头布局view
     */
    ArrayList<View> mHeaderViewInfos;
    /**
     * footer布局view
     */
    ArrayList<View> mFooterViewInfos;

    public HeaderViewRecyclerAdapter(ArrayList<View> headerViewInfos, ArrayList<View> footerViewInfos, Adapter mAdapter) {
        this.mAdapter = mAdapter;
        if (headerViewInfos == null) {
            mHeaderViewInfos = new ArrayList<>();
        } else {
            mHeaderViewInfos = headerViewInfos;
        }

        if (footerViewInfos == null) {
            mFooterViewInfos = new ArrayList<>();
        } else {
            mFooterViewInfos = footerViewInfos;
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return getFootersCount() + getHeadersCount() + mAdapter.getItemCount();
        } else {
            return getFootersCount() + getHeadersCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        //判断当前条目是什么类型，决定渲染什么视图 给什么数据
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            //头部
            return RecyclerView.INVALID_TYPE;
        }
        //正常条目部分
        //当前挑眉个数是5，header的个数是3.  正常条目就是5-3=2 。当前是正常条目的第二个
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);
            }
        }
        //其余的就是footer的view了呗
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据viewType 加载不同布局

        //Headers布局
        if (viewType == RecyclerView.INVALID_TYPE) {
                return new HeaderViewHolder(mHeaderViewInfos.get(0));
        } else if (viewType == RecyclerView.INVALID_TYPE - 1) {
            //footers布局
            return new HeaderViewHolder(mFooterViewInfos.get(0));
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        //头部
        if (position < numHeaders) {
            //头部和 footer实际上是自己设置好数据了
            return;
        }
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter != null) {
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                mAdapter.onBindViewHolder(holder, adjPosition);
                return;
            }
        }
        //footer

    }

    /**
     * 得到headerview的个数
     *
     * @return
     */
    private int getHeadersCount() {
        return mHeaderViewInfos.size();
    }

    /**
     * 得到footerview的个数
     *
     * @return
     */
    private int getFootersCount() {
        return mFooterViewInfos.size();
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }
}
