package com.example.dm_skb.bean;

public class RegisterJson2<T>{
	private Meta meta;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	private String data;

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

