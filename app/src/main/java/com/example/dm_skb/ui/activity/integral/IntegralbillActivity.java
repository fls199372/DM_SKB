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

import com.example.dm_skb.bean.RechargeQusery;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.widget.*;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.example.dm_skb.widget.PullToRefreshBase.Mode;
import com.example.dm_skb.widget.PullToRefreshBase.OnRefreshListener2;
import java.util.ArrayList;
//import android.widget.AdapterView.OnItemClickListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/10.
 * 积分账单
 */

public  class IntegralbillActivity extends BaseActivity implements ResponseT<ResponseList<RechargeQusery>> {

    private PullToRefreshListView listView;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    Dialog dialog;
    private int PageIndex=1;
    IntegralDao integralDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integralbill_main);
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
                integralDao.getRechargeQusery(Common.fuser_id+"/2003/"+PageIndex,
                        IntegralbillActivity.this);
                new FinishRefresh().execute();
            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex++;
                integralDao.getRechargeQusery(Common.fuser_id+"/2003/"+PageIndex,
                        IntegralbillActivity.this);
                new FinishRefresh().execute();
            }

        });
        dialog= CustomProgress.show(IntegralbillActivity.this, "正在查询积分账单", false, null);
        integralDao.getRechargeQusery(Common.fuser_id+"/2003/"+PageIndex,this);
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
                layout=(LinearLayout) mInflater.inflate(R.layout.integralbill_list,null);
                v=layout;
                holder.date=(TextView) v.findViewById(R.id.date);
                holder.period=(TextView) v.findViewById(R.id.period);
                holder.income=(TextView) v.findViewById(R.id.income);
                holder.expenditure=(TextView) v.findViewById(R.id.expenditure);
                holder.thefinal=(TextView) v.findViewById(R.id.thefinal);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.date.setText(resultList.get(position).get("date").toString());
            holder.period.setText(resultList.get(position).get("period").toString());
            holder.income.setText(""+resultList.get(position).get("income").toString()+"");
            holder.expenditure.setText(""+resultList.get(position).get("expenditure").toString()+"");
            holder.thefinal.setText(""+resultList.get(position).get("thefinal").toString()+"");

            return layout;
        }
        class MyHolder {
            public TextView date;//日期
            public TextView period;
            public TextView income;
            public TextView expenditure;
            public TextView thefinal;

        }
    }
    public void getT(ResponseList<RechargeQusery> t) {
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("date",t.getData().get(i).getdDate());
            arg.put("period",t.getData().get(i).getBeginScore());
            arg.put("income",t.getData().get(i).getInScore());
            arg.put("expenditure",t.getData().get(i).getOutScore());
            arg.put("thefinal",t.getData().get(i).getEndScore());

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
