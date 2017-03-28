package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/12/1.
 */

public class MySeeCompanyList {
    private String ViewTime;
    private String fCompany_Name;

    public void setViewTime(String viewTime) {
        ViewTime = viewTime;
    }

    public void setViewNumber(String viewNumber) {
        ViewNumber = viewNumber;
    }

    public void setfCompany_Name(String fCompany_Name) {
        this.fCompany_Name = fCompany_Name;
    }

    public void setfCompanyId(String fCompanyId) {
        this.fCompanyId = fCompanyId;
    }

    public String getViewTime() {
        return ViewTime;
    }

    public String getfCompany_Name() {
        return fCompany_Name;
    }

    public String getViewNumber() {
        return ViewNumber;
    }

    public String getfCompanyId() {
        return fCompanyId;
    }

    private String ViewNumber;
    private String fCompanyId;

}
