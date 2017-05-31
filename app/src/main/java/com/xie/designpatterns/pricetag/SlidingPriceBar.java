package com.xie.designpatterns.pricetag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xie.designpatterns.R;

/**
 * des：
 * author：Xie
 */
public class SlidingPriceBar extends View {

    private Bitmap gray_bg;
    private Bitmap green_bg;
    private Bitmap btn;
    private Bitmap num_price;
    private Paint paint;
    private int bg_height;
    private int bg_width;
    private float scale_h;
    //价格区间
    private final int FIRST_STAGE = 0;
    private final int SECEND_STAGE = 200;
    private final int THIRD_STAGE = 500;
    private final int FOURTH_STAGE = 1000;
    private final int FIFTH_STAGE = 10000;
    private int span;
    private int price_u;
    private int price_d;
    private float btn_x;
    private float y_u;
    private final float REAL_PER = 0.95F;
    private float half_round;
    private String TAG = "SlidingPriceBar";
    private float y_d;
    private int PRICE_PADDING = 15;


    public SlidingPriceBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化--图片
        gray_bg = BitmapFactory.decodeResource(getResources(), R.drawable.axis_before);
        green_bg = BitmapFactory.decodeResource(getResources(), R.drawable.axis_after);
        btn = BitmapFactory.decodeResource(getResources(), R.drawable.btn);
        num_price = BitmapFactory.decodeResource(getResources(), R.drawable.bg_number);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        //获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.slidingpricebar);
        price_u = array.getInt(R.styleable.slidingpricebar_price_u, 1000);
        price_d = array.getInt(R.styleable.slidingpricebar_price_d, 200);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 当前控件到底显示多大，有以下决定的：
         * 1.父容器给的宽高条件；
         * 2.自己的宽高；
         *
         * widthMeasureSpec，heightMeasureSpec(父容器指定的宽高信息)包含两种信息：
         * 1.类型mode
         * 2.value值
         */
//		MeasureSpec.EXACTLY 确定的值
//		MeasureSpec.AT_MOST match_parent/fill_parent
//		MeasureSpec.UNSPECIFIED 不确定大小wrap_content
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);

        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        bg_height = gray_bg.getHeight();
        bg_width = gray_bg.getWidth();
        //如果是wrap_content，只能根据自己的需求来决定宽高；
        int measuredHeight = (modeHeight==MeasureSpec.EXACTLY)?sizeHeight:bg_height;
        //怎么设置高度也不能超过父容器给的宽高
        measuredHeight = Math.min(measuredHeight, sizeHeight);
        int measuredWidth = measuredHeight*2/3;
        //压缩比例
        scale_h = (float)measuredHeight/bg_height;
        //分成四截
        span = (bg_height-bg_width)/4;
        //小圆的一半宽度
        half_round = bg_height*(1-REAL_PER)/2;
        //设置我自己测量后，给自己设置的宽高。
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //对画布缩放
        canvas.save();//保存当前画布的状态
        canvas.scale(scale_h, scale_h);

        //1.绘制滑竿
        float bg_x = (this.getWidth()/scale_h-gray_bg.getWidth())/2;
        canvas.drawBitmap(gray_bg, bg_x, 0, paint);

        //2.绘制右边几个文字
        String[] numbers = new String[]{
                "不限",
                String.valueOf(FOURTH_STAGE),
                String.valueOf(THIRD_STAGE),
                String.valueOf(SECEND_STAGE),
                String.valueOf(FIRST_STAGE)
        };
        paint.setTextSize(20/scale_h);
        for (int i = 0; i < numbers.length; i++) {
            int text_x = (int) (bg_x*5/4);
            //文字基线
            //(paint.descent()-paint.ascent())/2 字体高度的一半
//			float text_y = i*span+bg_width/2+(paint.descent()-paint.ascent())/2-paint.descent();
            float text_y = i*span+bg_width/2+(-paint.ascent()-paint.descent())/2;
            canvas.drawText(numbers[i], text_x, text_y, paint);
        }

        //画绿色大圆(上圆和下圆)
        btn_x = (this.getWidth()/scale_h - btn.getWidth())/2;
        y_u = getBtnYLocationByPrice(price_u);
        canvas.drawBitmap(btn, btn_x, y_u-btn.getHeight()/2, paint);
        y_d = getBtnYLocationByPrice(price_d);
        canvas.drawBitmap(btn, btn_x, y_d-btn.getHeight()/2, paint);
        //画绿色滑竿
        Rect src = new Rect(0, (int)(y_u+btn.getHeight()/2), green_bg.getWidth(), (int)(y_d-btn.getHeight()/2));
        Rect dst = new Rect((int)bg_x, (int)(y_u+btn.getHeight()/2), (int)(bg_x+green_bg.getWidth()), (int)(y_d-btn.getHeight()/2));
        canvas.drawBitmap(green_bg, src, dst, paint);
        //画左边的价格矩形(上下两个)
        Rect rect_u = getRectByMidLine(y_u);
        canvas.drawBitmap(num_price, null, rect_u, paint);
        Rect rect_d = getRectByMidLine(y_d);
        canvas.drawBitmap(num_price, null, rect_d, paint);
        //画左边的价格(上边价格的文本和下边的价格的文本)
        //上边文本坐标
        float text_u_x = (3*rect_u.width()/4 - paint.measureText(String.valueOf(price_u)))/2;
        float text_u_y = rect_u.top - paint.ascent() + PRICE_PADDING;
        //下边文字坐标
        float text_d_x = (3*rect_d.width()/4 - paint.measureText(String.valueOf(price_d)))/2;
        float text_d_y = rect_d.top - paint.ascent() + PRICE_PADDING;
        canvas.drawText(String.valueOf(price_u), text_u_x, text_u_y, paint);
        canvas.drawText(String.valueOf(price_d), text_d_x, text_d_y, paint);
        canvas.restore();//恢复之前保存的画板状态
        super.onDraw(canvas);
    }

    /**
     * 根据大圆的中线来确定坐标价格游标的矩形区域
     * @param y
     * @return
     */
    private Rect getRectByMidLine(float y) {
        Rect rect = new Rect();
        rect.left=0;
        rect.right = (int) btn_x;
        float text_h = paint.descent() - paint.ascent();
        rect.top = (int)(y-text_h/2) - PRICE_PADDING ;
        rect.bottom = (int)(y+text_h/2) + PRICE_PADDING ;
        return rect;
    }

    /**
     * 根据价格得到Y坐标
     * @param price
     * @return
     */
    private float getBtnYLocationByPrice(int price) {
        float y = 0;
        if(price<FIRST_STAGE){
            price = FIRST_STAGE;
        }
        if(price > FIFTH_STAGE){
            price = FIFTH_STAGE;
        }
        if(price>=FIRST_STAGE&&price<SECEND_STAGE){
            //0~200
            y =  bg_height - span*price/(SECEND_STAGE-FIRST_STAGE) -half_round;
        }else if(price>=SECEND_STAGE&&price<THIRD_STAGE){//200~500
            y =  bg_height - span*(price-SECEND_STAGE)/(THIRD_STAGE-SECEND_STAGE)-span -half_round;
        }else if(price>=THIRD_STAGE&&price<FOURTH_STAGE){//500~1000
            y = bg_height - span*(price-THIRD_STAGE)/(FOURTH_STAGE-THIRD_STAGE)-2*span -half_round;
        }else {//1000~10000
            y = span*(FIFTH_STAGE- price)/(FIFTH_STAGE-FOURTH_STAGE)+half_round;
        }
        Log.i(TAG , "price:"+price+"y坐标："+y);
        return y;
    }

    private boolean isDownTouched;
    private boolean isUpTouched;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 监听触摸Y--->price价格
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //按下，判断是否按到了大圆上面
                float x = event.getX()/scale_h;
                float y = event.getY()/scale_h;
                //x符合条件
                if(x>btn_x&&x<=btn_x+btn.getWidth()){
                    //y是否符合条件
                    //1.上价格
                    if(y>=y_u-btn.getHeight()/2&&y<=y_u+btn.getHeight()/2){
                        //符合条件，按到了
                        isUpTouched = true;
                        isDownTouched = false;
                    }
                    //2.下价格
                    if(y>=y_d-btn.getHeight()/2&&y<=y_d+btn.getHeight()/2){
                        //符合条件，按到了
                        isUpTouched = false;
                        isDownTouched = true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float y2 = event.getY();
                y2 = y2/scale_h;
                if(isUpTouched){
                    //根据坐标找价格
                    price_u = getPriceByPosition(y2);
                    if(price_u<price_d){
                        price_u = price_d;
                    }
                }
                if(isDownTouched){
                    price_d =getPriceByPosition(y2);
                    if(price_u<price_d){
                        price_d = price_u;
                    }
                }
                //刷新重绘
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isUpTouched = false;
                isDownTouched = false;
                break;

            default:
                break;
        }

        return true;
    }

    /**
     * 根据触摸的坐标转换成价格
     * @param y
     * @return
     */
    private int getPriceByPosition(float y) {

        float half_height = this.getHeight()*(1-REAL_PER)/2;
        int price;
        if(y<half_height){
            //y>10000
            price = FIFTH_STAGE;
        }else if(y>=half_height&&y<half_height+span){
            //1000~10000
            price = (int) (FIFTH_STAGE - (FIFTH_STAGE-FOURTH_STAGE)*(y-half_height)/span);
        }else if(y>half_height+span&&y<half_height+2*span){
            //500~1000
            price = (int) (FOURTH_STAGE - (FOURTH_STAGE-THIRD_STAGE)*(y-half_height-span)/span);
        }else if(y>half_height+2*span&&y<half_height+3*span){
            //200~500
            price = (int) (THIRD_STAGE - (THIRD_STAGE-SECEND_STAGE)*(y-half_height-2*span)/span);
        }else{
            //0~200
            price = (int) (SECEND_STAGE - (SECEND_STAGE-FIRST_STAGE)*(y-half_height-3*span)/span);
//			price = (int) ((SECEND_STAGE - FIRST_STAGE)*((this.getHeight()-y-half_height)/span));
        }
        if(price<FIRST_STAGE){
            price = FIRST_STAGE;
        }
        return price;
    }

}
