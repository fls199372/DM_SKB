package com.example.dm_skb.bean;

public class UserEntity {
	public String getFuser_id() {
		return fuser_id;
	}

	public void setFuser_id(String fuser_id) {
		this.fuser_id = fuser_id;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String fuser_id;
	public String Password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String id;
	public String getFuser_name() {
		return fuser_name;
	}

	public void setFuser_name(String fuser_name) {
		this.fuser_name = fuser_name;
	}

	public String fuser_name;
	public UserEntity(String fuser_id,String Password,String fuser_name,String id)
	{
		super();
		this.fuser_id=fuser_id;
		this.fuser_name=fuser_name;
		this.Password=Password;
		this.id=id;

	}
}
