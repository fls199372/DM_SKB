package com.example.dm_skb.ui.activity.registrationorpassword;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.widget.*;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseJsonCode;
import com.example.dm_skb.bean.VerificationCode;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.dao.VerificationCodeDao;
import com.example.dm_skb.ui.activity.MainActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.OkHttpClientManager;
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
public class LRegistrationactivity extends BaseActivity implements ResponseT<ResponseJsonCode<VerificationCode>> {

    Button Get_verification_code;//获取验证码
    VerificationCodeDao cerificationCodeDao;
    private MyCount mc;
    EditText phone;
    EditText Password;
    EditText Verification_Code;
    Dialog dialog;
    String Account="";
    String fuser_name="";
    String Flag="";
    int i=1;
    UserAdapter userAdapter;
    EditText recommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lregistrationc_main);
        Get_verification_code = (Button) findViewById(R.id.Get_verification_code);
        cerificationCodeDao = new VerificationCodeDao();
        phone=(EditText)findViewById(R.id.phone);
        Password=(EditText)findViewById(R.id.Password);
        Verification_Code=(EditText)findViewById(R.id.Verification_Code);
        Bundle bundle=getIntent().getExtras();
        Account=bundle.getString("Account");
        fuser_name=bundle.getString("fuser_name");
        Flag=bundle.getString("Flag");
        userAdapter=new UserAdapter(this);
        recommend=(EditText)findViewById(R.id.recommend);
    }

    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.new_login_btn) {
            startActivity(new Intent(LRegistrationactivity.this, MainActivity.class));

        }else if(view.getId()==R.id.agreement)
        {
            startActivity(new Intent(LRegistrationactivity.this, AgreementActivity.class));

        }else if(view.getId()==R.id.register_ok)
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

            String is=isPassword(Password.getText().toString());
            if(!is.equals("1"))
            {
                postmainHandler(is);
            }
            if(Verification_Code.getText().toString().length()==0)
            {
                postmainHandler("验证码不能为空");
                return;
            }
            dialog=CustomProgress.show(LRegistrationactivity.this, "正在注册", false, null);
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
                cerificationCodeDao.getFInfo(phone.getText().toString(),this);
                mc = new MyCount(60000, 1000);
                mc.start();
            }else {

            }

        }else if(view.getId()==R.id.to_log_on)
        {
            finish();
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
        postmainHandler(""+msg);
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
                main.put("RecommendTel",recommend.getText().toString());
                main.put("Account", ""+Account);//
                main.put("NikeName", ""+fuser_name);//
                main.put("password",md5(Password.getText().toString()));//密码
                main.put("validCode",Verification_Code.getText().toString());//验证码
                main.put("Flag",""+Flag);//账号类型!0 QQ 1  WX  2 WB 3 手机号
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("registerdata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"user/addUserRegisterNew2?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                dialog.cancel();
                                postmainHandler("注册成功");
                                Common.fuser_id=phone.getText().toString();
                                userAdapter.deleteByUserid(phone.getText().toString());
                                userAdapter.addUser(phone.getText().toString(),Password.getText()
                                        .toString(),"","1");
                                SharedPreferences preferences=getSharedPreferences("User",
                                        MODE_WORLD_READABLE);
                                //实例化editor对象
                                SharedPreferences.Editor editor=preferences.edit();
                                //存入数据
                                editor.putString("fuser_id", Common.fuser_id);
                                editor.putString("fUser_Name","");
                                editor.putString("fCompanyID","");
                                editor.putString("CompanyName","");
                                editor.putString("fCityName","");
                                editor.putString("QQNickName","");
                                editor.putString("WeiXinNickName","");
                                editor.putString("WeiBoNickName","");
                                editor.putString("fUsableScores","");
                                editor.putString("flogistics","");
                                editor.putString("freceiver","");
                                editor.putString("freceiverTel","");
                                //提交修改
                                editor.commit();
                                Intent intent=new Intent(LRegistrationactivity.this,StartActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id", "0");
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                                finish();
                            }else{
                                dialog.cancel();
                                postmainHandler(""+meta.getMsg
                                        ());

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

}

