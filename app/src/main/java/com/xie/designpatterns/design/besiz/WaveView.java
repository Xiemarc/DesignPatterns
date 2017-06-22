package com.xie.designpatterns.design.besiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.ValueAnimator;
import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/21.
 */
public class WaveView extends View {

    private Path path;
    private Paint paint;
    private int waveLength = 800;//波长
    private int dx;//不断变化的增量
    private int dy;
    private Bitmap mBitmap;
    protected float fraction;
    private PathMeasure mPathMeasure;
    private PathMeasure pathMeasure;
    private float[] pos;
    private float[] tan;
    private Matrix mMatrix;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chuan, options);
        mPathMeasure = new PathMeasure();
        pathMeasure = new PathMeasure();
        pos = new float[2];
        tan = new float[2];
        mMatrix = new Matrix();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        path.reset();
        int originY = 500;//波浪原始高度
        //半个波长,用来确定贝塞尔曲线的几个重要的点
        int halfLength = waveLength / 2;
        //起始点
        path.moveTo(-waveLength + dx, originY + dy);
        //屏幕（控件）宽度画多少个波长
        for (int i = -waveLength; i < getWidth() + waveLength; i += waveLength) {
            //有多少个波浪,r是相对绘制二阶贝塞尔曲线.相对于自己的起始点,也就是上一个曲线的终点(即是每次的moveTo的点)
            path.rQuadTo(halfLength / 2, -150, halfLength, 0);//往上的波峰
            //x1，y1的点相对于上面的（halfLength, 0）。
            path.rQuadTo(halfLength / 2, 150, halfLength, 0);//往下的波峰
        }
        //颜色填充。画封闭的空间
        path.lineTo(getWidth(), getHeight());//最右下角
        path.lineTo(0, getHeight());//最左下角
        path.close();//闭合
        canvas.drawPath(path, paint);
        mMatrix.reset();
        mPathMeasure.setPath(path, false);
        float length = mPathMeasure.getLength();
        //flag:表示要求哪一个值：tan或者pos
        mPathMeasure.getMatrix(length * fraction, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight());
        canvas.drawBitmap(mBitmap, mMatrix, paint);
    }


    public void startAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(0, waveLength);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);//循环
        animator.addUpdateListener(animation -> {
            dx = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        //y轴上下移动
        ValueAnimator yAnimtor = ValueAnimator.ofInt(0, 300);
        yAnimtor.setDuration(10000);
        yAnimtor.setRepeatMode(ValueAnimator.REVERSE);
        yAnimtor.setRepeatCount(ValueAnimator.INFINITE);
        yAnimtor.addUpdateListener(animation -> {
            dy = (int) animation.getAnimatedValue();
            postInvalidate();
        });
        animator.start();
        yAnimtor.start();
        //小船
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.addUpdateListener(animation -> {
            fraction = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        mAnimator.start();
    }
}
