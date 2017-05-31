package com.xie.designpatterns.chinamap;

/**
 * 省份实体数据
 * Created by marc on 2017/5/4.
 */

public class ProvinceData implements IProvinceData {
    /**
     * 省份id
     */
    private int provinceId;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 该省内人数
     */
    private int number;


    @Override
    public int getPersonNumber() {
        return number;
    }

    @Override
    public int getProvinceCode() {
        return provinceId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String toString() {
        return "ProvinceData{" +
                "provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", number=" + number +
                '}';
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
