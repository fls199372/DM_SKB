package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/11/17.
 */

public class MyNewsList {
    private String Title;
    private String SubTitle;
    private String Meat;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getMeat() {
        return Meat;
    }

    public void setMeat(String meat) {
        Meat = meat;
    }

    public String getIsDetail() {
        return IsDetail;
    }

    public void setIsDetail(String isDetail) {
        IsDetail = isDetail;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getCheckDate() {
        return CheckDate;
    }

    public void setCheckDate(String checkDate) {
        CheckDate = checkDate;
    }

    private String IsDetail;
    private String Details;
    private String CheckDate;
}
