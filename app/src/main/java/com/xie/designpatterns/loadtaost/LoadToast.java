package com.xie.designpatterns.loadtaost;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * des：
 * author：Xie
 */
public class LoadToast {

    private String mText = "";
    private LoadToastView mView;
    private ViewGroup mParentView;
    private int mTranslationY = 0;
    private boolean mShowCalled = false;
    private boolean mInflated = false;

    public LoadToast(Context context) {
        mView = new LoadToastView(context);
        mParentView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        //把自定义toastview添加到content中
        mParentView.addView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ViewHelper.setAlpha(mView, 0);
        mParentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationX(mView, (mParentView.getWidth() - mView.getWidth()) / 2);
                ViewHelper.setTranslationY(mView, -mView.getHeight() + mTranslationY);
                mInflated = true;
                if (mShowCalled) show();
            }
        }, 1);

        mParentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                checkZPosition();
            }
        });
    }

    public LoadToast setTranslationY(int pixels) {
        mTranslationY = pixels;
        return this;
    }

    public LoadToast setText(String message) {
        mText = message;
        mView.setText(mText);
        return this;
    }

    public LoadToast setTextColor(int color) {
        mView.setTextColor(color);
        return this;
    }

    public LoadToast setBackgroundColor(int color) {
        mView.setBackgroundColor(color);
        return this;
    }

    public LoadToast setProgressColor(int color) {
        mView.setProgressColor(color);
        return this;
    }

    public LoadToast show() {
        if (!mInflated) {
            mShowCalled = true;
            return this;
        }
        mView.show();
        ViewHelper.setAlpha(mView, 0f);
        ViewHelper.setTranslationY(mView, -mView.getHeight() + mTranslationY);
        //mView.setVisibility(View.VISIBLE);
        ViewPropertyAnimator.animate(mView).alpha(1f).translationY(25 + mTranslationY)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(300).setStartDelay(0).start();
        return this;
    }

    public void success() {
        mView.success();
        slideUp();
    }

    public void error() {
        mView.error();
        slideUp();
    }

    private void checkZPosition() {
        int pos = mParentView.indexOfChild(mView);
        int count = mParentView.getChildCount();
        if (pos != count - 1) {
            mParentView.removeView(mView);
            mParentView.requestLayout();
            mParentView.addView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 成功或者失败后向上滑动
     */
    private void slideUp() {
        ViewPropertyAnimator.animate(mView).setStartDelay(1000).alpha(0f)
                .translationY(-mView.getHeight() + mTranslationY)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(300).start();
    }
}
