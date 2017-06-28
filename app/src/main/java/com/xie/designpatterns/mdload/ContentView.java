package com.xie.designpatterns.mdload;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/6/28.
 */

public class ContentView extends ImageView{
    public ContentView(Context context) {
        this(context,null);
    }

    public ContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setImageResource(R.drawable.a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xff00ffff);
        super.onDraw(canvas);
    }
}
