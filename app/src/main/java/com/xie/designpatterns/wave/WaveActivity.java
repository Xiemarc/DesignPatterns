package com.xie.designpatterns.wave;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.xie.designpatterns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * des：
 * author：Xie
 */
public class WaveActivity extends AppCompatActivity {

    private WaveView mWaveView;
    private AnimatorSet mAnimatorSet;
    private List<Animator> animators;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;
    private ObjectAnimator waveShiftAnim;
    private ObjectAnimator waterLevelAnim;
    private ObjectAnimator amplitudeAnim;
    private boolean isPause = false;
    private ObjectAnimator objectAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        mWaveView = (WaveView) findViewById(R.id.wave);
//        mWaveView.setBorder(mBorderWidth, mBorderColor);
        mAnimatorSet = new AnimatorSet();
        mWaveView.setShapeType(WaveView.ShapeType.SQUARE);
        animators = new ArrayList<>();
//        initAnimation();
    }

    private void initAnimation() {

        //改变偏移量
//        PropertyValuesHolder waveShiftAnim = PropertyValuesHolder.ofFloat("waveShiftRatio", 0f, 1f);
//        PropertyValuesHolder waterLevelAnim = PropertyValuesHolder.ofFloat("waterLevelRatio", 0.0f, 0.5f);
//        PropertyValuesHolder amplitudeAnim = PropertyValuesHolder.ofFloat("amplitudeRatio", 0.0001f, 0.08f);
//        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mWaveView, waterLevelAnim, amplitudeAnim);
//        objectAnimator.setDuration(2000);
//        objectAnimator.setRepeatMode(REVERSE);
//        objectAnimator.setRepeatCount(INFINITE);
//        objectAnimator.start();
        waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        // 无限 循环
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        //匀速线性差值器
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);
//        改变水位比例
        waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0f, 1.0f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setRepeatCount(ValueAnimator.INFINITE);
        waterLevelAnim.setRepeatMode(ValueAnimator.REVERSE);
        //减速差值器
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);

//        改变振幅比例
        amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);
        mAnimatorSet.playTogether(animators);
    }

    /**
     * 这里使用api 19的,低版本可以添加addListener
     *
     * @param view
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void start(View view) {
        mWaveView.setShowWave(true);
        if (isPause) {
//            waterLevelAnim.resume();
//            waveShiftAnim.resume();
//            amplitudeAnim.resume();
            objectAnimator.resume();
            isPause = false;
            return;
        }
        if (mAnimatorSet != null) {
//            mAnimatorSet.start();
//            WaveAnimatorHelper.startAnimatorWithPropertyValuesHolder(
//                    0.0f, 0.4f, 0f, 1f, 0.0001f, 0.2f, mWaveView, 2000);

            WaveAnimatorHelper.startAnimatorWithAnimatorSet(0.0f, 0.4f, 0f, 1f, 0.0001f, 0.2f, mWaveView, 2000);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void stop(View view) {
        if (mAnimatorSet != null) {
//            mAnimatorSet.end();
            if (!isPause) {
//                waveShiftAnim.pause();
//                waterLevelAnim.pause();
//                amplitudeAnim.pause();
                objectAnimator.pause();
                isPause = true;
            }
        }
    }

}
