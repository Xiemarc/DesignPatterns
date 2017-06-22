package com.xie.designpatterns.searchview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 自定义实现controller1
 * Created by marc on 2017/6/12.
 */

public class MyController extends BaseController {
    private String mColor = "#4CAF50";
    private int cx, cy, cr;//圆心x，圆心y，半径
    private RectF mRectF;
    private int j = 15;//把手起始的x距离圆的外接矩形右侧的距离,就是间隙

    public MyController() {
        mRectF = new RectF();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.parseColor(mColor));
        switch (mState) {
            case STATE_ANIM_NONE:
                drawNormalView(paint, canvas);
                break;
            case STATE_ANIM_START:
                drawStartAnimView(paint, canvas);
                break;
            case STATE_ANIM_STOP:
                drawStopAnimView(paint, canvas);
                break;
        }
    }



    /**
     * 开启动画
     * 圆弧做动画，等圆弧没有的时候 直线做动画
     *
     * @param paint
     * @param canvas
     */
    private void drawStartAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        if (mpro <= 0.5) {
            //绘制圆和直线
            /**
             * -360 ~ 0 需要变换的范围
             * 	 0  ~ 0.5 mpro实际的变化范围
             * 转换公式：360*(mpro*2-1),
             */
            canvas.drawArc(mRectF,
                    45,
                    360 * (mpro * 2 - 1),
                    false,
                    paint);
            //绘制直线
            canvas.drawLine(
                    mRectF.right - j,
                    mRectF.bottom - j,
                    mRectF.right + cr - j,
                    mRectF.bottom + cr - j,
                    paint);
        } else {
            //绘制直线
            /**
             *   0    ~ 1 需要变换的范围
             * 	 0.5  ~ 1 mpro实际的变化范围
             * 转换公式：(mpro*2-1),
             */
            canvas.drawLine(
                    mRectF.right - j + cr * (mpro * 2 - 1),
                    mRectF.bottom - j + cr * (mpro * 2 - 1),
                    mRectF.right - j + cr,
                    mRectF.bottom + cr - j,
                    paint);
        }
        //绘制下面的横线
        canvas.drawLine(
                (mRectF.right - j + cr) * (1 - mpro * 0.8f),
                mRectF.bottom + cr - j,
                mRectF.right - j + cr,
                mRectF.bottom + cr - j,
                paint);
        canvas.restore();


        mRectF.left = cx - cr + mpro * 250;
        mRectF.right = cx + cr + mpro * 250;
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
    }

    /**
     * 闲置默认状态
     *
     * @param paint
     * @param canvas
     */
    private void drawNormalView(Paint paint, Canvas canvas) {
        cr = getWidth() / 20;
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mRectF.left = cx - cr;
        mRectF.top = cy - cr;
        mRectF.right = cx + cr;
        mRectF.bottom = cy + cr;
        canvas.save();//先保存画布
        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        //画布按照圆心旋转45度。这样子直接画出来一个斜的直线
        canvas.rotate(45, cx, cy);
        //画布已经旋转过，这里注意是相对于哪个画布
        canvas.drawLine(cx + cr, cy, cx + cr * 2, cy, paint);
        canvas.drawArc(mRectF,
                0,
                360,
                false,
                paint);
        canvas.restore();//恢复画布
    }

    /**
     * 结束动画
     *
     * @param paint
     * @param canvas
     */
    private void drawStopAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        //mpro在 0-0.5画 直线，在0.5-1画圆和直线
        if (mpro <= 0.5) {
            canvas.drawLine(mRectF.right - j + cr * (1 - mpro), mRectF.bottom - j +
                    cr * (1 - mpro), mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        } else {
            canvas.drawArc(mRectF, 45, -((mpro - 0.5f) * 360 * 2), false, paint);
            canvas.drawLine(mRectF.right - j, mRectF.bottom - j,
                    mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        }
        canvas.drawLine((mRectF.right + cr - j) * (mpro >= 0.2f ? mpro : 0.2f),
                mRectF.bottom + cr - j, mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        canvas.restore();
        mRectF.left = cx - cr + 250 * (1 - mpro);
        mRectF.right = cx + cr + 250 * (1 - mpro);
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
    }

    @Override
    public void startAnim() {
        super.startAnim();
        mState = STATE_ANIM_START;
        startValueAnimation();
    }

    @Override
    public void resetAnim() {
        super.resetAnim();
        mState = STATE_ANIM_STOP;
        startValueAnimation();
    }
}
