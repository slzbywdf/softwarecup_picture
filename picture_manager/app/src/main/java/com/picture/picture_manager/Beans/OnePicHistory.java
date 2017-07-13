package com.picture.picture_manager.Beans;

/**
 * Created by 13307 on 2017/4/16.
 */

public class OnePicHistory {
    String id;
    String path;
    String label;
    String isClickable;
    String recommends;

    public String getRecommends() {
        return recommends;
    }

    public void setRecommends(String recommends) {
        this.recommends = recommends;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OnePicHistory(String idd, String p, String l, String i, String r) {
        id = idd;
        path = p;
        label = l;
        isClickable = i;
        recommends = r;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIsClickable() {
        return isClickable;
    }

    public void setIsClickable(String isClickable) {
        this.isClickable = isClickable;
    }
}
