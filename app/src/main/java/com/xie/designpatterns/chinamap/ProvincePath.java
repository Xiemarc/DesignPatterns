package com.xie.designpatterns.chinamap;

import android.graphics.Path;

/**
 * 绘制省份信息的路径封装类
 * Created by marc on 2017/5/4.
 */

public class ProvincePath {
    /**
     * 省份绘制路径
     */
    private Path path;
    /**
     * 省份编码
     */
    private int code;
    /**
     * 省份名称
     */
    private String name;

    public ProvincePath(int code, String name, String pathdata) {
        this.code = code;
        this.name = name;
        this.path = PathParser.createPathFromPathData(pathdata);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProvincePath that = (ProvincePath) o;
        if (code != that.code)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProvincePath{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }
}
