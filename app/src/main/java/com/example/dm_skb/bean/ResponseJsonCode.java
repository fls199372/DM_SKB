package com.example.dm_skb.bean;

public class ResponseJsonCode<T>{
	private Meta meta;
	private VerificationCode data;
	
	public VerificationCode getData() {
		return data;
	}
	public void setData(VerificationCode data) {
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

