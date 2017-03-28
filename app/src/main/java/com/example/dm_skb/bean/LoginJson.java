package com.example.dm_skb.bean;

public class LoginJson<T>{
	private Meta meta;



	private Login data;

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Login getData() {
		return data;
	}

	public void setData(Login data) {
		this.data = data;
	}

	@Override
	public String toString(){
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

