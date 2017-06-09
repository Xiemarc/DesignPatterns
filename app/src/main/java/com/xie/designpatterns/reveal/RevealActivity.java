package com.xie.designpatterns.reveal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/7.
 */

public class RevealActivity extends AppCompatActivity {
    Drawable mUnSelectedDrawable;
    Drawable mSelectedDrawable;
    private ImageView iv;
    private int[] mImgIds = new int[]{ //7个
            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline,
            R.drawable.avft,
            R.drawable.box_stack,
            R.drawable.bubble_frame,
            R.drawable.bubbles,
            R.drawable.bullseye,
            R.drawable.circle_filled,
            R.drawable.circle_outline
    };
    private int[] mImgIds_active = new int[]{
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active,
            R.drawable.avft_active, R.drawable.box_stack_active, R.drawable.bubble_frame_active,
            R.drawable.bubbles_active, R.drawable.bullseye_active, R.drawable.circle_filled_active,
            R.drawable.circle_outline_active
    };

    public Drawable[] revealDrawables;
    protected int level = 5000;
    Handler handler = new Handler();
    private GallaryHorizonalScrollView hzv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal);
        initData();
        initView();
//        iv = new ImageView(this);
//        mUnSelectedDrawable = getResources().getDrawable(R.drawable.avft);
//        mSelectedDrawable = getResources().getDrawable(R.drawable.avft_active);
//        RevealDrawable revealDrawable = new RevealDrawable(mUnSelectedDrawable, mSelectedDrawable, 1);
//        iv.setImageDrawable(revealDrawable);
//        setContentView(iv);
//        iv.setImageLevel(level);
////        iv.setOnClickListener(view->{
////
////        });
//        handler.postDelayed(runnable,100);
    }

    private void initData(){
        revealDrawables = new Drawable[mImgIds.length];
    }

    private void initView()
    {
        for (int i = 0; i < mImgIds.length; i++)
        {
            RevealDrawable rd = new RevealDrawable(
                    getResources().getDrawable(mImgIds[i]),
                    getResources().getDrawable(mImgIds_active[i]),
                    RevealDrawable.HORIZONTAL);
            revealDrawables[i] = rd;
        }
        hzv = (GallaryHorizonalScrollView)findViewById(R.id.hsv);
        hzv.addImageViews(revealDrawables);
    }

//
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            level -= 100;
//            iv.getDrawable().setLevel(level);
//            handler.postDelayed(runnable, 200);
//            if(level >=10000){
//                handler.removeCallbacks(runnable);
//            }
//        }
//    };
}
