package com.example.dm_skb.ui.activity.aboutme;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import com.example.dm_skb.widget.*;

import com.example.dm_skb.dao.UserLoginDao;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.ResponseT;
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
 * 输入公司名称
 */

public class CorporateNameActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    Dialog dialog;
    EditText fcust_name;
    UserLoginDao userLoginDao;
    String fcustname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.corporate_name_main);
        fcust_name=(EditText)findViewById(R.id.fcust_name1);
        Bundle bundle=getIntent().getExtras();
        if(bundle.getString("fcust_name").length()==0)
        {

        }else {
            fcust_name.setText(""+bundle.getString("fcust_name"));
        }
        fcustname=bundle.getString("fcust_name");
        userLoginDao=new UserLoginDao();
    }

    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            submit();

        }
    }
    public void submit()
    {
        if(fcust_name.getText().toString().length()==0)
        {
            finish();
        }
        if(fcustname.equals(""+fcust_name.getText().toString()))
        {
            finish();
            return;
        }
        dialog= CustomProgress.show(CorporateNameActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("Name", ""+fcust_name.getText().toString());//公司名称
            main.put("fThirdpartyId", "");//备注
            main.put("Action", "2");//
            main.put("NickName", "5");//
            main.put("flogistics", "");//
            main.put("freceiver", "");//
            main.put("freceiverTel", "");//

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("userdata",main.toString());//主表
        userLoginDao.getupdateUserInfo(params,this);

    }
    public void getT(RegisterJson<Register> t) {
        dialog.dismiss();

        postmainHandler(""+t.getData().getSuessCode());
        SharedPreferences preferences = getSharedPreferences("User",
                MODE_WORLD_READABLE);
        //实例化editor对象
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CompanyName", ""+fcust_name.getText().toString());
        //提交修改
        editor.commit();
        Common.CompanyName=fcust_name.getText().toString();

        CorporateNameActivity.this.setResult(1, new Intent(CorporateNameActivity.this,
                StartActivity.class).putExtra("fcust_name",
                fcust_name.getText().toString()).putExtra("id", "2"));
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