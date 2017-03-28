package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/19.
 */
import java.util.List;

public class CompanyDetail {
    private String fCompanyProfile;
    private String fEvaluateStarQty;
    private String fCheckDate;
    private String fAgentBrand;
    private String fMyMemo;
    private String fMarketingChannel;
    private String fCompany_Name;
    private String IsCollect;

    public String getfPrimaryCargo() {
        return fPrimaryCargo;
    }

    public void setfPrimaryCargo(String fPrimaryCargo) {
        this.fPrimaryCargo = fPrimaryCargo;
    }

    private String fPrimaryCargo;

    public String getIsCollect() {
        return IsCollect;
    }

    public void setIsCollect(String isCollect) {
        IsCollect = isCollect;
    }


    public String getIsEvaluate() {
        return IsEvaluate;
    }

    public void setIsEvaluate(String isEvaluate) {
        IsEvaluate = isEvaluate;
    }

    private String IsEvaluate;
    public String getfCompanyProfile() {
        return fCompanyProfile;
    }

    public void setfCompanyProfile(String fCompanyProfile) {
        this.fCompanyProfile = fCompanyProfile;
    }

    public String getfEvaluateStarQty() {
        return fEvaluateStarQty;
    }

    public void setfEvaluateStarQty(String fEvaluateStarQty) {
        this.fEvaluateStarQty = fEvaluateStarQty;
    }

    public String getfCheckDate() {
        return fCheckDate;
    }

    public void setfCheckDate(String fCheckDate) {
        this.fCheckDate = fCheckDate;
    }

    public String getfMyMemo() {
        return fMyMemo;
    }

    public void setfMyMemo(String fMyMemo) {
        this.fMyMemo = fMyMemo;
    }

    public String getfAgentBrand() {
        return fAgentBrand;
    }

    public void setfAgentBrand(String fAgentBrand) {
        this.fAgentBrand = fAgentBrand;
    }

    public String getfMarketingChannel() {
        return fMarketingChannel;
    }

    public void setfMarketingChannel(String fMarketingChannel) {
        this.fMarketingChannel = fMarketingChannel;
    }

    public String getfAddress() {
        return fAddress;
    }

    public void setfAddress(String fAddress) {
        this.fAddress = fAddress;
    }

    public String getfCompany_Name() {
        return fCompany_Name;
    }

    public void setfCompany_Name(String fCompany_Name) {
        this.fCompany_Name = fCompany_Name;
    }

    public String getfRegisteredAssets() {
        return fRegisteredAssets;
    }

    public void setfRegisteredAssets(String fRegisteredAssets) {
        this.fRegisteredAssets = fRegisteredAssets;
    }

    public String getfCompanyID() {
        return fCompanyID;
    }

    public void setfCompanyID(String fCompanyID) {
        this.fCompanyID = fCompanyID;
    }

    public String getfBrowseQty() {
        return fBrowseQty;
    }

    public void setfBrowseQty(String fBrowseQty) {
        this.fBrowseQty = fBrowseQty;
    }

    public String getfEvaluateQty() {
        return fEvaluateQty;
    }

    public void setfEvaluateQty(String fEvaluateQty) {
        this.fEvaluateQty = fEvaluateQty;
    }

    public String getfBusinessScope() {
        return fBusinessScope;
    }

    public void setfBusinessScope(String fBusinessScope) {
        this.fBusinessScope = fBusinessScope;
    }

    public String getfEmail() {
        return fEmail;
    }

    public void setfEmail(String fEmail) {
        this.fEmail = fEmail;
    }

    public String getfGPSX() {
        return fGPSX;
    }

    public void setfGPSX(String fGPSX) {
        this.fGPSX = fGPSX;
    }

    public String getfRegAddress() {
        return fRegAddress;
    }

    public void setfRegAddress(String fRegAddress) {
        this.fRegAddress = fRegAddress;
    }

    public String getfGPSY() {
        return fGPSY;
    }

    public void setfGPSY(String fGPSY) {
        this.fGPSY = fGPSY;
    }

    public List<LinkInfo> getLinkInfo() {
        return linkInfo;
    }

    public void setLinkInfo(List<LinkInfo> linkInfo) {
        this.linkInfo = linkInfo;
    }

    private String fAddress;
    private String fRegisteredAssets;
    private String fCompanyID;
    private String fBrowseQty;
    private String fBusinessScope;
    private String fEvaluateQty;
    private String fEmail;
    private String fRegAddress;
    private String fGPSX;
    private String fGPSY;
    private List<LinkInfo> linkInfo;

    public String getFfMarketingClassProperty() {
        return ffMarketingClassProperty;
    }

    public void setFfMarketingClassProperty(String ffMarketingClassProperty) {
        this.ffMarketingClassProperty = ffMarketingClassProperty;
    }

    private String ffMarketingClassProperty;

    public String getIsCorrection() {
        return IsCorrection;
    }

    public void setIsCorrection(String isCorrection) {
        IsCorrection = isCorrection;
    }

    private String IsCorrection;

}
