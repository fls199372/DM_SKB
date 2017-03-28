package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/10/8.
 */

public class ProductList {
    private String Score;
    private String ProductName;
    private String ProductId;
    private String PicUrl;

    public String getCgClassName() {
        return CgClassName;
    }

    public void setCgClassName(String cgClassName) {
        CgClassName = cgClassName;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    private String CgClassName;

}
