package com.example.dm_skb.bean;

import java.util.List;

public class ResponseJson<T>{
	private Meta meta;
	private KanBan data;
	public Meta getMeta(){
		return meta;
	}
	public void setMeta(Meta meta){
		this.meta = meta;
	}
	public KanBan getData(){
		return data;
	}
	public void setData(KanBan data){
		this.data = data;
	}
	@Override
	public String toString(){
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

