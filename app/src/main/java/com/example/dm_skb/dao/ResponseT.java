package com.example.dm_skb.dao;

public interface ResponseT<T> {
	
	public void getT(T t);
	
	public void getError(String msg);
	
}
                                                  