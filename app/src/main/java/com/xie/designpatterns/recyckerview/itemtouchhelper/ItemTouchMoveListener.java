package com.xie.designpatterns.recyckerview.itemtouchhelper;

/**
 * des:
 * author: marc
 * date:  2017/2/20 15:43
 * email：aliali_ha@yeah.net
 */

public interface ItemTouchMoveListener {

    /**
     *  当拖拽的时候回调 </br>
     *  可以在此访中里面实现：拖拽条目并且实现刷新效果
     * @param fromPosition 从什么位置拖拽
     * @param toPosition 到什么位置
     * @return 是否执行了move
     */
    boolean onItemMove(int fromPosition,int toPosition);

    /**
     *  当条目被移除时候回调
     * @param position 移除的位置
     * @return
     */
    boolean onItemRemove(int position);
}
