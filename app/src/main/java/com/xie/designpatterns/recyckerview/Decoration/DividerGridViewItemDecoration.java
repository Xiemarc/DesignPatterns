package com.xie.designpatterns.recyckerview.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * des：gridview分割线
 * author：Xie
 */
public class DividerGridViewItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    //使用源码中的listview的分割线
    private int[] attrs = new int[]{android.R.attr.listDivider};

    public DividerGridViewItemDecoration(Context context) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }


    /**
     * 每个item相对于上一个item的偏移量
     *
     * @param outRect
     * @param itemPosition
     * @param parent
     */
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        // 四个方向的偏移值
        View child = parent.getChildAt(itemPosition);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        int left;
        int top;
        int right = mDivider.getIntrinsicWidth();
        int bottom = mDivider.getIntrinsicHeight();

        if (itemPosition <= 3) {
//            需要画最上面层
            top = child.getTop() - params.bottomMargin;
            outRect.set(0, top, right, bottom);
        } else if (itemPosition % 3 == 0) {
            left = child.getLeft() - params.leftMargin;
            outRect.set(left, 0, right, bottom);
        } else {
            outRect.set(0, 0, right, bottom);
        }
//        outRect.set(0, 0, right, bottom);
//        if(如果是第一列){
//            outRect.set(0,0,,bottom);
//        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        //绘制垂直间隔线(垂直的矩形)
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            if (i % 3 == 0) {
                //也绘制左侧的竖线
                int oriLeft = params.leftMargin;
                int oriRight = oriLeft + mDivider.getIntrinsicWidth();
                int oriTop = child.getTop() - params.topMargin;
                int oriBottom = child.getBottom() + params.bottomMargin;
                mDivider.setBounds(oriLeft, oriTop, oriRight, oriBottom);
                mDivider.draw(c);
            }
            if (i <= 2) {
                //绘制最上面的线
                int left = child.getLeft() - params.leftMargin;
                int right = child.getRight() + params.rightMargin;
                int top = child.getTop() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
            int left = child.getRight() + params.rightMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        // 绘制水平间隔线
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
