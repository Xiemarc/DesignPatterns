package com.xie.designpatterns.searchview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by marc on 2017/6/9.
 */
public abstract class BaseController {
    public static final int STATE_ANIM_NONE = 0;//无状态
    public static final int STATE_ANIM_START = 1;//动画开始
    public static final int STATE_ANIM_STOP = 2;//动画停止
    public static final int DEFAULT_ANIM_TIME = 500;//动画持续时间
    public static final float DEFAULT_ANIM_STARTF = 0;
    public static final float DEFAULT_ANIM_ENDF = 1;
    public int mState = STATE_ANIM_NONE;
    private MySearchView mySearchView;

    public abstract void draw(Canvas canvas, Paint paint);

    /**
     * 开启搜索动画
     */
    public void startAnim() {

    }

    /**
     * 重置搜索动画
     */
    public void resetAnim() {

    }

    public int getWidth() {
        return mySearchView.getWidth();
    }

    public int getHeight() {
        return mySearchView.getHeight();
    }

    public void setSearchView(MySearchView mySearchView) {
        this.mySearchView = mySearchView;
    }

    public float mpro = -1;

    public ValueAnimator startValueAnimation() {
        return startValueAnimation(DEFAULT_ANIM_STARTF, DEFAULT_ANIM_ENDF, DEFAULT_ANIM_TIME);
    }

    public ValueAnimator startValueAnimation(float startF, float endF, long time) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());//线性插值器
        valueAnimator.addUpdateListener(animation -> {
            //拿到不断变化的值
            mpro = (float) animation.getAnimatedValue();
            mySearchView.invalidate();
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        mpro = 0;//复原
        return valueAnimator;
    }


}
