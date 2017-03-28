package com.example.dm_skb.ui.activity;



import android.content.Intent;
import android.os.Bundle;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
public class Appstart extends BaseActivity{
	private String device_token="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.appstart);
		Bundle bundle1=getIntent().getExtras();
		Common.device_token=bundle1.getString("device_token");
		UserID();
		if(Common.id.equals("1"))
		{
			Intent intent=new Intent(Appstart.this,StartActivity.class);
			Bundle bundle=new Bundle();
			bundle.putString("id", "0");
			intent.putExtras(bundle);
			startActivity(intent);
			Appstart.this.finish();

		}else {
			Intent intent = new Intent (Appstart.this,MainActivity.class);	//MainActivity
			Bundle bundle=new Bundle();;
			bundle.putString("device_token",""+device_token);
			intent.putExtras(bundle);
			startActivity(intent);
			Appstart.this.finish();
		}

	}
}