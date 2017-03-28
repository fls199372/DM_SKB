package com.example.dm_skb.ui.activity.aboutme;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import com.example.dm_skb.widget.*;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.dao.UserLoginDao;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/10.
 * 物流地址
 */

public class LogisticsAddressActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    Dialog dialog;
    EditText EMSaddress;
    EditText Telephone;
    EditText Addressee;

    UserLoginDao userLoginDao;
    String cityname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logisticsaddress_main);
        EMSaddress=(EditText)findViewById(R.id.EMSaddress);
        Telephone=(EditText)findViewById(R.id.Telephone);
        Addressee=(EditText)findViewById(R.id.Addressee);

        userLoginDao=new UserLoginDao();
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
        EMSaddress.setText(""+Common.flogistics);
        Telephone.setText(""+Common.freceiverTel);
        Addressee.setText(""+Common.freceiver);

    }

    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            submit();

        }
    }
    public void submit()
    {
        if(EMSaddress.getText().toString().equals("")&&Telephone.getText().toString().equals("")
                &&Addressee.getText().toString().equals(""))
        {
            finish();
            return;
        }
        dialog= CustomProgress.show(LogisticsAddressActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("flogistics", ""+EMSaddress.getText().toString());//公司名称
            main.put("Action", "7");//
            main.put("freceiver", ""+Addressee.getText().toString());//
            main.put("freceiverTel", ""+Telephone.getText().toString());//

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("userdata",main.toString());//主表
        userLoginDao.getupdateUserInfo(params,this);

    }
    public void getT(RegisterJson<Register> t) {
        dialog.dismiss();
        SharedPreferences preferences = getSharedPreferences("User",
                MODE_WORLD_READABLE);
        //实例化editor对象
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("flogistics", "" +EMSaddress.getText().toString());
        editor.putString("freceiver", ""+Addressee.getText().toString());
        editor.putString("freceiverTel",""+Telephone.getText().toString());


        //提交修改
        editor.commit();
        postmainHandler(""+t.getData().getSuessCode());
        LogisticsAddressActivity.this.setResult(1, new Intent(LogisticsAddressActivity.this,
                StartActivity.class).putExtra("id", "7"));
        finish();

    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        postmainHandler(""+msg);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            submit();
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }
}
