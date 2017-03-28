package com.example.dm_skb.ui.activity.main;


import com.hanvon.HWCloudManager;

import com.hanvon.utils.BitmapUtil;
import com.hanvon.utils.ConnectionDetector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dm_skb.R;
import com.example.dm_skb.ui.base.BaseActivity;
/**
 * 示例说明：
 * 1、运行demo前，请到汉王开发者中心注册并申请云手写公式识别服务,在key管理页面创建android_key，并修改替换  your_android_key
 * 2、本demo基于汉王云识别，需要在联网的情况下运行
 * 3、云识别的返回结果是json形式，具体请参照开发文档
 */
public class MainActivity extends BaseActivity {
	
	private Button button1;
	private Button button2;
	private ImageView iv_image;  
	private TextView testView;
	private ProgressDialog pd;
	private DiscernHandler discernHandler;
	
	String picPath = null;
	String result = null;
	private HWCloudManager hwCloudManagerBcard; //鍚嶇墖
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main5);
		

		/**
		 * your_android_key  是您在开发者中心申请的android_key 并 申请了云身份证识别服务
		 * 开发者中心：http://developer.hanvon.com/
		 */
		hwCloudManagerBcard = new HWCloudManager(this, "your_android_key");
		
		
		
		discernHandler = new DiscernHandler();
		
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		iv_image = (ImageView) findViewById(R.id.iv_image);
		testView = (TextView) findViewById(R.id.result);
		
		button1.setOnClickListener(listener);
		button2.setOnClickListener(listener);
	}
	
	OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            case R.id.button1:
            	// 激活系统图库，选择一张图片
            	Intent intent = new Intent();
            	intent.setAction(Intent.ACTION_PICK);
            	intent.setType("image/*");
            	startActivityForResult(intent, 0);
            	break;
            	
            case R.id.button2:
            	//识别
            	testView.setText("");
            	ConnectionDetector connectionDetector = new ConnectionDetector(getApplication());
        		if (connectionDetector.isConnectingTOInternet()) {
        			if (null != picPath) {
        				pd = ProgressDialog.show(MainActivity.this, "", "正在识别请稍后......");
                    	DiscernThread discernThread = new DiscernThread();
                    	new Thread(discernThread).start();
        			} else {
    					Toast.makeText(getApplication(), "请选择图片后再试", Toast.LENGTH_LONG).show();
    				}
        		} else {
        			Toast.makeText(getApplication(), "网络连接失败，请检查网络后重试！", Toast.LENGTH_LONG).show();
        		}
            	
            	
            	break;
            }
        }
	};
	
	public class DiscernThread implements Runnable{

		@Override
		public void run() {
			try {
				/**
				 * 调用汉王云名片识别方法
				 */
				result = hwCloudManagerBcard.cardLanguage("chns", picPath);
//				result = hwCloudManagerBcard.cardLanguage4Https("chns", picPath); 
			} catch (Exception e) {
				// TODO: handle exception
			}
			Bundle mBundle = new Bundle();
			mBundle.putString("responce", result);
			Message msg = new Message();
			msg.setData(mBundle);
			discernHandler.sendMessage(msg);
		}
	}
	
	public class DiscernHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pd.dismiss();
			Bundle bundle = msg.getData();
			String responce = bundle.getString("responce");
			testView.setText(responce);
		}
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (data != null) {  
            Uri uri = data.getData();
            //
            String[] proj = { MediaStore.Images.Media.DATA };
        	Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        	int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        	cursor.moveToFirst();
        	picPath = cursor.getString(column_index);
        	System.out.println(picPath);
        	
        	final BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(picPath, options);
		    options.inSampleSize = BitmapUtil.calculateInSampleSize(options, 1280, 720);
		    options.inJustDecodeBounds = false;
		    Bitmap bitmap = BitmapFactory.decodeFile(picPath, options);
        	iv_image.setImageBitmap(bitmap);
        }  
        super.onActivityResult(requestCode, resultCode, data);  
    }
	
}
