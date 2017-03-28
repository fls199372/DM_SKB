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

public class CityAddressActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    Dialog dialog;
    EditText city_name;
    UserLoginDao userLoginDao;
    String cityname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.city_name_main);
        city_name=(EditText)findViewById(R.id.city_name);
        Bundle bundle=getIntent().getExtras();
        if(bundle.getString("city_name").length()==0)
        {

        }else {
            city_name.setText(""+bundle.getString("city_name"));
        }
        cityname=bundle.getString("city_name");
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
        if(city_name.getText().toString().length()==0)
        {
            finish();
        }
        if(cityname.equals(""+city_name.getText().toString()))
        {
            finish();
            return;
        }
        dialog= CustomProgress.show(CityAddressActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("Name", ""+city_name.getText().toString());//公司名称
            main.put("fThirdpartyId", "");//备注
            main.put("Action", "3");//
            main.put("NickName", "");//
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
        editor.putString("fCityName", ""+city_name.getText().toString());
        Common.fCityName=city_name.getText().toString();

        //提交修改
        editor.commit();
        CityAddressActivity.this.setResult(1, new Intent(CityAddressActivity.this,
                StartActivity.class).putExtra("city_name",
                city_name.getText().toString()).putExtra("id", "3"));
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
