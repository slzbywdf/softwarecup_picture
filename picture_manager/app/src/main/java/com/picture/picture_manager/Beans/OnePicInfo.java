package com.picture.picture_manager.Beans;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/4.
 */

public class OnePicInfo {
    private String picture_name;
    private String pass;
    private String finist_time;
    private String tag;
    private int isload;
    private boolean isflag;//用于判断复选框是否选中

    public OnePicInfo(String picture_name, String pass, String finist_time, String tag, int isload) {
        this.picture_name = picture_name;
        this.pass = pass;
        this.finist_time = finist_time;
        this.tag = tag;
        this.isload = isload;
    }

    public String getPicture_name() {
        return picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFinist_time() {
        return finist_time;
    }

    public void setFinist_time(String finist_time) {
        this.finist_time = finist_time;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getIsload() {
        return isload;
    }

    public void setIsload(int isload) {
        this.isload = isload;
    }

    public boolean isflag() {
        return isflag;
    }

    public void setIsflag(boolean isflag) {
        this.isflag = isflag;
    }
}
