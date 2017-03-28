package com.example.dm_skb.bean;

public class IntegralJson<T>{
	private Meta meta;

	public Integral getData() {
		return data;
	}

	public void setData(Integral data) {
		this.data = data;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	private Integral data;
	@Override
	public String toString(){
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

