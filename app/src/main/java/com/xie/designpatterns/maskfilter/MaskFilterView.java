package com.xie.designpatterns.maskfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * Created by marc on 2017/5/22.
 */

public class MaskFilterView extends View {
    public MaskFilterView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon3);
        canvas.drawBitmap(bitmap, 100, 300, paint);
    }
}
