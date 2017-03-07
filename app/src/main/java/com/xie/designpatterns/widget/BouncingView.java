package com.xie.designpatterns.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * des:跳跃的view
 * author: marc
 * date:  2017/2/22 09:53
 * email：aliali_ha@yeah.net
 */

public class BouncingView extends View {
    private Paint mPaint;
    private int mArcHeight;//当前弧度
    private int mMaxArcHeight;//最高弧度
    private Status mStatus = Status.NONE;
    private Path mPath = new Path();
    private AnimationListener animationListener;

    private enum Status {
        NONE,
        STATUS_SMOOTH_UP,
        STATUS_DOWN
    }

    public BouncingView(Context context) {
        super(context);
        init();
    }

    public BouncingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(android.R.color.white));
        mMaxArcHeight = getResources().getDimensionPixelSize(R.dimen.arc_max_height);
    }


    //绘制贝塞尔曲线
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int currentPointY = 0;
        //判断当前状态
        switch (mStatus) {
            case NONE:
                currentPointY = 0;
                break;
            case STATUS_SMOOTH_UP://往上走
                //currentPointY 的值 跟mArcHeight变化率是一样的
                //getHeight()~0  (自定义控件最上面是0)           跟mArcHeight:0~mMaxArcHeight
                currentPointY = (int) (getHeight() * (1 - (float) mArcHeight / mMaxArcHeight) + mMaxArcHeight);
                break;
            case STATUS_DOWN://往下走
                currentPointY = mMaxArcHeight;
                break;
        }
        mPath.reset();
        //先落笔到左上角
        mPath.moveTo(0, currentPointY);
        //画上面的线二阶贝塞尔曲线  ,需要起始点 终点，跟拐点。起点就是（0，currentPotintY），拐点就是最中间(getWidth()/2,)
        mPath.quadTo(getWidth() / 2, currentPointY - mArcHeight, getWidth(), currentPointY);
        //右边线
        mPath.lineTo(getWidth(), getHeight());
        //最下面的直线
        mPath.lineTo(0, getHeight());
        //自动画最后一条线
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    public void show() {

        if (animationListener != null) {
            this.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //延迟显示数据
                    animationListener.onShowContent();
                }
            }, 600);
        }

        //不短的控制mArcHeight的高度
        mStatus = Status.STATUS_SMOOTH_UP;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mMaxArcHeight);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight = (int) animation.getAnimatedValue();
                if (mArcHeight == mMaxArcHeight) {
                    //出现谈一下动画
                    bounce();
                }
                //不断的刷新调用ondraw方法
                invalidate();
            }


        });
        valueAnimator.start();
    }

    /**
     * 回弹动画
     */
    private void bounce() {
        mStatus = Status.STATUS_DOWN;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mMaxArcHeight, 0);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArcHeight = (int) animation.getAnimatedValue();
                //不断的刷新调用ondraw方法
                invalidate();
            }


        });
        valueAnimator.start();
    }

    public void setAnimationListener(AnimationListener listener) {
        this.animationListener = listener;
    }

    public interface AnimationListener {
        void onShowContent();
    }

}
