package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/29.
 */
//积分充值查询
public class IntegralList {
    private String Integral;
    private String dDate;

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
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

    private String CompanyName;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    private String ProductName;
}
