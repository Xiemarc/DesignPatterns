package com.xie.designpatterns.fb;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * 自定义 FloatingActionButton.Behavior
 * Created by marc on 2017/4/19.
 */

public class FabBehavior extends FloatingActionButton.Behavior {

    public FabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //防止动画一直不断的执行
    private boolean visible = true;//是否可见

    /**
     * 当观察的View（RecyclerView）发生滑动的开始的时候回调的
     *
     * @param coordinatorLayout
     * @param child
     * @param directTargetChild
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        //nestedScrollAxes:滑动关联轴， 我们现在只关心垂直的滑动。
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild,
                        target, nestedScrollAxes);
    }

    /**
     * // 当观察的view(RecyclerView)滑动的时候回调的
     *
     * @param coordinatorLayout 协调并且调度里面的控件
     * @param child             要改变的view
     * @param target            监听目标的滑动
     * @param dxConsumed        手指滑动x上面的距离
     * @param dyConsumed        手指滑动y上面的距离
     * @param dxUnconsumed      滑动时候x轴上面的惯性
     * @param dyUnconsumed      滑动时候y轴上面的惯性
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        // 当观察的view滑动的时候回调的
        //y轴上面滑动向下滑动并且此时也显示
        if (dyConsumed > 0 && visible) {
            //show
            visible = false;
            onHide(coordinatorLayout, child);
        } else if (dyConsumed < 0) {
            //hide
            visible = true;
            onShow(coordinatorLayout, child);
        }
    }

    public void onHide(CoordinatorLayout coordinatorLayout, FloatingActionButton fab) {
        // 隐藏动画--属性动画
//		toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(3));
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
//		fab.animate().translationY(fab.getHeight()+layoutParams.bottomMargin).setInterpolator(new AccelerateInterpolator(3));
//        ViewCompat.animate(fab).scaleX(0f).scaleY(0f).start();
        Toolbar toolbar = getToolbar(coordinatorLayout);
        toolbar.animate().translationY(toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(3));
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        fab.animate().translationY(fab.getHeight() + layoutParams.bottomMargin).setInterpolator(new AccelerateInterpolator(3));
    }

    public void onShow(CoordinatorLayout coordinatorLayout, FloatingActionButton fab) {
        Toolbar toolbar = getToolbar(coordinatorLayout);
        // 显示动画--属性动画
        if (null != toolbar) {
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
        }
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
        fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(3));
//        ViewCompat.animate(fab).scaleX(1f).scaleY(1f).start();
    }

    /**
     * 通过父布局得到里面的子布局
     *
     * @param coordinatorLayout 父布局 coordinatorLayout
     * @return 找到的toolbar布局
     */
    private Toolbar getToolbar(CoordinatorLayout coordinatorLayout) {
        int childCount = coordinatorLayout.getChildCount();
        Toolbar toolbar = null;
        for (int i = 0; i < childCount; i++) {
            View childAt = coordinatorLayout.getChildAt(i);
            if (childAt instanceof Toolbar) {
                toolbar = (Toolbar) childAt;
                break;
            }
        }
        return toolbar;
    }
}
