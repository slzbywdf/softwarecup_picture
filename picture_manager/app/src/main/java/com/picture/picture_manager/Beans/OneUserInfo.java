package com.picture.picture_manager.Beans;

/**
 * Created by 13307 on 2017/6/2.
 */

public class OneUserInfo {
    private String account;
    private String userName;
    private int iconIdx;
    private String email;
    private String major;
    private String interests;

    public OneUserInfo(String a, String u, int idx, String e, String m, String i) {
        account = a;
        userName = u;
        iconIdx = idx;
        email = e;
        major = m;
        interests = i;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public int getIconIdx() {
        return iconIdx;
    }

    public void setIconIdx(int iconIdx) {
        this.iconIdx = iconIdx;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
