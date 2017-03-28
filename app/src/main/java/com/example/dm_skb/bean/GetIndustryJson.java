package com.example.dm_skb.bean;

public class GetIndustryJson<T>{
	private Meta meta;

	public GetIndustry getData() {
		return data;
	}

	public void setData(GetIndustry data) {
		this.data = data;
	}

	private GetIndustry data;
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

