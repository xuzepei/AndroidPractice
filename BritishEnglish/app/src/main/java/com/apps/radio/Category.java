package com.apps.radio;

/**
 * Created by xuzepei on 2018/2/9.
 */

public class Category {

    private String title;
    private String subtitle;
    private int imageResID;
    private String cateID;

    public Category(String title, String subtitle, int resID, String cateID) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageResID = resID;
        this.cateID = cateID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getImageResID() {
        return imageResID;
    }

    public void setImageResID(int imageResID) {
        this.imageResID = imageResID;
    }

    public String getCateID() {
        return cateID;
    }

    public void setCateID(String cateID) {
        this.cateID = cateID;
    }

}
