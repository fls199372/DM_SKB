package com.example.dm_skb.bean;

public class RegisterJson1<T>{
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Register1 getData() {
		return data;
	}

	public void setData(Register1 data) {
		this.data = data;
	}

	private Meta meta;
	private Register1 data;
	@Override
	public String toString(){
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

