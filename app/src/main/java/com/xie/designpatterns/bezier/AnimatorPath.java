package com.xie.designpatterns.bezier;

import java.util.ArrayList;
import java.util.Collection;

/**
 * des:动画路径
 * author: marc
 * date:  2017/2/8 11:26
 * email：aliali_ha@yeah.net
 */

public class AnimatorPath {

    //存储路径集合
    ArrayList<PathPoint> mPoints = new ArrayList<>();

    /**
     * 移动到哪个位置
     *
     * @param x
     * @param y
     */
    public void moveTo(float x, float y) {
        mPoints.add(PathPoint.moveTo(x, y));
    }

    //直线
    public void lineTo(float x, float y) {
        mPoints.add(PathPoint.lineTo(x, y));
    }

    //贝塞尔曲线
    public void curveTo(float c0x, float c0y, float c1x, float c1y, float x, float y) {
        mPoints.add(PathPoint.curveTo(c0x, c0y, c1x, c1y, x, y));
    }

    public Collection<PathPoint> getPoints() {
        return mPoints;
    }

    public void clear() {
        if (mPoints != null && mPoints.size() > 0) {
            mPoints.clear();
        }
    }
}
