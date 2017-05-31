package com.xie.designpatterns.parallel;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.xie.designpatterns.R;
import com.xie.designpatterns.utils.logger.Logger;

/**
 * 监听viewapger滑动时的变换
 * 方法设置在viewpasger身上，在页面滑动过程中，都会回调该方法transformPage
 * Created by marc on 2017/4/20.
 */
public class WelcompagerTransformer implements ViewPager.PageTransformer, ViewPager.OnPageChangeListener {

    private static final float ROT_MOD = -15f;
    //第几个页面
    private int pageIndex;
    //页面是否变化，防止滑动一点  里面的image 和scrollview就做动画
    private boolean pageChanged = true;

    /**
     * 此方法是滑动的时候每一个页面View都会调用该方法
     * page:当前的页面
     * position:当前滑动的位置
     * 视差效果：在View正常滑动的情况下，给当前View或者当前view里面的每一个子view来一个加速度
     * 而且每一个加速度大小不一样。
     * 在当前view正好显示的时候，position是0，整个完全往左划出去的时候 是-1。view往右边完全划出去的时候是1.
     */
    @Override
    public void transformPage(View page, float position) {
        ViewGroup v = (ViewGroup) page.findViewById(R.id.rl);
        final MyScrollView mscv = (MyScrollView) v.findViewById(R.id.mscv);
        //FrameLayout中的2张背景图,做横向移动动画
        View bg1 = v.findViewById(R.id.imageView0);
        View bg2 = v.findViewById(R.id.imageView0_2);
        View bg_container = v.findViewById(R.id.bg_container);
        //背景颜色
        int bg1_green = page.getContext().getResources().getColor(R.color.bg1_green);
        int bg2_blue = page.getContext().getResources().getColor(R.color.bg2_blue);
        Integer tag = (Integer) page.getTag();
        //父布局是 viewpager
        View parent = (View) page.getParent();
        if (page instanceof ViewPager) {
            Logger.i("marc", "parent:" + parent.getClass().getSimpleName());
        }
        //每个页面的背景使用颜色估值器估算出,使用ArgbEvaluator分分钟实现背景渐变
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int color = bg1_green;
        if (tag.intValue() == pageIndex) {
            switch (pageIndex) {
                case 0://viewpager第一个页面
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
                case 1://viewpager第二个页面
                    color = (int) evaluator.evaluate(Math.abs(position), bg2_blue, bg1_green);
                    break;
                case 2://viewpager第三个页面
                    color = (int) evaluator.evaluate(Math.abs(position), bg1_green, bg2_blue);
                    break;
            }
            //设置整个viewpager的背景颜色
            parent.setBackgroundColor(color);
        }
        //正好移动到位于屏幕中
        if (position == 0) {
            Logger.i("marc", "position==0");
            //pageChanged作用--解决问题：只有在切换页面的时候才展示平移动画，
            //如果不判断则会只是移动一点点当前页面松开也会执行一次平移动画
            if (pageChanged) {
                bg1.setVisibility(View.VISIBLE);
                bg2.setVisibility(View.VISIBLE);
                //第一张背景做动画,需要做从当前位置向右做动画
                ObjectAnimator animator_bg1 = ObjectAnimator.ofFloat(bg1, "translationX", 0, -bg1.getWidth());
                animator_bg1.setDuration(500);
                animator_bg1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //大的背景图变化的时候 下面scrollview中的图片跟着变化
                        mscv.smoothScrollTo((int) (mscv.getWidth() * animation.getAnimatedFraction()), 0);
                    }
                });
                animator_bg1.start();

//                //第二张大的背景图做动画,从右边移动到0位置
                ObjectAnimator animator_bg2 = ObjectAnimator.ofFloat(bg2, "translationX", bg2.getWidth(), 0);
                animator_bg2.setDuration(500);
                animator_bg2.start();
                pageChanged = false;
            }
        } else if (position == 1 || position == -1) {
            //滑动到位于左侧或者位于右侧,复原所有效果
            bg2.setTranslationX(0);
            bg1.setTranslationX(0);
            mscv.smoothScrollTo(0, 0);
        } else if (position < 1 && position > -1) {
            //正在滑动中
            final float width = bg1.getWidth();
            final float height = bg1.getHeight();
            final float rotation = ROT_MOD * position * -1.25f;
            //这里不去分别处理bg1、bg2，而是用包裹的父容器执行动画，目的是避免难以处理两个bg的属性位置恢复。
            //设置中心轴坐标
            bg_container.setPivotX(width * 0.5f);
            bg_container.setPivotY(height);
            bg_container.setRotation(rotation);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * 当前页面 完全选中
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        pageIndex = position;
        pageChanged = true;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
