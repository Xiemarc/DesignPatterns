package com.xie.designpatterns.recyckerview.itemtouchhelper;

import android.support.v7.widget.RecyclerView;

/**
 * des:开始拖拽监听
 * author: marc
 * date:  2017/2/20 15:39
 * email：aliali_ha@yeah.net
 */

public interface StartDragListener {

    /**
     * 该接口用户需要主动回调拖拽效果的
     * @param viewHolder
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
