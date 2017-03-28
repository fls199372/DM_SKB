package com.example.dm_skb.widget;

public class MessageItem {

	private  String  title;
	private String msgString;

	private  SideView mSideView;

	public MessageItem(String  title, SideView mSideView,String msgString)
	{
		this.msgString=msgString;
		this.mSideView=mSideView;
		this.title=title;
	}
	
	public MessageItem(String  title,String msgString)
	{
		this.msgString=msgString;
		this.title=title;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsgString() {
		return msgString;
	}

	public void setMsgString(String msgString) {
		this.msgString = msgString;
	}



	public SideView getmSideView() {
		return mSideView;
	}

	public void setmSideView(SideView mSideView) {
		this.mSideView = mSideView;
	}
    
}
