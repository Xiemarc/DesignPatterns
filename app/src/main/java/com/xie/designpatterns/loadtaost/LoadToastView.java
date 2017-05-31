package com.xie.designpatterns.loadtaost;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.ValueAnimator;
import com.xie.designpatterns.R;


/**
 * des：
 * author：Xie
 */
public class LoadToastView extends View {
    //显示内容
    private String mText = "";
    //文字画笔
    private Paint textPaint = new Paint();
    //返回画笔
    private Paint backPaint = new Paint();
    //图标返回画笔
    private Paint iconBackPaint = new Paint();
    //加载中画笔
    private Paint loaderPaint = new Paint();
    //成功画笔
    private Paint successPaint = new Paint();
    //错误画笔
    private Paint errorPaint = new Paint();
    //图标的Rect
    private Rect iconBounds;
    //text文字的矩形
    private Rect mTextBounds = new Rect();

    private RectF spinnerRect = new RectF();

    private int MAX_TEXT_WIDTH = 100; // in DP
    private int BASE_TEXT_SIZE = 20;
    private int IMAGE_WIDTH = 40;
    private int TOAST_HEIGHT = 48;
    private float WIDTH_SCALE = 0f;

    private Drawable completeicon;
    private Drawable failedicon;

    private ValueAnimator va;
    private ValueAnimator cmp;

    private boolean success = true;

    private Path toastPath = new Path();
    //加速减速插值器
    private AccelerateDecelerateInterpolator easeinterpol = new AccelerateDecelerateInterpolator();

    public LoadToastView(Context context) {
        super(context);
        textPaint.setTextSize(15);
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);

        backPaint.setColor(Color.WHITE);
        backPaint.setAntiAlias(true);

        iconBackPaint.setColor(Color.BLUE);
        iconBackPaint.setAntiAlias(true);

        loaderPaint.setStrokeWidth(dpToPx(4));
        loaderPaint.setAntiAlias(true);
        loaderPaint.setColor(fetchPrimaryColor());
        loaderPaint.setStyle(Paint.Style.STROKE);

        successPaint.setColor(getResources().getColor(R.color.color_success));
        errorPaint.setColor(getResources().getColor(R.color.color_error));
        successPaint.setAntiAlias(true);
        errorPaint.setAntiAlias(true);

        MAX_TEXT_WIDTH = dpToPx(MAX_TEXT_WIDTH);
        BASE_TEXT_SIZE = dpToPx(BASE_TEXT_SIZE);
        IMAGE_WIDTH = dpToPx(IMAGE_WIDTH);
        TOAST_HEIGHT = dpToPx(TOAST_HEIGHT);

        int padding = (TOAST_HEIGHT - IMAGE_WIDTH) / 2;
        iconBounds = new Rect(TOAST_HEIGHT + MAX_TEXT_WIDTH - padding, padding, TOAST_HEIGHT + MAX_TEXT_WIDTH - padding + IMAGE_WIDTH, IMAGE_WIDTH + padding);
        //loadicon = getResources().getDrawable(R.mipmap.ic_launcher);
        //loadicon.setBounds(iconBounds);
        completeicon = getResources().getDrawable(R.drawable.ic_navigation_check);
        completeicon.setBounds(iconBounds);
        failedicon = getResources().getDrawable(R.drawable.ic_error);
        failedicon.setBounds(iconBounds);

        va = ValueAnimator.ofFloat(0, 1);
        va.setDuration(6000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //WIDTH_SCALE = valueAnimator.getAnimatedFraction();
                postInvalidate();
            }
        });
        va.setRepeatMode(ValueAnimator.INFINITE);
        va.setRepeatCount(9999999);
        va.setInterpolator(new LinearInterpolator());
        va.start();

        calculateBounds();
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public void setBackgroundColor(int color) {
        backPaint.setColor(color);
        iconBackPaint.setColor(color);
    }

    public void setProgressColor(int color) {
        loaderPaint.setColor(color);
    }

    public void show() {
        WIDTH_SCALE = 0f;
        if (cmp != null) cmp.removeAllUpdateListeners();
    }

    public void success() {
        success = true;
        done();
    }

    public void error() {
        success = false;
        done();
    }

    private void done() {
        cmp = ValueAnimator.ofFloat(0, 1);
        cmp.setDuration(600);
        cmp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                WIDTH_SCALE = 2f * (valueAnimator.getAnimatedFraction());
                //Log.d("lt", "ws " + WIDTH_SCALE);
                postInvalidate();
            }
        });
        cmp.setInterpolator(new DecelerateInterpolator());
        cmp.start();
    }

    private int fetchPrimaryColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TypedValue typedValue = new TypedValue();

            TypedArray a = getContext().obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorAccent});
            int color = a.getColor(0, 0);

            a.recycle();

            return color;
        }
        return Color.rgb(155, 155, 155);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void setText(String text) {
        mText = text;
        calculateBounds();
    }

    /**
     * 计算bounds
     */
    private void calculateBounds() {
        textPaint.setTextSize(BASE_TEXT_SIZE);
        textPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
        if (mTextBounds.width() > MAX_TEXT_WIDTH) {
            int textSize = BASE_TEXT_SIZE;
            while (textSize > dpToPx(13) && mTextBounds.width() > MAX_TEXT_WIDTH) {
                textSize--;
                textPaint.setTextSize(textSize);
                textPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
            }
            if (mTextBounds.width() > MAX_TEXT_WIDTH) {
                float keep = (float) MAX_TEXT_WIDTH / (float) mTextBounds.width();
                int charcount = (int) (mText.length() * keep);
                mText = mText.substring(0, charcount);
                textPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
            }
        }
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        float ws = Math.max(1f - WIDTH_SCALE, 0f);
        float translateLoad = (1f - ws) * (IMAGE_WIDTH + MAX_TEXT_WIDTH);
        float leftMargin = translateLoad / 2;
        float textOpactity = Math.max(0, ws * 10f - 9f);
        textPaint.setAlpha((int) (textOpactity * 255));
        spinnerRect.set(iconBounds.left + dpToPx(4) - translateLoad / 2, iconBounds.top + dpToPx(4),
                iconBounds.right - dpToPx(4) - translateLoad / 2, iconBounds.bottom - dpToPx(4));

        int circleOffset = (int) (TOAST_HEIGHT * 2 * (Math.sqrt(2) - 1) / 3);
        int th = TOAST_HEIGHT;
        int pd = (TOAST_HEIGHT - IMAGE_WIDTH) / 2;
        int iconoffset = (int) (IMAGE_WIDTH * 2 * (Math.sqrt(2) - 1) / 3);
        int iw = IMAGE_WIDTH;
        //背景
        toastPath.reset();
        toastPath.moveTo(leftMargin + th / 2, 0);
        toastPath.rLineTo(ws * (IMAGE_WIDTH + MAX_TEXT_WIDTH), 0);
        toastPath.rCubicTo(circleOffset, 0, th / 2, th / 2 - circleOffset, th / 2, th / 2);

        toastPath.rLineTo(-pd, 0);
        toastPath.rCubicTo(0, -iconoffset, -iw / 2 + iconoffset, -iw / 2, -iw / 2, -iw / 2);
        toastPath.rCubicTo(-iconoffset, 0, -iw / 2, iw / 2 - iconoffset, -iw / 2, iw / 2);
        toastPath.rCubicTo(0, iconoffset, iw / 2 - iconoffset, iw / 2, iw / 2, iw / 2);
        toastPath.rCubicTo(iconoffset, 0, iw / 2, -iw / 2 + iconoffset, iw / 2, -iw / 2);
        toastPath.rLineTo(pd, 0);

        toastPath.rCubicTo(0, circleOffset, circleOffset - th / 2, th / 2, -th / 2, th / 2);
        toastPath.rLineTo(ws * (-IMAGE_WIDTH - MAX_TEXT_WIDTH), 0);
        toastPath.rCubicTo(-circleOffset, 0, -th / 2, -th / 2 + circleOffset, -th / 2, -th / 2);
        toastPath.rCubicTo(0, -circleOffset, -circleOffset + th / 2, -th / 2, th / 2, -th / 2);

        c.drawCircle(spinnerRect.centerX(), spinnerRect.centerY(), iconBounds.height() / 1.9f, backPaint);
        //loadicon.draw(c);
        c.drawPath(toastPath, backPaint);
        int yPos = (int) ((th / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));
        c.drawText(mText, 0, mText.length(), th / 2 + (MAX_TEXT_WIDTH - mTextBounds.width()) / 2, yPos, textPaint);

        float prog = va.getAnimatedFraction() * 6.0f;
        float progrot = prog % 2.0f;
        float proglength = easeinterpol.getInterpolation(prog % 3f / 3f) * 3f - .75f;
        if (proglength > .75f) {
            proglength = .75f - (prog % 3f - 1.5f);
            progrot += (prog % 3f - 1.5f) / 1.5f * 2f;
        }
        //Log.d("spin", "rot " + progrot + " len " + proglength);

        toastPath.reset();
        toastPath.arcTo(spinnerRect, 180 * progrot, Math.min((200 / .75f) * proglength + 1 + 560 * (1f - ws), 359.9999f));
        loaderPaint.setAlpha((int) (255 * ws));
        c.drawPath(toastPath, loaderPaint);

        if (WIDTH_SCALE > 1f) {
            //判断绘制哪个drawable图片
            Drawable icon = (success) ? completeicon : failedicon;
            float circleProg = WIDTH_SCALE - 1f;
            textPaint.setAlpha((int) (128 * circleProg + 127));
            int paddingicon = (int) ((1f - (.25f + (.75f * circleProg))) * TOAST_HEIGHT / 2);
            int completeoff = (int) ((1f - circleProg) * TOAST_HEIGHT / 8);
            icon.setBounds((int) spinnerRect.left + paddingicon, (int) spinnerRect.top + paddingicon + completeoff, (int) spinnerRect.right - paddingicon, (int) spinnerRect.bottom - paddingicon + completeoff);
            c.drawCircle(leftMargin + TOAST_HEIGHT / 2, (1f - circleProg) * TOAST_HEIGHT / 8 + TOAST_HEIGHT / 2,
                    (.25f + (.75f * circleProg)) * TOAST_HEIGHT / 2, (success) ? successPaint : errorPaint);
            c.save();
            c.rotate(90 * (1f - circleProg), leftMargin + TOAST_HEIGHT / 2, TOAST_HEIGHT / 2);
            icon.draw(c);
            c.restore();
        }
        //c.drawArc(spinnerRect, 360 * progrot, 200 * proglength + 1, true, loaderPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text

            result = IMAGE_WIDTH + MAX_TEXT_WIDTH + TOAST_HEIGHT;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = TOAST_HEIGHT;
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
