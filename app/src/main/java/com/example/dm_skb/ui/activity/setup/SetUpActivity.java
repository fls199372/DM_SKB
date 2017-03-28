package com.example.dm_skb.ui.activity.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.Version;
import com.example.dm_skb.bean.VersionJson;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.dao.UserLoginDao;
import com.example.dm_skb.ui.activity.MainActivity;
import com.example.dm_skb.ui.activity.main.Defaultcontent;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.MyService;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.qihoo.appstore.common.updatesdk.lib.UpdateHelper;
import com.squareup.okhttp.Request;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 设置
 */
public  class SetUpActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    private UMShareAPI mShareAPI = null;
    Dialog dialog;
    UserLoginDao userLoginDao;
    TextView fuser_name;
    TextView phone;
    TextView WX;
    TextView sina;
    TextView QQ;
    UMImage image = new UMImage(SetUpActivity.this, "http://shdingmou.cn/dmsock/MainIcon.png");
    UserAdapter userAdapter;
    ToggleButton toggleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setupmain);
        mShareAPI = UMShareAPI.get(this);
        UserID();
        toggleBtn=(ToggleButton)findViewById(R.id.toggleBtn);
        fuser_name=(TextView)findViewById(R.id.fuser_name);
        phone=(TextView)findViewById(R.id.phone);
        WX=(TextView)findViewById(R.id.WX);
        sina=(TextView)findViewById(R.id.sina);
        QQ=(TextView)findViewById(R.id.QQ);
        userLoginDao=new UserLoginDao();
        userAdapter=new UserAdapter(this);
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
        fuser_name.setText(""+Common.fUser_Name);
        phone.setText(""+Common.fuser_id);
        WX.setText(""+Common.WeiXinNickName);
        sina.setText(""+Common.WeiBoNickName);
        QQ.setText(""+Common.QQNickName);
        //切换开关
        toggleBtn.toggle();
        //切换无动画
        toggleBtn.toggle(true);
        //开关切换事件
        toggleBtn.setOnToggleChanged(new OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                if(on==true)
                {
                    SharedPreferences preferences = getApplication().getSharedPreferences("remind",
                            getApplication().MODE_PRIVATE);
                    //实例化editor对象
                    SharedPreferences.Editor editor=preferences.edit();
                    //存入数据true
                    editor.putBoolean("isFirstUse",true);
                    //提交修改
                    editor.commit();
                }else {
                    SharedPreferences preferences = getApplication().getSharedPreferences("remind",
                            getApplication().MODE_PRIVATE);
                    //实例化editor对象
                    SharedPreferences.Editor editor=preferences.edit();
                    //存入数据true
                    editor.putBoolean("isFirstUse",false);
                    //提交修改
                    editor.commit();
                }
            }
        });
        boolean isFirstUse1;
        // 读取ShareedPreferences中需要的数据
        SharedPreferences preferences1 = getApplication().getSharedPreferences("remind",
                getApplication().MODE_PRIVATE);
        isFirstUse1=preferences1.getBoolean("isFirstUse", true);
        if(isFirstUse1)
        {
            toggleBtn.setToggleOn();

        }else {
            toggleBtn.setToggleOff();

        }

    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.password_layout)
        {
            startActivity(new Intent(SetUpActivity.this, ModifyPasswordActivity.class));
        }else if(view.getId()==R.id.About_DM)
        {
            startActivity(new Intent(SetUpActivity.this, AboutDingMouActivity.class));

        }else if(view.getId()==R.id.QQ_layout)
        {
            platform= SHARE_MEDIA.QQ;
            mShareAPI.doOauthVerify(SetUpActivity.this, platform, umAuthListener);

        }else if(view.getId()==R.id.wx_layout)
        {
            platform= SHARE_MEDIA.WEIXIN;
            mShareAPI.doOauthVerify(SetUpActivity.this, platform, umAuthListener);

        }else if(view.getId()==R.id.wb_layout)
        {
            platform= SHARE_MEDIA.SINA;
            mShareAPI.doOauthVerify(SetUpActivity.this, platform, umAuthListener);

        }else if(view.getId()==R.id.sign_out)
        {
            userAdapter.deleteByUserid(Common.fuser_id);
            userAdapter.addUser(Common.fuser_id,Common.fuser_pwd,Common.fuser_name,"0");
            startActivity(new Intent(SetUpActivity.this, MainActivity.class));

        }else if(view.getId()==R.id.FX)
        {
            new ShareAction(SetUpActivity.this).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,
                    SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withTitle(Defaultcontent.title)
                    .withText(Defaultcontent.text+"")
                    .withTargetUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.example.dm_skb")
                    .withMedia(image)
                    .setCallback(umShareListener)
                    .open();
        }else if(view.getId()==R.id.upgraded)
        {
//            try{
//                submit(getVersionCode());
//            }catch (Exception e)
//            {
//
//            }
            checkUpdateManual();
        }
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
    private void submit(int code)
    {

        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"user/getAndroidVersion/"+code+"/0",
                new OkHttpClientManager.ResultCallback<VersionJson<Version>>()
                {
                    @Override
                    public void onResponse(VersionJson<Version> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                visitDialog(u.getData().getURL(),u.getData().getDescs());

                            }else{
                                postmainHandler("当前系统已是最新版本");

                            }
                        }else{
                            postmainHandler("当前系统已是最新版本");
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        postmainHandler("当前系统已是最新版本");
                    }
                });
    }
    private void visitDialog(final String url,final String descs) {
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

        Button ok=(Button)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyService.class);
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
    private UMShareListener umShareListener = new UMShareListener() {
        public void onResult(SHARE_MEDIA platform) {
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(SetUpActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                if(platform.name().equals("SINA"))
                {
                    submitshare("1");
                }else if(platform.name().equals("QQ"))
                {
                    submitshare("2");
                }else if(platform.name().equals("QZONE"))
                {
                    submitshare("3");
                }else if(platform.name().equals("WEIXIN"))
                {
                    submitshare("4");
                }else if(platform.name().equals("WEIXIN_CIRCLE"))
                {
                    submitshare("4");
                }
            }
        }
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SetUpActivity.this," 您的分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
            }
        }
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SetUpActivity.this,"您的分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * 提交信息
     */
    private void submitshare(String type)
    {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_Id", Common.fuser_id);//手机号
            main.put("ShareType", type);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("sharedata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"company/addUserShare?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                postmainHandler(""+u.getData().getSuessCode());
                            }else{
                            }
                        }else{
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        postmainHandler("ggggg");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    /** auth callback interface**/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            if (data!=null) {
                // data.get("openid").toString();
                if (platform.toString().equals("QQ")) {
                    submit(data.get("openid"), data.get("screen_name"), "4");
                    if(data.get("screen_name")==null)
                    {

                    }else
                    {
                        Common.QQNickName = data.get("screen_name");
                    }
                    SharedPreferences preferences = getSharedPreferences("User",
                            MODE_WORLD_READABLE);
                    //实例化editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("QQNickName", "" + Common.QQNickName);
                    //提交修改
                    editor.commit();
                } else if (platform.toString().equals("WEIXIN"))
                {
                    submit(data.get("openid"),data.get("screen_name"), "5");
                    if(data.get("screen_name")==null)
                    {

                    }else
                    {
                        Common.WeiXinNickName = data.get("screen_name");
                    }
                    SharedPreferences preferences = getSharedPreferences("User",
                            MODE_WORLD_READABLE);
                    //实例化editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("WeiXinNickName", "" + Common.WeiXinNickName);
                    //提交修改
                    editor.commit();
                }else if (platform.toString().equals("SINA"))
                {
                    submit(data.get("access_token"),data.get("userName"), "6");
                    Common.WeiBoNickName = data.get("userName");
                    SharedPreferences preferences = getSharedPreferences("User",
                            MODE_WORLD_READABLE);
                    //实例化editor对象
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("WeiBoNickName", "" + Common.WeiBoNickName);
                    //提交修改
                    editor.commit();
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
    public void getT(RegisterJson<Register> t) {
        dialog.dismiss();
        postmainHandler(""+t.getData().getSuessCode());
        QQ.setText(""+Common.QQNickName);

    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        postmainHandler(""+msg);
    }
    public void submit(final String fThirdpartyId,final String NickName,final String Action)
    {

        dialog= CustomProgress.show(SetUpActivity.this, "正在提交信息", false, null);
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("Action",Action);//
            main.put("fThirdpartyId",fThirdpartyId);//
            main.put("NickName", NickName);//
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("userdata",main.toString());//主表
        userLoginDao.getupdateUserInfo(params,this);
    }
    private void checkUpdateManual() {
        String packageName = "com.example.dm_skb";
        if (!TextUtils.isEmpty(packageName)) {
            UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#ffffff"));
            UpdateHelper.getInstance().setDebugMode(true);
            UpdateHelper.getInstance().manualUpdate(packageName);
        }
    }
}
