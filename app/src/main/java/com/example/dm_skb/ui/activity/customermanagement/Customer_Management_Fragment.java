package com.example.dm_skb.ui.activity.customermanagement;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Companycollection;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Linkman;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.MySeeCompanyList;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.dao.CusetDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.activity.fragment.BaseFragment;
import com.example.dm_skb.ui.activity.searchcustomer.CustomerDetailActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.example.dm_skb.widget.ILoadingLayout;
import com.example.dm_skb.widget.PullToRefreshBase;
import com.example.dm_skb.widget.PullToRefreshBase.Mode;
import com.example.dm_skb.widget.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Customer_Management_Fragment extends BaseFragment implements ResponseT<ResponseList<Companycollection>> {

    Button Collection;//收藏
    Button contact;//未联系
    Button ok_contact;//已联系
    Button Already_view;//已查看
    PullToRefreshListView Collection_list;//收藏客户
    PullToRefreshListView to_be_contacted_tomorrow_list;//明天待联系
    MyAdapter1 to_Be_Contacted_Tomorrow_Adapter;//明天待联系
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();
    public List<Map<String, String>> tomorrow_list=new ArrayList<Map<String,String>>();
    public List<Map<String, String>> touch_today_list=new ArrayList<Map<String,String>>();
    public List<Map<String, String>> history_linked_list=new ArrayList<Map<String,String>>();
    public List<Map<String, String>> future_contacted_list=new ArrayList<Map<String,String>>();
    public List<Map<String, String>> linkman_list=new ArrayList<Map<String,String>>();

    MyAdapter adapter;
    private LayoutInflater mInflater;
    MyAdapter2 stay_in_touch_today_adapter;

    AutoLinearLayout stay_in_touch_today;//今天待联系

    PullToRefreshListView stay_in_touch_today_list;

    AutoLinearLayout contact_layout;//待联系
    AutoLinearLayout history_to_be_linked;//历史待联系

    PullToRefreshListView history_to_be_linked_list;
    MyAdapter3 linked_list_adapter;
    MyAdapter4 future_contacted_adapter;

    AutoLinearLayout future_to_be_contacted;//未来待联系
    PullToRefreshListView future_to_be_contacted_list;
    AutoLinearLayout ok_contact_layout;//已联系
    AutoLinearLayout to_be_contacted_tomorrow;//明天待联系


    AutoLinearLayout to_be_contacted_tomorrow_ok;//昨天已联系
    PullToRefreshListView to_be_contacted_tomorrow_list_ok;//
    MyAdapter6 contacted_tomorrow_adapter;

    public List<Map<String, String>> contacted_tomorrow_list=new ArrayList<Map<String,String>>();

    AutoLinearLayout stay_in_touch_today_ok;//今天已联系
    PullToRefreshListView stay_in_touch_today_ok_list;//
    MyAdapter5 stay_today_adapter;

    public List<Map<String, String>> stay_touch_today_list=new ArrayList<Map<String,String>>();


    AutoLinearLayout history_to_be_linked_ok;//一周前已联系
    PullToRefreshListView history_to_be_linked_list_ok;//
    MyAdapter7 history_to_be_linked_adapter;
    public List<Map<String, String>> history_linked_ok_list=new ArrayList<Map<String,String>>();

    AutoLinearLayout future_to_be_contacted_ok;//一月内已联系
    PullToRefreshListView months_ago_list;//
    MyAdapter9 months_ago_adapter;

    public List<Map<String, String>> months_ago_ok_list=new ArrayList<Map<String,String>>();

    AutoLinearLayout months_ago;//一月前已联系
    PullToRefreshListView future_to_be_contacted_list_ok;//
    MyAdapter8 future_to_be_contacted_adapter;

    public List<Map<String, String>> future_contacted_ok_list=new ArrayList<Map<String,String>>();

    PullToRefreshListView getMySeeCompanyList_list;
    MyAdapter10 getMySeeCompanyList_adapter;
    List<Map<String, String>> getMySeeCompanyListlist=new
            ArrayList<Map<String,String>>();

    CusetDao cusetDao;
    Dialog dialog;
    int PageIndex=1;
    int PageIndex1=1;
    int PageIndex2=1;
    int PageIndex3=1;
    int PageIndex4=1;
    int PageIndex5=1;
    int PageIndex6=1;
    int PageIndex7=1;
    int PageIndex8=1;
    int PageIndex9=1;
    int PageIndex10=1;

    int tyepID=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_customer, container, false);
    }
    /**
     * 获取xml控件
     */
   
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog= CustomProgress.show(getActivity(), "正在查询收藏客户", false, null);

        ok_contact_layout=(AutoLinearLayout)view.findViewById(R.id.ok_contact_layout);
        stay_in_touch_today=(AutoLinearLayout)view.findViewById(R.id.stay_in_touch_today);
        contact_layout=(AutoLinearLayout)view.findViewById(R.id.contact_layout);
        history_to_be_linked=(AutoLinearLayout)view.findViewById(R.id.history_to_be_linked);
        stay_in_touch_today=(AutoLinearLayout)view.findViewById(R.id.stay_in_touch_today);
        stay_in_touch_today.setOnClickListener(clickListener);
        history_to_be_linked.setOnClickListener(clickListener);
        future_to_be_contacted=(AutoLinearLayout)view.findViewById(R.id.future_to_be_contacted);
        future_to_be_contacted.setOnClickListener(clickListener);
        Collection_list=(PullToRefreshListView)view.findViewById(R.id.Collection_list);
        to_be_contacted_tomorrow=(AutoLinearLayout)view.findViewById(R.id.to_be_contacted_tomorrow);
        Collection=(Button)view.findViewById(R.id.Collection);
        contact=(Button)view.findViewById(R.id.contact);
        ok_contact=(Button)view.findViewById(R.id.ok_contact);
        Already_view=(Button)view.findViewById(R.id.Already_view);
        Collection.setOnClickListener(clickListener);
        contact.setOnClickListener(clickListener);
        ok_contact.setOnClickListener(clickListener);
        Already_view.setOnClickListener(clickListener);
        to_be_contacted_tomorrow.setOnClickListener(clickListener);
        cusetDao=new CusetDao();
        mInflater=LayoutInflater.from(getActivity());

        Collection_list.setOnItemClickListener(clickListener1);

        ListView actualListView = Collection_list.getRefreshableView();
        adapter = new MyAdapter();
        actualListView.setAdapter(adapter);

        Collection_list.setMode(Mode.BOTH);
        //   listView.setOnItemClickListener(this);
        init();
        tyepID=1;
        cusetDao.getCompanycollection(Common.fuser_id+"/1/1/"+PageIndex,this);

        Collection_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex=1;
                tyepID=1;
                resultList.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/1/1/"+PageIndex,
                        Customer_Management_Fragment.this);
                new FinishRefresh().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex++;
                tyepID=1;
                cusetDao.getCompanycollection(Common.fuser_id+"/1/1/"+PageIndex,
                        Customer_Management_Fragment.this);
                new FinishRefresh().execute();

            }

        });
        Collection_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();;
                bundle.putString("fcust_id",resultList.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",resultList.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        to_be_contacted_tomorrow_list=(PullToRefreshListView)view.findViewById(R.id.to_be_contacted_tomorrow_list);
        to_be_contacted_tomorrow_list.setVisibility(View.GONE);
        to_be_contacted_tomorrow_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",tomorrow_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",tomorrow_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        MTDLX();
        stay_in_touch_today_list=(PullToRefreshListView)view.findViewById(R.id.stay_in_touch_today_list);
        stay_in_touch_today_list.setVisibility(View.GONE);
        stay_in_touch_today_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",touch_today_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",touch_today_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        JTDLX();
        history_to_be_linked_list=(PullToRefreshListView)view.findViewById(R.id.history_to_be_linked_list);
        history_to_be_linked_list.setVisibility(View.GONE);
        history_to_be_linked_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",history_linked_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",history_linked_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        LSDLX();
        future_to_be_contacted_list=(PullToRefreshListView)view.findViewById(R.id.future_to_be_contacted_list);
        future_to_be_contacted_list.setVisibility(View.GONE);
        future_to_be_contacted_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",future_contacted_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",future_contacted_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        WLDLX();

        stay_in_touch_today_ok=(AutoLinearLayout)view.findViewById(R.id.stay_in_touch_today_ok);
        stay_in_touch_today_ok.setOnClickListener(clickListener);
        stay_in_touch_today_ok_list=(PullToRefreshListView)view.findViewById(R.id.stay_in_touch_today_ok_list);
        stay_in_touch_today_ok_list.setVisibility(View.GONE);
        stay_in_touch_today_ok_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",stay_touch_today_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",stay_touch_today_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        JTYLX();

        to_be_contacted_tomorrow_ok=(AutoLinearLayout)view.findViewById(R.id.to_be_contacted_tomorrow_ok);
        to_be_contacted_tomorrow_ok.setOnClickListener(clickListener);
        to_be_contacted_tomorrow_list_ok=(PullToRefreshListView)view.findViewById(R.id.to_be_contacted_tomorrow_list_ok);
        to_be_contacted_tomorrow_list_ok.setVisibility(View.GONE);
        to_be_contacted_tomorrow_list_ok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",contacted_tomorrow_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",contacted_tomorrow_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        ZTYLX();

        history_to_be_linked_ok=(AutoLinearLayout)view.findViewById(R.id.history_to_be_linked_ok);
        history_to_be_linked_ok.setOnClickListener(clickListener);
        history_to_be_linked_list_ok=(PullToRefreshListView)view.findViewById(R.id.history_to_be_linked_list_ok);
        history_to_be_linked_list_ok.setVisibility(View.GONE);
        history_to_be_linked_list_ok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",history_linked_ok_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",history_linked_ok_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        YZQYLX();

        future_to_be_contacted_ok=(AutoLinearLayout)view.findViewById(R.id.future_to_be_contacted_ok);
        future_to_be_contacted_ok.setOnClickListener(clickListener);
        future_to_be_contacted_list_ok=(PullToRefreshListView)view.findViewById(R.id.future_to_be_contacted_list_ok);
        future_to_be_contacted_list_ok.setVisibility(View.GONE);
        future_to_be_contacted_list_ok.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",future_contacted_ok_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",future_contacted_ok_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        YGQYLX();

        months_ago=(AutoLinearLayout)view.findViewById(R.id.months_ago);
        months_ago.setOnClickListener(clickListener);
        months_ago_list=(PullToRefreshListView)view.findViewById(R.id.months_ago_list);
        months_ago_list.setVisibility(View.GONE);
        months_ago_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",months_ago_ok_list.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",months_ago_ok_list.get(position).get("fcuer_name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        YGYQLX();
        getMySeeCompanyList_list=(PullToRefreshListView)view.findViewById(R.id.getMySeeCompanyList_list);
        getMySeeCompanyList_list.setVisibility(View.GONE);
        getMySeeCompanyList_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position=position-1;
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("fcust_id",getMySeeCompanyListlist.get(position).get("fCompanyId").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",getMySeeCompanyListlist.get(position).get("fCompany_Name").toString());
                bundle.putString("activity","Customer_Management_Fragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });
        YCK();

    }
    public void YCK()
    {
        getMySeeCompanyList_adapter=new MyAdapter10();
        ListView actualListView1 = getMySeeCompanyList_list.getRefreshableView();
        actualListView1.setAdapter(getMySeeCompanyList_adapter);
        getMySeeCompanyList_list.setMode(PullToRefreshBase.Mode.BOTH);
        init10();

        getMySeeCompanyList_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex10=1;
                getMySeeCompanyListlist.clear();
                getMySeeCompanyList(Common.fuser_id,PageIndex10);
                new FinishRefresh10().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex10++;
                getMySeeCompanyList(Common.fuser_id,PageIndex10);

                new FinishRefresh10().execute();

            }

        });
    }
    private void init10()
    {
        ILoadingLayout startLabels = getMySeeCompanyList_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = getMySeeCompanyList_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        getMySeeCompanyList_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        getMySeeCompanyList_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        getMySeeCompanyList_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        getMySeeCompanyList_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        getMySeeCompanyList_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        getMySeeCompanyList_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh10 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            getMySeeCompanyList_list.onRefreshComplete();
        }
    }

    public void YGYQLX()
    {
        months_ago_adapter=new MyAdapter9();
        ListView actualListView1 = months_ago_list.getRefreshableView();
        actualListView1.setAdapter(months_ago_adapter);
        months_ago_list.setMode(PullToRefreshBase.Mode.BOTH);
        init8();

        months_ago_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex9=1;
                tyepID=10;
                months_ago_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/5/"+PageIndex9,
                        Customer_Management_Fragment.this);
                new FinishRefresh9().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex9++;
                tyepID=10;
                cusetDao.getCompanycollection(Common.fuser_id+"/3/5/"+PageIndex9,
                        Customer_Management_Fragment.this);
                new FinishRefresh9().execute();

            }

        });
    }
    private void init9()
    {
        ILoadingLayout startLabels = months_ago_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = months_ago_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        months_ago_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        months_ago_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        months_ago_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        months_ago_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        months_ago_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        months_ago_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh9 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            months_ago_list.onRefreshComplete();
        }
    }



    public void YGQYLX()
    {
        future_to_be_contacted_adapter=new MyAdapter8();
        ListView actualListView1 = future_to_be_contacted_list_ok.getRefreshableView();
        actualListView1.setAdapter(future_to_be_contacted_adapter);
        future_to_be_contacted_list_ok.setMode(PullToRefreshBase.Mode.BOTH);
        init8();

        future_to_be_contacted_list_ok.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex8=1;
                tyepID=9;
                future_contacted_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/4/"+PageIndex8,
                        Customer_Management_Fragment.this);
                new FinishRefresh8().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex8++;
                tyepID=9;
                cusetDao.getCompanycollection(Common.fuser_id+"/3/4/"+PageIndex8,
                        Customer_Management_Fragment.this);
                new FinishRefresh8().execute();

            }

        });
    }
    private void init8()
    {
        ILoadingLayout startLabels = future_to_be_contacted_list_ok
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = future_to_be_contacted_list_ok.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        future_to_be_contacted_list_ok.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh8 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            future_to_be_contacted_list_ok.onRefreshComplete();
        }
    }


    public void YZQYLX()
    {
        history_to_be_linked_adapter=new MyAdapter7();
        ListView actualListView1 = history_to_be_linked_list_ok.getRefreshableView();
        actualListView1.setAdapter(history_to_be_linked_adapter);
        history_to_be_linked_list_ok.setMode(PullToRefreshBase.Mode.BOTH);
        init7();

        history_to_be_linked_list_ok.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex7=1;
                tyepID=8;
                history_linked_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/3/"+PageIndex7,
                        Customer_Management_Fragment.this);
                new FinishRefresh7().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex7++;
                tyepID=8;
                cusetDao.getCompanycollection(Common.fuser_id+"/3/3/"+PageIndex7,
                        Customer_Management_Fragment.this);
                new FinishRefresh7().execute();

            }

        });
    }
    private void init7()
    {
        ILoadingLayout startLabels = history_to_be_linked_list_ok
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = history_to_be_linked_list_ok.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        history_to_be_linked_list_ok.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        history_to_be_linked_list_ok.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        history_to_be_linked_list_ok.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        history_to_be_linked_list_ok.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        history_to_be_linked_list_ok.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        history_to_be_linked_list_ok.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh7 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            history_to_be_linked_list_ok.onRefreshComplete();
        }
    }

    public void ZTYLX()
    {
        contacted_tomorrow_adapter=new MyAdapter6();
        ListView actualListView1 = to_be_contacted_tomorrow_list_ok.getRefreshableView();
        actualListView1.setAdapter(contacted_tomorrow_adapter);
        to_be_contacted_tomorrow_list_ok.setMode(PullToRefreshBase.Mode.BOTH);
        init6();

        to_be_contacted_tomorrow_list_ok.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex6=1;
                tyepID=7;
                contacted_tomorrow_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/2/"+PageIndex6,
                        Customer_Management_Fragment.this);
                new FinishRefresh6().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex6++;
                tyepID=7;
                cusetDao.getCompanycollection(Common.fuser_id+"/3/2/"+PageIndex6,
                        Customer_Management_Fragment.this);
                new FinishRefresh6().execute();

            }

        });
    }
    private void init6()
    {
        ILoadingLayout startLabels = to_be_contacted_tomorrow_list_ok
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        to_be_contacted_tomorrow_list_ok.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh6 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            to_be_contacted_tomorrow_list_ok.onRefreshComplete();
        }
    }

    public void JTYLX()
    {
        stay_today_adapter=new MyAdapter5();
        ListView actualListView1 = stay_in_touch_today_ok_list.getRefreshableView();
        actualListView1.setAdapter(stay_today_adapter);
        stay_in_touch_today_ok_list.setMode(PullToRefreshBase.Mode.BOTH);
        init5();

        stay_in_touch_today_ok_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex5=1;
                tyepID=6;
                stay_touch_today_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/1/"+PageIndex5,
                        Customer_Management_Fragment.this);
                new FinishRefresh5().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex5++;
                tyepID=6;
                cusetDao.getCompanycollection(Common.fuser_id+"/3/1/"+PageIndex5,
                        Customer_Management_Fragment.this);
                new FinishRefresh5().execute();

            }

        });
    }
    private void init5()
    {
        ILoadingLayout startLabels = stay_in_touch_today_ok_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = stay_in_touch_today_ok_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        stay_in_touch_today_ok_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh5 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            stay_in_touch_today_ok_list.onRefreshComplete();
        }
    }

    public void WLDLX()
    {
        future_contacted_adapter=new MyAdapter4();
        ListView actualListView1 = future_to_be_contacted_list.getRefreshableView();
        actualListView1.setAdapter(future_contacted_adapter);
        history_to_be_linked_list.setMode(PullToRefreshBase.Mode.BOTH);
        init4();

        future_to_be_contacted_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex4=1;
                tyepID=5;
                future_contacted_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/4/"+PageIndex4,
                        Customer_Management_Fragment.this);
                new FinishRefresh4().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex4++;
                tyepID=5;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/4/"+PageIndex4,
                        Customer_Management_Fragment.this);
                new FinishRefresh4().execute();

            }

        });
    }
    private void init4()
    {
        ILoadingLayout startLabels = future_to_be_contacted_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = future_to_be_contacted_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        future_to_be_contacted_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        future_to_be_contacted_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        future_to_be_contacted_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        future_to_be_contacted_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        future_to_be_contacted_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        future_to_be_contacted_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh4 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            future_to_be_contacted_list.onRefreshComplete();
        }
    }

    public void LSDLX()
    {
        linked_list_adapter=new MyAdapter3();
        ListView actualListView1 = history_to_be_linked_list.getRefreshableView();
        actualListView1.setAdapter(linked_list_adapter);
        history_to_be_linked_list.setMode(PullToRefreshBase.Mode.BOTH);
        init3();

        history_to_be_linked_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex3=1;
                tyepID=4;
                history_linked_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/3/"+PageIndex3,
                        Customer_Management_Fragment.this);
                new FinishRefresh3().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex3++;
                tyepID=4;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/3/"+PageIndex3,
                        Customer_Management_Fragment.this);
                new FinishRefresh3().execute();

            }

        });
    }
    private void init3()
    {
        ILoadingLayout startLabels = history_to_be_linked_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = history_to_be_linked_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        history_to_be_linked_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        history_to_be_linked_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        history_to_be_linked_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        history_to_be_linked_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        history_to_be_linked_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        history_to_be_linked_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh3 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            history_to_be_linked_list.onRefreshComplete();
        }
    }

    public void JTDLX()
    {
        stay_in_touch_today_adapter=new MyAdapter2();
        ListView actualListView1 = stay_in_touch_today_list.getRefreshableView();
        actualListView1.setAdapter(stay_in_touch_today_adapter);
        stay_in_touch_today_list.setMode(PullToRefreshBase.Mode.BOTH);
        init2();

        stay_in_touch_today_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex2=1;
                touch_today_list.clear();
                tyepID=2;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/1/"+PageIndex2,
                        Customer_Management_Fragment.this);
                new FinishRefresh2().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex2++;
                tyepID=2;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/1/"+PageIndex2,
                        Customer_Management_Fragment.this);
                new FinishRefresh2().execute();

            }

        });
    }
    private void init2()
    {
        ILoadingLayout startLabels = stay_in_touch_today_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = stay_in_touch_today_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        stay_in_touch_today_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        stay_in_touch_today_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        stay_in_touch_today_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        stay_in_touch_today_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        stay_in_touch_today_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        stay_in_touch_today_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            stay_in_touch_today_list.onRefreshComplete();
        }
    }
    public void MTDLX()
    {
        to_Be_Contacted_Tomorrow_Adapter=new MyAdapter1();
        ListView actualListView1 = to_be_contacted_tomorrow_list.getRefreshableView();
        actualListView1.setAdapter(to_Be_Contacted_Tomorrow_Adapter);
        to_be_contacted_tomorrow_list.setMode(PullToRefreshBase.Mode.BOTH);
        init1();

        to_be_contacted_tomorrow_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex1=1;
                tyepID=3;
                tomorrow_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/2/"+PageIndex1,
                        Customer_Management_Fragment.this);
                new FinishRefresh1().execute();

            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex1++;
                tyepID=3;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/2/"+PageIndex1,
                        Customer_Management_Fragment.this);
                new FinishRefresh1().execute();

            }

        });
    }
    private void init1()
    {
        ILoadingLayout startLabels = to_be_contacted_tomorrow_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = to_be_contacted_tomorrow_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        to_be_contacted_tomorrow_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh1 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
//        	to_Be_Contacted_Tomorrow_Adapter.notifyDataSetChanged();
            to_be_contacted_tomorrow_list.onRefreshComplete();
        }
    }
    private AdapterView.OnItemClickListener clickListener1=new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long arg3) {
            startActivity(new Intent(getActivity(), CustomerDetailActivity.class));
        }
    };
    private void init()
    {
        ILoadingLayout startLabels = Collection_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = Collection_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        Collection_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        Collection_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        Collection_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        Collection_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        Collection_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        Collection_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
//        	adapter.notifyDataSetChanged();
            Collection_list.onRefreshComplete();
        }
    }
    public View.OnClickListener clickListener=new View.OnClickListener() {

        public void onClick(View v) {
            if(Collection==v)
            {
                resultList.clear();
                Collection.setBackgroundResource(R.drawable.shape_button34);
                Collection.setTextColor(Color.parseColor("#FFFFFF"));
                contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                contact.setTextColor(Color.parseColor("#50FFFFFF"));
                ok_contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ok_contact.setTextColor(Color.parseColor("#50FFFFFF"));
                Collection_list.setVisibility(View.VISIBLE);
                contact_layout.setVisibility(View.GONE);
                ok_contact_layout.setVisibility(View.GONE);
                getMySeeCompanyList_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                Already_view.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Already_view.setTextColor(Color.parseColor("#50FFFFFF"));

            }else if(contact==v)
            {
                contact.setBackgroundResource(R.drawable.shape_button34);
                contact.setTextColor(Color.parseColor("#FFFFFF"));
                Collection.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Collection.setTextColor(Color.parseColor("#50FFFFFF"));
                ok_contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ok_contact.setTextColor(Color.parseColor("#50FFFFFF"));
                contact_layout.setVisibility(View.VISIBLE);
                Collection_list.setVisibility(View.GONE);
                ok_contact_layout.setVisibility(View.GONE);
                getMySeeCompanyList_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                Already_view.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Already_view.setTextColor(Color.parseColor("#50FFFFFF"));
            }else if(Already_view==v)
            {
                Already_view.setBackgroundResource(R.drawable.shape_button34);
                Already_view.setTextColor(Color.parseColor("#FFFFFF"));
                contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                contact.setTextColor(Color.parseColor("#50FFFFFF"));
                Collection.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Collection.setTextColor(Color.parseColor("#50FFFFFF"));
                ok_contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                ok_contact.setTextColor(Color.parseColor("#50FFFFFF"));
                getMySeeCompanyList_list.setVisibility(View.VISIBLE);
                contact_layout.setVisibility(View.GONE);
                Collection_list.setVisibility(View.GONE);
                ok_contact_layout.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                getMySeeCompanyListlist.clear();
                getMySeeCompanyList(Common.fuser_id,PageIndex10);
            }
            else if(v==ok_contact)
            {
                Already_view.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Already_view.setTextColor(Color.parseColor("#50FFFFFF"));
                ok_contact.setBackgroundResource(R.drawable.shape_button34);
                ok_contact.setTextColor(Color.parseColor("#FFFFFF"));
                contact.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                contact.setTextColor(Color.parseColor("#50FFFFFF"));
                Collection.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                Collection.setTextColor(Color.parseColor("#50FFFFFF"));
                ok_contact_layout.setVisibility(View.VISIBLE);
                Collection_list.setVisibility(View.GONE);
                contact_layout.setVisibility(View.GONE);
                getMySeeCompanyList_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);

            }else if(v==to_be_contacted_tomorrow)
            {
                //明天待联系
                dialog= CustomProgress.show(getActivity(), "正在查询明天待联系", false, null);
                stay_in_touch_today_list.setVisibility(View.GONE);
                history_to_be_linked_list.setVisibility(View.GONE);
                future_to_be_contacted_list.setVisibility(View.GONE);
                tomorrow_list.clear();
                tyepID=3;
                cusetDao.getCompanycollection(Common.fuser_id+"/2/2/"+PageIndex1,
                        Customer_Management_Fragment.this);


            }else if(v==stay_in_touch_today)
            {
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                history_to_be_linked_list.setVisibility(View.GONE);
                future_to_be_contacted_list.setVisibility(View.GONE);

                //今天待联系
                dialog= CustomProgress.show(getActivity(), "正在查询今天待联系", false, null);

                tyepID=2;
                touch_today_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/1/"+PageIndex2,
                        Customer_Management_Fragment.this);

            }else if(v==history_to_be_linked)
            {
                stay_in_touch_today_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                future_to_be_contacted_list.setVisibility(View.GONE);

                //今天待联系
                dialog= CustomProgress.show(getActivity(), "正在查询历史待联系", false, null);

                tyepID=4;
                history_linked_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/3/"+PageIndex3,
                        Customer_Management_Fragment.this);
            }else if(v==future_to_be_contacted)
            {
                history_to_be_linked_list.setVisibility(View.GONE);
                stay_in_touch_today_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list.setVisibility(View.GONE);
                //未来待联系
                dialog= CustomProgress.show(getActivity(), "正在查询未来待联系", false, null);

                tyepID=5;
                future_contacted_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/2/4/"+PageIndex4,
                        Customer_Management_Fragment.this);
            }else if(stay_in_touch_today_ok==v)
            {
                //今天已联系
                tyepID=6;

                months_ago_list.setVisibility(View.GONE);
                future_to_be_contacted_list_ok.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list_ok.setVisibility(View.GONE);
                history_to_be_linked_list_ok.setVisibility(View.GONE);
                dialog= CustomProgress.show(getActivity(), "正在查询今天已联系", false, null);

                stay_touch_today_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/1/"+PageIndex5,
                        Customer_Management_Fragment.this);
            }else if(to_be_contacted_tomorrow_ok==v)
            {
                //昨天已联系
                tyepID=7;
                months_ago_list.setVisibility(View.GONE);
                stay_in_touch_today_ok_list.setVisibility(View.GONE);
                history_to_be_linked_list_ok.setVisibility(View.GONE);
                future_to_be_contacted_list_ok.setVisibility(View.GONE);
                dialog= CustomProgress.show(getActivity(), "正在查询今天已联系", false, null);
                contacted_tomorrow_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/2/"+PageIndex6,
                        Customer_Management_Fragment.this);
            }else if(v==history_to_be_linked_ok)
            {

                //一周前已联系
                tyepID=8;
                months_ago_list.setVisibility(View.GONE);
                stay_in_touch_today_ok_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list_ok.setVisibility(View.GONE);
                future_to_be_contacted_list_ok.setVisibility(View.GONE);

                dialog= CustomProgress.show(getActivity(), "正在查询一周前已联系", false, null);

                history_linked_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/3/"+PageIndex7,
                        Customer_Management_Fragment.this);
            }else if(v==future_to_be_contacted_ok)
            {
                //一月内已联系
                tyepID=9;
                months_ago_list.setVisibility(View.GONE);
                stay_in_touch_today_ok_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list_ok.setVisibility(View.GONE);
                history_to_be_linked_list_ok.setVisibility(View.GONE);
                dialog= CustomProgress.show(getActivity(), "正在查询一个月前已联系", false, null);
                months_ago_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/4/"+PageIndex8,
                        Customer_Management_Fragment.this);
            }else if(months_ago==v)
            {
                //一月内已联系
                tyepID=10;
                stay_in_touch_today_ok_list.setVisibility(View.GONE);
                to_be_contacted_tomorrow_list_ok.setVisibility(View.GONE);
                history_to_be_linked_list_ok.setVisibility(View.GONE);
                future_to_be_contacted_list_ok.setVisibility(View.GONE);
                months_ago_ok_list.clear();
                dialog= CustomProgress.show(getActivity(), "正在查询一个月前已联系", false, null);

                future_contacted_ok_list.clear();
                cusetDao.getCompanycollection(Common.fuser_id+"/3/5/"+PageIndex9,
                        Customer_Management_Fragment.this);
            }

        }
    };
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
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(resultList.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(resultList.get(position).get("fMyMemo").toString());
            holder.date.setText(resultList.get(position).get("sTime").toString());
            if(resultList.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(resultList.get(p).get("fcust_id").toString(),resultList.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;
        }
    }

    class MyAdapter1 extends BaseAdapter {
        public int getCount() {

            return tomorrow_list.size();
        }
        public Object getItem(int position) {

            return tomorrow_list.get(position);
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
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(tomorrow_list.get(position).get("fcuer_name").toString());
            if(tomorrow_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(tomorrow_list.get(p).get("fcust_id").toString(),tomorrow_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;


        }
    }

    class MyAdapter2 extends BaseAdapter {
        public int getCount() {

            return touch_today_list.size();
        }
        public Object getItem(int position) {

            return touch_today_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(touch_today_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(touch_today_list.get(position).get("fMyMemo").toString());
            holder.date.setText(touch_today_list.get(position).get("sTime").toString());
            if(touch_today_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(touch_today_list.get(p).get("fcust_id").toString(),touch_today_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter3 extends BaseAdapter {
        public int getCount() {

            return history_linked_list.size();
        }
        public Object getItem(int position) {

            return history_linked_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(history_linked_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(history_linked_list.get(position).get("fMyMemo").toString());
            holder.date.setText(history_linked_list.get(position).get("sTime").toString());
            if(history_linked_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(history_linked_list.get(p).get("fcust_id").toString(),history_linked_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter4 extends BaseAdapter {
        public int getCount() {

            return future_contacted_list.size();
        }
        public Object getItem(int position) {

            return future_contacted_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(future_contacted_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(future_contacted_list.get(position).get("fMyMemo").toString());
            holder.date.setText(future_contacted_list.get(position).get("sTime").toString());
            if(future_contacted_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(future_contacted_list.get(p).get("fcust_id").toString(),future_contacted_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter5 extends BaseAdapter {
        public int getCount() {

            return stay_touch_today_list.size();
        }
        public Object getItem(int position) {

            return stay_touch_today_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            if(stay_touch_today_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            holder.fcust_name.setText(stay_touch_today_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(stay_touch_today_list.get(position).get("fMyMemo").toString());
            holder.date.setText(stay_touch_today_list.get(position).get("sTime").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(stay_touch_today_list.get(p).get("fcust_id").toString(),stay_touch_today_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter6 extends BaseAdapter {
        public int getCount() {

            return contacted_tomorrow_list.size();
        }
        public Object getItem(int position) {

            return contacted_tomorrow_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            if(contacted_tomorrow_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            holder.fcust_name.setText(contacted_tomorrow_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(contacted_tomorrow_list.get(position).get("fMyMemo").toString());
            holder.date.setText(contacted_tomorrow_list.get(position).get("sTime").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(contacted_tomorrow_list.get(p).get("fcust_id").toString(),contacted_tomorrow_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter7 extends BaseAdapter {
        public int getCount() {

            return history_linked_ok_list.size();
        }
        public Object getItem(int position) {

            return history_linked_ok_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            if(history_linked_ok_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            holder.fcust_name.setText(history_linked_ok_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(history_linked_ok_list.get(position).get("fMyMemo").toString());
            holder.date.setText(history_linked_ok_list.get(position).get("sTime").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(history_linked_ok_list.get(p).get("fcust_id").toString(),history_linked_ok_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }
    class MyAdapter8 extends BaseAdapter {
        public int getCount() {

            return future_contacted_ok_list.size();
        }
        public Object getItem(int position) {

            return future_contacted_ok_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            if(future_contacted_ok_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            holder.fcust_name.setText(future_contacted_ok_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(future_contacted_ok_list.get(position).get("fMyMemo").toString());
            holder.date.setText(future_contacted_ok_list.get(position).get("sTime").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(history_linked_ok_list.get(p).get("fcust_id").toString(),history_linked_ok_list.get(p).get("fcuer_name").toString());
                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;
        }
    }
    class MyAdapter9 extends BaseAdapter {
        public int getCount() {

            return months_ago_ok_list.size();
        }
        public Object getItem(int position) {

            return months_ago_ok_list.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_collection_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.fcust_type=(TextView) v.findViewById(R.id.fcust_type);
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                holder.zzz=(ImageView) v.findViewById(R.id.zzz);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            if(months_ago_ok_list.get(position).get("sTime").toString().length()==0)
            {
                holder.zzz.setVisibility(View.GONE);

            }else {

            }
            holder.fcust_name.setText(months_ago_ok_list.get(position).get("fcuer_name").toString());
            holder.fcust_type.setText(months_ago_ok_list.get(position).get("fMyMemo").toString());
            holder.date.setText(months_ago_ok_list.get(position).get("sTime").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContactPhoneRecords(history_linked_ok_list.get(p).get("fcust_id").toString(),history_linked_ok_list.get(p).get("fcuer_name").toString());

                }
            });
            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView fcust_type;
            public TextView date;
            public ImageView phone;
            public ImageView zzz;

        }
    }


    public void getT(ResponseList<Companycollection> t) {
        if(tyepID==1)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getsTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                resultList.add(arg);
            }
            adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }else if(tyepID==2)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfAlertsTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                touch_today_list.add(arg);
            }
            stay_in_touch_today_list.setVisibility(View.VISIBLE);

            stay_in_touch_today_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }

        else if(tyepID==3)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfAlertsTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                tomorrow_list.add(arg);
            }
            to_be_contacted_tomorrow_list.setVisibility(View.VISIBLE);

            to_Be_Contacted_Tomorrow_Adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }else if(tyepID==4)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfAlertsTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                history_linked_list.add(arg);
            }
            history_to_be_linked_list.setVisibility(View.VISIBLE);

            linked_list_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }else if(tyepID==5)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfAlertsTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                future_contacted_list.add(arg);
            }
            future_to_be_contacted_list.setVisibility(View.VISIBLE);

            future_contacted_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }else if(tyepID==6)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfDialTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                stay_touch_today_list.add(arg);
            }
            stay_in_touch_today_ok_list.setVisibility(View.VISIBLE);

            stay_today_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }else if(tyepID==7)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfDialTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                contacted_tomorrow_list.add(arg);
            }
            to_be_contacted_tomorrow_list_ok.setVisibility(View.VISIBLE);

            contacted_tomorrow_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }

        else if(tyepID==8)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfDialTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                history_linked_ok_list.add(arg);
            }
            history_to_be_linked_list_ok.setVisibility(View.VISIBLE);

            history_to_be_linked_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }
        else if(tyepID==9)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfDialTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                future_contacted_ok_list.add(arg);
            }
            future_to_be_contacted_list_ok.setVisibility(View.VISIBLE);

            future_to_be_contacted_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        } else if(tyepID==10)
        {
            for(int i=0;i<t.getData().size();i++){
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("fAutoID",t.getData().get(i).getfAutoID());
                arg.put("fcuer_name",t.getData().get(i).getfCompany_Name());
                arg.put("fcust_id",t.getData().get(i).getfCompanyID());
                arg.put("rowID",t.getData().get(i).getRowId());
                arg.put("sTime",t.getData().get(i).getfDialTime());
                arg.put("fMyMemo",t.getData().get(i).getfMyMemo());
                months_ago_ok_list.add(arg);
            }
            months_ago_list.setVisibility(View.VISIBLE);

            months_ago_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            dialog.dismiss();
        }


    }

    @Override
    public void getError(String msg) {
        // TODO Auto-generated method stub
        Toast.makeText(getActivity(),""+msg, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
    public void getContactPhoneRecords(final String fcust_id,final String fcust_name)
    {
        linkman_list.clear();
        OkHttpClientManager.getAsyn(OkHttpClientManager
                .BASE_URL+"linkman/getContactPhoneRecords/"+fcust_id+"/"+Common.fuser_id,
                new OkHttpClientManager.ResultCallback<ResponseList<Linkman>>()
                {
                    @Override
                    public void onResponse(ResponseList<Linkman> u)
                    {
                            if(u!=null){
                                Meta meta=u.getMeta();
                                if(meta!=null && meta.getCode()==200){
                                    for(int i=0;i<u.getData().size();i++){
                                        Map<String, String> arg=new HashMap<String, String>();
                                        arg.put("fTel",u.getData().get(i).getfTel());
                                        arg.put("flinkman",u.getData().get(i).getFlinkman());
                                        linkman_list.add(arg);

                                    }
                                     fCompanyID=fcust_id;
                                     fCompanyName=fcust_name;
                                    Contacts();
                                }else{
                                    Toast.makeText(getActivity(),""+meta.getMsg(), Toast.LENGTH_SHORT).show();
                                }

                        }
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"亲!网络异常!请稍后再试", Toast.LENGTH_SHORT).show();
                    }});
    }
    private void Contacts() {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.contacts_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();//获取屏幕宽、高度
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散

        /*WindowManager.LayoutParams p = dialogWindow.getAttributes();//获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);//高度设置为屏幕的0.6根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96);//宽度设置为屏幕的0.65根据实际情况调整
        dialogWindow.setAttributes(p);*/
        dlg.show();
        ListView Contacts_list=(ListView)window.findViewById(R.id.Contacts_list);
        contactsAdapter=new ContactsAdapter();
        Contacts_list.setAdapter(contactsAdapter);
    }
    ContactsAdapter contactsAdapter;
    class ContactsAdapter extends BaseAdapter {
        public int getCount() {

            return linkman_list.size();
        }
        public Object getItem(int position) {

            return linkman_list.get(position);
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
                layout=(LinearLayout) mInflater.inflate(R.layout.contact_list1,null);
                v=layout;
                holder.linkman=(TextView) v.findViewById(R.id.linkman);
                holder.telephone=(TextView) v.findViewById(R.id.telephone);
                holder.phone=(ImageView) v.findViewById(R.id.phone);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.linkman.setText("联系人   "+linkman_list.get(position).get("flinkman").toString());
            holder.telephone.setText("电  话   "+linkman_list.get(position).get("fTel").toString());
            final View view = v;
            final int one = holder.phone.getId();
            final int p = position;
            holder.phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    call_dialog(linkman_list.get(p).get("fTel").toString(),fCompanyID,fCompanyName);
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
    String fCompanyID="";
    String fCompanyName="";

    private void call_dialog(final String phone,final String fCompanyID,final String fCompanyName) {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.call_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = getActivity().getWindowManager();

        dialogWindow.setWindowAnimations(R.style.dialog_animation);

        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        submit2(phone,fCompanyID,fCompanyName);

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
    private void submit2(final String remark,final  String fCompanyID,final String fCompanyName)
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
    public  void getMySeeCompanyList(String fuser_id,int k)
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"company/getMySeeCompanyList/"+
                fuser_id+"/"+k,
                new OkHttpClientManager.ResultCallback<ResponseList<MySeeCompanyList>>()
                {
                    @Override
                    public void onResponse(ResponseList<MySeeCompanyList> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                for(int i=0;i<u.getData().size();i++){
                                    Map<String, String> arg=new HashMap<String, String>();
                                    arg.put("fCompany_Name",u.getData().get(i).getfCompany_Name());
                                    arg.put("fCompanyId",u.getData().get(i).getfCompanyId());
                                    arg.put("ViewNumber",u.getData().get(i).getViewNumber());
                                    arg.put("ViewTime",u.getData().get(i).getViewTime());

                                    getMySeeCompanyListlist.add(arg);
                                }
                                getMySeeCompanyList_list.setVisibility(View.VISIBLE);

                                getMySeeCompanyList_adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                            }else{
                                Toast.makeText(getActivity(),""+u.getMeta()
                                        .getMsg(), Toast
                                        .LENGTH_SHORT)
                                        .show();

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(), "亲!网络异常!请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    class MyAdapter10 extends BaseAdapter {
        public int getCount() {

            return getMySeeCompanyListlist.size();
        }
        public Object getItem(int position) {

            return getMySeeCompanyListlist.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View v, final ViewGroup parent) {
            // TODO Auto-generated method stub
            MyHolder holder=new MyHolder();
            LinearLayout layout=null;
            if(v==null){
                layout=(LinearLayout) mInflater.inflate(R.layout.yck_list,null);
                v=layout;
                holder.fcust_name=(TextView) v.findViewById(R.id.fcust_name);
                holder.date=(TextView) v.findViewById(R.id.date);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.fcust_name.setText(getMySeeCompanyListlist.get(position).get("fCompany_Name").toString());
            holder.date.setText(getMySeeCompanyListlist.get(position).get("ViewTime").toString());

            return layout;
        }
        class MyHolder {
            public TextView fcust_name;//
            public TextView date;

        }
    }

}
