package com.example.dm_skb.ui.activity.integral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dm_skb.wxapi.WXPayEntryActivity;
import com.alipay.sdk.app.PayTask;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.AuthResult;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.util.PayResult;
import com.example.dm_skb.wxapi.Constants;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by yangxiaoping on 16/9/10.
 * 积分充值
 */

public  class IntegralRechargeActivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {

    private ListView integral_list;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    private IWXAPI api;
    private TextView jfmx;
    String url = "http://121.42.199.210:8067/dmsock/weixin/CreateOrder?";
    private String money="";
    int position1=0;
    TextView displayPayment;
    IntegralDao integralDao;
    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integral_recharge_main);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        integralDao=new IntegralDao();
        integral();

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
       // api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        //api.registerApp("wxb4ba3c02aa476ea1");
        displayPayment=(TextView) findViewById(R.id.displayPayment);
        for(int i=0;i<1;i++) {
            Map<String, String> arg = new HashMap<String, String>();
            arg.put("integral", "100");

            arg.put("amountOfMoney", "10");
            arg.put("discount", "10折");
            arg.put("id","1");
            displayPayment.setText("￥ "+10+".00");
            resultList.add(arg);

        }
        for(int i=0;i<1;i++) {
            Map<String, String> arg = new HashMap<String, String>();
            arg.put("integral", "1000");

            arg.put("amountOfMoney", "95");
            arg.put("discount", "9.5折");
            arg.put("id","0");

            resultList.add(arg);

        }
        for(int i=0;i<1;i++) {
            Map<String, String> arg = new HashMap<String, String>();
            arg.put("integral", "5000");

            arg.put("amountOfMoney", "450");
            arg.put("discount", "9.0折");
            arg.put("id","0");

            resultList.add(arg);

        }for(int i=0;i<1;i++) {
            Map<String, String> arg = new HashMap<String, String>();
            arg.put("integral", "10000");

            arg.put("amountOfMoney", "850");
            arg.put("discount", "8.5折");
            arg.put("id","0");

            resultList.add(arg);

        }for(int i=0;i<1;i++) {
            Map<String, String> arg = new HashMap<String, String>();
            arg.put("integral", "20000");

            arg.put("amountOfMoney", "1600");
            arg.put("discount", "8.0折");
            arg.put("id","0");

            resultList.add(arg);

        }

        jfmx=(TextView)findViewById(R.id.jfmx);

        integral_list=(ListView)findViewById(R.id.integral_list);
        mInflater=LayoutInflater.from(this);
        adapter = new MyAdapter();
        integral_list.setAdapter(adapter);
        integral_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                if(position1!=-1)
                {
                    ((HashMap)resultList.get(position1)).put("id","0");
                }
                ((HashMap)resultList.get(position)).put("id","1");
                displayPayment.setText("￥ "+resultList.get(position).get("amountOfMoney")
                        .toString()+".00");

                position1=position;
                adapter.notifyDataSetChanged(); //数据集变化后,通知adapter

            }
        });
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.WXpayment)
        {
            //subimt();
            Toast.makeText(IntegralRechargeActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
//            RegistryTel    手机号
//            Score            积分
//            totalAmount  金额
            final JSONObject main=new JSONObject();
            final Map<String, String> params = new HashMap<String, String>();
            try {

                main.put("RegistryTel", Common.fuser_id);//手机号
                main.put("Score", ""+resultList.get(position1).get("integral").toString());//
                main.put("totalAmount",""+resultList.get(position1).get("amountOfMoney").toString());//
                Common.integral1=resultList.get(position1).get("integral").toString();
                Common.amountOfMoney= resultList.get(position1).get("amountOfMoney").toString();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }//
            params.put("OrderData",main.toString());//主表
            OkHttpClientManager.postAsyn(OkHttpClientManager
                            .BASE_URL+"weixin/CreateOrder?",params,
                    new OkHttpClientManager.ResultCallback<String>()
                    {
                        @Override
                        public void onError(Request request, Exception e)
                        {
                            e.printStackTrace();
                            postmainHandler("亲!网络异常!请稍后再试");

                        }

                        @Override
                        public void onResponse(String result)
                        {
                            try{
                                JSONObject jsonObject=new JSONObject(result);
                                subimt(jsonObject);
                            }catch (Exception e)
                            {
                                postmainHandler("亲!网络异常!请稍后再试");

                            }
                        }
                    });
        }else if(view.getId()==R.id.payPayment)
        {
            payV2();
        }
    }
    public void subimt(final JSONObject json)
    {
        try{
            if(null != json && !json.has("retcode") ){
                PayReq req = new PayReq();
                req.appId			= json.getString("appid");
                req.partnerId		= json.getString("partnerid");
                req.prepayId		= json.getString("prepayid");
                req.nonceStr		= json.getString("noncestr");
                req.timeStamp		= json.getString("timestamp");
                req.packageValue	= json.getString("package");
                req.sign			= json.getString("sign");
                req.extData			= "app data";//optional
                postmainHandler("正常调起支付");
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.sendReq(req);
            }else{
                postmainHandler("亲!网络异常!请稍后再试");
            }
        }catch(Exception e){
            postmainHandler("亲!网络异常!请稍后再试");

        }
    }

    class MyAdapter extends BaseAdapter {
        public int getCount() {

            return resultList.size();
        }
        public Object getItem(int position) {

            return resultList.get(position);
        }
        @Override
        public long getItemId(int position) {
            System.out.println(""+position);
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.integral_recharge_list,null);
                v=layout;
                holder.integral=(TextView) v.findViewById(R.id.integral);
                holder.amountOfMoney=(TextView) v.findViewById(R.id.amountOfMoney);
                holder.discount=(TextView) v.findViewById(R.id.discount);
                holder.need=(TextView) v.findViewById(R.id.need);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.integral.setText(resultList.get(position).get("integral").toString());
            holder.amountOfMoney.setText(resultList.get(position).get("amountOfMoney").toString());
            holder.discount.setText(""+resultList.get(position).get("discount").toString()+"");
            if(resultList.get(position).get("id").toString().equals("1"))
            {
                holder.need.setBackgroundResource(R.drawable.selected);

            }else {
                holder.need.setBackgroundResource(R.drawable.unchecked);

            }
            return layout;
        }
        class MyHolder {
            public TextView integral;//日期
            public TextView amountOfMoney;
            public TextView discount;
            public TextView need;

        }
    }
    public void integral()
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"user/getUserScore/"+ Common.fuser_id,
                new OkHttpClientManager.ResultCallback<IntegralJson<Integral>>()
                {
                    @Override
                    public void onResponse(IntegralJson<Integral> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                jfmx.setText("账户积分余额:"+Common.integral+"积分");
                            }else{

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    public void getT(RegisterJson<Register> t) {
    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
    }
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        integral();
    }
    /**
     * 支付宝支付业务
     *
     * @param
     */
    public void payV2() {
        final JSONObject main=new JSONObject();

        final Map<String, String> params = new HashMap<String, String>();
        try {
            main.put("RegistryTel", Common.fuser_id);//手机号
            main.put("Score", ""+resultList.get(position1).get("integral").toString());//
            main.put("totalAmount",""+resultList.get(position1).get("amountOfMoney").toString());//
            Common.integral1=resultList.get(position1).get("integral").toString();
            Common.amountOfMoney= resultList.get(position1).get("amountOfMoney").toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("OrderData",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager
                        .BASE_URL+"alipay/payment?",params,
                new OkHttpClientManager.ResultCallback<String>()
                {
                    @Override
                    public void onError(Request request, Exception e)
                    {
                        e.printStackTrace();

                    }
                    @Override
                    public void onResponse(String result)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            //PayResult payResult = new PayResult((Map<String, String>)
                            //		result.);

                            /**
                             * 这里只是为了方便直接向商户展示支付宝的整个支付流程；；
                             * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
                             * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
                             *
                             * orderInfo的获取必须来自服务端；
                             */
                            final String orderInfo = jsonObject.getString("data");


                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(IntegralRechargeActivity.this);
                                    Map<String, String> result = alipay.payV2(orderInfo, true);
                                    Log.i("msp", result.toString());

                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };

                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        }catch (Exception e)
                        {

                        }
                    }
                });
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();// 同步返回需要验证的信息

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                       // Toast.makeText(IntegralRechargeActivity.this, "支付成功", Toast
                       //         .LENGTH_SHORT).show();
                        startActivity(new Intent(IntegralRechargeActivity.this, PayEntryActivity.class));

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(IntegralRechargeActivity.this, ""+memo, Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(IntegralRechargeActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(IntegralRechargeActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
}
