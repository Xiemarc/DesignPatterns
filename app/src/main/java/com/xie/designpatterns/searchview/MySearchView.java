package com.xie.designpatterns.searchview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by marc on 2017/6/9.
 */

public class MySearchView extends View {
    private Paint mPaint;
    private BaseController mController;

    public MySearchView(Context context) {
        this(context, null);
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(5);
    }

    public void setController(BaseController controller) {
        this.mController = controller;
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把具体的绘制交给controller中
        mController.draw(canvas, mPaint);
    }

    public void startAnimation() {
        if (null != mController) {
            mController.startAnim();
        }
    }

    public void resetAnimation() {
        if (null != mController) {
            mController.resetAnim();
        }
    }
}
