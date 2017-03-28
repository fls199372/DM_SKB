package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/11/16.
 */

public class MyNewsType {
    private String NewsTypeId;
    private String Picture;

    public String getNewsTypeId() {
        return NewsTypeId;
    }

    public void setNewsTypeId(String newsTypeId) {
        NewsTypeId = newsTypeId;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getNewsTypeName() {
        return NewsTypeName;
    }

    public void setNewsTypeName(String newsTypeName) {
        NewsTypeName = newsTypeName;
    }

    private String Total;
    private String NewsTypeName;

}
