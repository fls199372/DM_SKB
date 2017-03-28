package com.example.dm_skb.bean;

public class Meta{
	private String msg;
	private int code;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Meta [msg=" + msg + ", code=" + code+"]";
	}
}

