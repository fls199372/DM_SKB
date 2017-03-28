package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/19.
 */

public class Companylist {
    public String getEvaluateStarQty() {
        return EvaluateStarQty;
    }

    public void setEvaluateStarQty(String evaluateStarQty) {
        EvaluateStarQty = evaluateStarQty;
    }

    private String EvaluateStarQty;//评价星数
    private String fcustname;//客户名称



    public String getFcustname() {
        return fcustname;
    }

    public void setFcustname(String fcustname) {
        this.fcustname = fcustname;
    }

    public String getsDistance() {
        return sDistance;
    }

    public void setsDistance(String sDistance) {
        this.sDistance = sDistance;
    }

    public String getIsSee() {
        return IsSee;
    }

    public void setIsSee(String isSee) {
        IsSee = isSee;
    }

    public String getFcustid() {
        return fcustid;
    }

    public void setFcustid(String fcustid) {
        this.fcustid = fcustid;
    }

    private String sDistance;//距离
    private String IsSee;//当前用户是否查看过   0 未看过  1 已看过
    private String fcustid;//客户ID
}
