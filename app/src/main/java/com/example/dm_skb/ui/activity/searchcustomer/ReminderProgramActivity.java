package com.example.dm_skb.ui.activity.searchcustomer;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import org.feezu.liuli.timeselector.TimeSelector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 提醒计划
 */

public  class ReminderProgramActivity extends BaseActivity  {

    private TextView date;
    private String fCompanyID="";
    private String position="";
    private String fCompanyName="";
    Dialog dialog;
    private TimeSelector timeSelector;

    private EditText cemo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reminder_program_main);
        cemo=(EditText)findViewById(R.id.cemo);
        date=(TextView)findViewById(R.id.date);
        Bundle bundle=getIntent().getExtras();
        fCompanyID=bundle.getString("fcust_id");
        position=bundle.getString("position");
        fCompanyName=bundle.getString("fcust_name");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        date.setText(""+df.format(new Date()));
        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                date.setText(""+time);
            }
        }, df.format(new Date()), "2150-12-31 23:59");
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.add_plan)
        {
            submit(cemo.getText().toString(),date.getText().toString());
        }else if(view.getId()==R.id.time)
        {
            timeSelector.show();

        }
    }
    /**
     * 提交信息
     */
    private void submit(final String sEvent,final String dDate)
    {
        dialog= CustomProgress.show(ReminderProgramActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("fCompanyID", fCompanyID);//公司ID
            main.put("fCompanyName", fCompanyName);//公司名称
            main.put("sEvent", sEvent);//备注

            main.put("Action", "1");//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
            main.put("iStarQty", "");//评价星数 行为参数=6 则值不能为空
            main.put("iErrFalg", "");//纠错标致 行为参数=5 则值不能为空  1 无此客户 2 客户渠道信息有误 3企业基础信息有误 4 电话号码信息有误
            // 5 地址信息有误
            // 6  网址邮箱信息有误 7联系人信息有误 8联系人电话有误
            main.put("dDate", ""+dDate);//

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("mydata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"mycompany/addMyCompanyInfo?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){

                                postmainHandler(""+u.getData().getSuessCode());
                                dialog.dismiss();
                                finish();;
                            }else{
                                postmainHandler(""+meta.getMsg
                                        ());
                                dialog.dismiss();
                            }
                        }else{
                            postmainHandler("亲!网络异常!请稍后再试");
                            dialog.dismiss();
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        postmainHandler("亲!网络异常!请稍后再试");
                        dialog.dismiss();
                    }
                });
    }
}
