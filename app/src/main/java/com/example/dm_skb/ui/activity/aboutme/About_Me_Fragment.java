package com.example.dm_skb.ui.activity.aboutme;



import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.bean.NewsTotal;
import com.example.dm_skb.bean.NewsTotalJson;
import com.example.dm_skb.ui.activity.apply.ApplyActivity;
import com.example.dm_skb.ui.activity.integral.EarnPointsActivity;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.activity.fragment.BaseFragment;
import com.example.dm_skb.ui.activity.integral.IntegralMainAcitivity;
import com.example.dm_skb.ui.activity.news.NewsMainActivity;
import com.example.dm_skb.ui.activity.setup.SetUpActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class About_Me_Fragment extends BaseFragment implements ResponseT<RegisterJson<Register>> {

    private AutoLinearLayout Integralmanagement;//积分管理
    private AutoLinearLayout Commodityaddress;//商品地址
    private AutoLinearLayout corporatename;//公司名称
    private AutoLinearLayout Points;//赚取积分
    private AutoLinearLayout news;
    private AutoLinearLayout Apply;
    public static  TextView integral_text;//积分
    private TextView fcust_name;//客户名称
    private TextView city_name;//城市名称
    Dialog dialog;
    IntegralDao integralDao;
    private AutoLinearLayout city_id;
    private AutoLinearLayout fuser_name_layout;
    private TextView fuser_name;
    private ImageView setup;
    public static ImageView right;
    public static TextView number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_me_main, container, false);
    }
    /**
     * 获取xml控件
     */
   
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        right=(ImageView)view.findViewById(R.id.right);
        number=(TextView)view.findViewById(R.id.number);
        right.setVisibility(View.GONE);
        Integralmanagement=(AutoLinearLayout)view.findViewById(R.id.Integralmanagement);
        Integralmanagement.setOnClickListener(clickListener);
        Commodityaddress=(AutoLinearLayout)view.findViewById(R.id.Commodityaddress);
        Commodityaddress.setOnClickListener(clickListener);
        corporatename=(AutoLinearLayout)view.findViewById(R.id.corporatename);
        corporatename.setOnClickListener(clickListener);
        city_id=(AutoLinearLayout)view.findViewById(R.id.city_id);
        city_id.setOnClickListener(clickListener);
        integral_text=(TextView)view.findViewById(R.id.integral);
        fcust_name=(TextView)view.findViewById(R.id.fcust_name);
        city_name=(TextView)view.findViewById(R.id.city_name);
        fuser_name_layout=(AutoLinearLayout)view.findViewById(R.id.fuser_name_layout);
        Apply=(AutoLinearLayout)view.findViewById(R.id.Apply);
        Apply.setOnClickListener(clickListener);
        fuser_name_layout.setOnClickListener(clickListener);
        fuser_name=(TextView) view.findViewById(R.id.fuser_name);
        setup=(ImageView) view.findViewById(R.id.setup);
        setup.setOnClickListener(clickListener);
        integralDao=new IntegralDao();
        fuser_name.setText(""+Common.fUser_Name);
        fcust_name.setText(""+Common.CompanyName);
        city_name.setText(""+Common.fCityName);
        Points=(AutoLinearLayout)view.findViewById(R.id.Points);
        Points.setOnClickListener(clickListener);
        integral();
        news=(AutoLinearLayout)view.findViewById(R.id.news);
        news.setOnClickListener(clickListener);
        getMyNewsTotal();
    }
    public static void integral()
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
                                    integral_text.setText(""+u.getData().getfUsableScores());
                                    Common.integral=u.getData().getfUsableScores();
                                }else{
                                }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    public OnClickListener clickListener=new OnClickListener() {

        public void onClick(View v) {
            if(v==Integralmanagement)
            {
                startActivity(new Intent(getActivity(), IntegralMainAcitivity.class));

            }else if(v==Commodityaddress)
            {
                Intent intent = new Intent(getActivity(), LogisticsAddressActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==corporatename)
            {
                Intent intent = new Intent(getActivity(), CorporateNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fcust_name", city_name.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==city_id)
            {
                Intent intent = new Intent(getActivity(), CityAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("city_name", city_name.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==fuser_name_layout)
            {
                Intent intent = new Intent(getActivity(), NameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("fuser_name", fuser_name.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==setup)
            {
                startActivity(new Intent(getActivity(), SetUpActivity.class));
            }else if(v==Points)
            {
                startActivity(new Intent(getActivity(), EarnPointsActivity.class));
            }else if(v==news)
            {
                Intent intent = new Intent(getActivity(), NewsMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("activity", "About_Me_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }else if(v==Apply)
            {
                startActivity(new Intent(getActivity(), ApplyActivity.class));

            }

        }
    };
    public void getT(RegisterJson<Register> t) {
        dialog.dismiss();
    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
    }
    public void submit()
    {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("Name", "");//公司名称
            main.put("fThirdpartyId", "");//备注
            main.put("Action", "5");//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
            main.put("NickName", "5");//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
            main.put("flogistics", "");//评价星数 行为参数=6 则值不能为空
            main.put("freceiver", "");//纠错标致 行为参数=5 则值不能为空  1 无此客户 2 客户渠道信息有误 3企业基础信息有误 4 电话号码信息有误
            // 5 地址信息有误
            // 6  网址邮箱信息有误 7联系人信息有误 8联系人电话有误
            main.put("freceiverTel", "");//

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
                                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), ""+meta.getMsg
                                        (), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "亲!网络异常!请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "亲!网络异常!请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    String fcustName;
    String id;
    String cityName;
    String fusername="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        id=arg2!=null?arg2.getStringExtra("id"):"";
        if(id==null)
        {

        }else if(id.equals("2"))
        {
            fcustName=arg2!=null?arg2.getStringExtra("fcust_name"):"";
            fcust_name.setText(""+fcustName);
        }else if(id.equals("3"))
        {
            cityName=arg2!=null?arg2.getStringExtra("city_name"):"";
            city_name.setText(""+cityName);
        }else if(id.equals("1"))
        {
            fusername=arg2!=null?arg2.getStringExtra("fuser_name"):"";
            fuser_name.setText(""+fusername);
        }
        getMyNewsTotal();
    }
    public static void getMyNewsTotal()
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"mynews/getMyNewsTotal/"+ Common
                .fuser_id,
                new OkHttpClientManager.ResultCallback<NewsTotalJson<NewsTotal>>()
                {
                    @Override
                    public void onResponse(NewsTotalJson<NewsTotal> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                String num=u.getData().getTotal()!=null? u.getData().getTotal():"0";

                                if(num.equals("0"))
                                {
                                    right.setVisibility(View.VISIBLE);
                                    number.setVisibility(View.GONE);
                                }else
                                {
                                    right.setVisibility(View.GONE);
                                    number.setText(""+u.getData().getTotal());
                                }
                            }else{
                                right.setVisibility(View.VISIBLE);
                                number.setVisibility(View.GONE);

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        number.setVisibility(View.GONE);
                        right.setVisibility(View.VISIBLE);
                    }
                });
    }
}
