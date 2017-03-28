package com.example.dm_skb.ui.activity.integral;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.IntegralList;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.widget.PullToRefreshBase.Mode;
import com.example.dm_skb.widget.PullToRefreshBase.OnRefreshListener2;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.example.dm_skb.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by yangxiaoping on 16/9/10.
 * 积分兑换查询
 */

public  class IntegralExchangeQueryActivity extends BaseActivity implements ResponseT<ResponseList<IntegralList>> {

    private PullToRefreshListView listView;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    IntegralDao integralDao;
    private int PageIndex=1;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integral_exchange_query_main);
        integralDao=new IntegralDao();

        listView=(PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        mInflater=LayoutInflater.from(this);
        ListView actualListView = listView.getRefreshableView();
        adapter = new MyAdapter();
        actualListView.setAdapter(adapter);
		/*
		 * Mode.BOTH：同时支持上拉下拉
         * Mode.PULL_FROM_START：只支持下拉Pulling Down
         * Mode.PULL_FROM_END：只支持上拉Pulling Up
		 */
		/*
		 * 如果Mode设置成Mode.BOTH，需要设置刷新Listener为OnRefreshListener2，并实现onPullDownToRefresh()、onPullUpToRefresh()两个方法。
         * 如果Mode设置成Mode.PULL_FROM_START或Mode.PULL_FROM_END，需要设置刷新Listener为OnRefreshListener，同时实现onRefresh()方法。
         * 当然也可以设置为OnRefreshListener2，但是Mode.PULL_FROM_START的时候只调用onPullDownToRefresh()方法，
         * Mode.PULL_FROM的时候只调用onPullUpToRefresh()方法.
		 */
        listView.setMode(Mode.BOTH);
     //   listView.setOnItemClickListener(this);
        init();
        listView.setOnRefreshListener(new OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex=1;
                resultList.clear();
                integralDao.getIntegralList(Common.fuser_id+"/2004/"+PageIndex,
                        IntegralExchangeQueryActivity.this);

                new FinishRefresh().execute();
            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub

                PageIndex++;
                integralDao.getIntegralList(Common.fuser_id+"/2004/"+PageIndex,
                        IntegralExchangeQueryActivity.this);
                new FinishRefresh().execute();            }

        });
        dialog= CustomProgress.show(IntegralExchangeQueryActivity.this, "正在查询分兑换", false, null);
        integralDao.getIntegralList(Common.fuser_id+"/2004/"+PageIndex,this);

    }
    private void init()
    {
        ILoadingLayout startLabels = listView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = listView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        listView.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        listView.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
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
            listView.onRefreshComplete();
        }
    }

    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            finish();
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
                layout=(LinearLayout) mInflater.inflate(R.layout.recharge_query_list,null);
                v=layout;
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.name=(TextView) v.findViewById(R.id.name);
                holder.integral=(TextView) v.findViewById(R.id.integral);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.date.setText(resultList.get(position).get("date").toString());
            holder.name.setText(resultList.get(position).get("period").toString());
            holder.integral.setText(""+resultList.get(position).get("income").toString()+"");

            return layout;
        }
        class MyHolder {
            public TextView date;//日期
            public TextView name;
            public TextView integral;

        }
    }
    public void getT(ResponseList<IntegralList> t) {
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("date",t.getData().get(i).getdDate());
            arg.put("period",t.getData().get(i).getProductName());
            arg.put("income",t.getData().get(i).getIntegral());
            resultList.add(arg);
        }
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        dialog.dismiss();
    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();

        postmainHandler(""+msg);
    }
}
