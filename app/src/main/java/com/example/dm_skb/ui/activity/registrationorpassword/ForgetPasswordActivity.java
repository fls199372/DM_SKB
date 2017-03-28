package com.example.dm_skb.ui.activity.registrationorpassword;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Login;
import com.example.dm_skb.bean.LoginJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseJsonCode;
import com.example.dm_skb.bean.VerificationCode;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.dao.VerificationCodeDao;
import com.example.dm_skb.ui.activity.MainActivity;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * A login screen that offers login via email/password.
 */
public class ForgetPasswordActivity extends BaseActivity implements ResponseT<ResponseJsonCode<VerificationCode>> {

    Button Get_verification_code;//获取验证码
    VerificationCodeDao cerificationCodeDao;
    private MyCount mc;
    EditText phone;
    EditText Password;
    EditText Verification_Code;
    Dialog dialog;
    int i=1;
    // 天安门坐标
    double mLat1 = 39.915291;
    double mLon1 = 116.403857;
    // 百度大厦坐标
    double mLat2 = 40.056858;
    double mLon2 = 116.308194;

    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forget_password_main);
        userAdapter=new UserAdapter(this);

        Get_verification_code = (Button) findViewById(R.id.Get_verification_code);
        cerificationCodeDao = new VerificationCodeDao();
        phone=(EditText)findViewById(R.id.phone);
        Password=(EditText)findViewById(R.id.Password);
        Verification_Code=(EditText)findViewById(R.id.Verification_Code);
    }

    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
       if(view.getId()==R.id.register_text)
        {
            finish();
        }
        else if(view.getId()==R.id.Determine)
        {
            if(phone.getText().toString().length()==0)
            {
                postmainHandler("手机号码不能为空");
                return;
            }
            if(!isPhoneNumberValid(phone.getText().toString()))
            {
                postmainHandler("请录入正确的手机号");
                return;
            }
            if(phone.getText().toString().length()==0)
            {
                postmainHandler("手机号码不能为空");
                return;
            }
            String is=isPassword(phone.getText().toString());
            if(!is.equals("1"))
            {
                postmainHandler(is);
            }
            if(Verification_Code.getText().toString().length()==0)
            {
                postmainHandler("验证码不能为空");
                return;
            }
            dialog= CustomProgress.show(ForgetPasswordActivity.this, "正在修改密码", false, null);
            submit();
        }else if (view.getId() == R.id.Get_verification_code) {
           if(i==1)
           {
               i++;
               if(phone.getText().toString().length()==0)
               {
                   postmainHandler("手机号码不能为空");
                   return;
               }
               if(!isPhoneNumberValid(phone.getText().toString()))
               {
                   postmainHandler("请录入正确的手机号");
                   return;
               }
               if(phone.getText().toString().length()==0)
               {
                   postmainHandler("手机号码不能为空");
                   return;
               }
               cerificationCodeDao.getFInfo(phone.getText().toString(),this);
               mc = new MyCount(60000, 1000);
               mc.start();
           }else
           {

           }

        }
    }
    /*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            i=1;
            Get_verification_code.setText("获取验证码");
        }
        @Override
        public void onTick(long millisUntilFinished) {

            Get_verification_code.setText("" + millisUntilFinished / 1000 + "s");
        }
    }
    public void getT(ResponseJsonCode<VerificationCode> t) {

    }
    @Override
    public void getError(String msg) {
        // TODO Auto-generated method stub
        postmainHandler("" + msg);
    }
    /**
     * 提交信息
     */
    private void submit()
    {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("RegistryTel",phone.getText().toString());//手机号
            main.put("password",md5(Password.getText().toString()));//密码
            main.put("validCode",Verification_Code.getText().toString());//验证码
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("passdata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"user/updateForgetPassword?",params,
                new OkHttpClientManager.ResultCallback<LoginJson<Login>>()
                {
                    @Override
                    public void onResponse(LoginJson<Login> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){

                                Common.fuser_id=phone.getText().toString();
                                userAdapter.deleteByUserid(phone.getText().toString());
                                userAdapter.addUser(phone.getText().toString(),Password.getText()
                                        .toString(),u.getData
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
                                Intent intent=new Intent(ForgetPasswordActivity.this,StartActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id", "0");
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                                finish();
                                dialog.dismiss();
                            }else{
                                dialog.cancel();
                                postmainHandler(""+meta.getMsg());

                            }
                        }else{
                            postmainHandler("亲!网络异常!请稍后再试");

                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        dialog.cancel();
                        postmainHandler("亲!网络异常!请稍后再试");
                    }
                });
    }

    /**
     * 开始导航
     *
     * @param view
     */
    /*public void startNavi(View view) {
        int lat = (int) (mLat1 * 1E6);
        int lon = (int) (mLon1 * 1E6);
        GeoPoint pt1 = new GeoPoint(lat, lon);
        lat = (int) (mLat2 * 1E6);
        lon = (int) (mLon2 * 1E6);
        GeoPoint pt2 = new GeoPoint(lat, lon);
        // 构建 导航参数
        NaviPara para = new NaviPara();
        para.startPoint = pt1;
        para.startName = "从这里开始";
        para.endPoint = pt2;
        para.endName = "到这里结束";

        try {

            BaiduMapNavigation.openBaiduMapNavi(para, this);

        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    BaiduMapNavigation.GetLatestBaiduMapApp(ForgetPasswordActivity.this);
                }
            });

            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
    }*/
}

