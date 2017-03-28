package com.example.dm_skb.bean;

public class ResponseJsonCustDetail<T>{
	private Meta meta;

	public CompanyDetail getData() {
		return data;
	}

	public void setData(CompanyDetail data) {
		this.data = data;
	}

	private CompanyDetail data;
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

