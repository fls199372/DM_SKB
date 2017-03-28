package com.example.dm_skb.ui.activity.searchcustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Intent;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 纠错
 */

public  class CorrectingActivity extends BaseActivity  {

    ImageView without_this_customer_image;
    TextView without_this_customer;
    TextView without_this_customer_Line;
    EditText without_this_customer_edittext;
    private String editString="";
    private String editString1="";
    private String editString2="";
    private String editString3="";
    private String editString4="";
    private String editString5="";
    private String editString6="";



    private String fcust_id="";
    private String fcust_name="";
    private ImageView channel_no_image;
    private EditText channel_no_edittext;
    private TextView channel_no_len;

    private ImageView basic_information_error_image;
    private TextView basic_information_error_lien;
    private EditText basic_information_error_edittext;

    private ImageView phone_error_image;//企业电话号码信息有误
    private TextView phone_error_line;
    private EditText phone_error_edittext;

    private ImageView address_error_image;//企业地址信息有误
    private TextView address_error_line;
    private EditText address_error_edittext;


    private ImageView EMS_error_image;//企业网址信息有误
    private TextView EMS_error_ling;
    private EditText EMS_error_edittext;

    private ImageView Contacts_error_image;//企业联系人信息有误
    private TextView Contacts_error_ling;
    private EditText Contacts_error_edittext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.correcting_main);
        without_this_customer_image=(ImageView)findViewById(R.id.without_this_customer_image);
        without_this_customer=(TextView) findViewById(R.id.without_this_customer);
        without_this_customer_Line=(TextView)findViewById(R.id.without_this_customer_Line);
        without_this_customer_edittext=(EditText)findViewById(R.id.without_this_customer_edittext);
        without_this_customer_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                editString = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 2000);
            }
        });
        Bundle bundle=getIntent().getExtras();
        fcust_id=bundle.getString("fcust_id");
        fcust_name=bundle.getString("fcust_name");
        channel_no_image=(ImageView)findViewById(R.id.channel_no_image);
        channel_no_edittext=(EditText)findViewById(R.id.channel_no_edittext);
        channel_no_len=(TextView)findViewById(R.id.channel_no_len);
        channel_no_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun1!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler1.removeCallbacks(delayRun1);
                }
                editString1 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler1.postDelayed(delayRun1, 2000);
            }
        });


        basic_information_error_image=(ImageView)findViewById(R.id.basic_information_error_image);
        basic_information_error_lien=(TextView)findViewById(R.id.basic_information_error_lien);
        basic_information_error_edittext=(EditText)findViewById(R.id.basic_information_error_edittext);
        basic_information_error_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun2!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler2.removeCallbacks(delayRun2);
                }
                editString2 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler2.postDelayed(delayRun2, 2000);
            }
        });
        phone_error_image=(ImageView)findViewById(R.id.phone_error_image);
        phone_error_line=(TextView)findViewById(R.id.phone_error_line);
        phone_error_edittext=(EditText)findViewById(R.id.phone_error_edittext);
        phone_error_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun3!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler3.removeCallbacks(delayRun3);
                }
                editString3 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler3.postDelayed(delayRun3, 2000);
            }
        });
        address_error_image=(ImageView)findViewById(R.id.address_error_image);
        address_error_line=(TextView)findViewById(R.id.address_error_line);
        address_error_edittext=(EditText)findViewById(R.id.address_error_edittext);
        address_error_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun4!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler4.removeCallbacks(delayRun4);
                }
                editString4 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler4.postDelayed(delayRun4, 2000);
            }
        });
        EMS_error_image=(ImageView)findViewById(R.id.EMS_error_image);
        EMS_error_ling=(TextView)findViewById(R.id.EMS_error_ling);
        EMS_error_edittext=(EditText)findViewById(R.id.EMS_error_edittext);
        EMS_error_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun5!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler5.removeCallbacks(delayRun5);
                }
                editString5 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler5.postDelayed(delayRun5, 2000);
            }
        });
        Contacts_error_image=(ImageView)findViewById(R.id.Contacts_error_image);
        Contacts_error_ling=(TextView)findViewById(R.id.Contacts_error_ling);
        Contacts_error_edittext=(EditText)findViewById(R.id.Contacts_error_edittext);
        Contacts_error_edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(delayRun6!=null){
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler6.removeCallbacks(delayRun6);
                }
                editString6 = s.toString();
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler6.postDelayed(delayRun6, 2000);
            }
        });
    }
    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString.equals(""))
            {

            }else {
                submit("1",editString);
            }
        }
    };
    private Handler handler1 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun1 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString1.equals(""))
            {
            }else {
                submit("2",editString1);
            }
        }
    };
    private Handler handler2 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun2 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString2.equals(""))
            {
            }else {
                submit("3",editString2);
            }
        }
    };
    private Handler handler3 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun3 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString3.equals(""))
            {
            }else {
                submit("4",editString3);
            }
        }
    };
    private Handler handler4 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun4 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString4.equals(""))
            {
            }else {
                submit("5",editString4);
            }
        }
    };
    private Handler handler5 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun5 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString5.equals(""))
            {
            }else {
                submit("6",editString5);
            }
        }
    };
    private Handler handler6 = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun6 = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            if(editString6.equals(""))
            {
            }else {
                submit("7",editString6);
            }
        }
    };

    /**
     * 提交信息
     */
    private void submit(final String iErrFalg,final String sEvent)
    {

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("fCompanyID", fcust_id);//公司ID
            main.put("fCompanyName", fcust_name);//公司名称
            main.put("sEvent", sEvent);//备注

            main.put("Action", "5");//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
            main.put("iStarQty", "");//评价星数 行为参数=6 则值不能为空
            main.put("iErrFalg", iErrFalg);//纠错标致 行为参数=5 则值不能为空  1 无此客户 2 客户渠道信息有误 3企业基础信息有误 4 电话号码信息有误
            // 5 地址信息有误
            // 6  网址邮箱信息有误 7联系人信息有误 8联系人电话有误
            main.put("dDate", "");//

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

                                postmainHandler("添加成功");
                            }else{
                                postmainHandler(""+meta.getMsg
                                        ());

                            }
                        }else{
                            postmainHandler("亲!网络异常!请稍后再试");

                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        postmainHandler("亲!网络异常!请稍后再试");
                    }
                });
    }


    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.ok)
        {
            Intent intent=new Intent(CorrectingActivity.this,CardPicturesActivity.class);
            Bundle bundle=new Bundle();

            bundle.putString("fcust_id", fcust_id);

            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.without_this_customer_image)//无客户
        {
            if(without_this_customer_edittext.getVisibility()==View.GONE)
            {
                without_this_customer_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                without_this_customer_edittext.setVisibility(View.VISIBLE);
                without_this_customer_Line.setVisibility(View.GONE);
            }else {
                without_this_customer_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                without_this_customer_edittext.setVisibility(View.GONE);
                without_this_customer_Line.setVisibility(View.VISIBLE);
            }

        }else if(view.getId()==R.id.channel_no_image)//渠道有误
        {
            if(channel_no_edittext.getVisibility()==View.GONE)
            {
                channel_no_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                channel_no_edittext.setVisibility(View.VISIBLE);
                channel_no_len.setVisibility(View.GONE);
            }else {
                channel_no_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                channel_no_edittext.setVisibility(View.GONE);
                channel_no_len.setVisibility(View.VISIBLE);
            }


        }else if(view.getId()==R.id.basic_information_error_image)//企业基本信息错误
        {
            if(basic_information_error_edittext.getVisibility()==View.GONE)
            {
                basic_information_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                basic_information_error_edittext.setVisibility(View.VISIBLE);
                basic_information_error_lien.setVisibility(View.GONE);
            }else {
                basic_information_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                basic_information_error_edittext.setVisibility(View.GONE);
                basic_information_error_lien.setVisibility(View.VISIBLE);
            }


        }else if(view.getId()==R.id.phone_error_image)//企业电话号码信息有误
        {
            if(phone_error_edittext.getVisibility()==View.GONE)
            {
                phone_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                phone_error_edittext.setVisibility(View.VISIBLE);
                phone_error_line.setVisibility(View.GONE);
            }else {
                phone_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                phone_error_edittext.setVisibility(View.GONE);
                phone_error_line.setVisibility(View.VISIBLE);
            }


        }else if(view.getId()==R.id.address_error_image)//企业地址信息有误
        {
            if(address_error_edittext.getVisibility()==View.GONE)
            {
                address_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                address_error_edittext.setVisibility(View.VISIBLE);
                address_error_line.setVisibility(View.GONE);
            }else {
                address_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                address_error_edittext.setVisibility(View.GONE);
                address_error_line.setVisibility(View.VISIBLE);
            }


        }else if(view.getId()==R.id.EMS_error_image)//企业地址信息有误
        {
            if(EMS_error_edittext.getVisibility()==View.GONE)
            {
                EMS_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                EMS_error_edittext.setVisibility(View.VISIBLE);
                EMS_error_ling.setVisibility(View.GONE);
            }else {
                EMS_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                EMS_error_edittext.setVisibility(View.GONE);
                EMS_error_ling.setVisibility(View.VISIBLE);
            }
        }else if(view.getId()==R.id.Contacts_error_image)//企业地址信息有误
        {
            if(Contacts_error_edittext.getVisibility()==View.GONE)
            {
                Contacts_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.minus));
                Contacts_error_edittext.setVisibility(View.VISIBLE);
                Contacts_error_ling.setVisibility(View.GONE);
            }else {
                Contacts_error_image.setImageDrawable(getResources().getDrawable(R
                        .drawable.add1));
                Contacts_error_edittext.setVisibility(View.GONE);
                Contacts_error_ling.setVisibility(View.VISIBLE);
            }


        }
    }


}
