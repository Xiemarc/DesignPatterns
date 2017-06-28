package com.xie.designpatterns.mdload;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.xie.designpatterns.R;

/**
 * 闪屏的那个view
 * Created by marc on 2017/6/28.
 */

public class SplashView extends View {
    private Paint cPaint;//小圆画笔
    private Paint bPaint;//背景画笔
    private int width;//控件宽度
    private int height;//控件高度
    private int[] colorArray;//小圆颜色的数组
    private static final int MAX_DOT_CONUNT = 6;//最大小圆的个数
    private static int BIG_CIRCLE_RADIUS = 80;//小圆距离屏幕中心的距离
    private static final int S_CIRCLE_RADIUS = 10;//小圆的半径
    private float singleAngle;//每个小圆之间的夹角
    private float rotateAngle = 0;//旋转动画转动的角度
    private int currentRadius = BIG_CIRCLE_RADIUS;//当前小圆距离屏幕中心的距离

    private ValueAnimator mAnimator;

    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bPaint.setStyle(Paint.Style.STROKE);
        bPaint.setColor(0xffffffff);
        colorArray = getContext().getResources().getIntArray(R.array.circleColor);
        singleAngle = (float) (2 * Math.PI / MAX_DOT_CONUNT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == design) {
            design = new RotateAnim();
        }
        design.startAnim(canvas);
    }

    private MatrielDesign design;

    /**
     * 暴露方法，停止转动
     * 闪屏页检查版本更新后可以调用停止第一阶段动画并且开启第二阶段
     */
    public void stopRotate() {
        mAnimator.cancel();
        design = new ScaleAnim();
        invalidate();
    }

    /**
     * 定义一个抽象的父类
     */
    private abstract class MatrielDesign {
        public abstract void startAnim(Canvas canvas);
    }

    /**
     * 第一阶段的旋转动画,其实就是改变的rotateAngle大小,让drawCircle()的时候可以每次及时计算出每个小圆的坐标
     */
    private class RotateAnim extends MatrielDesign {

        public RotateAnim() {
            mAnimator = ValueAnimator.ofFloat(0, 0.1f);
            mAnimator.setRepeatCount(Integer.MAX_VALUE);
            mAnimator.setDuration(1000);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(animation -> {
                float fraction = (float) animation.getAnimatedValue();
                rotateAngle = (float) (Math.PI * fraction);
                invalidate();
            });
            mAnimator.start();
        }

        @Override
        public void startAnim(Canvas canvas) {
            drawBackground(canvas);
            drawCircle(canvas);
        }
    }

    /**
     * 第二阶段动画，改变小圆和屏幕中心的距离
     * 定义ValueAnimator.ofFloat(1.0f,0.2f,1.2f)实现动画有一个回弹效果
     */
    private class ScaleAnim extends MatrielDesign {
        public ScaleAnim() {
            mAnimator = ValueAnimator.ofFloat(1.0f, 0.2f, 1.2f);
            mAnimator.setDuration(800);
            mAnimator.addUpdateListener(animation -> {
                float fraction = (float) animation.getAnimatedValue();
                currentRadius = (int) (BIG_CIRCLE_RADIUS * fraction);
                invalidate();
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    design = new CrinkleAnim();
                    invalidate();
                }
            });
            mAnimator.start();
        }

        @Override
        public void startAnim(Canvas canvas) {
            drawBackground(canvas);
            drawCircle(canvas);
        }
    }

    /**
     * 第三阶段水波纹动画,其实就是画背景的时候画一个圆,然后调整画笔的描线宽度就可以了
     * 这个思路也可以做一个圆形头像的效果,你觉得呢?
     */
    private class CrinkleAnim extends MatrielDesign {
        public CrinkleAnim() {
            mAnimator = ValueAnimator.ofFloat(0, 1.0f);
            mAnimator.setDuration(1000);
            mAnimator.addUpdateListener(animation -> {
                float fraction = (Float) animation.getAnimatedValue();
                crinkleCircle = (int) (diagonal * fraction);
                strokeWidth = (int) (diagonal - crinkleCircle);
                bPaint.setStrokeWidth(strokeWidth);
                invalidate();
            });
            mAnimator.start();
        }

        @Override
        public void startAnim(Canvas canvas) {
            //第三阶段，只画背景图就OK
            isCrinkle = true;
            drawBackground(canvas);
        }
    }

    private int crinkleCircle;
    private double diagonal;
    private int strokeWidth;
    private boolean isCrinkle;

    /**
     * 画几个小圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        for (int i = 0; i < colorArray.length; i++) {
            float angle = singleAngle * i + rotateAngle;
            float cx = (float) (currentRadius * Math.cos(angle)) + width / 2;//圆心x
            float cy = (float) (currentRadius * Math.sin(angle)) + height / 2;//圆心y
            cPaint.setColor(colorArray[i]);
            canvas.drawCircle(cx, cy, S_CIRCLE_RADIUS, cPaint);
        }
    }


    /**
     * 画背景图片
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if (!isCrinkle) {
            canvas.drawColor(0xffffffff);
        } else {//第三阶段动画
            canvas.drawCircle(width / 2, height / 2, crinkleCircle + strokeWidth / 2, bPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        diagonal = Math.sqrt(w * w + h * h) / 2;
    }
}
