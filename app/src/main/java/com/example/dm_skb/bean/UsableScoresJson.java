package com.example.dm_skb.bean;

public class UsableScoresJson<T>{
	private Meta meta;

	public UserScore getData() {
		return data;
	}

	public void setData(UserScore data) {
		this.data = data;
	}

	private UserScore data;
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

