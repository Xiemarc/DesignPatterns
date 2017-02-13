package com.xie.designpatterns.wave;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import static android.animation.ValueAnimator.INFINITE;
import static android.animation.ValueAnimator.REVERSE;

/**
 * des: 水波纹效果
 * author: marc
 * date:  2017/2/10 22:15
 * email：aliali_ha@yeah.net
 */

public class WaveAnimatorHelper {
    /**
     * 动画集合
     */
    private static AnimatorSet mAnimatorSet;

    private static List<Animator> animators;
    /**
     * 偏移量动画
     */
    private static ObjectAnimator waveShiftAnim;
    /**
     * 水位高度动画
     */
    private  static ObjectAnimator waterLevelAnim;
    /**
     * 振幅角度动画
     */
    private static ObjectAnimator amplitudeAnim;
    private static ObjectAnimator objectAnimator;
    /**
     * 是否暂停
     */
    private static boolean isPause = false;

    public enum ANIMATORTYPE {
        PROPERTYVALUES,
        ANIMATORSET
    }


    /**
     * 工具类的构造方法通常私有化
     */
    private WaveAnimatorHelper(ANIMATORTYPE type, float waterLevelStart, float waterLevelEnd, float waveShiftStart,
                               float waveShiftEnd, float amplitudeStart, float amplitudeEnd, WaveView mWaveView, int duration) {
        //改变偏移量
        if (type == ANIMATORTYPE.PROPERTYVALUES) {
//        PropertyValuesHolder waterLevelAnim = PropertyValuesHolder.ofFloat("waterLevelRatio", 0.0f, 0.5f);
            PropertyValuesHolder waterLevelAnim = PropertyValuesHolder.ofFloat("waterLevelRatio", waterLevelStart, waterLevelEnd);
//        PropertyValuesHolder waveShiftAnim = PropertyValuesHolder.ofFloat("waveShiftRatio", 0f, 1f);
            PropertyValuesHolder waveShiftAnim = PropertyValuesHolder.ofFloat("waveShiftRatio", waveShiftStart, waveShiftEnd);
//        PropertyValuesHolder amplitudeAnim = PropertyValuesHolder.ofFloat("amplitudeRatio", 0.0001f, 0.08f);
            PropertyValuesHolder amplitudeAnim = PropertyValuesHolder.ofFloat("amplitudeRatio", amplitudeStart, amplitudeEnd);
            objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mWaveView, waterLevelAnim, waveShiftAnim, amplitudeAnim);
            objectAnimator.setDuration(duration);
            objectAnimator.setRepeatMode(REVERSE);
            objectAnimator.setRepeatCount(INFINITE);
            objectAnimator.start();
        } else {
            animators = new ArrayList<>();
            waveShiftAnim = ObjectAnimator.ofFloat(
                    mWaveView, "waveShiftRatio", waveShiftStart, waveShiftEnd);
            // 无限 循环
            waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
            waveShiftAnim.setDuration(duration);
            //匀速线性差值器
            waveShiftAnim.setInterpolator(new LinearInterpolator());
            animators.add(waveShiftAnim);

//            改变水位比例
            waterLevelAnim = ObjectAnimator.ofFloat(
                    mWaveView, "waterLevelRatio", waterLevelStart, waterLevelEnd);
            waterLevelAnim.setDuration(duration * 3);
            waterLevelAnim.setRepeatCount(ValueAnimator.INFINITE);
            waterLevelAnim.setRepeatMode(ValueAnimator.REVERSE);
            //减速差值器
            waterLevelAnim.setInterpolator(new DecelerateInterpolator());
            animators.add(waterLevelAnim);

//            改变振幅比例
            amplitudeAnim = ObjectAnimator.ofFloat(
                    mWaveView, "amplitudeRatio", amplitudeStart, amplitudeEnd);
            amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
            amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
            amplitudeAnim.setDuration(duration * 2);
            amplitudeAnim.setInterpolator(new LinearInterpolator());
            animators.add(amplitudeAnim);
            mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(animators);
            if (mAnimatorSet != null) {
                mAnimatorSet.start();
            }
        }
    }


    /**
     * 通过ObjectAnimator.ofPropertyValuesHolder方式启动动画。3个动画持续时间相同
     *
     * @param waterLevelStart 水位开始位置
     * @param waterLevelEnd   水位结束位置
     * @param waveShiftStart  偏移开始位置
     * @param waveShiftEnd    偏移结束位置
     * @param amplitudeStart  振幅开始
     * @param amplitudeEnd    振幅结束
     * @param waveView        当前waveview
     * @param duration        持续时间
     * @return
     */
    public static WaveAnimatorHelper startAnimatorWithPropertyValuesHolder
    (float waterLevelStart, float waterLevelEnd, float waveShiftStart,
     float waveShiftEnd, float amplitudeStart, float amplitudeEnd, WaveView waveView, int duration) {
        return new WaveAnimatorHelper(ANIMATORTYPE.PROPERTYVALUES, waterLevelStart, waterLevelEnd, waveShiftStart,
                waveShiftEnd, amplitudeStart, amplitudeEnd, waveView, duration);
    }

    /**
     * 通过ObjectAnimator.ofPropertyValuesHolder方式启动动画。3个动画持续时间相同
     *
     * @param waterLevelStart 水位开始位置
     * @param waterLevelEnd   水位结束位置
     * @param waveShiftStart  偏移开始位置
     * @param waveShiftEnd    偏移结束位置
     * @param amplitudeStart  振幅开始
     * @param amplitudeEnd    振幅结束
     * @param waveView        当前waveview
     * @param duration        持续时间
     * @return
     */
    public static WaveAnimatorHelper startAnimatorWithAnimatorSet
    (float waterLevelStart, float waterLevelEnd, float waveShiftStart,
     float waveShiftEnd, float amplitudeStart, float amplitudeEnd, WaveView waveView, int duration) {
        return new WaveAnimatorHelper(ANIMATORTYPE.ANIMATORSET, waterLevelStart, waterLevelEnd, waveShiftStart,
                waveShiftEnd, amplitudeStart, amplitudeEnd, waveView, duration);
    }


    /**
     * 暂停PropertyValuesHolder动画
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void stopAnimatorWithPropertyValuesHolder() {
        if (objectAnimator != null) {
            if (!isPause) {
                objectAnimator.pause();
                isPause = true;
            }
        }
    }

    /**
     * 继续PropertyValuesHolder动画
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void resumePropertyValuesHolder() {
        if (objectAnimator != null) {
            if (isPause) {
                objectAnimator.resume();
                isPause = false;
                return;
            }
            objectAnimator.resume();
        }
    }

    public static void stopAnimationWithAnimatorSet(){
        if(mAnimatorSet != null){

        }
    }
}
