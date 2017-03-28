package com.example.dm_skb.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.dm_skb.widget.EppNotificationControl;
import com.thin.downloadmanager.DownloadManager;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadRequest.Priority;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

public class MyService extends IntentService {

    private ThinDownloadManager downloadManager;
    String urlPath;
    // 下载的线程数
    private static final int DOWNLOAD_THREAD_POOL_SIZE = 4;
    /**
     * 服务端apk路径
     */
    private static String FILE1 = "http://shdingmou.cn/AndroidVersion/Sals_Client.APK";
    MyDownloadListner myDownloadStatusListener = new MyDownloadListner();
    int downloadId1 = 0;
    private DownloadRequest request1;

    EppNotificationControl notificationControl;

    public MyService() {
        super("MyService");
    }

    Context context;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        context = this;
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    		try{
    			urlPath = com.example.dm_skb.widget.Util.createSDCardDir("DMSKB.apk");
    			FILE1=intent.getStringExtra("student_id");
    			notificationControl = new EppNotificationControl(urlPath, context);
    			downloadManager = new ThinDownloadManager(DOWNLOAD_THREAD_POOL_SIZE);
    			initDownload();
    			if (downloadManager.query(downloadId1) == DownloadManager.STATUS_NOT_FOUND) {
    				downloadId1 = downloadManager.add(request1);
    			}
    			notificationControl.showProgressNotify();
    		}catch(Exception e)
    		{
				Toast.makeText(getApplicationContext(), "内存没有了!", Toast.LENGTH_LONG).show();

    		}

    }

    private void initDownload() {
        Uri downloadUri = Uri.parse(FILE1);
        Uri destinationUri = Uri.parse(urlPath);
        request1 = new DownloadRequest(downloadUri)
                .setDestinationURI(destinationUri).setPriority(Priority.HIGH)
                .setDownloadListener(myDownloadStatusListener);

    }

    class MyDownloadListner implements DownloadStatusListener {

        public void onDownloadComplete(int id) {
            if (id == downloadId1) {
            	
                Log.i("jone", "download completed");
            }
          /*  if (id == downloadId1) {  
                mProgress1Txt.setText("Download1 id: " + id  
                        + " Failed: ErrorCode " + errorCode + ", "  
                        + errorMessage);  
                mProgress1.setProgress(0);  
            }  */
        }

        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
        	System.out.println("GGGGGGGGGGGGGGG"+downloadId1);
            if (id == downloadId1) {
                notificationControl. mNotificationManager.cancel(1001);
                Log.i("jone", "DownloadFailed" + errorMessage);
            }
        }

        public void onProgress(int id, long totalBytes, long downloadedBytes,
                int progress) {
            if (id == downloadId1) {
                Log.i("jone", progress + "");
                notificationControl.updateNotification(progress);
            }

        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i("jone", "onDestroy");
        super.onDestroy();
    }
}
