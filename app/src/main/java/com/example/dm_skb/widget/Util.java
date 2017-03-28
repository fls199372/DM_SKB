package com.example.dm_skb.widget;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class Util {

	  /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉 sd卡中创建一个目标文件
     * 
     * @param name
     * @return Author: 14052012 zyn Date: 2014年11月7日 下午3:10:35
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static String createSDCardDir(String name) {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String path = sdcardDir.getPath() + "/MUDOWN";
        File file = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                file = new File(dir + File.separator + name);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return file.getPath();
    }

    public static void installApk(String urlPath, Context context,
            NotificationManager mNotificationManager, int NOTIFYCATIONID) {
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(Intent.ACTION_VIEW);
        File apkFile = new File(urlPath);
        Log.i("jone", "apk length " + apkFile.length() + "");
        Uri uri = Uri.fromFile(apkFile);
        apkIntent
                .setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(apkIntent);
        System.out.println("执行到这里installApk");
        mNotificationManager.cancel(NOTIFYCATIONID);// 删除一个特定的通知ID对应的通知
    };

}
