package com.xie.designpatterns.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.xie.designpatterns.R;
import com.xie.designpatterns.logger.Logger;

/**
 * des:弹出视图
 * author: marc
 * date:  2017/2/22 09:19
 * email：aliali_ha@yeah.net
 */

public class BouncingMenu {

    private final ViewGroup mParentViewGroup;
    private View rootView;
    private final BouncingView boucingView;
    private final RecyclerView recycleView;
    private RecyclerView.Adapter adapter;
    private int screenWidht;
    private int screenHeight;

    /**
     * @param resId   布局资源id
     * @param adapter
     */
    public BouncingMenu(View view, int resId, RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        //不断地往上追溯找到 系统id为content的帧布局，添加进去
        // @android:id/content 就是锚点view
        mParentViewGroup = findRootParent(view);
        //渲染
        rootView = LayoutInflater.from(view.getContext()).inflate(resId, null, false);
        //拿到高度,然后把rootview的高度设置为0
        Logger.i("视图的跟布局是" + rootView);
        boucingView = (BouncingView) rootView.findViewById(R.id.sv);
        boucingView.setAnimationListener(new MyAnimationListener());
        recycleView = (RecyclerView) rootView.findViewById(R.id.rv);
        recycleView.setAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.rv_layout_animation));
        recycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getScreentWight(view.getContext());
    }

    private ViewGroup findRootParent(View view) {
        do {
            //判断是帧布局
            if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    //判断布局id为content
                    return (ViewGroup) view;
                }
            }
            //没找到 ，就不断的往上找
            if (view != null) {
                ViewParent parent = view.getParent();
                //如果父布局是view的话往上继续找
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);
        return null;
    }

    public static BouncingMenu makeMenu(View view, int resId, RecyclerView.Adapter adapter) {

        return new BouncingMenu(view, resId, adapter);
    }

    /**
     * 显示
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public BouncingMenu show() {
        //显示,往帧布局里面addView（xxxView）
        if (rootView.getParent() != null) {
            //防止已经show了再次show
            mParentViewGroup.removeView(rootView);
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        params.gravity = Gravity.CENTER;
//        params.width = (int) (screenWidht * 0.8);
        rootView.setLayoutParams(params);
//        int shape_corner_up = R.drawable.shape_corner_up;
//        rootView.setBackgroundResource(shape_corner_up);
        mParentViewGroup.addView(rootView);
        boucingView.show();
        return this;
    }

    /**
     * 关闭
     */
    public void dissmiss() {
        //属性动画实现消失
        ObjectAnimator animator = ObjectAnimator.ofFloat(rootView, "translationY", 0, rootView.getHeight());
        animator.setDuration(600);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束
                super.onAnimationEnd(animation);
                mParentViewGroup.removeView(rootView);
                rootView = null;
            }
        });
        animator.start();
    }

    private class MyAnimationListener implements BouncingView.AnimationListener {
        @Override
        public void onShowContent() {
            recycleView.setVisibility(View.VISIBLE);
            recycleView.setAdapter(adapter);
            recycleView.scheduleLayoutAnimation();
        }
    }

    private void getScreentWight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        screenWidht = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }
}
