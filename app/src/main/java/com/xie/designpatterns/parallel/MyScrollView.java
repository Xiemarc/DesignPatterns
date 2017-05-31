package com.xie.designpatterns.parallel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * 自定义horiHorizontalScrollView,屏蔽滑动事件
 * Created by marc on 2017/4/20.
 */

public class MyScrollView extends HorizontalScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
        return true;
    }
}
