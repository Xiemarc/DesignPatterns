package com.xie.designpatterns.reveal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 自定义横向scrollview
 * Created by marc on 2017/6/9.
 */

public class GallaryHorizonalScrollView extends HorizontalScrollView implements View.OnTouchListener {
    private LinearLayout mContainer;
    private int centerX;
    private int icon_width;

    public GallaryHorizonalScrollView(Context context) {
        this(context, null);
    }

    public GallaryHorizonalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        //在ScrollView里面放置一个水平线性布局，再往里面放置很多ImageView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mContainer = new LinearLayout(getContext());
        mContainer.setLayoutParams(params);
        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View childView = mContainer.getChildAt(0);
        icon_width = childView.getWidth();//图标的宽度
        //得到horizonalscrollview的中间点的坐标
        centerX = getWidth() / 2;
        //中心坐标改为中心图片的左边界
        centerX = centerX - icon_width / 2;
        //给LinearLayout和horizonalscrollview之间设置边框距离
        mContainer.setPadding(centerX, 0, centerX, 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //图片效果变化
            reveal();
        }
        return false;
    }

    /**
     * 渐变效果
     */
    private void reveal() {
        //得到horizonalscrollview滑出去的距离
        int scrollX = getScrollX();
        //找到两张渐变的图片的下标--左，右
        int index_left = scrollX / icon_width;
        int index_right = index_left + 1;
        //设置图片的level
        for (int i = 0; i < mContainer.getChildCount(); i++) {
            if (i == index_left || i == index_right) {
                //变化
                //比例：
                float ratio = 5000f / icon_width;
                ImageView iv_left = (ImageView) mContainer.getChildAt(index_left);
                //scrollX%icon_width:代表滑出去的距离
                //滑出去了icon_width/2  icon_width/2%icon_width
                iv_left.setImageLevel(
                        (int) (5000 - scrollX % icon_width * ratio)
                );
                //右边
                if (index_right < mContainer.getChildCount()) {
                    ImageView iv_right = (ImageView) mContainer.getChildAt(index_right);
                    //scrollX%icon_width:代表滑出去的距离
                    //滑出去了icon_width/2  icon_width/2%icon_width
                    iv_right.setImageLevel(
                            (int) (10000 - scrollX % icon_width * ratio)
                    );
                }
            } else {
                //灰色
                ImageView iv = (ImageView) mContainer.getChildAt(i);
                iv.setImageLevel(0);
            }
        }
    }

    public void addImageViews(Drawable[] revealDrawables) {
        for (int i = 0; i < revealDrawables.length; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable(revealDrawables[i]);
            mContainer.addView(iv);
            if (i == 0) {
                iv.setImageLevel(5000);
            }
        }
        addView(mContainer);
    }
}
