package com.xie.designpatterns.recyckerview.itemtouchhelper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * des：侧滑删除的item
 * author：Xie
 */
public class SlidingItemMenuLayout extends LinearLayout {
    private Scroller mScroller;
    private View leftChild;
    private View rightChild;

    public SlidingItemMenuLayout(Context context) {
        this(context, null);
    }

    public SlidingItemMenuLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        mScroller = new Scroller(getContext(), null, true);
    }

    /**
     * 结束绘制的时候拿到2个child
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftChild = getChildAt(0);
        rightChild = getChildAt(1);
    }

    private float startX;
    private float startY;
    private float dx;
    private float dy;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = ev.getX();
                startY = ev.getY();
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                dx = ev.getX() - startX;
                dy = ev.getY() - startY;
                //dx >0 往右滑。  dx<0  往左滑
                if (Math.abs(dx) - Math.abs(dy) > ViewConfiguration.getTouchSlop()) {
                    //滑动的距离不能大于rightWidth
                    if (getScrollX() + (-dx) > rightChild.getWidth() || getScrollX() + (-dx) < 0) {
                        return true;
                    }
                    this.scrollBy((int) -dx, 0);
                    startX = ev.getX();
                    startY = ev.getY();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //仅仅只是把滑动的情况和参数描述和记录。
                //判断当前松开手是往左滑还是往右滑
                int offset = (getScrollX() / (float) rightChild.getWidth()) > 0.5f ? rightChild.getWidth() - getScrollX() : -getScrollX();
                mScroller.startScroll(getScrollX(), getScrollY(), offset, 0);
                invalidate();
                startX = 0;
                startY = 0;
                dx = 0;
                dy = 0;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //在开启滑动的情况下（mScroller.startScroll），滑动的过程当中此方法会被不断调用
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
