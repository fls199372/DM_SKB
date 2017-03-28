package com.example.dm_skb.ui.activity.searchcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.CompanyDetail;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseJsonCustDetail;
import com.example.dm_skb.dao.CusetDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.activity.integral.EarnPointsActivity;
import com.example.dm_skb.ui.activity.integral.ExchangeActivity;
import com.example.dm_skb.ui.activity.integral.IntegralRechargeActivity;
import com.example.dm_skb.ui.activity.main.Defaultcontent;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.example.dm_skb.widget.RatingBar;
import com.squareup.okhttp.Request;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by yangxiaoping on 16/9/10.
 * 客户明细
 */

public  class CustomerDetailActivity extends BaseActivity implements ResponseT<ResponseJsonCustDetail<CompanyDetail>> {

    private TextView register_main;
    private ImageView Contacts;//增加联系人
    private List<ApplicationInfo> mAppList;

    Dialog dialog;
    private String fCompanyID="";
    private String fCompanyName="";
    private String position="";
    private String activity="";
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    private SwipeMenuListView basic_list;
    private LayoutInflater mInflater;
    MyAdapter adapter;
    private CusetDao cusetDao;
    private TextView cemo;
    private TextView email;
    private TextView address;
    private TextView fMarketingChannel;
    private TextView fAgentBrand;
    private TextView ffMarketingClassProperty;
    private TextView fPrimaryCargo;
    private TextView fCompanyProfile;
    private TextView fCheckDate;
    private TextView fBrowseQty;
    private RatingBar sectorView;
    private String IsEvaluate="";
    private ImageView share;
    ScrollView scrollView1;
    private ImageView location;
    private  LinearLayout yc;
    UMImage image = new UMImage(CustomerDetailActivity.this, "http://shdingmou.cn/dmsock/MainIcon.png");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customerdetailmain1);
        mAppList = getPackageManager().getInstalledApplications(0);
        mInflater=LayoutInflater.from(this);
        UserID();
        Bundle bundle=getIntent().getExtras();
        fCompanyID=bundle.getString("fcust_id");
        position=bundle.getString("position");
        fCompanyName=bundle.getString("fcust_name");
        activity=bundle.getString("activity");
        register_main=(TextView)findViewById(R.id.register_main);
        basic_list=(SwipeMenuListView)findViewById(R.id.basic_list1);
        adapter = new MyAdapter();
        basic_list.setAdapter(adapter);
        sectorView=(RatingBar) findViewById(R.id.sectorView);
        cemo=(TextView) findViewById(R.id.cemo);
        email=(TextView) findViewById(R.id.email);
        address=(TextView)findViewById(R.id.address);
        location=(ImageView) findViewById(R.id.location);
        fMarketingChannel=(TextView)findViewById(R.id.fMarketingChannel);
        fAgentBrand=(TextView)findViewById(R.id.fAgentBrand);
        ffMarketingClassProperty=(TextView)findViewById(R.id.ffMarketingClassProperty);
        fPrimaryCargo=(TextView)findViewById(R.id.fPrimaryCargo);
        fCompanyProfile=(TextView)findViewById(R.id.fCompanyProfile);
        fCheckDate=(TextView)findViewById(R.id.fCheckDate);
        fBrowseQty=(TextView)findViewById(R.id.fBrowseQty);
        share=(ImageView) findViewById(R.id.share);
        scrollView1=(ScrollView)findViewById(R.id.scrollView1);
        scrollView1.smoothScrollTo(0,20);
        cusetDao=new CusetDao();
        dialog= CustomProgress.show(CustomerDetailActivity.this, "正在提交查询客户明细", false, null);
        cusetDao.getCompanyDetail(fCompanyID+"/"+Common.fuser_id,this);
        yc=(LinearLayout) findViewById(R.id.yc);
        yc.setVisibility(View.GONE);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(80));
                // set a icon
                deleteItem.setTitle("删除");
                deleteItem.setTitleColor(Color.WHITE);
                deleteItem.setTitleSize(18);

                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        basic_list.setMenuCreator(creator);

        basic_list.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
              //  ApplicationInfo item = mAppList.get(position);

                switch (index) {
                    case 0:
                        if(resultList.get(position).get("fCanDel").toString().equals("true"))
                        {
                            postmainHandler(resultList.get(position).get("LinkId").toString()
                                    +""+position);

                            updateDataSet(resultList.get(position).get("LinkId").toString(),position);

                        }else {
                            postmainHandler("系统联系人不能删除");
                            adapter.notifyDataSetChanged();

                        }
                        break;
                    case 1:

                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        basic_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    scrollView1.requestDisallowInterceptTouchEvent(false);

                }else{
                    scrollView1.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

    }

    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);
            intent.setComponent(componentName);
            startActivity(intent);
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

   /* public void updateDataSet(int position) {
        resultList.remove(position);
        adapter.notifyDataSetChanged();
    }*/
    /**
     * 提交信息
     */
    private void updateDataSet(final String position1,final int position)
    {
       // resultList.remove(position);
       // adapter.notifyDataSetChanged();
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
                main.put("fUser_ID", Common.fuser_id);//手机号
                main.put("fmylinkautoid", ""+position1);//公司ID
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("linkmandata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"linkman/deleteMyLinkMan?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                resultList.remove(position);
                                setListViewHeightBasedOnChildren(basic_list);
                                final ScrollView svResult = (ScrollView) findViewById(R.id.scrollView1);
                                svResult.post(new Runnable() {
                                    public void run() {
                                        svResult.fullScroll(ScrollView.FOCUS_DOWN);
                                        svResult.smoothScrollTo(0,0);
                                    }
                                });
                                adapter.notifyDataSetChanged();
                            }else{
                            }
                        }else{
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.plans)
        {
            Intent intent = new Intent(CustomerDetailActivity.this,ReminderProgramActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fcust_id", fCompanyID);
            bundle.putString("position", "" + position);
            bundle.putString("fcust_name", fCompanyName);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.errorcorrection)
        {
            if(IsCorrection.equals("1"))
            {
                postmainHandler("您已提交纠错信息\n不能重复提交！");
                return;
            }
            Intent intent = new Intent(CustomerDetailActivity.this, CorrectingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fcust_id", fCompanyID);
            bundle.putString("position", "" + position);
            bundle.putString("fcust_name", fCompanyName);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.evaluate)
        {
            if(IsEvaluate.equals("1"))
            {
                postmainHandler("您已评价过\n不能再次评价！");
                return;
            }
            showUploadFileDialog();
        }else if(view.getId()==R.id.Contacts)
        {
            addMyLinkMan();
        }else if(view.getId()==R.id.add_cemo)
        {
            remark(cemo.getText().toString());
        }else if(view.getId()==R.id.share)
        {
            shareDialog();
        }else if(view.getId()==R.id.location)
        {
            Intent intent = new Intent(CustomerDetailActivity.this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("fcust_id", fCompanyID);
            bundle.putString("position", "" + position);
            bundle.putString("fcust_name", fCompanyName);
            bundle.putString("fGPSX", fGPSX);
            bundle.putString("fGPSY", fGPSY);
            bundle.putString("fAddress", fAddress);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
        }else if(view.getId()==R.id.visit)
        {
            startActivity(new Intent(CustomerDetailActivity.this, EarnPointsActivity.class));
            // visitDialog();
        }
    }
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName)
                .exists();
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("SINA"))
            {
                submitshare("1");
            }else if(platform.name().equals("QQ"))
            {
                submitshare("2");
            }else if(platform.name().equals("QZONE"))
            {
                submitshare("3");
            }else if(platform.name().equals("WEIXIN"))
            {
                submitshare("4");
            }
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            postmainHandler(platform+"分享失败啦");
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            postmainHandler("您已取消分享");
        }
    };
    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {


        @Override
        public void onclick(SnsPlatform snsPlatform,SHARE_MEDIA share_media) {
            new ShareAction(CustomerDetailActivity.this).setPlatform(share_media).setCallback(umShareListener)
                    .withText("多平台分享")
                    .share();
        }
    };
    private void shareDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this,R.style.Custom_Progress).create();

        dlg.show();
        Window win = dlg.getWindow();
        win.setContentView(R.layout.share_dialog);

        WindowManager m = this.getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        m.getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = (int) (dm.heightPixels * 0.2); // 高度设置为屏幕的0.6，根据实际情况调整
        params.width = (int) (dm.widthPixels); // 宽度设置为屏幕的0.65，根据实际情况调整
        win.setWindowAnimations(R.style.dialog_animation);
        win.setAttributes(params);
        dlg.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        dlg.show();
       final TextView textView=(TextView)win.findViewById(R.id.qx);
        if(IsCollect.equals("1"))
        {
            textView.setText("取消");
        }
        //收藏
        AutoLinearLayout Collection_layout=(AutoLinearLayout)win.findViewById(R.id.Collection_layout);
        Collection_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsCollect.equals("1"))
                {
                    IsCollect="0";
                }else
                {
                    IsCollect="1";
                }

                submit1("","","2","");
                dlg.cancel();

            }
        });
        //WX分享
        AutoLinearLayout wx_layout=(AutoLinearLayout)win.findViewById(R.id.wx_layout);
        wx_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHARE_MEDIA platform= SHARE_MEDIA.WEIXIN;
                new ShareAction(CustomerDetailActivity.this)
                        .withTitle(Defaultcontent.title)
                        .withText(Defaultcontent.text+"")
                        .withTargetUrl(Defaultcontent.url)
                        .setPlatform(platform)
                        .withMedia(image)
                        .setCallback(umShareListener)
                        .share();
                dlg.cancel();
            }
        });
        //QQ分享
        AutoLinearLayout QQ_layout=(AutoLinearLayout)win.findViewById(R.id.QQ_layout);
        QQ_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHARE_MEDIA platform= SHARE_MEDIA.QQ;
                new ShareAction(CustomerDetailActivity.this)
                        .withTitle(Defaultcontent.title)
                        .withText(Defaultcontent.text+"")
                        .withTargetUrl(Defaultcontent.url)
                        .setPlatform(platform)
                        .withMedia(image)
                        .setCallback(umShareListener)
                        .share();
                dlg.cancel();
            }
        });
        //微博
        AutoLinearLayout wb=(AutoLinearLayout)win.findViewById(R.id.wb);
        wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHARE_MEDIA platform= SHARE_MEDIA.SINA;
                new ShareAction(CustomerDetailActivity.this)
                        .withTitle(Defaultcontent.title)
                        .withText(Defaultcontent.text+"")
                        .withTargetUrl(Defaultcontent.url)
                        .withMedia(image)
                        .setPlatform(platform)
                        .setCallback(umShareListener)
                        .share();
                dlg.cancel();

            }
        });
        //QQ空间
        AutoLinearLayout space=(AutoLinearLayout)win.findViewById(R.id.space);
        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHARE_MEDIA platform= SHARE_MEDIA.QZONE;
                new ShareAction(CustomerDetailActivity.this)
                        .withTitle(Defaultcontent.title)
                        .withText(Defaultcontent.text+"")
                        .withTargetUrl(Defaultcontent.url)
                        .withMedia(image)
                        .setPlatform(platform)
                        .setCallback(umShareListener)
                        .share();
                dlg.cancel();

            }
        });
    }

    /**
     * 提交信息
     */
    private void submitshare(String type)
    {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_Id", Common.fuser_id);//手机号
            main.put("ShareType", type);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("sharedata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"company/addUserShare?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                postmainHandler(""+u.getData().getSuessCode());
                            }else{

                            }
                        }else{

                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {

                    }
                });
    }
    private void visitDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.visit_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();

        Button ok=(Button)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dlg.cancel();
            }
        });
    }
    float a;
    private void showUploadFileDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.evaluate_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        final RatingBar ratingBar=(RatingBar)window.findViewById(R.id.sectorView);
        AutoLinearLayout xxx=(AutoLinearLayout)window.findViewById(R.id.xxx);
        a=ratingBar.getRating();
        RatingBar.xx="0";
        Button ok=(Button)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(IsEvaluate.equals("1"))
                {
                    already_evaluated();
                    dlg.cancel();

                }else {
                    if(xxxxx.equals("0"))
                    {
                        postmainHandler("评价星数不能为空");
                        return;
                    }
                    xxxxx=ratingBar.getRating1();
                    submit1("","","6",xxxxx);
                    dlg.cancel();

                }
            }
        });
    }
    private void already_evaluated() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alreadyevaluated_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.28); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        TextView cemo=(TextView)window.findViewById(R.id.cemo);
        cemo.setText("您已评价过,不能再次评价!");
        TextView ok=(TextView)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });


    }
    String xxxxx="";
    private void addMyLinkMan() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.addmylinkman_dialog, null);

        dlg.setView(layout);

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.addmylinkman_dialog);

        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.43); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        final EditText Sflinkman=(EditText)window.findViewById(R.id.Sflinkman);
        final EditText sfTel=(EditText)window.findViewById(R.id.sfTel);
        sfTel.setInputType(InputType.TYPE_CLASS_NUMBER);
        Sflinkman.setInputType(InputType.TYPE_CLASS_TEXT);

        Button submit=(Button)window.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Sflinkman.getText().toString().length()==0)
                {
                    if(sfTel.getText().toString().length()==0)
                    {
                        postmainHandler("联系人与电话不能都为空");
                        return;
                    }
                    dlg.cancel();
                    submit(Sflinkman.getText().toString(),sfTel.getText().toString());
                }else {
                    if(sfTel.getText().toString().length()==0)
                    {
                        dlg.cancel();
                        submit(Sflinkman.getText().toString(),sfTel.getText().toString());
                        return;
                    }
                    dlg.cancel();
                    submit(Sflinkman.getText().toString(),sfTel.getText().toString());
                }
            }
        });
    }

    private void remark(String cemo) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.remark_dialog, null);
        dlg.setView(layout);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.remark_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.48); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        final EditText remark=(EditText)window.findViewById(R.id.remark);
        final TextView length=(TextView)window.findViewById(R.id.length);
        final Button submit=(Button)window.findViewById(R.id.submit);
        remark.setText(""+cemo);
        remark.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
                length.setText("已填写字数"+temp.length()+"/200");
            }
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            public void afterTextChanged(Editable s) {

                selectionStart = remark.getSelectionStart();
                selectionEnd = remark.getSelectionEnd();
                //System.out.println("start="+selectionStart+",end="+selectionEnd);
                if (temp.length() > 200) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    remark.setText(s);
                    remark.setSelection(tempSelection);//设置光标在最后

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(remark.getText().toString().length()==0)
                {
                    postmainHandler("备注不能为空");
                    return;
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                submit1(remark.getText().toString(),""+df.format(new Date()),"4","");
                dlg.cancel();
            }
        });
    }
    /**
     * 提交信息
     */
    private void submit1(final String remark,final String dDate,final String Action,final  String
            iStarQty)
    {
        dialog= CustomProgress.show(CustomerDetailActivity.this, "正在提交信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("fCompanyID", fCompanyID);//公司ID
            main.put("fCompanyName", fCompanyName);//公司名称
            main.put("sEvent", remark);//联系人
            main.put("Action", Action);//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
            main.put("iStarQty", iStarQty);//评价星数 行为参数=6 则值不能为空
            main.put("iErrFalg", "");//纠错标致 行为参数=5 则值不能为空  1 无此客户 2 客户渠道信息有误 3企业基础信息有误 4 电话号码信息有误 5 地址信息有误
            // 6  网址邮箱信息有误 7联系人信息有误 8联系人电话有误
            main.put("dDate", dDate);//

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
                                dialog.cancel();
                                cemo.setText(""+remark);
                                postmainHandler(""+u.getData().getSuessCode());
                            }else{
                                dialog.cancel();
                                postmainHandler(""+meta.getMsg
                                        ());

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
    /**
     * 提交信息
     */
    private void submit(final String Sflinkman,final String sfTel)
    {
        dialog= CustomProgress.show(CustomerDetailActivity.this, "正在提交联系人信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_ID", Common.fuser_id);//手机号
            main.put("fCompanyID", fCompanyID);//公司ID
            main.put("fCompanyName", fCompanyName);//公司名称
            main.put("sflinkman", Sflinkman);//联系人
            main.put("sfTel", sfTel);//联系电话

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("linkmandata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"linkman/addMyLinkMan?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                Map<String, String> arg=new HashMap<String, String>();
                                arg.put("Sflinkman",Sflinkman);
                                arg.put("sfTel",sfTel);
                                arg.put("fCanDel","true");
                                arg.put("LinkId",""+u.getData().getfMyLinkAutoID());
                                resultList.add(arg);
                                setListViewHeightBasedOnChildren(basic_list);
                                final ScrollView svResult = (ScrollView) findViewById(R.id.scrollView1);

                                svResult.post(new Runnable() {
                                  public void run() {
                                        svResult.fullScroll(ScrollView.FOCUS_DOWN);
                                      svResult.smoothScrollTo(0,0);

                                  }
                                });

                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                                postmainHandler("联系人添加成功");
                            }else{
                                dialog.cancel();
                                postmainHandler(""+meta.getMsg
                                        ());

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
                layout=(LinearLayout) mInflater.inflate(R.layout.contact_list,null);
                v=layout;
                holder.linkman=(TextView) v.findViewById(R.id.linkman);
                holder.telephone=(TextView) v.findViewById(R.id.telephone);
                holder.see=(TextView) v.findViewById(R.id.see);

                holder.phone=(ImageView) v.findViewById(R.id.phone);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.linkman.setText("联系人   "+resultList.get(position).get("Sflinkman").toString());
            holder.telephone.setText("电  话   "+resultList.get(position).get("sfTel").toString());
            holder.telephone.setVisibility(View.GONE);
            holder.phone.setVisibility(View.GONE);

            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CustomerDetailActivity.this.onClick(view, parent, p, one);
                }
            });
            final int one_see = holder.see.getId();

            holder.see.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    CustomerDetailActivity.this.onClick(view, parent, p, one_see);
                }
            });
            return layout;
        }
        class MyHolder {
            public TextView linkman;//
            public TextView telephone;
            public ImageView phone;
            public TextView see;
        }
    }
//    boolean isFirstUse;
//    // 读取ShareedPreferences中需要的数据
//    SharedPreferences preferences = this.getSharedPreferences("remind",
//            this.MODE_PRIVATE);
//    isFirstUse=preferences.getBoolean("isFirstUse", true);
//    if(isFirstUse)
//    {
//        showUploadFileDialog(resultList.get(position).get("fcust_id").toString(),
//                position,resultList.get(position).get("fcust_name")
//                        .toString(),Common.integral);
//    }else {
//        submit(resultList.get(position).get("fcust_id").toString(),position,
//                resultList.get(position).get("fcust_name").toString());
//    }
    public void onClick(View item, View widget, int position, int which) {
        switch (which) {
            case R.id.phone:
                call_dialog(resultList.get(position).get("sfTel").toString(),resultList.get(position).get("LinkId").toString());
                break;
            case R.id.see:
                boolean isFirstUse;
                // 读取ShareedPreferences中需要的数据
                SharedPreferences preferences = this.getSharedPreferences("remind",
                        this.MODE_PRIVATE);
                isFirstUse=preferences.getBoolean("isFirstUse", true);
                if(isFirstUse)
                {
                    showUploadFileDialog(fCompanyID,
                            position,fCompanyName,Common.integral);
                }else {

                }
                break;
            default:
                break;
        }
    }
    private void showUploadFileDialog(final String fcust_id,final  int position,final String
            fcust_name,final String UsableScores) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.remind_points_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.8); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.show();
        TextView integral=(TextView)window.findViewById(R.id.integral);
        integral.setText("账户积分余额："+UsableScores+"分");
        Button no_need=(Button)window.findViewById(R.id.no_need);
        no_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences =CustomerDetailActivity.this.getSharedPreferences("remind",
                        CustomerDetailActivity.this.MODE_PRIVATE);
                //实例化editor对象
                SharedPreferences.Editor editor=preferences.edit();
                //存入数据true
                editor.putBoolean("isFirstUse",false);
                //提交修改
                editor.commit();
                dlg.cancel();
                //submit(fcust_id,position,fcust_name);
            }
        });
        Button need=(Button)window.findViewById(R.id.need);
        need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  submit(fcust_id,position,fcust_name);
                dlg.cancel();
            }
        });
        Button cancel=(Button)window.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        AutoLinearLayout Recharge=(AutoLinearLayout)window.findViewById(R.id.Recharge);
        Recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //积分充值
                startActivity(new Intent(CustomerDetailActivity.this, IntegralRechargeActivity.class));
                dlg.cancel();
            }
        });

        AutoLinearLayout earn=(AutoLinearLayout)window.findViewById(R.id.earn);
        earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //赚积分
                //赚积分
                Intent intent=new Intent(CustomerDetailActivity.this,StartActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "2");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                dlg.cancel();
            }
        });
        AutoLinearLayout exchange=(AutoLinearLayout)window.findViewById(R.id.exchange);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //积分兑换
                Intent intent=new Intent(CustomerDetailActivity.this,ExchangeActivity.class);
                startActivityForResult(intent, 0);
                dlg.cancel();
            }
        });
    }
    private void call_dialog(final String phone,final String LinkId) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.call_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        TextView cemo=(TextView)window.findViewById(R.id.cemo);
        cemo.setText("是否呼叫  "+phone);
        TextView cancel=(TextView)window.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        TextView call=(TextView)window.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit2(phone,LinkId);
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + ""+phone);
                intent.setData(data);
                startActivity(intent);
                dlg.cancel();
            }
        });
    }
    /**
     * 提交信息
     */
    private void submit2(final String remark,final  String LinkId)
    {
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
                main.put("fUser_ID", Common.fuser_id);//手机号
                main.put("fCompanyID", fCompanyID);//公司ID
                main.put("fCompanyName", fCompanyName);//公司名称
                main.put("sEvent", remark);//联系人
                main.put("dDate","");
                main.put("iStarQty","");
                main.put("Action", "3");//行为参数 1提醒计划（自动进入待联系）2 收藏 3 已联系 4 我要备注 5 客户纠错 6 我要评价
                main.put("iErrFalg", "");//纠错标致 行为参数=5 则值不能为空  1 无此客户 2 客户渠道信息有误 3企业基础信息有误 4 电话号码信息有误 5 地址信息有误
                // 6  网址邮箱信息有误 7联系人信息有误 8联系人电话有误
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
                            }else{
                            }
                        }else{
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                    }
                });
    }
    String IsCollect="";
    public void getT(ResponseJsonCustDetail<CompanyDetail> t) {
        for(int i=0;i<t.getData().getLinkInfo().size();i++)
        {
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("Sflinkman",t.getData().getLinkInfo().get(i).getFlinkman()==null?"":t.getData
                    ().getLinkInfo().get(i).getFlinkman());
            arg.put("sfTel",t.getData().getLinkInfo().get(i).getfTel()==null?"":t.getData
                    ().getLinkInfo().get(i).getfTel());
            arg.put("fCanDel",t.getData().getLinkInfo().get(i).getfCanDel()==null?"":t.getData
                    ().getLinkInfo().get(i).getfCanDel());
            arg.put("LinkId",t.getData().getLinkInfo().get(i).getLinkId()==null?"":t.getData
                    ().getLinkInfo().get(i).getLinkId());
            resultList.add(arg);
        }
        if(t.getData().getIsCollect()==null)
        {
            IsCollect="";
        }else {
            IsCollect=t.getData().getIsCollect();
        }
        sectorView.setStep1(Float.parseFloat(t.getData().getfEvaluateStarQty()));
        cemo.setText(""+t.getData().getfMyMemo());
        email.setText("邮箱:"+t.getData().getfEmail());
        address.setText("地址:"+t.getData().getfAddress());
        fAgentBrand.setText("代理品牌:"+t.getData().getfAgentBrand());
        ffMarketingClassProperty.setText("代理品类:"+t.getData().getFfMarketingClassProperty());
        fMarketingChannel.setText("经营渠道:"+t.getData().getfMarketingChannel());
        fPrimaryCargo.setText("主营商品:"+t.getData().getfPrimaryCargo());
        fCompanyProfile.setText("企业介绍:"+t.getData().getfCompanyProfile());
        fCheckDate.setText("成立日期:"+t.getData().getfCheckDate()+"\n注册资本:"+t.getData()
                .getfRegisteredAssets()+"\n经营范围:"+t.getData().getfBusinessScope()+"\n注册地址:"+t
                .getData
                ().getfRegAddress());
        fBrowseQty.setText("浏览人数: "+t.getData().getfBrowseQty()+"\n评价人数: "+t.getData()
                .getfEvaluateQty()+"");
        IsEvaluate=t.getData().getIsEvaluate();
        IsCorrection=t.getData().getIsCorrection();
        fGPSX=t.getData().getfGPSX();
        fGPSY= t.getData().getfGPSY();
        fAddress=t.getData().getfAddress();
        register_main.setText(""+t.getData().getfCompany_Name());
        setListViewHeightBasedOnChildren(basic_list);
        final ScrollView svResult = (ScrollView) findViewById(R.id.scrollView1);
        svResult.post(new Runnable() {
            public void run() {

                svResult.fullScroll(ScrollView.FOCUS_DOWN);
                svResult.smoothScrollTo(0,0);

            }
        });

        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }
    String fAddress="";
    String fGPSX="";
    String fGPSY="";
    String IsCorrection="";
    @Override
    public void getError(String msg) {
        // TODO Auto-generated method stub
        postmainHandler(""+msg);
        dialog.dismiss();
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0;i<listAdapter.getCount(); i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0,0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
            System.out.println("totalHeight"+totalHeight);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        //Log.d("result","onActivityResult");
    }
}
