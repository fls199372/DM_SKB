package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/29.
 */

public class RechargeQusery {
    private String Integral;

    public String getFeeMoney() {
        return FeeMoney;
    }

    public void setFeeMoney(String feeMoney) {
        FeeMoney = feeMoney;
    }

    public String getdDate() {
        return dDate;
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public String getIntegral() {
        return Integral;
    }

    public void setIntegral(String integral) {
        Integral = integral;
    }

    private String FeeMoney;
    private String dDate;
    private String TypeName;

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getOutScore() {
        return OutScore;
    }

    public void setOutScore(String outScore) {
        OutScore = outScore;
    }

    public String getInScore() {
        return InScore;
    }

    public void setInScore(String inScore) {
        InScore = inScore;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    private String  Balance;
    private String BeginScore;

    public String getEndScore() {
        return EndScore;
    }

    public void setEndScore(String endScore) {
        EndScore = endScore;
    }

    public String getBeginScore() {
        return BeginScore;
    }

    public void setBeginScore(String beginScore) {
        BeginScore = beginScore;
    }

    private String EndScore;
    private String OutScore;
    private String InScore;

}
