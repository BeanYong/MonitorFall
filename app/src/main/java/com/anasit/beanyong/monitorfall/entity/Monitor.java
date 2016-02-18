package com.anasit.beanyong.monitorfall.entity;

/**
 * Created by BeanYong on 2015/12/17.
 * 监视器对象
 */
public class Monitor {
    /**
     * 报警器id
     */
    private int id;
    /**
     * 报警器号码
     */
    private String tel;
    /**
     * 密码
     */
    private String psw;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;

    public Monitor(int id, String tel, String psw, double latitude, double longitude) {
        this.id = id;
        this.tel = tel;
        this.psw = psw;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 设置报警器号码
     *
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获取报警器号码
     *
     * @return
     */
    public String getTel() {
        return tel;
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPsw() {
        return psw;
    }

    /**
     * 设置密码
     *
     * @param psw
     */
    public void setPsw(String psw) {
        this.psw = psw;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Monitor{" +
                "id=" + id +
                ", tel='" + tel + '\'' +
                ", psw='" + psw + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
