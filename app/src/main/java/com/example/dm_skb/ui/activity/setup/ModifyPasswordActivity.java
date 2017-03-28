package com.example.dm_skb.ui.activity.setup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dm_skb.ui.activity.MainActivity;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.widget.*;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 修改密码
 */

public  class ModifyPasswordActivity extends BaseActivity  {

    EditText old_password;
    EditText new_password;
    EditText new_password1;
    Dialog dialog;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modify_password_main);
        old_password=(EditText)findViewById(R.id.old_password);
        new_password=(EditText)findViewById(R.id.new_password);
        new_password1=(EditText)findViewById(R.id.new_password1);
        userAdapter=new UserAdapter(this);
        UserID();

    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.confirm)
        {
            String is=isPassword(old_password.getText().toString());
            if(!is.equals("1"))
            {
                postmainHandler(is);
            }
            String is1=isPassword(new_password.getText().toString());
            if(!is1.equals("1"))
            {
                postmainHandler(is1);
            }
            String is2=isPassword(new_password1.getText().toString());
            if(!is2.equals("1")) {
                postmainHandler(is2);
            }
            if(!new_password.getText().toString().equals(""+new_password1.getText().toString()))
            {
                postmainHandler("新密码和确认密码不一样");
            }
            dialog= CustomProgress.show(ModifyPasswordActivity.this, "正在修改密码", false, null);
            submit();
        }
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

            main.put("RegistryTel", Common.fuser_id);//手机号
            main.put("OldPass",md5(old_password.getText().toString()));//密码
            main.put("NewPass",md5(new_password.getText().toString()));//密码
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("passdata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"user/modifyPassword?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                dialog.cancel();
                                userAdapter.deleteByUserid(Common.fuser_id);
                                userAdapter.addUser(Common.fuser_id,Common.fuser_pwd,Common.fuser_name,"0");
                                startActivity(new Intent(ModifyPasswordActivity.this, MainActivity.class));

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


}
