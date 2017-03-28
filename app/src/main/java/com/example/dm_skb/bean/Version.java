package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/10/10.
 */

public class Version {
    private String Versions;

    public String getDescs() {
        return Descs;
    }

    public void setDescs(String descs) {
        Descs = descs;
    }

    public String getVersions() {
        return Versions;
    }

    public void setVersions(String versions) {
        Versions = versions;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    private String Descs;
    private String URL;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    private String Title;

}
