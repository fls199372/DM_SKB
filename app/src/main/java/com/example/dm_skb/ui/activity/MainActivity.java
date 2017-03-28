package com.example.dm_skb.ui.activity;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.bean.Version;
import com.example.dm_skb.bean.VersionJson;
import com.example.dm_skb.ui.base.BaseApplication;
import com.example.dm_skb.util.MyService;
import com.umeng.common.UmengMessageDeviceConfig;
import com.umeng.message.IUmengCallback;
import com.umeng.message.MessageSharedPrefs;
import com.umeng.socialize.view.UMFriendListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Login;
import com.example.dm_skb.bean.LoginJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.dao.UserLoginDao;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.activity.registrationorpassword.ForgetPasswordActivity;
import com.example.dm_skb.ui.activity.registrationorpassword.LRegistrationactivity;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.ui.service.LocationService;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.squareup.okhttp.Request;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.message.IUmengRegisterCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 第三方登录功能
 *
 */
public class MainActivity extends BaseActivity implements ResponseT<LoginJson<Login>> {
    private TextView mUserInfo;
    private ImageView mUserLogo;
    private Button mNewLoginButton;
    private TextView backInfo;
    private UMShareAPI mShareAPI = null;
    Dialog dialog;
    EditText User_id;
    EditText Password;
    // 申请的id
    public Button WX;
    String device_token="";
    private PushAgent mPushAgent;
    UserLoginDao userLoginDao;
    UserAdapter userAdapter;
    public LocationService locationService;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    ImageView Sina;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        UserID();
        getPersimmions();
        User_id=(EditText) findViewById(R.id.User_id);
        User_id.setText(""+Common.fuser_id);
        Password=(EditText) findViewById(R.id.Password);
        Password.setText(""+Common.fuser_pwd);
        mShareAPI = UMShareAPI.get(this);
        DisplayMetrics dm =getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        PushAgent.getInstance(MainActivity.this).onAppStart();
        mPushAgent = PushAgent.getInstance(MainActivity.this);
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
        int xx=px2dip(this,340);
        userLoginDao=new UserLoginDao();
        //开启推送并设置注册的回调处理
        mPushAgent.enable(mEnableCallback);
        Sina=(ImageView)findViewById(R.id.Sina);
        userAdapter=new UserAdapter(this);
        updateStatus();

        device_token=mPushAgent.getRegistrationId();
        Common.device_token=device_token;
        mLocationClient =new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        SDKInitializer.initialize(this);
        initLocation();//注册监听函数
        mLocationClient.start();
        try{
            submit(getVersionCode());
        }catch (Exception e)
        {

        }
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=3000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    //此处是开启的回调处理
    public IUmengCallback mEnableCallback = new IUmengCallback() {

        @Override
        public void onSuccess() {
            MessageSharedPrefs.getInstance(MainActivity.this).setIsEnabled(true);
            ((BaseApplication) MainActivity.this.getApplication()).setPush_status(true);

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
    //此处是注册的回调处理
    //参考集成文档的1.7.10
    //http://dev.umeng.com/push/android/integration#1_7_10
  /*  public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    updateStatus();
                }
            });
        }
    };*/
    public Handler handler = new Handler();

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
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
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.QQ){
            platform= SHARE_MEDIA.QQ;
        }
        else if(view.getId()==R.id.WEIXIN)
        {
            platform= SHARE_MEDIA.WEIXIN;

        }else if(R.id.Sina==view.getId())
        {
            platform= SHARE_MEDIA.SINA;

        }else if (view.getId()==R.id.Login)
        {
            if(User_id.getText().toString().length()==0)
            {
                postmainHandler("手机号码不能为空");
                return;
            }
            if(!isPhoneNumberValid(User_id.getText().toString()))
            {
                postmainHandler("请录入正确的手机号");
                return;
            }
            String is=isPassword(Password.getText().toString());
            if(!is.equals("1"))
            {
                postmainHandler(is);
            }
            dialog= CustomProgress.show(MainActivity.this, "正在登录", false, null);
            String url=User_id.getText().toString()+"/"+md5(Password.getText().toString());
            userLoginDao.getFInfo(""+url,this);
        }else if(view.getId()==R.id.register_text)
        {
            //注册
            Intent intent = new Intent(MainActivity.this, LRegistrationactivity
                    .class);
            Bundle bundle = new Bundle();
            bundle.putString("Account","");
            bundle.putString("fuser_name","");
            bundle.putString("Flag","3");
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.forget_password)
        {
            //忘记密码
           startActivity(new Intent(MainActivity.this,ForgetPasswordActivity.class));

        }
        /**begin invoke umeng api**/
        mShareAPI.doOauthVerify(MainActivity.this, platform, umAuthListener);
    }
    public void getT(LoginJson<Login> t){
        Common.fuser_id=User_id.getText().toString();
        userAdapter.deleteByUserid(User_id.getText().toString());
        userAdapter.addUser(User_id.getText().toString(),Password.getText().toString(),t.getData
                ().getfUser_Name(),"1");
        SharedPreferences preferences=getSharedPreferences("User",
                MODE_WORLD_READABLE);
        //实例化editor对象
        SharedPreferences.Editor editor=preferences.edit();
        //存入数据
        editor.putString("fuser_id",t.getData().getfUser_Id());
        editor.putString("fUser_Name",t.getData().getfUser_Name());
        editor.putString("fCompanyID",t.getData().getfCompanyID());
        editor.putString("CompanyName",t.getData().getCompanyName());
        editor.putString("fCityName",t.getData().getfCityName());
        editor.putString("QQNickName",t.getData().getQQNickName());
        editor.putString("WeiXinNickName",t.getData().getWeiXinNickName());
        editor.putString("WeiBoNickName",t.getData().getWeiBoNickName());
        editor.putString("fUsableScores",t.getData().getfUsableScores());
        editor.putString("flogistics",t.getData().getFlogistics());
        editor.putString("freceiver",t.getData().getFreceiver());
        editor.putString("freceiverTel",t.getData().getFreceiverTel());
        //提交修改
        editor.commit();
        Intent intent=new Intent(MainActivity.this,StartActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("id", "0");
        intent.putExtras(bundle);
        startActivityForResult(intent, 0);
        finish();
        dialog.dismiss();
    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        postmainHandler(""+msg);
    }
    /** auth callback interface**/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data!=null){
               // data.get("openid").toString();
                if(platform.toString().equals("QQ"))
                {
                    Thirdparty(data.get("openid"),data.get("screen_name"),"0");
                }
                else if(platform.toString().equals("WEIXIN"))
                {
                    Thirdparty(data.get("openid"),data.get("screen_name"),"1");
                }else if(platform.toString().equals("SINA"))
                {
                    Thirdparty(data.get("access_token"),data.get("userName"),"2");
                }
        }
        }
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "授权失败", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "您取消登录了", Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
       /* UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }*/
    }
    public void Thirdparty(final String Account,final String fuser_name,final String Flag)
    {

        dialog= CustomProgress.show(MainActivity.this, "正在登录", false, null);
        OkHttpClientManager.getAsyn(OkHttpClientManager
                .BASE_URL+"user/getAccountIsExist/"+Account+"/"+Flag,
                new OkHttpClientManager.ResultCallback<LoginJson<Login>>()
                {
                    @Override
                    public void onResponse(LoginJson<Login> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                Common.fuser_id=u.getData().getfUser_Id();
                                userAdapter.deleteByUserid(u.getData().getfUser_Id());
                                userAdapter.addUser(u.getData().getfUser_Id(),Password.getText
                                        ().toString(),u.getData
                                        ().getfUser_Name(),"1");
                                SharedPreferences preferences=getSharedPreferences("User",
                                        MODE_WORLD_READABLE);
                                //实例化editor对象
                                SharedPreferences.Editor editor=preferences.edit();
                                //存入数据
                                editor.putString("fuser_id",u.getData().getfUser_Id());
                                editor.putString("fUser_Name",u.getData().getfUser_Name());
                                editor.putString("fCompanyID",u.getData().getfCompanyID());
                                editor.putString("CompanyName",u.getData().getCompanyName());
                                editor.putString("fCityName",u.getData().getfCityName());
                                editor.putString("QQNickName",u.getData().getQQNickName());
                                editor.putString("WeiXinNickName",u.getData().getWeiXinNickName());
                                editor.putString("WeiBoNickName",u.getData().getWeiBoNickName());
                                editor.putString("fUsableScores",u.getData().getfUsableScores());
                                editor.putString("flogistics",u.getData().getFlogistics());
                                editor.putString("freceiver",u.getData().getFreceiver());
                                editor.putString("freceiverTel",u.getData().getFreceiverTel());
                                //提交修改
                                editor.commit();
                                Intent intent=new Intent(MainActivity.this,StartActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id", "0");
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                                finish();
                            }else{
                                dialog.dismiss();
                                Intent intent = new Intent(MainActivity.this, LRegistrationactivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Account",""+Account);
                                bundle.putString("fuser_name",""+fuser_name);
                                bundle.putString("Flag",""+Flag);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                                postmainHandler(""+meta.getMsg());
                            }
                        }else{
                            dialog.dismiss();
                            postmainHandler("亲!网络异常!请稍后再试");
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        dialog.dismiss();
                        postmainHandler("亲!网络异常!请稍后再试");

                    }
                });
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            Common.gpsy=location.getLatitude()+"";
            Common.gpsx=location.getLongitude()+"";

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层

        super.onDestroy();
    }
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    public void getFriendbyClick(View view) {

        mShareAPI.getFriend(MainActivity.this, SHARE_MEDIA.SINA, umGetfriendListener);

    }
    private UMFriendListener umGetfriendListener = new UMFriendListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, Object> data) {
            if (data!=null){
                Toast.makeText(getApplicationContext(), data.get("json").toString(), Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( getApplicationContext(), "get fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( getApplicationContext(), "get cancel", Toast.LENGTH_SHORT).show();
        }
    };
    private final int SDK_PERMISSION_REQUEST = 127;
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this,"再按一次退出鼎搜客", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                BaseApplication.GetInstance().exit();
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 获取当前程序的版本号
     *
     * @throws Exception
     */
    private int getVersionCode() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名,0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);

        return packInfo.versionCode;
    }
    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名,0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }
    /**
     * 提交信息
     */
    private void submit(int code)//code
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager
                        .BASE_URL+"upgrade/getMobileVersion/"+code+"/0",
                new OkHttpClientManager.ResultCallback<VersionJson<Version>>()
                {
                    @Override
                    public void onResponse(VersionJson<Version> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                visitDialog(u.getData().getURL(),u.getData().getDescs(),u.getData
                                        ().getTitle());
                            }else{
                            }
                        }else{
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    private void visitDialog(final String url,final String descs,final String Title) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.descs_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        TextView title=(TextView) window.findViewById(R.id.Title);
        title.setText(""+Title);
        TextView cemo=(TextView) window.findViewById(R.id.cemo);
        cemo.setText(""+descs);
        Button ok=(Button)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("student_id", ""+url);
                startService(intent);
                dlg.cancel();
            }
        });
        Button qx=(Button)window.findViewById(R.id.qx);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

}