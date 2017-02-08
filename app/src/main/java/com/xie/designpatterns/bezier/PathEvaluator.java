package com.xie.designpatterns.bezier;

import android.animation.TypeEvaluator;

/**
 * des:自定义估值器
 * author: marc
 * date:  2017/2/8 13:06
 * email：aliali_ha@yeah.net
 */

public class PathEvaluator implements TypeEvaluator<PathPoint> {

    /**
     * @param t          动画执行的百分比 ,其实就是时间
     * @param startValue
     * @param endValue
     * @return
     */
    @Override
    public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
        //进行估值
        float x, y;
        //判断进行哪种运动
        if (endValue.mOperation == PathPoint.CURVE) {
            //贝塞尔曲线方式
            float oneMinusT = 1 - t;
            //x的实时坐标
            x = oneMinusT * oneMinusT * oneMinusT * startValue.mX +
                    3 * oneMinusT * oneMinusT * t * endValue.mControl0X +
                    3 * oneMinusT * t * t * endValue.mControl1X +
                    t * t * t * endValue.mX;
            //y的实时坐标
            y = oneMinusT * oneMinusT * oneMinusT * startValue.mY +
                    3 * oneMinusT * oneMinusT * t * endValue.mControl0Y +
                    3 * oneMinusT * t * t * endValue.mControl1Y +
                    t * t * t * endValue.mY;
        } else if (endValue.mOperation == PathPoint.LINE) {
            //直线运动方式
            //当前坐标点（x,y） = 起始点 +t*起始点和终点的距离
            x = startValue.mX + t * (endValue.mX - startValue.mX);
            y = startValue.mY + t * (endValue.mY - startValue.mY);
        } else {
            //moveto方式
            x = endValue.mX;
            y = endValue.mY;
        }
        //不断的把控制点 move到 x,y的位置
        return PathPoint.moveTo(x, y);
    }
}
