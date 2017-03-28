package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/9/20.
 */

public class LinkInfo {
    private String fTel;
    private String fCanDel;
    private String flinkman;

    public String getfTel() {
        return fTel;
    }

    public void setfTel(String fTel) {
        this.fTel = fTel;
    }

    public String getfCanDel() {
        return fCanDel;
    }

    public void setfCanDel(String fCanDel) {
        this.fCanDel = fCanDel;
    }

    public String getFlinkman() {
        return flinkman;
    }

    public void setFlinkman(String flinkman) {
        this.flinkman = flinkman;
    }

    public String getLinkId() {
        return LinkId;
    }

    public void setLinkId(String linkId) {
        LinkId = linkId;
    }

    private String LinkId;

}
