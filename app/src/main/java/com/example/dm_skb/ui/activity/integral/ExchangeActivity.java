package com.example.dm_skb.ui.activity.integral;

import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.dm_skb.bean.CgClass;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Companylist;
import com.example.dm_skb.bean.IntegralList;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.ProductList;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.dao.IntegralDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.activity.newcustomer.NewCustomerActivity;
import com.example.dm_skb.ui.adapter.GirdDropDownAdapter;
import com.example.dm_skb.ui.adapter.ListDropDownAdapter;
import com.example.dm_skb.ui.base.BaseActivity;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.example.dm_skb.widget.DropDownMenu;
import com.example.dm_skb.widget.ILoadingLayout;
import com.example.dm_skb.widget.PullToRefreshBase;
import com.example.dm_skb.widget.PullToRefreshBase.Mode;
import com.example.dm_skb.widget.PullToRefreshBase.OnRefreshListener2;
import com.example.dm_skb.widget.PullToRefreshGridView;
import com.squareup.okhttp.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import java.util.LinkedList;
import android.graphics.Bitmap;
import java.util.Collections;
import android.content.Intent;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import com.zhy.autolayout.AutoLinearLayout;
import android.graphics.Color;
/**
 * Created by yangxiaoping on 16/9/10.
 * 积分兑换查询
 */

public  class ExchangeActivity extends BaseActivity implements ResponseT<ResponseList<ProductList>> {

    private PullToRefreshGridView mPullRefreshListView;
    public List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();//
    MyAdapter adapter;
    private LayoutInflater mInflater;
    IntegralDao integralDao;
    private int PageIndex=1;
    Dialog dialog;
    String ScoreType="0";
    String CgClassCode="0";
    DisplayImageOptions options;		// DisplayImageOptions是用于设置图片显示的类
    String[] imageUrls;					// 图片路径
    ArrayList list = new ArrayList();
    int position1=-1;
    private String headers[] = {"积分范围","商品类别"};
    DropDownMenu mDropDownMenu;
    private String sexs[] = { "全部", "0~300积分","301~500积分","501~1000积分","1001~3000积分","3001积分以上"};
    private ListDropDownAdapter sexAdapter;
    private List<View> popupViews = new ArrayList<>();
    public List<Map<String, String>> industryList=new ArrayList<Map<String,String>>();//
    private GirdDropDownAdapter industryAdapter;
    private TextView ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.integral_exchange_main);
        mDropDownMenu =(DropDownMenu)findViewById(R.id.dropDownMenu);
        cgclass();
        integralDao=new IntegralDao();
        mPullRefreshListView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
        mInflater=LayoutInflater.from(this);
        adapter = new MyAdapter();
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position1!=-1)
                {
                    ((HashMap)resultList.get(position1)).put("id","0");
                }
                ((HashMap)resultList.get(position)).put("id","1");

                position1=position;
                adapter.notifyDataSetChanged(); //数据集变化后,通知adapter

            }
        });

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
        mPullRefreshListView.setMode(Mode.BOTH);
        init();
        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<GridView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                PageIndex=1;
                resultList.clear();
                list.clear();

                integralDao.getProductList(ScoreType+"/"+CgClassCode+"/"+PageIndex,
                        ExchangeActivity.this);
                new FinishRefresh().execute();
            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<GridView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex++;
                integralDao.getProductList(ScoreType+"/"+CgClassCode+"/"+PageIndex,
                        ExchangeActivity.this);
                new FinishRefresh().execute();
            }
        });
        dialog= CustomProgress.show(ExchangeActivity.this, "正在查询商品列表", false, null);
        integralDao.getProductList(ScoreType+"/"+ScoreType+"/"+PageIndex,
                ExchangeActivity.this);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.sb)			// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.sb)	// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.sb)		// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                .displayer(new RoundedBitmapDisplayer(20))	// 设置成圆角图片
                .build();
        //init sex menu
        final ListView sexView = new ListView(this);
        sexView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(this, Arrays.asList(sexs));
        sexView.setAdapter(sexAdapter);

        final ListView ageView = new ListView(this);
        ageView.setDividerHeight(0);
        industryAdapter = new GirdDropDownAdapter(this, industryList);
        ageView.setAdapter(industryAdapter);
        popupViews.add(sexView);
        popupViews.add(ageView);
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
        //行业
        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                industryAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : industryList.get(position).get
                        ("farea_name").toString());
                dialog= CustomProgress.show(ExchangeActivity.this, "正在查询商品列表", false, null);
                PageIndex=1;
                resultList.clear();
                list.clear();
                CgClassCode=industryList.get(position).get
                        ("CgClassCode").toString();
                integralDao.getProductList(ScoreType+"/"+CgClassCode+"/"+PageIndex,
                        ExchangeActivity.this);

                mDropDownMenu.closeMenu();
            }
        });
        sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : sexs[position]);
                dialog= CustomProgress.show(ExchangeActivity.this, "正在查询商品列表", false, null);
                PageIndex=1;
                resultList.clear();
                list.clear();
                ScoreType=position+"";
                integralDao.getProductList(ScoreType+"/"+CgClassCode+"/"+PageIndex,
                        ExchangeActivity.this);
                mDropDownMenu.closeMenu();
            }
        });
    }
    private void init()
    {
        ILoadingLayout startLabels = mPullRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        // 设置下拉刷新文本
        mPullRefreshListView.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        mPullRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        mPullRefreshListView.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        mPullRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel(
                "正在加载...");
    }
    private class FinishRefresh extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e){
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result){
//        	adapter.notifyDataSetChanged();
            mPullRefreshListView.onRefreshComplete();
        }
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return) {
            finish();
        }else if(view.getId()==R.id.plans)
        {
            startActivity(new Intent(ExchangeActivity.this, IntegralExchangeQueryActivity.class));
        }else if(view.getId()==R.id.dh)
        {
            if(position1<0)
            {
                postmainHandler("请先选择商品才能兑换");
                return;
            }
            call_dialog(position1,resultList.get(position1).get("Score").toString());
        }
    }
    class MyAdapter extends BaseAdapter {
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
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
                layout=(LinearLayout) mInflater.inflate(R.layout.exchange_list,null);
                v=layout;
                holder.legal=(TextView) v.findViewById(R.id.legal);
                holder.commodity_name=(TextView) v.findViewById(R.id.commodity_name);
                holder.integral=(TextView) v.findViewById(R.id.integral);
                holder.image=(ImageView) v.findViewById(R.id.image);
                holder.layout=(AutoLinearLayout) v.findViewById(R.id.layout);
                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.legal.setText(resultList.get(position).get("CgClassName").toString());
            holder.commodity_name.setText(resultList.get(position).get("ProductName").toString());
            holder.integral.setText("价值:"+resultList.get(position).get("Score").toString()+"积分");
            imageLoader.displayImage(imageUrls[position], holder.image, options);
            if(resultList.get(position).get("id").toString().equals("0"))
            {
                holder.layout.setBackgroundColor(Color.WHITE);
            }else
            {
                holder.layout.setBackgroundResource(R.drawable.bjt);
            }
            return layout;
        }
        class MyHolder {
            public TextView legal;//
            public TextView commodity_name;
            public TextView integral;
            public ImageView image;
            public AutoLinearLayout layout;
        }
    }
    /**
     * 图片加载第一次显示监听器
     * @author Administrator
     *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
    public void getT(ResponseList<ProductList> t) {
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("ProductName",t.getData().get(i).getProductName());
            arg.put("CgClassName",t.getData().get(i).getCgClassName());
            arg.put("Score",t.getData().get(i).getScore());
            arg.put("ProductId",t.getData().get(i).getScore());
            arg.put("id","0");

            list.add(""+t.getData().get(i).getPicUrl());
            resultList.add(arg);
        }
        imageUrls= (String[])list.toArray(new String[list.size()]);
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        dialog.dismiss();
    }
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        postmainHandler(""+msg);
    }
    /**
     * 提交信息
     */
    private void submit(int position)
    {
        dialog= CustomProgress.show(ExchangeActivity.this, "正在兑换商品", false, null);

        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_Id", Common.fuser_id);//手机号
            main.put("ProductId", ""+resultList.get(position).get("ProductId").toString());//产品ID
            main.put("ProductName", ""+resultList.get(position).get("ProductName").toString());
            //产品名称
            main.put("Score", ""+resultList.get(position).get("Score").toString());//价格
            main.put("TypeId", "0");//公司名称
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//

        params.put("exchangedata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"product/addExchange?",params,
                new OkHttpClientManager.ResultCallback<RegisterJson<Register>>()
                {
                    @Override
                    public void onResponse(RegisterJson<Register> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){

                                postmainHandler(""+u.getData().getSuessCode());
                                ExchangeActivity.this.setResult(2, new Intent(ExchangeActivity
                                        .this,IntegralMainAcitivity.class));
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
    private void call_dialog(final int position1,final String Score) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();

        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.exchange_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = this.getWindowManager();
       /* Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.32); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96); // 宽度设置为屏幕的0.65，根据实际情况调整
        dialogWindow.setAttributes(p);*/
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        TextView cemo=(TextView)window.findViewById(R.id.cemo);
        cemo.setText("将从您的账户扣除"+Score+"积分兑换您选中\n的礼品,请您确认!");
        TextView no=(TextView)window.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });
        TextView ok=(TextView)window.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(position1);

                dlg.cancel();
            }
        });
    }
    public void onBackPress() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
        }
    }
    public  void cgclass()
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"company/getCategory",
                new OkHttpClientManager.ResultCallback<ResponseList<CgClass>>()
                {
                    @Override
                    public void onResponse(ResponseList<CgClass> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                industryList.clear();
                                for(int i=0;i<u.getData().size();i++){
                                    Map<String, String> arg=new HashMap<String, String>();
                                    arg.put("farea_name",u.getData().get(i)
                                            .getCategoryName());
                                    arg.put("CgClassCode",u.getData().get(i).getCategoryCode());

                                    industryList.add(arg);
                                }
                            }else{

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {

                    }
                });
    }

}
