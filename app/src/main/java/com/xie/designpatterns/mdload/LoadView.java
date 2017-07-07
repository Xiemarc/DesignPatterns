package com.xie.designpatterns.mdload;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/28.
 */

public class LoadView extends View {
    /**
     * 大圆的半径（里面含有好多小圆）
     */
    private float mRotationRadius = 90;
    /**
     * 小圆的半径
     */
    private float mCircleRadius = 18;
    /**
     * 小圆的颜色列表
     */
    private int[] mCircleColors;

    /**
     * 小圆旋转一周需要的时间
     */
    private long mRotationDuration = 1200;
    /**
     * 其他动画执行的时间（除了旋转的动画）
     */
    private long mSplashDuration = 500;
    /**
     * view的背景颜色
     */
    private int mBgColor = Color.WHITE;
    /**
     * 当前大圆的半径（动态变化）
     */
    private float mCurrentRatationRadios;
    /**
     * 当前大圆旋转的角度（弧度）
     */
    private float mCurrentRotationAngle = 0f;
    /**
     * 空心圆半径
     */
    private float mHoleRadius = 0f;
    /**
     * 绘制圆的画笔
     */
    private Paint mPaint = new Paint();
    /**
     * 绘制背景的画笔
     */
    private Paint mBgPaint = new Paint();

    /**
     * view中心的坐标
     */
    private float mCenterX;
    private float mCenterY;

    /**
     * view对角线的一半
     */
    private float mDiagonalDist;
    /**
     * 保存当前动画状态-->当前在执行那种动画
     */
    private LoadState mState = null;

    /**
     * 小圆之间的间隔角度
     */
    private float mRotationAngle = 0f;
    /**
     * 聚合后缩放圆的半径
     */
    private float mScaleCircle;

    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 获取view的中点坐标,view对角线长度的一半
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2f;
        mCenterY = h / 2f;
        //屏幕对角线的一半. 勾股定理
        mDiagonalDist = (float) (Math.sqrt(w * w + h * h) / 2f);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        //抗锯齿
        mPaint.setAntiAlias(true);
        mBgPaint.setAntiAlias(true);
        //设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        mPaint.setDither(true);
        mBgPaint.setDither(true);
        //设置画笔的样式：空心
        mBgPaint.setStyle(Paint.Style.STROKE);
        //设置画笔颜色
//        mBgPaint.setColor(mBgColor);
        mBgPaint.setColor(Color.BLACK);
        //旋转的时候大圆的半径不变，为初始化值
        mCurrentRatationRadios = mRotationRadius;
        //聚合后放大圆的初始半径为小圆半径
        mScaleCircle = mCircleRadius;
        //获取颜色数组，小圆的个数和定义颜色的个数相等
        mCircleColors = context.getResources().getIntArray(R.array.circleColor);
        if (mCircleColors.length > 0) {
            //每个小圆之间的间隔角度（弧度）
            mRotationAngle = (float) (2 * Math.PI / mCircleColors.length);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //如果开始为空，则执行旋转动画
        if (mState == null) {
            mState = new RotationState();
        }
        //这边采用多态的方式，不断改变mState对象类型，执行不同的绘图动作
        mState.drawState(canvas);
        super.onDraw(canvas);
    }

    /**
     * 数据加载完毕，关闭第一个动画，执行后面三个动画
     */
    public void splashAndDisappear() {
        //取消第一个动画
        if (mState != null && mState instanceof RotationState) {
            //停止第一个动画
            ((RotationState) mState).cancel();
            post(() -> mState = new MergingState());
        }
    }

    private ValueAnimator animator;

    /**
     * 第一个动画：小圆旋转动画
     */
    private class RotationState implements LoadState {
        public RotationState() {
            animator = ValueAnimator.ofFloat(0f, (float) (2 * Math.PI));
            animator.setDuration(mRotationDuration);
            //设置无限循环
            animator.setRepeatCount(ValueAnimator.INFINITE);
            //匀速旋转
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                //获取大圆旋转的当前角度
                mCurrentRotationAngle = (float) animation.getAnimatedValue();
                //重绘图像
                postInvalidate();
            });
            animator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
            darwCircle(canvas);
        }

        /**
         * 取消旋转动画
         */
        public void cancel() {
            animator.cancel();
        }
    }


    /**
     * 第二个动画：聚合动画 ,改变大圆（不显示的）的半径
     */
    private class MergingState implements LoadState {

        public MergingState() {
            animator = ValueAnimator.ofFloat(0f, mRotationRadius);
            //开始有个弹射效果，输入的参数越大，弹射效果越明显
            animator.setInterpolator(new OvershootInterpolator(6f));
            animator.setDuration(mSplashDuration);
            animator.addUpdateListener(animation -> {
                //当前时间点 大圆的半径
                mCurrentRatationRadios = (float) animation.getAnimatedValue();
                invalidate();
            });
            //反转动画 ,直接反转然后start . 半径从mRotationRadius到0 执行
            animator.reverse();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    post(() -> mState = new CircleStae());
                }
            });
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
            darwCircle(canvas);
        }
    }

    /**
     * 圆缩放动画
     */
    private class CircleStae implements LoadState {
        public CircleStae() {
            animator = ValueAnimator.ofFloat(mCircleRadius, 2.5f * mCircleRadius, mCircleRadius);
            animator.setDuration(mSplashDuration);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                //当前圆的圆心
                mScaleCircle = (float) animation.getAnimatedValue();
                //重绘图像
                invalidate();
            });
            animator.start();
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    post(() -> mState = new ExpandingStae());
                }
            });
        }

        @Override
        public void drawState(Canvas canvas) {
            //把原来的圆擦除
            drawBackground(canvas);
            canvas.drawCircle(mCenterX, mCenterY, mScaleCircle, mPaint);
        }
    }


    /**
     * 第三个动画：小圆水波纹扩散动画
     */
    private class ExpandingStae implements LoadState {
        private ValueAnimator animator;

        public ExpandingStae() {
            animator = ValueAnimator.ofFloat(0, mDiagonalDist);
            animator.setDuration(mSplashDuration);
            //扩散我这边使用的是匀速，可以换成加速：AccelerateInterpolator
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                //获取空心圆的半径,不断的控制画笔的宽度
                mHoleRadius = (float) animation.getAnimatedValue();
                //重绘图像
                invalidate();
            });
            animator.start();
        }

        @Override
        public void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

    /**
     * 绘制小圆
     *
     * @param canvas
     */
    private void darwCircle(Canvas canvas) {
        for (int i = 0; i < mCircleColors.length; i++) {
            //设置画笔颜色
            mPaint.setColor(mCircleColors[i]);
            //小圆的x,y坐标
            float cx = (float) (mCurrentRatationRadios * Math.cos(mCurrentRotationAngle + mRotationAngle * i) + mCenterX);
            float cy = (float) (mCurrentRatationRadios * Math.sin(mCurrentRotationAngle + mRotationAngle * i) + mCenterY);
            canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
        }
    }

    /**
     * 清空画布
     *
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        //如果空心圆的半径为0，则清空画布，如果mHoleRadius不为零，说明正在执行扩散动画
        if (mHoleRadius > 0f) {
            //画笔的宽度
            mBgPaint.setStrokeWidth(mDiagonalDist - mHoleRadius);
            //空心圆的半径
            float radius = mDiagonalDist / 2 + mHoleRadius / 2;
            canvas.drawCircle(mCenterX, mCenterY, radius, mBgPaint);
        } else {
            //背景白色，就相当于把背景画的几个小圆擦除
            canvas.drawColor(mBgColor);
        }
    }
}
