package com.example.dm_skb.bean;

public class RegisterJson<T>{
	private Meta meta;
	private Register data;
	
	public Register getData() {
		return data;
	}
	public void setData(Register data) {
		this.data = data;
	}
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

