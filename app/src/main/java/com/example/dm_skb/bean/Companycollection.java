package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class Companycollection {
    private String fAutoID;//表主键（自增长）
    private String fCompanyID;//公司Id

    public String getfAutoID() {
        return fAutoID;
    }

    public void setfAutoID(String fAutoID) {
        this.fAutoID = fAutoID;
    }

    public String getfCompanyID() {
        return fCompanyID;
    }

    public void setfCompanyID(String fCompanyID) {
        this.fCompanyID = fCompanyID;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getfCompany_Name() {
        return fCompany_Name;
    }

    public void setfCompany_Name(String fCompany_Name) {
        this.fCompany_Name = fCompany_Name;
    }

    private String rowId;//当前行
    private String fCompany_Name;//公司名称

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getfMyMemo() {
        return fMyMemo;
    }

    public void setfMyMemo(String fMyMemo) {
        this.fMyMemo = fMyMemo;
    }

    private String sTime;
    private String fMyMemo;
    private String fAlertsTime;

    public String getfAlertsMemo() {
        return fAlertsMemo;
    }

    public void setfAlertsMemo(String fAlertsMemo) {
        this.fAlertsMemo = fAlertsMemo;
    }

    public String getfAlertsTime() {
        return fAlertsTime;
    }

    public void setfAlertsTime(String fAlertsTime) {
        this.fAlertsTime = fAlertsTime;
    }

    private String fAlertsMemo;

    public String getfDialTime() {
        return fDialTime;
    }

    public void setfDialTime(String fDialTime) {
        this.fDialTime = fDialTime;
    }

    private String fDialTime;

}
