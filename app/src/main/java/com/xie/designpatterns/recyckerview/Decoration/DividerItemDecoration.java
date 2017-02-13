package com.xie.designpatterns.recyckerview.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * des:recyclerview分割线
 * author: marc
 * date:  2017/2/12 11:01
 * email：aliali_ha@yeah.net
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * recycleview的方向，默认竖直方向
     */
    private int mOrientation = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    //使用系统的divider
    private int[] attrs = new int[]{
            android.R.attr.listDivider
    };

    public DividerItemDecoration(Context context, int orientation) {
        //使用系统的divider
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        mDivider = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("必须是横向或者竖向的,ok?");
        }
        this.mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //1.首先调用此方法获得条目的偏移量,通过Rect获得2个条目的间隔宽度
        // ,所有的条目都会调用一次该方法
        if (mOrientation == LinearLayoutManager.VERTICAL) {//垂直
            //右给的是0，是因为会自动拉伸  ,上一个item跟下一个item偏移的距离就是 间隔线的高度
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);//水平
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //2.调用此方法
        // recyclerView会回调该绘制方法，需要你自己取绘制条目的间隔线
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            //竖直
            drawVertical(c, parent);
        } else {
            //水平
            drawHorizontal(c, parent);
        }
        super.onDraw(c, parent, state);

    }

    /**
     * 竖向方向的布局画水平线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        //图片贴到哪个位置
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();//内边距
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //最后的 ViewCompat.getTranslationY(child) 是为了防止recycleview做动画。属性动画
            int top = (child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child)));
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 横向的布局画竖直线
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        //图片贴到哪个位置
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();//内边距
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            //最后的 ViewCompat.getTranslationY(child) 是为了防止recycleview做动画。属性动画
            int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}

