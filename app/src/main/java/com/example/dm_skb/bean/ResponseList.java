package com.example.dm_skb.bean;

import java.util.List;

public class ResponseList<T> {
	private Meta meta;
	private List<T> data;
	public Meta getMeta() {
		return meta;
	}
	public void setMeta(Meta meta) {
		this.meta = meta;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

