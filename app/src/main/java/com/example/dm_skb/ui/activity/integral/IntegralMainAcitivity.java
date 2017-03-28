package com.example.dm_skb.ui.activity.integral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by yangxiaoping on 16/9/10.
 */

public class IntegralMainAcitivity extends BaseActivity implements ResponseT<RegisterJson<Register>> {
    private LinearLayout Return;
    private ImageView Integralbill;
    IntegralDao integralDao;
    private TextView jf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integral_management_main);
        Return=(LinearLayout) findViewById(R.id.Return);
        integralDao=new IntegralDao();

        jf=(TextView)findViewById(R.id.jf);
        jf.setText(Common.integral);
        integral();
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
                                jf.setText(""+u.getData().getfUsableScores());
                            }else{

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {

            finish();
        }else if(view.getId()==R.id.Integralbill)//积分账单
        {
            startActivity(new Intent(IntegralMainAcitivity.this, IntegralbillActivity.class));

        }else if(view.getId()==R.id.Integraldetail)//积分明细
        {
            startActivity(new Intent(IntegralMainAcitivity.this, IntegralDetailActivity.class));

        }else if(view.getId()==R.id.Integralcostbreakdown)//积分消耗明细
        {
            startActivity(new Intent(IntegralMainAcitivity.this, IntegralCostBreakdownActivity.class));
        }else if(view.getId()==R.id.exchange)//积分兑换
        {
            Intent intent=new Intent(IntegralMainAcitivity.this,ExchangeActivity.class);
            startActivityForResult(intent, 0);

        }else if(view.getId()==R.id.rechargequery)//充值查询
        {
            startActivity(new Intent(IntegralMainAcitivity.this, RechargeQueryActivity.class));
        }else if(view.getId()==R.id.Points)//赚取积分
        {
            //积分充值
            Intent intent=new Intent(IntegralMainAcitivity.this,IntegralRechargeActivity.class);
            startActivityForResult(intent, 0);
            //充值成功
           // startActivity(new Intent(IntegralMainAcitivity.this, RechargeTipsActivity.class));

        }
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
}