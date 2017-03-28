package com.example.dm_skb.ui.activity.main;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.zhy.autolayout.AutoLinearLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dm_skb.ui.activity.earnpoints.Earn_Fragment;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.Version;
import com.example.dm_skb.bean.VersionJson;
import com.example.dm_skb.ui.base.BaseApplication;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.MyService;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.*;
import com.example.dm_skb.R;
import com.example.dm_skb.widget.ReportTabView.OnTabChangeListener;
import com.example.dm_skb.ui.activity.searchcustomer.Search_CustomerFragment;
import com.example.dm_skb.ui.activity.customermanagement.Customer_Management_Fragment;
import com.example.dm_skb.ui.activity.earnpoints.Earn_Points_Fragment;
import com.example.dm_skb.ui.activity.aboutme.About_Me_Fragment;
import com.squareup.okhttp.Request;
import com.umeng.socialize.UMShareAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.LinearLayout;
public class StartActivity extends FragmentActivity implements OnTabChangeListener, FragmentCallback {
    private FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private ReportTabView mTabView;
    private ImageView mageView;
    private Button button_forward;
    /** 上一次的状态 */
    private int mPreviousTabIndex = 0;
    /** 当前状态 */
    private int mCurrentTabIndex = 0;
    private int k=1;
    String is="0";
    ReportTabView report_tab;
    AutoLinearLayout layout_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        BaseApplication.GetInstance().addActivity(this);//添加自己到activity管理集合
        mFragmentManager = getSupportFragmentManager();
        mCurrentTabIndex = 0;
        mPreviousTabIndex = 0;
        Bundle bundle=getIntent().getExtras();
        is=bundle.getString("id");
        setupViews();
    }
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param
     *（DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    String set="";
    String setleng="";
    private void setupViews()
    {
        Bundle bundle=getIntent().getExtras();
        setContentView(R.layout.homepage_main);
        DisplayMetrics dm = new DisplayMetrics();
        dm = this.getApplicationContext().getResources().getDisplayMetrics();
        Common.screenWidth = dm.widthPixels;
        Common.screenHeight = dm.heightPixels;
        layout_main=(AutoLinearLayout)findViewById(R.id.layout_main);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) layout_main.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height =Common.screenHeight/14;
        layout_main.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
        report_tab=(ReportTabView)findViewById(R.id.report_tab);
        try{
            submit(getVersionCode());
        }catch (Exception e)
        {

        }
        SharedPreferences preferences = getSharedPreferences("User",
                MODE_WORLD_READABLE);
        Common.fuser_id=preferences.getString("fuser_id", "");
        Common.fUser_Name=preferences.getString("fUser_Name", "");
        Common.fCompanyID=preferences.getString("fCompanyID", "");
        Common.CompanyName=preferences.getString("CompanyName", "");
        Common.fCityName=preferences.getString("fCityName", "");
        Common.QQNickName=preferences.getString("QQNickName", "");
        Common.WeiXinNickName=preferences.getString("WeiXinNickName", "");
        Common.WeiBoNickName=preferences.getString("WeiBoNickName", "");
        Common.fUsableScores=preferences.getString("fUsableScores", "");
        Common.flogistics=preferences.getString("flogistics", "");
        Common.freceiver=preferences.getString("freceiver", "");
        Common.freceiverTel=preferences.getString("freceiverTel", "");

        submit();
        getPersimmions();
        mTabView = (ReportTabView) findViewById(R.id.report_tab);
        mTabView.setOnTabChangeListener(this);
        if(is.equals("0"))
        {
            mTabView.setCurrentTab(0);
            Bundle bundle1 = new Bundle();
            bundle1.putString("fuser_id", "");
            bundle1.putString("setleng", setleng+"");
            bundle1.putString("set", set+"");
            mCurrentFragment = new Fragment();
            replaceFragment(Search_CustomerFragment.class,bundle1);
        }else
        {
            mTabView.setCurrentTab(1);
            mPreviousTabIndex = mCurrentTabIndex;
            mCurrentTabIndex = 1;
            Bundle bundle1 = new Bundle();
            bundle1.putString("fuser_id","");
            bundle1.putString("farea_name", farea_name+"");
            bundle1.putString("farea_id", farea_id+"");
            replaceFragment(Customer_Management_Fragment.class,bundle1);
        }
    }
    @SuppressWarnings("unused")
    private OnClickListener clickListener=new OnClickListener(){
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(v==mageView){        //查询签到
                finish();
            }else if(v==button_forward)
            {
				/*Intent intent=new Intent(StartActivity.this, ScreenMainActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("id","2");
		    	intent.putExtras(bundle);
				startActivity(intent);*/
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        farea_name=arg2!=null?arg2.getStringExtra("farea_name"):"";
        farea_id=arg2!=null?arg2.getStringExtra("farea_id"):"";
        id=arg2!=null?arg2.getStringExtra("id"):"";
        if(id==null)
        {

        }else if(id.equals("9"))
        {

        }
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, arg2);

    }
    public void onFragmentCallback(Fragment fragment, int id, Bundle args) {
        mTabView.setCurrentTab(1);
    }
    public void onTabChange(String tag) {
        if (tag != null) {
            if (tag.equals("search_customer")) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 0;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fuser_id", "");
                bundle1.putString("setleng", setleng+"");
                bundle1.putString("set", set+"");
                replaceFragment(Search_CustomerFragment.class,bundle1);
                //  检查，如果没有登录则跳转到登录界面
            }else if ("customer_management".equals(tag)) {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 1;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fuser_id","");
                bundle1.putString("farea_name", farea_name+"");
                bundle1.putString("farea_id", farea_id+"");
                replaceFragment(Customer_Management_Fragment.class,bundle1);
            }else if ("earn_points".equals(tag)){
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 3;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fuser_id","");
                replaceFragment(Earn_Points_Fragment.class,bundle1);
            }else if("about_me".equals(tag))
            {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 4;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fuser_id","");
                About_Me_Fragment.integral();
                replaceFragment(About_Me_Fragment.class,bundle1);
            }else if("earn".equals(tag))
            {
                mPreviousTabIndex = mCurrentTabIndex;
                mCurrentTabIndex = 2;
                Bundle bundle1 = new Bundle();
                bundle1.putString("fuser_id","");
                replaceFragment(Earn_Fragment.class,bundle1);
            }
        }
    }
    private void replaceFragment(Class<?extends Fragment> newFragment, Bundle bundle1) {
        mCurrentFragment = FragmentUtils.switchFragment(mFragmentManager,
                R.id.report_content, mCurrentFragment,
                newFragment, bundle1, false);
    }
    String farea_name="";
    String farea_id="";
    String id="";
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
     * 提交信息
     */
    private void submit()
    {
        if(Common.device_token.equals("")||Common.device_token.equals("null")||Common
                .device_token==null)
        {
            return;
        }
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("MobileNo", Common.fuser_id);//手机号
            main.put("DeviceType","0");//手机类型
            main.put("DeviceToken",Common.device_token);//
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("mobiledata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"user/addUserMobileDevices?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                            }else{
                            }
                        }else{
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {

                    }});
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
    /**
     * 获取当前程序的版本号
     *
     * @throws Exception
     */
    private int getVersionCode() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名,0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);

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
                .BASE_URL+"upgrade/getMobileVersion/"+1+"/0",
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
                Intent intent = new Intent(StartActivity.this, MyService.class);
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


    private final int SDK_PERMISSION_REQUEST = 127;
}
