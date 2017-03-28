package com.example.dm_skb.ui.activity.newcustomer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
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
import com.example.dm_skb.widget.*;
import android.view.MotionEvent;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.PullToRefreshBase.Mode;
import com.example.dm_skb.widget.PullToRefreshBase.OnRefreshListener2;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.autolayout.AutoLinearLayout;
import android.os.SystemClock;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 客户新增
 */

public  class NewCustomerActivity extends BaseActivity {

    private PullToRefreshListView listView;
    public List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();//
    TextView register_main;
    TextView fcust_name;
    LinearLayout fcust_name_layout;
    private LayoutInflater mInflater;
    MyAdapter adapter;
    private ListView basic_list;
    private LinearLayout Company_information_layout;
    private TextView email;
    private TextView address;
    private TextView main_channel;
    private TextView main_farea;
    private TextView main_brand;
    private LinearLayout channel_layout;
    Dialog dialog;
    private LinearLayout cemo_layout;
    private TextView fcust_cemo;

    private LinearLayout introduce_layout;
    private TextView introduce_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_customer_main);
        introduce_text=(TextView)findViewById(R.id.introduce_text);
        introduce_layout=(LinearLayout)findViewById(R.id.introduce_layout);
        introduce_layout.setVisibility(View.GONE);


        fcust_cemo=(TextView)findViewById(R.id.fcust_cemo);
        cemo_layout=(LinearLayout)findViewById(R.id.cemo_layout);
        cemo_layout.setVisibility(View.GONE);

        main_channel=(TextView)findViewById(R.id.main_channel);
        main_farea=(TextView)findViewById(R.id.main_farea);
        main_brand=(TextView)findViewById(R.id.main_brand);
        channel_layout=(LinearLayout)findViewById(R.id.channel_layout);
        channel_layout.setVisibility(View.GONE);
        register_main=(TextView)findViewById(R.id.register_main);
        register_main.setText("客户新增");
        fcust_name=(TextView)findViewById(R.id.fcust_name);
        fcust_name_layout=(LinearLayout)findViewById(R.id.fcustname_layout);
        fcust_name_layout.setVisibility(View.GONE);
        mInflater=LayoutInflater.from(this);
        adapter=new MyAdapter();
        basic_list=(ListView)findViewById(R.id.basic_list);
        basic_list.setAdapter(adapter);
        Company_information_layout=(LinearLayout)findViewById(R.id.Company_information_layout);
        Company_information_layout.setVisibility(View.GONE);
        email=(TextView)findViewById(R.id.email);
        address=(TextView)findViewById(R.id.address);

    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            finish();
        }else if(view.getId()==R.id.fcust_name_layout)
        {
            showUploadFileDialog();
        }else if(view.getId()==R.id.Contacts_layout)
        {
            addMyLinkMan();
        }else if(view.getId()==R.id.Company_information)
        {

            Companyinformation(email.getText().toString(),address.getText().toString());
        }else if(view.getId()==R.id.channel)
        {
            channeldialog();
        }else if(view.getId()==R.id.themain)
        {
            remark(fcust_cemo.getText().toString());
        }else if(R.id.introduce==view.getId())
        {
            remark1(fcust_cemo.getText().toString());

        }else if(R.id.ok==view.getId())
        {
            if(fcust_name.getText().toString().length()==0)
            {
                postmainHandler("客户名称不能为空");
                return;
            }
            //submit();
            remind();
        }
    }
    private void remark1(String cemo) {
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
        final TextView title_view=(TextView)window.findViewById(R.id.title_view);
        title_view.setText("请添加公司概况");
        if(cemo.length()==0)
        {
            remark.setHint("请填写公司概况");

        }else {
            remark.setText(""+cemo);

        }
        remark.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
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
                introduce_text.setText(""+remark.getText().toString());
                introduce_layout.setVisibility(View.VISIBLE);
                dlg.cancel();
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
        final TextView title_view=(TextView)window.findViewById(R.id.title_view);
        title_view.setText("请添加主营商品");
        remark.setHint("请填写公司概况");
        if(cemo.length()==0)
        {
            remark.setHint("请填写公司概况");

        }else {
            remark.setText(""+cemo);

        }
        remark.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
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
                fcust_cemo.setText(""+remark.getText().toString());
                cemo_layout.setVisibility(View.VISIBLE);
                dlg.cancel();
            }
        });
    }
    private void channeldialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.channel_dialog, null);

        dlg.setView(layout);

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.channel_dialog);


        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.53); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        final EditText cemo2=(EditText)window.findViewById(R.id.cemo2);
        final EditText cemo1=(EditText)window.findViewById(R.id.cemo1);
        final EditText farea=(EditText)window.findViewById(R.id.farea);

        cemo2.setInputType(InputType.TYPE_CLASS_TEXT);
        cemo1.setInputType(InputType.TYPE_CLASS_TEXT);
        farea.setInputType(InputType.TYPE_CLASS_TEXT);
        if(cemo1.getText().toString().length()==0)
        {

        }else {
            main_channel.setText(""+cemo1.getText().toString());
        }

        if(farea.getText().toString().length()==0)
        {

        }else {
            main_farea.setText(""+farea.getText().toString());
        }

        if(cemo2.getText().toString().length()==0)
        {

        }else {
            main_brand.setText(""+cemo2.getText().toString());
        }
        Button submit=(Button)window.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_channel.setText(""+cemo1.getText().toString());
                main_farea.setText(""+farea.getText().toString());
                main_brand.setText(""+cemo2.getText().toString());
                channel_layout.setVisibility(View.VISIBLE);
                dlg.cancel();
            }
        });
    }
    private void Companyinformation(final String mailbox1,final String address2) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.company_information_dialog, null);

        dlg.setView(layout);

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.company_information_dialog);


        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.43); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        final EditText mailbox=(EditText)window.findViewById(R.id.mailbox);
        final EditText address1=(EditText)window.findViewById(R.id.address);
        address1.setInputType(InputType.TYPE_CLASS_TEXT);
        if(mailbox1.length()==0)
        {

        }else {
            mailbox.setText(""+mailbox1);
        } if(address2.length()==0)
        {

        }else {
            address1.setText(""+address2);
        }
        mailbox.setInputType(InputType.TYPE_CLASS_TEXT);

        Button submit=(Button)window.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mailbox.getText().toString().length()==0)
                {
                    Company_information_layout.setVisibility(View.VISIBLE);
                    email.setText(""+mailbox.getText().toString());
                    address.setText(""+address1.getText().toString());
                    dlg.cancel();
                }else {
                    if(!isEmail(mailbox.getText().toString()))
                    {
                        postmainHandler("请输入正确的邮箱");
                        return;
                    }
                    Company_information_layout.setVisibility(View.VISIBLE);
                    email.setText(""+mailbox.getText().toString());
                    address.setText(""+address1.getText().toString());
                    dlg.cancel();
                }

            }
        });

    }
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

                    listlikeman(sfTel.getText().toString(),Sflinkman.getText().toString());
                    dlg.cancel();
                }else {
                    if(sfTel.getText().toString().length()==0)
                    {
                        listlikeman(sfTel.getText().toString(),Sflinkman.getText().toString());
                        dlg.cancel();
                        return;
                    }

                    listlikeman(sfTel.getText().toString(),Sflinkman.getText().toString());
                    dlg.cancel();
                }
            }
        });
    }
    public void listlikeman(String sfTel,String Sflinkman)
    {
        Map<String, String> arg=new HashMap<String, String>();
        arg.put("Sflinkman",""+Sflinkman);
        arg.put("sfTel",""+sfTel);
        postmainHandler(""+sfTel);
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
            listItem.measure(0, 0);
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
    private void showUploadFileDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.fcust_name_dialog, null);

        dlg.setView(layout);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.fcust_name_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.34); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        Button submit=(Button)window.findViewById(R.id.submit);
        final TextView fcustname=(TextView)window.findViewById(R.id.fcustname);

        fcustname.setInputType(InputType.TYPE_CLASS_TEXT);
        if(fcust_name.getText().toString().length()==0)
        {

        }else {
            fcustname.setText(""+fcust_name.getText().toString());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fcustname.getText().toString().length()==0)
                {
                    postmainHandler("客户名称不能为空");
                }else
                {
                    fcust_name.setVisibility(View.VISIBLE);
                    fcust_name_layout.setVisibility(View.VISIBLE);
                    fcust_name.setText(""+fcustname.getText().toString());
                    dlg.cancel();
                }
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
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.linkman.setText("联系人   "+resultList.get(position).get("Sflinkman").toString());
            holder.telephone.setText("电  话   "+resultList.get(position).get("sfTel").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NewCustomerActivity.this.onClick(view, parent, p, one);
                }
            });

            return layout;
        }
        class MyHolder {
            public TextView linkman;//
            public TextView telephone;
            public ImageView phone;
        }
    }
    public void onClick(View item, View widget, int position, int which) {
        switch (which) {
            case R.id.phone:
                //call_dialog(resultList.get(position).get("period").toString());
                break;
            default:
                break;
        }
    }
    /**
     * 提交信息
     */
    private void submit()
    {
        dialog= CustomProgress.show(NewCustomerActivity.this, "正在提交新客户信息", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("fUser_Id", Common.fuser_id);//手机号
            main.put("CustName", ""+fcust_name.getText().toString());//公司名称
            main.put("fEmail", ""+email.getText().toString());//邮箱
            main.put("fAddress", ""+address.getText().toString());//地址
            main.put("fMarketingChannel", ""+main_channel.getText().toString());//渠道
            main.put("fPrimaryMarket", ""+main_farea.getText().toString());//区域
            main.put("fAgentBrand", ""+main_brand.getText().toString());//品牌
            main.put("fPrimaryCargo", ""+fcust_cemo.getText().toString());//主营商品
            main.put("Descs", ""+introduce_text.getText().toString());//公司概况
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        for(int i=0;i<resultList.size();i++){
            try {
                JSONObject maina = new JSONObject();
                try {

                    maina.put("fLinkMan", ""+resultList.get(i).get("Sflinkman").toString());//联系人
                    maina.put("fTel",""+resultList.get(i).get("sfTel").toString());//联系电话

                    filearray.put(maina);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
              main.put("linkdata",""+filearray.toString());
             } catch (Exception e) {
                e.printStackTrace();
            }
        params.put("customerdata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"customer/addMyNewCustomer?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){

                                postmainHandler(""+u.getData().getSuessCode());
                                finish();
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
    private void remind() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        AutoLinearLayout layout = (AutoLinearLayout)mInflater.inflate(R.layout.remind_dialog, null);
        dlg.setView(layout);
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.remind_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();

        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        TextView ok=(TextView)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
                dlg.cancel();
            }
        });
        TextView qx=(TextView)window.findViewById(R.id.qx);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }
}
