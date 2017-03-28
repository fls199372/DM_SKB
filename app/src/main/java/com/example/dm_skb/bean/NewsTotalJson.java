package com.example.dm_skb.bean;

public class NewsTotalJson<T>{
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}



	private Meta meta;

	public NewsTotal getData() {
		return data;
	}

	public void setData(NewsTotal data) {
		this.data = data;
	}

	private NewsTotal data;
	@Override
	public String toString(){
		return "Response [meta=" + meta + ", data=" + data + "]";
	}	
}

