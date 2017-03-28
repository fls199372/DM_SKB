package com.example.dm_skb.ui.activity.apply;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Login;
import com.example.dm_skb.bean.LoginJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register1;
import com.example.dm_skb.bean.RegisterJson1;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.adapter.UserAdapter;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.ClearEditText;
import com.example.dm_skb.widget.CustomProgress;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的信息入住
 */
public class ApplyActivity extends BaseActivity implements ResponseT<LoginJson<Login>> {
    private EditText User_id;
    private EditText Password;
    Dialog dialog;
    UserAdapter userAdapter;
    private TextView brand_length;
    private TextView cemo_length;
    private TextView register_main;
    private EditText farea_name;
    private  ClearEditText date;
    private TimeSelector timeSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_main);
        register_main= (TextView) findViewById(R.id.register_main);
        register_main.setText("厂家免费使用积分申请表");

    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if(view.getId()==R.id.Return)
        {
            finish();
        }else if(view.getId()==R.id.Return)
        {
           postmainHandler("ffffffffff");
        }else if(view.getId()==R.id.ok)
        {
            dialog= CustomProgress.show(ApplyActivity.this, "", false, null);
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
            main.put("fLiveDate",""+date.getText().toString());//时间
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("applydata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"live/addCompanyLiveApply?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson1<Register1>>()
                {
                    @Override
                    public void onResponse(RegisterJson1<Register1> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                dialog.cancel();
                                postmainHandler(""+u.getData().getSuessCode());
                                finish();
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
    @Override
    public void getT(LoginJson<Login> t) {
        dialog.dismiss();
    }
    @Override
    public void getError(String msg) {
        postmainHandler(""+msg);
        dialog.dismiss();
    }
}
