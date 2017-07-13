package com.picture.picture_manager.Beans;

/**
 * Created by 13307 on 2017/5/3.
 */

public class OnePic {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRecommends() {
        return recommends;
    }

    public void setRecommends(String recommends) {
        this.recommends = recommends;
    }

    private String id;
    private String path;
    private String recommends;

    public OnePic(String a, String b, String c) {
        id = a;
        path = b;
        recommends = c;
    }
}
