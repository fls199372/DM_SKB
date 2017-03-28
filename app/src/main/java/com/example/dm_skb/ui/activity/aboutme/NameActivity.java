package com.example.dm_skb.ui.activity.aboutme;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
 * 姓名
 */

public class NameActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    Dialog dialog;
    EditText user;
    UserLoginDao userLoginDao;
    String user_name="";
    private TextView title_view;
    ImageView city_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.city_name_main);
        user=(EditText)findViewById(R.id.city_name);
        Bundle bundle=getIntent().getExtras();
        title_view=(TextView) findViewById(R.id.register_main);
        title_view.setText("修改昵称");
        user.setHint("请输入昵称");
        if(bundle.getString("fuser_name").length()==0)
        {

        }else {
            user.setText(""+bundle.getString("fuser_name"));
        }
        user_name=bundle.getString("fuser_name");
        city_image=(ImageView)findViewById(R.id.city_image);
        city_image.setBackgroundResource(R.drawable.name);
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
        if(user.getText().toString().length()==0)
        {
            finish();
        }
        if(user_name.equals(""+user.getText().toString()))
        {
            finish();
            return;
        }
        dialog= CustomProgress.show(NameActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("Name", ""+user.getText().toString());//公司名称
            main.put("Action", "1");//

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
        editor.putString("fUser_Name", ""+user.getText().toString());
        Common.fUser_Name=user.getText().toString();
        //提交修改
        editor.commit();
        postmainHandler(""+t.getData().getSuessCode());
        NameActivity.this.setResult(1, new Intent(NameActivity.this,
                StartActivity.class).putExtra("fuser_name",
                user.getText().toString()).putExtra("id", "1"));
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
