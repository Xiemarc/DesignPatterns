package com.xie.designpatterns.chinamap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.xie.designpatterns.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图自定义view
 * Created by marc on 2017/5/4.
 */

public class ChinaMapView extends View {
    private Paint paint;
    private int miniWidth;//最小宽度
    private int miniHeight;//最小高度
    private int provinceTextSize;//省份文本大小
    private int provinceMargin;//省份的margin值
    private int numberMargin;//省份人口的margin值
    private int bottomPadding;//距离底部的padding值

    private float scale = 1;

    private RectF mapSize;

    private ProvinceItem selectedItem;//被选中的省份
    private List<ProvinceItem> itemList;//所有省份的集合
    private Collection<? extends IProvinceData> dataList;
    private Drawable drawable;

    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    private GestureDetectorCompat gestureDetectorCompat;

    public ChinaMapView(Context context) {
        this(context, null);
    }

    public ChinaMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChinaMapView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    /**
     * 初始化加载地图数据并设置相关手势监听
     *
     * @param attrs    属性
     * @param defStyle 默认属性
     */
    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
        paint.setAntiAlias(true);
        miniWidth = getContext().getResources().getDimensionPixelSize(R.dimen.map_min_width);
        miniHeight = getContext().getResources().getDimensionPixelSize(R.dimen.map_min_height);
        provinceTextSize = getResources().getDimensionPixelSize(R.dimen.map_province_text_size);
        provinceMargin = getResources().getDimensionPixelSize(R.dimen.map_province_margin);
        numberMargin = getResources().getDimensionPixelSize(R.dimen.map_number_margin);

        gestureDetectorCompat = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                handlerTouch((int) x, (int) y);
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                super.onShowPress(e);
            }
        });
        drawable = getResources().getDrawable(R.drawable.scale_rule);
        bottomPadding = getContext().getResources().getDimensionPixelSize(R.dimen.map_bottom_padding);

        if (!isInEditMode()) {
            //获取地图svg封装信息
            MapSVGManager.getInstance(getContext()).getProvincePathListAsync(
                    ((provincePathList, size) -> {
                        List<ProvinceItem> list = new ArrayList<>();
                        for (ProvincePath provincePath : provincePathList) {
                            ProvinceItem item = new ProvinceItem();
                            item.setPath(provincePath.getPath());
                            item.setProvinceCode(provincePath.getCode());
                            item.setProvinceName(provincePath.getName());
                            list.add(item);
                        }

                        if (dataList != null) {
                            setMapColor(list, dataList);
                        }
                        mapSize = size;
                        itemList = list;
                        //刷新布局
                        requestLayout();
                        postInvalidate();
                    })
            );
        }
    }


    /**
     * 处理手势
     *
     * @param x 当前x
     * @param y 当前y
     * @return 是否触摸到省份区域部分
     */
    private boolean handlerTouch(int x, int y) {
        ProvinceItem provinceItem = null;
        List<ProvinceItem> list = itemList;
        if (list == null) {
            return false;
        }
        for (ProvinceItem temp : list) {
            if (temp.isTouched((int) (x / scale), (int) (y / scale))) {
                provinceItem = temp;
                break;
            }
        }
        if (provinceItem != null && !provinceItem.equals(selectedItem)) {
            selectedItem = provinceItem;
            postInvalidate();
        }
        return provinceItem != null;
    }

    /**
     * 设置地图区域颜色，根据所占比例来绘制区域颜色深浅即初始化区域绘制信息
     *
     * @param itemList 省份区域集合
     * @param dataList 实际解析数据集合
     */
    private void setMapColor(List<ProvinceItem> itemList, Collection<? extends IProvinceData> dataList) {
        int totalNumber = 0;
        //key:省份code  value:省份人数
        Map<Integer, Integer> map = new HashMap<>();
        if (dataList != null) {
            for (IProvinceData data : dataList) {
                totalNumber += data.getPersonNumber();
                map.put(data.getProvinceCode(), data.getPersonNumber());
            }
        }
        for (ProvinceItem item : itemList) {
            int provinceCode = item.getProvinceCode();
            int number = 0;
            if (map.containsKey(provinceCode)) {
                number = map.get(provinceCode);
            }
            item.setPersonNumber(number);

            int color = Color.WHITE;
            if (totalNumber > 0) {
                double flag = (double) number / totalNumber;
                if (flag > 0.2) {
                    color = colorArray[0];
                } else if (flag > 0.1) {
                    color = colorArray[1];
                } else if (flag > 0) {
                    color = colorArray[2];
                } else {
                    color = Color.WHITE;
                }
            }
            item.setDrawColor(color);
        }
    }

    /**
     * 设置数据
     *
     * @param list 加载数据集合
     * @param <T>  泛型
     */
    public <T extends IProvinceData> void setData(Collection<T> list) {
        if (null != itemList) {
            //重新设置绘制区域信息
            setMapColor(itemList, list);
            postInvalidate();
        }
        dataList = list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final List<ProvinceItem> list = itemList;
        if (null != list) {
            int width = getWidth();
            int height = getHeight();
            //保存画布
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem item:list) {
                if(!item.equals(selectedItem)){
                    //绘制不选中的省份区域样式
                    item.drawItem(canvas,paint,false);
                }
            }
            if (selectedItem != null) {
                //绘制选中的省份区域样式
                selectedItem.drawItem(canvas, paint, true);
            }
            canvas.restore();
            if (selectedItem != null) {
                //绘制文字
                paint.setTypeface(Typeface.DEFAULT);
                paint.setColor(0xFF333333);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.clearShadowLayer();
                paint.setTextSize(provinceTextSize);
                String provinceName = selectedItem.getProvinceName();
                canvas.drawText(provinceName, width / 2, provinceMargin, paint);

                int number = selectedItem.getPersonNumber();
                canvas.drawText(number + "人", width / 2, provinceMargin + provinceTextSize + numberMargin, paint);
            }
            if (drawable instanceof BitmapDrawable) {
                //绘制左下角的drawable
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                int bitmapHeight = bitmap.getHeight();
                int bitmapWidth = bitmap.getWidth();
                drawable.setBounds(0, height - bitmapHeight, bitmapWidth, height);
                drawable.draw(canvas);
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int viewWidth = width;
        int viewHeight = height;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                viewWidth = width > miniWidth ? width : miniWidth;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                viewWidth = miniWidth;
                break;
        }

        int computeHeight;
        if (mapSize != null) {
            double mapWidth = mapSize.width();
            double mapHeight = mapSize.height();
            scale = (float) (viewWidth / mapWidth);
            computeHeight = (int) (mapHeight * viewWidth / mapWidth);
        } else {
            computeHeight = (miniHeight * viewWidth / miniWidth);
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                viewHeight = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                viewHeight = miniHeight > computeHeight ? miniHeight : computeHeight;
                break;
        }

        if (mapSize != null) {
            double mapWidth = mapSize.width();
            scale = (float) (viewWidth / mapWidth);
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(viewHeight + bottomPadding, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetectorCompat.onTouchEvent(event);
    }
}
