package com.example.dm_skb.ui.activity.news;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.util.Common;
import com.zhy.autolayout.AutoLinearLayout;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.MyNewsList;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.ui.base.BaseApplication;
import com.example.dm_skb.widget.CustomProgress;
import com.example.dm_skb.widget.ILoadingLayout;
import com.example.dm_skb.widget.PullToRefreshBase;
import com.example.dm_skb.widget.PullToRefreshListView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.example.dm_skb.dao.NewsDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxiaoping on 16/9/10.
 * 积分充值
 */

public  class NewsDetailedActivity extends BaseActivity implements ResponseT<ResponseList<MyNewsList>> {

    private PullToRefreshListView listView;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    NewsDao newsDao;
    private TextView register_main;
    private String id="";
    Dialog dialog;
    int pages=1;
    String name="";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_detailed);
        Bundle bundle=getIntent().getExtras();
        register_main=(TextView) findViewById(R.id.register_main);
        register_main.setText(""+bundle.getString("name"));
        name=bundle.getString("name");
        id=bundle.getString("id");
        newsDao=new NewsDao();
        dialog= CustomProgress.show(NewsDetailedActivity.this, "" +
                "", false, null);
        newsDao.getMyNewsList(Common.fuser_id+"/"+id+"/"+pages,this);
        listView=(PullToRefreshListView)findViewById(R.id.pull_refresh_list);
        mInflater=LayoutInflater.from(this);
        adapter = new MyAdapter();
        ListView actualListView = listView.getRefreshableView();

        actualListView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        //   listView.setOnItemClickListener(this);
        init();
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                pages=1;
                resultList.clear();
                newsDao.getMyNewsList(Common.fuser_id+"/"+id+"/"+pages,NewsDetailedActivity.this);
                new FinishRefresh().execute();

            }

            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                pages++;
                newsDao.getMyNewsList(Common.fuser_id+"/"+id+"/"+pages,NewsDetailedActivity.this);
                new FinishRefresh().execute();

            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
            }
        });
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
        if (view.getId() == R.id.Return){
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
                layout=(LinearLayout) mInflater.inflate(R.layout.news_detailed_list,null);
                v=layout;
                holder.integral=(TextView) v.findViewById(R.id.integral);
                holder.content=(TextView) v.findViewById(R.id.content);
                holder.fust_name=(TextView) v.findViewById(R.id.fust_name);
                holder.layout=(AutoLinearLayout) v.findViewById(R.id.layout);
                holder.details=(TextView) v.findViewById(R.id.details);
                holder.theme=(TextView) v.findViewById(R.id.theme);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.integral.setText(resultList.get(position).get("CheckDate").toString());
            holder.content.setText(resultList.get(position).get("SubTitle").toString());
            holder.fust_name.setText(resultList.get(position).get("Meat").toString());
            holder.theme.setText(resultList.get(position).get("Title").toString());

            if(resultList.get(position).get("IsDetail").toString().equals("0"))
            {
                holder.layout.setVisibility(View.GONE);
            }else
            {
                holder.layout.setVisibility(View.VISIBLE);
            }
            final View view = v;
            final int one = holder.details.getId();
            final int p = position;
            holder.details.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NewsDetailedActivity.this.onClick(view, parent, p, one);
                }
            });
            return layout;
        }
        class MyHolder {
            public TextView integral;//
            public TextView content;
            public ImageView right;
            public TextView fust_name;
            public AutoLinearLayout layout;
            public TextView details;
            public TextView theme;

        }
    }
    public void onClick(View item, View widget, int position, int which) {
        switch (which) {
            case R.id.details:
                Intent intent=new Intent(NewsDetailedActivity.this,AgreementActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url",resultList.get(position).get("Details").toString());
                bundle.putString("name",name);

                intent.putExtras(bundle);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
    public void getT(ResponseList<MyNewsList> t) {
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("CheckDate",t.getData().get(i).getCheckDate());
            arg.put("Details",t.getData().get(i).getDetails());
            arg.put("IsDetail",t.getData().get(i).getIsDetail());
            arg.put("Meat",t.getData().get(i).getMeat());
            arg.put("SubTitle",t.getData().get(i).getSubTitle());
            arg.put("Title",t.getData().get(i).getTitle());

            resultList.add(arg);
        }
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        dialog.dismiss();

    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        postmainHandler(""+msg);
        dialog.dismiss();

    }
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
