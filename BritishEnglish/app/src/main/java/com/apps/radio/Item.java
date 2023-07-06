package com.apps.radio;

/**
 * Created by xuzepei on 2018/2/14.
 */

public class Item {

    private String typeId = "";
    private String title = "";
    private String desc = "";
    private String imageUrl = "";
    private Boolean isHidden = false;
    private String timestamp = "";
    private String mp3Url = "";
    private String url = "";
    private String pdfUrl = "";
    private String text = "";
    private String idString = "";
    private Boolean isFavorite = false;
    private Boolean isRead = false;
    private int downloadStatus = -1;

    public Item(String typeId, String title, String desc, String imageUrl, String timestamp, String mp3Url, String url, String pdfUrl, String text, String idString) {
        this.typeId = typeId;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.timestamp = timestamp;
        this.mp3Url = mp3Url;
        this.url = url;
        this.pdfUrl = pdfUrl;
        this.text = text;
        this.idString = idString;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getHidden() {
        return isHidden;
    }

    public void setHidden(Boolean hidden) {
        isHidden = hidden;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }



}
