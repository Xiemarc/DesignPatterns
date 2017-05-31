package com.xie.designpatterns.translucentscrollivew;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义scrollview
 * Created by marc on 2017/4/18.
 */
public class MyScrollView extends ScrollView {
    private TranslucentListener listener;

    public void setTranslucentListener(TranslucentListener listener) {
        this.listener = listener;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(null != listener){
            int scrollY = getScrollY();
            //得到屏幕高度
            int screen_height = getContext().getResources().getDisplayMetrics().heightPixels;
            if(scrollY<=screen_height/3f){//0~1f
                listener.onTranlucent(1-scrollY/(screen_height/3f));//alpha=
            }
        }
    }
}
