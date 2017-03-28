package com.example.dm_skb.bean;

/**
 * Created by yangxiaoping on 16/10/10.
 */

public class VersionJson <T>{
    private Meta meta;

    public Version getData() {
        return data;
    }

    public void setData(Version data) {
        this.data = data;
    }

    private Version data;


    public Meta getMeta(){
        return meta;
    }
    public void setMeta(Meta meta){
        this.meta = meta;
    }

    @Override
    public String toString(){
        return "Response [meta=" + meta + ", data=" + data + "]";
    }
}

