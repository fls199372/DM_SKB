package com.example.dm_skb.ui.activity;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.example.dm_skb.R;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.example.dm_skb.ui.base.BaseApplication;
import com.umeng.common.UmengMessageDeviceConfig;
import com.umeng.message.IUmengCallback;
import com.umeng.message.MessageSharedPrefs;
import android.os.Build;
/**
 * 欢迎界面
 * 
 * @author Administrator
 * 
 */
public class Welcome_News_Activity extends BaseActivity {
	// 是否第一次使用
	private Boolean isFirstUse;
	String device_token="";
	private PushAgent mPushAgent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		MobclickAgent.setDebugMode(true);
		MobclickAgent.setCatchUncaughtExceptions(true);

		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

		UserID();
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
			}
			@Override
			public void onFailure(String s, String s1) {
			}
		});

		mPushAgent.onAppStart();
		mPushAgent.enable(mEnableCallback);
		MessageSharedPrefs.getInstance(Welcome_News_Activity.this).setIsEnabled(true);
		((BaseApplication) Welcome_News_Activity.this.getApplication()).setPush_status(true);
		updateStatus();
		device_token=mPushAgent.getRegistrationId();
		//开启推送并设置注册的回调处理

		if (((BaseApplication) this.getApplication()).isPush_status()) {
			//开启推送并设置开启的回调处理
		}
		//new Thread(this).start();
		try {
			//读取ShareedPreferences中需要的数据
			SharedPreferences preferences = getSharedPreferences("isFirstUse",
					MODE_WORLD_READABLE);
			isFirstUse=preferences.getBoolean("isFirstUse", true);
			/**
			 * 如果用户不是第一次使用则直接跳转到显示界面，否�?就跳转到引导界面
			 */
			if (isFirstUse) {
				//用户是第一次进来
				Intent intent=new Intent(Welcome_News_Activity.this,Guide_News_Activity.class);
				Bundle bundle=new Bundle();
				bundle.putString("device_token",""+device_token);
				intent.putExtras(bundle);
				startActivity(intent);
				Welcome_News_Activity.this.finish();

			}else {
				if(Common.id.equals("1"))
				{
					Intent intent=new Intent(Welcome_News_Activity.this,StartActivity.class);
					Bundle bundle=new Bundle();
					bundle.putString("id", "0");
					intent.putExtras(bundle);
					startActivity(intent);
					Welcome_News_Activity.this.finish();
				}else {
					Intent intent = new Intent (Welcome_News_Activity.this,MainActivity.class);	//MainActivity
					Bundle bundle=new Bundle();;
					bundle.putString("device_token",""+device_token);
					intent.putExtras(bundle);
					startActivity(intent);
					Welcome_News_Activity.this.finish();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Handler handler = new Handler();
	protected static final String TAG = MainActivity.class.getSimpleName();
	private void updateStatus() {
		String pkgName = getApplicationContext().getPackageName();
		String info = String.format("enabled:%s\nDeviceToken:%s\n" +
						"SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
				((BaseApplication) this.getApplication()).isPush_status(),
				mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
				UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));

		copyToClipBoard();

		Log.i(TAG, "updateStatus:" + String.format("enabled:%s",
				((BaseApplication) this.getApplication()).isPush_status()));
		Log.i(TAG, "=============================");
	}
	@SuppressLint("NewApi")
	private void copyToClipBoard() {
		if (Build.VERSION.SDK_INT < 11)
			return;
		String deviceToken = mPushAgent.getRegistrationId();
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	class MyReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			updateStatus();
		}
	}
	//此处是开启的回调处理
	public IUmengCallback mEnableCallback = new IUmengCallback() {

		@Override
		public void onSuccess() {
			MessageSharedPrefs.getInstance(Welcome_News_Activity.this).setIsEnabled(true);
			((BaseApplication) Welcome_News_Activity.this.getApplication()).setPush_status(true);

			handler.post(new Runnable() {

				@Override
				public void run() {
					updateStatus();
				}
			});
		}

		@Override
		public void onFailure(String s, String s1) {

		}
	};
}
