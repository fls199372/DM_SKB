package com.example.dm_skb.ui.base;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import com.example.dm_skb.ui.activity.news.NewsDetailedActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.content.Intent;
import com.baidu.mapapi.SDKInitializer;
import com.example.dm_skb.R;
import com.example.dm_skb.ui.service.LocationService;
import com.example.dm_skb.util.Common;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.example.dm_skb.ui.activity.searchcustomer.CustomerDetailActivity;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import java.util.LinkedList;
import android.content.SharedPreferences;
import com.umeng.message.IUmengRegisterCallback;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.util.OkHttpClientManager.HttpsDelegate;
import java.io.IOException;
public final class BaseApplication extends Application {

    private static BaseApplication mApplication = new BaseApplication();
    private static BaseApplication mInstance = null;
    private static final String TAG = BaseApplication.class.getName();
    private PushAgent mPushAgent;
    public LocationService locationService;
    private SharedPreferences tempSharedPreference;

    public static BaseApplication GetInstance() {
        return mApplication;
    }
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(getApplicationContext());
        tempSharedPreference = getSharedPreferences("push_status", Context.MODE_PRIVATE);
        try
        {
            OkHttpClientManager.getInstance()
                    .setCertificates(getAssets().open("dingmou.cer"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try{
            SDKInitializer.initialize(getApplicationContext());

        }catch (Exception e)
        {
            e.printStackTrace();
        }


        mInstance = this;
        initImageLoader(getApplicationContext());
        //微信
        PlatformConfig.setWeixin("wx9d1f12ac49b48386", "e9452bd4fe511362c6fff0e959eda4ad");
        //新浪微博
        //PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");

        PlatformConfig.setSinaWeibo("2620994668", "a72657d8dab6b7067fab77ecf21fe0a4");
       // handlermController.getConfig().setSsoHandler(new SinaSsoHandler());
        //QQ  1105580603  nQaKAVhD9g1cMy4D
        PlatformConfig.setQQZone("1105580603", "nQaKAVhD9g1cMy4D");
        mPushAgent = PushAgent.getInstance(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        mPushAgent.setDebugMode(true);
        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            /**
             *参考集成文档的1.6.3
             *http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                    }
                });
            }
            /**
             * 参考集成文档的1.6.4  自定义消息处理
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                String xx=msg.builder_id+"";

                switch (msg.builder_id) {
                    case 1:

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        //myNotificationView.setImageViewBitmap(R.id.notification_large_icon,R.drawable.push);
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title)
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return super.getNotification(context, msg);
                    default:

                        return super.getNotification(context, msg);
                }
            }
        };

        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                JSONObject json;
                String companyId="";
                String type="";
                try {
                    json = new JSONObject(msg.custom);
                    type= json.getString("MsgType");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(type.equals("0"))
                {
                    try {
                        json = new JSONObject(msg.custom);
                        companyId= json.getString("companyId");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Intent intent1 = new Intent();
                    intent1.setClass(context,CustomerDetailActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("fcust_id",""+companyId);
                    bundle.putString("position","");
                    bundle.putString("fcust_name","");
                    bundle.putString("activity","");
                    intent1.putExtras(bundle);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent1);
                }else if(type.equals("1"))
                {
                    String NewsId="";
                    String Theme="";
                    String fUser_ID="";

                    try {
                        json = new JSONObject(msg.custom);
                        NewsId= json.getString("NewsId");
                        Theme= json.getString("Theme");
                       Common.fuser_id= json.getString("fUser_ID");

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Intent intent1 = new Intent();
                    intent1.setClass(context,NewsDetailedActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("id",""+NewsId);
                    bundle.putString("name",""+Theme);
                    intent1.putExtras(bundle);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(intent1);
                }
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();

        mPushAgent.setNotificationClickHandler(notificationClickHandler);

//        if (MiPushRegistar.checkDevice(this)) {
//            MiPushRegistar.register(this, "2882303761517400865", "5501740053865");
//        }
    }
    private void Dialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.visit_dialog);
        Window dialogWindow = dlg.getWindow();
        /*WindowManager m = this.get;
        Display d = m.getDefaultDisplay(); */// 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (getScreenHeight() * 0.6); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (getScreenWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();

        Button ok=(Button)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
    public static BaseApplication getInstance() {
        return mInstance;
    }
    /** 使用集合类统一理Activity实例 */
    private LinkedList<Activity> activityList = new LinkedList<Activity>();
    /** 设备的宽 */
    private int screenWidth = 0;
    /** 设备的高 */
    private int screenHeight = 0;

    /**
     * 设置屏幕的高在程序的第一个界面的oncreate方法调用设置
     *
     * @param screenWidth
     *            设备的宽
     * @param screenHeight
     *            设备的高
     */
    public void SetScreen(int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    /** 得到屏幕的宽 */
    public int getScreenWidth() {
        return screenWidth;
    }

    /** 得到屏幕的高 */
    public int getScreenHeight() {
        return screenHeight;
    }

    /** 添加Activity到容器，在每个Activity的onCreate调用 */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /** 从Activity到容器中移除，在每个Activity的onDestroy调用 */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /** 遍历Activity并finish，在主界面当点击back键或者在要退出程序的时候调用 */
    public void exit() {
        // 遍历所有存在的Activity实例，挨个finish
        for (Activity activity : activityList) {
            if (activity != null)
                activity.finish();
        }
        android.os.Process.killProcess(android.os.Process.myPid());// 获取当前进程PID，并�?��
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    public boolean isPush_status() {
        return tempSharedPreference.getBoolean("status", true);
    }

    public void setPush_status(boolean push_status) {
        tempSharedPreference.edit().putBoolean("status", push_status).commit();
    }
}
