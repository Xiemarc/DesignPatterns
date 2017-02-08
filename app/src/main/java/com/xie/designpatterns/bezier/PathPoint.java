package com.xie.designpatterns.bezier;

/**
 * des:具体的路径集合
 * author: marc
 * date:  2017/2/8 11:31
 * email：aliali_ha@yeah.net
 */

public class PathPoint {
    //移动指令
    public static final int MOVE = 0;
    //直线运动
    public static final int LINE = 1;
    //贝塞尔曲线
    public static final int CURVE = 2;

    //当前指令
    int mOperation;
    float mX;
    float mY;
    float mControl0X, mControl1X;//2个拐点
    float mControl0Y, mControl1Y;

    private PathPoint(int operation, float x, float y) {
        mOperation = operation;
        mX = x;
        mY = y;
    }

    private PathPoint(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        mOperation = CURVE;
        //终点
        mX = x;
        mY = y;
        mControl0X = c0x;
        mControl0Y = c0y;

        mControl1X = c1x;
        mControl1Y = c1y;
    }


    /**
     * 移动
     *
     * @param x
     * @param y
     * @return
     */
    public static PathPoint moveTo(float x, float y) {
        return new PathPoint(MOVE, x, y);
    }

    /**
     * 直线
     *
     * @param x
     * @param y
     * @return
     */
    public static PathPoint lineTo(float x, float y) {
        return new PathPoint(LINE, x, y);
    }

    /**
     * 贝塞尔曲线
     *
     * @return
     */
    public static PathPoint curveTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        return new PathPoint(c0x, c0y, c1x, c1y, x, y);
    }
}
