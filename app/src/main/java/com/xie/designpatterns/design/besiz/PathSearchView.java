package com.xie.designpatterns.design.besiz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * Created by marc on 2017/6/22.
 */

public class PathSearchView extends View {
    private Paint mPaint;
    private int mViewHeight;
    private int mViewWidth;
    private Path path_search;//放大镜把手
    private Path path_circle;//放大镜圆环
    //截取工具
    private PathMeasure mMeasure;
    private long defaultDuration = 2000;//默认动画时间

    //放大镜的三个状态
    public static enum State {
        NONE,
        STARTING,
        SEARCHING,
        ENDING
    }

    private State mCurrentState = State.NONE;
    // 控制各个过程的动画
    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchingAnimator;
    private ValueAnimator mEndingAnimator;
    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue = 0;
    // 动效过程监听器
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    // 用于控制动画状态转换
    private Handler mAnimatorHandler;

    // 判断是否已经搜索结束
    private boolean isOver = false;
    //转的圈数
    private int count = 0;

    public PathSearchView(Context context) {
        this(context, null);
    }

    public PathSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initPath();
        initListen();
        initHandler();
        initAnimator();
    }

    private void initListen() {
        mUpdateListener = (animator) -> {
            mAnimatorValue = (float) animator.getAnimatedValue();
            invalidate();
        };
        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //动画开始
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束,向handler发送消息
                mAnimatorHandler.sendEmptyMessage(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //动画取消
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                //动画重复
            }
        };
    }

    private void initHandler() {
        mAnimatorHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //根据当前状态去做具体操作
                switch (mCurrentState) {
                    case STARTING:
                        isOver = false;//动画未结束
                        //从开始动画转换为搜索动画
                        mCurrentState = State.SEARCHING;
                        mStartingAnimator.removeAllListeners();//移除所有监听
                        mSearchingAnimator.start();//搜索动画开启
                        break;
                    case SEARCHING:
                        if (!isOver) {
                            // 如果搜索未结束 则继续执行搜索动画
                            mSearchingAnimator.start();
                            count++;
                            if (count > 2) {
                                //count大于2 则让结束
                                isOver = true;
                            }
                        } else {
                            // 如果搜索已经结束 则进入结束动画
                            mCurrentState = State.ENDING;
                            mEndingAnimator.start();
                        }
                        break;
                    case ENDING:
                        // 从结束动画转变为无状态
                        mCurrentState = State.NONE;
                        break;
                }
            }
        };
    }

    private void initAnimator() {
        //开始动画
        mStartingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        //搜索动画
        mSearchingAnimator = ValueAnimator.ofFloat(0, 1).setDuration(defaultDuration);
        //结束动画，反着来
        mEndingAnimator = ValueAnimator.ofFloat(1, 0).setDuration(defaultDuration);

        mStartingAnimator.addUpdateListener(mUpdateListener);
        mSearchingAnimator.addUpdateListener(mUpdateListener);
        mEndingAnimator.addUpdateListener(mUpdateListener);


        mSearchingAnimator.addListener(mAnimatorListener);
        mEndingAnimator.addListener(mAnimatorListener);
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(15);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        path_search = new Path();
        path_circle = new Path();
        mMeasure = new PathMeasure();
    }

    /**
     * 初始化路径
     */
    private void initPath() {
        //画小圆
        RectF oval1 = new RectF(-50, -50, 50, 50);//放大镜的外接矩形
        path_search.addArc(oval1, 45, 359.9f);//从45度开始画。
        //画大圆
        RectF oval2 = new RectF(-100, -100, 100, 100);
        path_circle.addArc(oval2, 45, -359.9f);//这个大圆的路径跟小圆正好方向相反
        float[] pos = new float[2];
        mMeasure.setPath(path_circle, false);
        //得到放大镜把手的坐标,不要正切值.也就是开始画圆的位置的起点的值
        //把手从小圆起点到大圆位置起点
        mMeasure.getPosTan(0, pos, null);//得到大圆起点位置的坐标
        path_search.lineTo(pos[0], pos[1]);
    }

    private void drawSearch(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        //画布移动中间
        canvas.translate(mViewWidth / 2, mViewHeight / 2);
        canvas.drawColor(Color.parseColor("#0082D7"));
        switch (mCurrentState) {
            case NONE:
                canvas.drawPath(path_search, mPaint);
                break;
            case STARTING:
                //开始动画,画小圆
                mMeasure.setPath(path_search, false);
                Path dst = new Path();
                // 得到片段,给dst设置path值
                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst, true);
                canvas.drawPath(dst, mPaint);
                break;
            case SEARCHING:
                //搜索动画,画大圆
                mMeasure.setPath(path_circle, false);
                Path dst2 = new Path();
                float stop = mMeasure.getLength() * mAnimatorValue;
                float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * 200f));
                mMeasure.getSegment(start, stop, dst2, true);
                canvas.drawPath(dst2, mPaint);
                break;
            case ENDING:
                //结束动画画 小圆动画
                mMeasure.setPath(path_search, false);
                Path dst3 = new Path();
                mMeasure.getSegment(mMeasure.getLength() * mAnimatorValue, mMeasure.getLength(), dst3, true);
                canvas.drawPath(dst3, mPaint);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSearch(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        mCurrentState = State.STARTING;
        //监听个数
        ArrayList<Animator.AnimatorListener> listeners = mStartingAnimator.getListeners();
        mStartingAnimator.addListener(mAnimatorListener);
        mStartingAnimator.start();
    }

}
