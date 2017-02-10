package com.xie.designpatterns.wave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * des：波浪View
 * author：Xie
 */
public class WaveView extends View {
    //*************属性开始**************//
    /**
     * 振幅比例
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    /**
     * 水位比例
     */
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    /**
     * 波长比例
     */
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    /**
     * 偏移
     */
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    /**
     * 后面波浪的颜色
     */
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    /**
     * 前面波浪的颜色
     */
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    /**
     * 外观样式
     */
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    /**
     * 是否展示波浪动画
     */
    private boolean mShowWave;
    /**
     * 着色器
     */
    private BitmapShader mWaveShader;
    /**
     * shader矩阵
     */
    private Matrix mShaderMatrix;
    /**
     * view的画笔
     */
    private Paint mViewPaint;
    /**
     * 边界border的画笔
     */
    private Paint mBorderPaint;
    /**
     * 默认振幅
     */
    private float mDefaultAmplitude;
    /**
     * 默认水位
     */
    private float mDefaultWaterLevel;
    /**
     * 默认wave宽度
     */
    private float mDefaultWaveLength;
    /**
     * 默认波浪角频率
     */
    private double mDefaultAngularFrequency;
    /**
     * 默认振幅比例
     */
    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    /**
     * 波浪宽度
     */
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    /**
     * 波浪水位
     */
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    /**
     * 位移
     */
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    /**
     * 默认后面波浪颜色
     */
    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    /**
     * 默认前面波浪颜色
     */
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    /**
     * 默认着色外观
     */
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;
    //*************属性结束**************//

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 做一些初始化的操作
     */
    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createShader();
    }

    /**
     * 绘制初始波形
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        //默认振幅（）= 控件的高度* 默认振幅比例
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        //创建长宽恰等于WaveView的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);
        //把波浪画到bitmap上面
        //画一个正弦曲线
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];
        //初始化后面波浪
        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            //波浪形的x
            double wx = beginX * mDefaultAngularFrequency;
            //开始y的值通过公式计算
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }
        //初始化前面波浪  前面波浪相对于后面波浪的偏移量
        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }
        //产生一个画有一个位图的渲染器  ,
        // Shader.TileMode.REPEAT: 在x轴上面的平铺模式、REPEAT:横向和纵向的重复渲染器图片，平铺
        //Shader.TileMode.CLAMP  渲染器超出原始边界范围，会复制范围内边缘染色
        // 在x轴采用横向和纵向平铺。在y轴采用渲染器超出原始边界范围，会复制范围内边缘染色
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //如果当前未显示wave
        if (mShowWave && mWaveShader != null) {
            //还必须确定自定义控件的shader 不能为空
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }
            //先做缩放变换
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,//x轴的缩放比例
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,//y轴的缩放该比例
                    0,//中心点x
                    mDefaultWaterLevel);//中心点y
            //再做平移,这里注意下，是postTranslate ，不是setTranslate.是因为需要后乘
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(),//在x轴移动的量
                    //在y轴移动的量,
                    //y轴上使用正数进行平移将向下移动图像，而使用负数将向上移动图像。
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());
            //自定义控件的边框宽度
            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        //如果边框的宽度大于0，则绘制出一个挨着自定义控件的圆形。
                        //该圆的中心肯定在自定义控件的中心。
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                                (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    //绘制自定义控件
                    float radius = getWidth() / 2f - borderWidth;
                    //波形图已经在mViewPaint 中的shader中了
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, mViewPaint);
                    break;
                case SQUARE:
                    if (borderWidth > 0) {
                        canvas.drawRect(
                                borderWidth / 2f,
                                borderWidth / 2f,
                                getWidth() - borderWidth / 2f - 0.5f,
                                getHeight() - borderWidth / 2f - 0.5f,
                                mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                            getHeight() - borderWidth, mViewPaint);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }

    //*********************get和set开始********************************//

    /**
     * 得到偏移比例
     *
     * @return 获得偏移的比例
     */
    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * 设置波浪转换偏移比例
     *
     * @param waveShiftRatio waveShiftRatio 必须是 0 ~ 1.0的值，默认是0
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    /**
     * 得到 水位比例
     *
     * @return
     */
    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * 设置水位比例
     *
     * @param waterLevelRatio 必须是 0 ~ 1. 默认0.5f
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    /**
     * 得到振幅比例
     *
     * @return
     */
    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }


    /**
     * 设置振幅比例
     *
     * @param amplitudeRatio 默认是 0.05. 不能大于1
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    /**
     * 得到宽度
     *
     * @return
     */
    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * 设置横向波浪宽度的比例
     *
     * @param waveLengthRatio 默认1
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    /**
     * 设置 边框的宽度和颜色
     *
     * @param width
     * @param color
     */
    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Paint.Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    /**
     * 设置 波浪的颜色
     *
     * @param behindWaveColor 后面波浪的颜色
     * @param frontWaveColor  前面波浪的颜色
     */
    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        if (getWidth() > 0 && getHeight() > 0) {
            // need to recreate shader when color changed
            mWaveShader = null;
            createShader();
            invalidate();
        }
    }

    /**
     * 设置外形
     *
     * @param shapeType
     */
    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    //*********************get和set结束********************************//
}
