package com.xie.designpatterns.recyckerview.itemtouchhelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xie.designpatterns.R;

/**
 * des:
 * author: marc
 * date:  2017/2/20 15:45
 * email：aliali_ha@yeah.net
 */

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchMoveListener moveListener;

    public MyItemTouchHelperCallback(ItemTouchMoveListener moveListener) {
        this.moveListener = moveListener;
    }

    //Callback 回调监听时先调用的，用来判断当前是什么动作。比如判断方向。（我要监听哪个方向的拖动）
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //方向  UP、DOWN、LEFT、Right
        //拖拽方向是哪俩个方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;//或运算
        //我要监听的侧滑方向
        int swapFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        int flags = makeMovementFlags(dragFlags, swapFlags);
        return flags;
    }

    /**
     * @param recyclerView
     * @param srcHolder    原viewholder
     * @param targetHolder 目标viewholder
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        //在拖拽的过程当中，不断的调用适配器的notifyItemMove(fromPosition,toPositon)方法
        if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) {
            //如果2个条目的类型不一样。返回false不执行下面的拖拽
            return false;
        }
        //在拖拽的过程中不断段勇adapter.notifyItemMoved(fromPosition,toPosition);
        boolean result = moveListener.onItemMove(srcHolder.getAdapterPosition(), targetHolder.getAdapterPosition());
        return result;
    }

    //是否允许长按拖拽
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
// 监听侧滑，1.删除数据；2.调用adapter.notifyItemRemove(position)
        moveListener.onItemRemove(viewHolder.getAdapterPosition());
    }

    //判断选中状态
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_green_light));
            } else {
                viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    //恢复状态
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 恢复
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        //需要把当前条目设置回来
//        viewHolder.itemView.setAlpha(1);//1~0
//        viewHolder.itemView.setScaleX(1);//1~0
//        viewHolder.itemView.setScaleY(1);//1~0
        super.clearView(recyclerView, viewHolder);
    }

    /**
     * 给条目做特效
     *
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {
        //dX:水平方向移动的增量（负：往左；正：往右）范围：0~View.getWidth  0~1
        float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //透明度动画
            viewHolder.itemView.setAlpha(alpha);//1~0
//            viewHolder.itemView.setScaleX(alpha);//1~0
//            viewHolder.itemView.setScaleY(alpha);//1~0
        }

        //条目划出后直接还原
        if (alpha == 0) {
            viewHolder.itemView.setAlpha(1);//1~0
            viewHolder.itemView.setScaleX(1);//1~0
            viewHolder.itemView.setScaleY(1);//1~0
        }
        //往左边滑动添加限定值,是否超出或者达到width的一半，就让其translation一直是一半
        if (Math.abs(dX) <= viewHolder.itemView.getWidth() / 2) {
            viewHolder.itemView.setTranslationX(-0.5f * viewHolder.itemView.getWidth());
        }else {
            viewHolder.itemView.setTranslationX(dX);
        }
        //此super方法会自动处理setTranslationX
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                isCurrentlyActive);
    }

}
