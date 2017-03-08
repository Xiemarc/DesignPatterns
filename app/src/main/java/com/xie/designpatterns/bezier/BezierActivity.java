package com.xie.designpatterns.bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.xie.designpatterns.R;

/**
 * des:
 * author: marc
 * date:  2017/2/8 09:53
 * email：aliali_ha@yeah.net
 */
public class BezierActivity extends AppCompatActivity {
    private static final long ANIMATION_DUARTION = 400;//运动时间
    private static final float MINIMUN_X_DISTANCE = 200;//x轴运动距离
    private static final float SCALE_FACTOR_EXPAND = 13;//扩大倍数
    private static final float SCALE_FACTOR_ORI = 1;//最初的倍数
    ImageButton mFab;
    FrameLayout mFabContainer;//帧布局
    LinearLayout mControlsContainer;
    private int mFabSize;//ImageButton的大小
    private boolean mRevealFlag;
    private boolean mResetFlag;
    private float startX;
    private float startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bezier);
        mFabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        bindViews();
    }

    private void bindViews() {
        mFab = (ImageButton) findViewById(R.id.fab);
        mFabContainer = (FrameLayout) findViewById(R.id.fab_container);
        mControlsContainer = (LinearLayout) findViewById(R.id.media_controls_container);
    }

    public void onFabPressed(View view) {
        //还没运动前的X坐标
        startX = mFab.getX();
        startY = mFab.getY();
        //开启动画 使用属性动画   属性动画控制对象身上的任何属性值 (必须有set方法)

        AnimatorPath mPath = new AnimatorPath();
        mPath.moveTo(0, 0);
        //贝塞尔曲线
        mPath.curveTo(-200, 200, -400, 100, -600, 0);
        //相对于原来移动的点
        //填this是控制当前对象（当前是BezierActivity）的PathPoint p 这个属性 。 mPath.getPoints()是相当于从某个值到某个值
        ObjectAnimator animator = ObjectAnimator.ofObject(this, "fabLocation", new PathEvaluator(), mPath.getPoints().toArray());
        animator.setDuration(ANIMATION_DUARTION);
        animator.start();
        //水波纹效果
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //X轴运动距离超过200  开始水波纹扩散
                if (Math.abs(startX - mFab.getX()) > MINIMUN_X_DISTANCE) {
                    if (!mRevealFlag) {
                        //高版本 api 运动
                        //已经设置成透明的了。设置回来
                        mFab.setImageDrawable(new BitmapDrawable());
                        mFabContainer.setY(mFabContainer.getY() + mFabSize / 2);
                        mFab.animate()
                                .scaleX(SCALE_FACTOR_EXPAND)
                                .scaleY(SCALE_FACTOR_EXPAND)
                                .setListener(endListener)
                                .setDuration(ANIMATION_DUARTION)
                                .start();
                        mRevealFlag = true;
                        mResetFlag = false;
                    }

                }
            }
        });
    }

    //反射  不用直接设置属性
    PathPoint fabLocation;

    public void setFabLocation(PathPoint fabLocation) {
        //达到不断的控制view进行移动
        mFab.setTranslationX(fabLocation.mX);
        mFab.setTranslationY(fabLocation.mY);
    }


    //开始动画。效果跟点击开始按钮一样
    public void startAnimator(View view) {
        onFabPressed(view);
    }

    /**
     * fab做正向移动时的listerner
     */
    private AnimatorListenerAdapter endListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mFab.setVisibility(View.INVISIBLE);
            mFabContainer.setBackgroundColor(getResources().getColor(R.color.brand_accent));
            for (int i = 0; i < mControlsContainer.getChildCount(); i++) {
                View v = mControlsContainer.getChildAt(i);
                ViewPropertyAnimator animator2 = v.animate().scaleX(1).scaleY(1).setDuration(ANIMATION_DUARTION);
                //依次显示
                animator2.setStartDelay(i * 50).start();
            }
        }

    };

    /**
     * 重置动画  1.水波纹
     * 2. framelayout布局上移
     * 3. fab做位移
     *
     * @param view
     */
    public void reset(View view) {
        //1.先隐藏
        for (int i = 0; i < mControlsContainer.getChildCount(); i++) {
            View v = mControlsContainer.getChildAt(i);
            ViewPropertyAnimator animator = v.animate().scaleX(0).scaleY(0).setDuration(ANIMATION_DUARTION);
            //依次显示
            animator.setStartDelay(i * 50);
            //最后一个动画的时候监听动画结束 ，开始显示fab。然后进行缩放
//            if (i == mControlsContainer.getChildCount() - 1) {
//            }
        }
//        ViewPropertyAnimator viewPropertyAnimator = mControlsContainer.animate().alpha(0).setDuration(ANIMATION_DUARTION);
//        viewPropertyAnimator.setListener(reverseListener);
//        viewPropertyAnimator.start();
        AnimatorPath mPath1 = new AnimatorPath();
        mPath1.moveTo(-600, 0);
        mPath1.lineTo(0, 0);
        //相对于原来移动的点
        //填this是控制当前对象（当前是BezierActivity）的PathPoint p 这个属性 。 mPath.getPoints()是相当于从某个值到某个值
        ObjectAnimator fabLocation = ObjectAnimator.ofObject(this, "fabLocation", new PathEvaluator(), mPath1.getPoints().toArray());
        fabLocation.setDuration(ANIMATION_DUARTION);
        fabLocation.addListener(reverseListener);
        //设置加速
        fabLocation.start();


    }


    /**
     * 2.结束的时候fab显示。然后进行缩放
     */
    private AnimatorListenerAdapter reverseListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationEnd(Animator animation) {
            //结束的时候背景显示成透明
            mFabContainer.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            //ImageButton设置成显示状态
            mFab.setVisibility(View.VISIBLE);
            //2. ImageButton开始缩放 从13-1的缩放
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 13, 1);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 13, 1);
            ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(mFab, scaleX, scaleY);
            objectAnimator.setDuration(ANIMATION_DUARTION);
            objectAnimator.addListener(listenerAdapter);
            objectAnimator.start();
        }
    };


    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            //ImageButton缩放动画结束的时候
            if (!mResetFlag) {
                mFabContainer.setY(mFabContainer.getY() - mFabSize / 2);
                mFab.setImageBitmap(BitmapFactory.decodeResource(getResources(), android.R.drawable.ic_media_pause));
                mResetFlag = true;
                mRevealFlag = false;
            }
        }
    };

    public void showtoast(View v) {
        Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
    }
}
