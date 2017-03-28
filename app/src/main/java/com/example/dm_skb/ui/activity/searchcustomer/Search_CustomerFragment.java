package com.example.dm_skb.ui.activity.searchcustomer;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.example.dm_skb.R;
import com.example.dm_skb.bean.Companylist;
import com.example.dm_skb.bean.Integral;
import com.example.dm_skb.bean.IntegralJson;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.NewsTotal;
import com.example.dm_skb.bean.NewsTotalJson;
import com.example.dm_skb.bean.ResponseList;
import com.example.dm_skb.bean.UsableScoresJson;
import com.example.dm_skb.bean.UserScore;
import com.example.dm_skb.dao.CusetDao;
import com.example.dm_skb.dao.ResponseT;
import com.example.dm_skb.ui.activity.fragment.BaseFragment;
import com.example.dm_skb.ui.activity.integral.ExchangeActivity;
import com.example.dm_skb.ui.activity.integral.IntegralRechargeActivity;
import com.example.dm_skb.ui.activity.main.StartActivity;
import com.example.dm_skb.ui.activity.news.NewsMainActivity;
import com.example.dm_skb.ui.adapter.GirdDropDownAdapter;
import com.example.dm_skb.ui.adapter.ListDropDownAdapter;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.example.dm_skb.widget.CustomProgress;
import com.example.dm_skb.widget.DropDownMenu;
import com.example.dm_skb.widget.ILoadingLayout;
import com.example.dm_skb.widget.OnMenuSelectedListener;
import com.example.dm_skb.widget.PullToRefreshBase;
import com.example.dm_skb.widget.PullToRefreshListView;
import com.example.dm_skb.widget.RatingBar;
import com.squareup.okhttp.Request;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class Search_CustomerFragment extends BaseFragment  implements ResponseT<ResponseList<Companylist>> {

     DropDownMenu mDropDownMenu;
    private String headers[] = {"范围", "渠道", "行业", "排序"};
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter cityAdapter;
    private GirdDropDownAdapter provinceAdapter;

    private GirdDropDownAdapter industryAdapter;
    private ListDropDownAdapter sexAdapter;
    private GirdDropDownAdapter Adapter1;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private final static String fileName = "areaJson.json";
    public List<Map<String, String>> AreaList=new ArrayList<Map<String,String>>();//

    private List<Map<String,String>> ChannelList=new ArrayList<Map<String,String>>();
    private String sexs[] = { "距离由近及远", "评价由高到低","更新由新到旧","信息由全到简","客户由大到小"};
    private int constellationPosition = 0;
    private PullToRefreshListView pull_refresh_list;
    public List<Map<String, String>> resultList=new ArrayList<Map<String,String>>();//
    public List<Map<String, String>> cityList=new ArrayList<Map<String,String>>();//
    public List<Map<String, String>> countyList=new ArrayList<Map<String,String>>();//
    public List<Map<String, String>> industryList=new ArrayList<Map<String,String>>();//

    MyAdapter adapter;
    private LayoutInflater mInflater;
    private GirdDropDownAdapter constellationAdapter;

    private CusetDao custDao;
    private EditText FindString;
    String url="";
    private String CityId="";//城市
    private String Channel="";//渠道
    private String Brand="";//品牌
    private String Sort="-1";//默认=0 由高到低=1  由近到远=2 由新到旧=3  由全到简=4
    private int PageIndex=1;
    Dialog dialog;
    final Map<String, String> params = new HashMap<String, String>();
    private DropDownMenu menu;
    TextView ok;
    int frequency=0;
    int jjj=1;
    private ImageView message;
    public static TextView number;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.dropdownmenu, container, false);
        ButterKnife.inject(getActivity(),view);
        mLocationClient = new LocationClient(getActivity());//声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        SDKInitializer.initialize(getActivity());
        initLocation();//
        mLocationClient.start();
        return view;
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    class DataThread extends Thread {
        @Override
        public void run() {
           String addStr = getJson(fileName);
        }
        //读取方法
        public  String getJson(String fileName) {
            String jsonString="";
            String resultString="";
            try {
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                        getActivity().getResources().getAssets().open(fileName)));
                while ((jsonString=bufferedReader.readLine())!=null) {
                    resultString+=jsonString;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            Toast.makeText(getActivity(), "网络不给力"+resultString,Toast.LENGTH_LONG).show();
            return resultString;
        }
    }
    String findString="";//模糊查找客户名称
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        custDao=new CusetDao();
        dialog= CustomProgress.show(getActivity(), "正在查询客户", false, null);
        getIndustryList();
        number=(TextView) view.findViewById(R.id.number);
        integral();
        getMyNewsTotal();
        FindString=(EditText)view.findViewById(R.id.FindString);
        FindString.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,KeyEvent event)  {
                if (actionId==EditorInfo.IME_ACTION_SEND )
                {
                    switch (actionId) {
                    case KeyEvent.ACTION_UP:
                        dialog= CustomProgress.show(getActivity(), "正在模糊查询客户", false, null);

                        findString=FindString.getText().toString();
                        PageIndex=1;
                        resultList.clear();

                        fcust_query(Sort,findString,CityId,Channel,Brand,1+"");
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                                .INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(FindString.getWindowToken(), 0);
                        return true;
                    default:
                        return true;
                }

                }
                return false;
            }
        });
        ok=(TextView) view.findViewById(R.id.ok);
         ok.setOnClickListener(clickListener);
        message=(ImageView) view.findViewById(R.id.message);
        message.setOnClickListener(clickListener);
        findString=FindString.getText().toString();
        //fcust_query(Sort,findString,CityId,Channel,Brand,PageIndex+"");
        mDropDownMenu =(DropDownMenu)view.findViewById(R.id.dropDownMenu);
        mDropDownMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                //过滤筛选
             //   setFilter();
            }
        });
        pull_refresh_list=(PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
        initView();
        mInflater=LayoutInflater.from(getActivity());
        pull_refresh_list.setOnItemClickListener(clickListener1);
        ListView actualListView = pull_refresh_list.getRefreshableView();
        adapter = new MyAdapter();
        actualListView.setAdapter(adapter);
        pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
        init();
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                PageIndex=1;
                resultList.clear();
                findString=FindString.getText().toString();
                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,1+"");
                new FinishRefresh().execute();
            }
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub
                PageIndex++;
                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,PageIndex+"");
                new FinishRefresh().execute();
            }
        });

    }
    public void fcust_query(String Sort,String FindString,String CityId,String Channel,String Brand,String PageIndex)
    {
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {
            main.put("fUser_Id",Common.fuser_id);//手机号
            main.put("FindString",FindString);//
            main.put("CityId",CityId);//城市
            main.put("Channel",Channel);//渠道
            main.put("Brand",""+Brand);//行业
            main.put("Sort",""+Sort);//默认=0 由高到低=1  由近到远=2 由新到旧=3  由全到简=4
            main.put("gpsX",""+Common.gpsx);//经纬度
            main.put("gpsY",""+Common.gpsy);//经纬度
            main.put("PageIndex",PageIndex);//页数
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("parameterdata",main.toString());//主表
        custDao.getCompanylist(params,this);
    }
    private OnItemClickListener clickListener1=new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long arg3) {
            position=position-1;
            integral();
            if(resultList.get(position).get("isSee").toString().equals("1"))
            {
                Intent intent=new Intent(getActivity(),CustomerDetailActivity.class);
                Bundle bundle=new Bundle();;
                bundle.putString("fcust_id",resultList.get(position).get("fcust_id").toString());
                bundle.putString("position",""+position);
                bundle.putString("fcust_name",resultList.get(position).get("fcust_name").toString());
                bundle.putString("activity","Search_CustomerFragment");
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }else
            {
                submit(resultList.get(position).get("fcust_id").toString(),position,
                        resultList.get(position).get("fcust_name").toString());

            }
        }

    };
    /**
     * 提交信息
     */
    private void submit(final String fcust_id,final int position,final String fcust_name)
    {
        dialog= CustomProgress.show(getActivity(), "正在查询客户明细", false, null);
        final Map<String, String> params = new HashMap<String, String>();
        JSONObject main=new JSONObject();
        JSONArray filearray = new JSONArray();
        try {

            main.put("fUser_Id",Common.fuser_id);//手机号
            main.put("fCompanyId", fcust_id);//
            main.put("CompanyName", fcust_name);//

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }//
        params.put("companydata",main.toString());//主表
        OkHttpClientManager.postAsyn(OkHttpClientManager.BASE_URL+"mycompany/addMySeeCompany?",params,
                new OkHttpClientManager.ResultCallback<UsableScoresJson<UserScore>>()
                {
                    @Override
                    public void onResponse(UsableScoresJson<UserScore> u)
                    {
                        if(u!=null){
                            Meta meta=u.getMeta();
                            if(meta!=null && meta.getCode()==200){
                                dialog.cancel();
                                Intent intent = new Intent(getActivity(), CustomerDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("fcust_id", fcust_id);
                                bundle.putString("position", "" + position);
                                bundle.putString("fcust_name", fcust_name);
                                bundle.putString("activity", "Search_CustomerFragment");
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                            }else if(meta.getCode()==300){
                                Deficiencyofintegral(u.getData().getUsableScores());
                                dialog.cancel();
                            }else {
                                Toast.makeText(getActivity(), meta.getMsg
                                        (), Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        }else{
                            Toast.makeText(getActivity(), "亲!网络异常!请稍后再试", Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        dialog.cancel();
                        Toast.makeText(getActivity(), "亲!网络异常!请稍后再试", Toast.LENGTH_SHORT);
                    }
                });
    }
    private void init()
    {
        ILoadingLayout startLabels = pull_refresh_list
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pull_refresh_list.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示

        // 设置下拉刷新文本
        pull_refresh_list.getLoadingLayoutProxy(false, true)
                .setPullLabel("上拉加载...");
        pull_refresh_list.getLoadingLayoutProxy(false, true).setReleaseLabel(
                "放开加载...");
        pull_refresh_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(
                "正在加载...");
        // 设置上拉刷新文本
        pull_refresh_list.getLoadingLayoutProxy(true, false)
                .setPullLabel("下拉刷新...");
        pull_refresh_list.getLoadingLayoutProxy(true, false).setReleaseLabel(
                "放开刷新...");
        pull_refresh_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(
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
            pull_refresh_list.onRefreshComplete();

        }
    }

    private OnClickListener clickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
                if(ok==v)
                {
                    findString=FindString.getText().toString();
                    dialog= CustomProgress.show(getActivity(), "正在模糊查询客户", false, null);
                    PageIndex=1;
                    resultList.clear();
                    fcust_query(Sort,findString,CityId,Channel,Brand
                            ,1+"");
                }else if(message==v)
                {
                    Intent intent = new Intent(getActivity(), NewsMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("activity", "Search_CustomerFragment");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 0);
                }
        }
    };
    private void initView() {
        //init city menu
        final ListView cityView = new ListView(getActivity());
        String jsonString="";
        String resultString="";
        JSONObject nameList=null;
        try {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    getActivity().getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
            nameList=new JSONObject(resultString);
            JSONObject Listarea=null;
            Listarea=new JSONObject(nameList.getString("area0"));
            Iterator it = Listarea.keys();
            while (it.hasNext()) {
                Map<String, String> arg=new HashMap<String, String>();
                String key = it.next().toString();
                arg.put("farea_id",""+key);//ID
                arg.put("farea_name", "" + Listarea.getString("" + key));
                AreaList.add(arg);
            }
            Collections.sort(AreaList, new MapComparatorAsc());

        } catch (Exception e) {
            // TODO: handle exception
        }
//城市
        try {
            resultString="";
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    getActivity().getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
            nameList=new JSONObject(resultString);
            JSONObject Listarea=null;
            Listarea=new JSONObject(nameList.getString("area1"));
            JSONArray Listcity=null;
            Listcity=new JSONArray(Listarea.getString("100000"));
            for(int i=0;i<Listcity.length();i++)
            {
                JSONArray Listcity2 = new JSONArray(Listcity.get(i).toString());
                Object obj=Listcity2.get(0);
                Object obj1=Listcity2.get(1);
                Map<String, String> arg=new HashMap<String, String>();
                arg.put("farea_id",""+obj);//ID
                arg.put("farea_name", "" +obj1);
                cityList.add(arg);
            }
            Collections.sort(cityList, new MapComparatorAsc());

        } catch (Exception e) {
            // TODO: handle exception
        }
        Adapter1 = new GirdDropDownAdapter(getActivity(),ChannelList);
        cityView.setDividerHeight(0);
        cityView.setAdapter(Adapter1);
        //init age menu
        final ListView ageView = new ListView(getActivity());
        ageView.setDividerHeight(0);
        industryAdapter = new GirdDropDownAdapter(getActivity(), industryList);
        ageView.setAdapter(industryAdapter);

        //init sex menu
        final ListView sexView = new ListView(getActivity());
        sexView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(sexs));
        sexView.setAdapter(sexAdapter);

        //init constellation
        final View constellationView = getActivity().getLayoutInflater().inflate(R.layout
                .custom_layout, null);
        ListView province = ButterKnife.findById(constellationView, R.id.province);
        provinceAdapter = new GirdDropDownAdapter(getActivity(),AreaList);
        province.setAdapter(provinceAdapter);
        ListView city_id = ButterKnife.findById(constellationView, R.id.city_id);
        cityAdapter = new GirdDropDownAdapter(getActivity(),cityList);
        city_id.setAdapter(cityAdapter);
        ListView county= ButterKnife.findById(constellationView, R.id.county);

        constellationAdapter = new GirdDropDownAdapter(getActivity(),countyList);

        county.setAdapter(constellationAdapter);
        /*TextView ok = ButterKnife.findById(constellationView, R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu.setTabText(constellationPosition == 0 ? headers[0] :
                        constellations[constellationPosition]);
                mDropDownMenu.closeMenu();
            }
        });*/
        //init popupViews
        province.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provinceAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : AreaList.get(position).get
                        ("farea_name").toString());
                cityid( AreaList.get(position).get("farea_id").toString());

            }
        });
        city_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : cityList.get(position).get
                        ("farea_name").toString());
                county( cityList.get(position).get("farea_id").toString());
                if(position==0)
                {
                    CityId=cityList.get(position).get
                            ("farea_id").toString();
                    resultList.clear();
                    findString=FindString.getText().toString();

                    fcust_query(Sort,findString,CityId,Channel,Brand
                            ,1+"");
                     mDropDownMenu.closeMenu();
                }
            }
        });
        county.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                constellationAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : countyList.get(position).get
                        ("farea_name").toString());
                CityId=countyList.get(position).get
                        ("farea_id").toString();
                findString=FindString.getText().toString();

                resultList.clear();
                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,1+"");
                mDropDownMenu.closeMenu();
            }
        });
        popupViews.add(constellationView);
        popupViews.add(cityView);
        popupViews.add(ageView);
        popupViews.add(sexView);
        //add item click event
        // http://www.oschina.net/code/snippet_1424777_50584
        cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                cityAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : ChannelList.get(position).get
                        ("farea_name").toString());
                Channel=ChannelList.get(position).get("farea_name").toString();
                resultList.clear();
                findString=FindString.getText().toString();

                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,1+"");
                mDropDownMenu.closeMenu();
            }
        });
        //行业
        ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                industryAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : industryList.get(position).get
                        ("farea_name").toString());
                findString=FindString.getText().toString();

                Brand=industryList.get(position).get("farea_name").toString();
                resultList.clear();
                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,1+"");
                mDropDownMenu.closeMenu();
            }
        });
        sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[3] : sexs[position]);
                Sort=position+"";
                resultList.clear();
                findString=FindString.getText().toString();

                fcust_query(Sort,findString,CityId,Channel,Brand
                        ,1+"");
                mDropDownMenu.closeMenu();
            }
        });

        TextView contentView = new TextView(getActivity());
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 1);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }
    public void cityid(String fcity_id)
    {
        cityList.clear();
        String jsonString="";
        String resultString="";
        JSONObject nameList=null;
        //城市
        try {
            resultString="";
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    getActivity().getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
            nameList=new JSONObject(resultString);
            JSONObject Listarea=null;
            Listarea=new JSONObject(nameList.getString("area1"));
            JSONArray Listcity=null;
            Listcity=new JSONArray(Listarea.getString(""+fcity_id));
            Map<String, String> arg=new HashMap<String, String>();

            arg.put("farea_id",""+fcity_id);//ID
            arg.put("farea_name", "全部");
            cityList.add(arg);

            for(int i=0;i<Listcity.length();i++)
            {
                JSONArray Listcity2 = new JSONArray(Listcity.get(i).toString());
                Object obj=Listcity2.get(0);
                Object obj1=Listcity2.get(1);
                Map<String, String> arg1=new HashMap<String, String>();
                arg1.put("farea_id",""+obj);//ID
                arg1.put("farea_name", "" +obj1);
                cityList.add(arg1);
            }
            cityAdapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            Collections.sort(cityList, new MapComparatorAsc());

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
    public void county(String fcity_id)
    {
        countyList.clear();
        String jsonString="";
        String resultString="";
        JSONObject nameList=null;
        //城市
        try {
            resultString="";
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                    getActivity().getResources().getAssets().open(fileName)));
            while ((jsonString=bufferedReader.readLine())!=null) {
                resultString+=jsonString;
            }
            nameList=new JSONObject(resultString);
            JSONObject Listarea=null;
            Listarea=new JSONObject(nameList.getString("area2"));
            JSONArray Listcity=null;
            Listcity=new JSONArray(Listarea.getString(""+fcity_id));
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("farea_id",""+fcity_id);//ID
            arg.put("farea_name", "全部");
            countyList.add(arg);
            for(int i=0;i<Listcity.length();i++)
            {
                JSONArray Listcity2 = new JSONArray(Listcity.get(i).toString());
                Object obj=Listcity2.get(0);
                Object obj1=Listcity2.get(1);
                Map<String, String> arg1=new HashMap<String, String>();
                arg1.put("farea_id",""+obj);//ID
                arg1.put("farea_name", "" +obj1);
                countyList.add(arg1);
            }
            constellationAdapter.notifyDataSetChanged(); //数据集变化后,通知adapter
            Collections.sort(countyList, new MapComparatorAsc());
        } catch (Exception e) {
            // TODO: handle exception
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
    public static class MapComparatorAsc implements Comparator<Map<String, String>> {
        @Override
        public int compare(Map<String, String> m1, Map<String, String> m2) {
            Integer v1 = Integer.valueOf(m1.get("count").toString());
            Integer v2 = Integer.valueOf(m2.get("count").toString());
            if(v1 != null){
                return v1.compareTo(v2);
            }
            return 0;
        }

    }
    public void onBackPress() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
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
                layout=(LinearLayout) mInflater.inflate(R.layout.cust_query_list,null);
                v=layout;
                holder.cust_name=(TextView) v.findViewById(R.id.cust_name);
                holder.distance=(TextView) v.findViewById(R.id.distance);
                holder.sectorView=(RatingBar) v.findViewById(R.id.sectorView);
                holder.isSee=(ImageView) v.findViewById(R.id.isSee);

                layout.setTag(holder);
            }else{
                layout=(LinearLayout) v;
                holder=(MyHolder) layout.getTag();
            }
            holder.cust_name.setText(resultList.get(position).get("fcust_name").toString());
            holder.distance.setText(resultList.get(position).get("sDistance").toString()+"");
            if(resultList.get(position).get("isSee").toString().equals("0"))
            {
                holder.isSee.setImageDrawable(getResources().getDrawable(R.drawable.viewed));
            }
            else
            {
                holder.isSee.setImageDrawable(getResources().getDrawable(R.drawable.view));
                //相对麻烦一点但可防止内存溢出。
            }
            holder.sectorView.setStep1(Float.parseFloat(resultList.get(position).get
                    ("fEvaluateStarQty").toString()));
            return layout;
        }
        class MyHolder {
            public TextView cust_name;//
            public TextView distance;
            public RatingBar sectorView;
            public ImageView isSee;
        }
    }

    private void Deficiencyofintegral(final String UserScore) {
        final AlertDialog dlg = new AlertDialog.Builder(getActivity()).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.deficiencyofintegral_dialog);
        Window dialogWindow = dlg.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();//获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes();//获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6);//高度设置为屏幕的0.6根据实际情况调整
        p.width = (int) (d.getWidth() * 0.96);//宽度设置为屏幕的0.65根据实际情况调整
        dialogWindow.setAttributes(p);
        dlg.show();
        TextView cemo=(TextView)window.findViewById(R.id.cemo);
        cemo.setText("本次点击需消耗您10个积分,您的积分余额为“"+UserScore+"”,\n请进行积分充值, 或者我要赚积分");
        TextView integral=(TextView)window.findViewById(R.id.integral);
        integral.setText("账户积分余额："+UserScore+"分");
        AutoLinearLayout Recharge=(AutoLinearLayout)window.findViewById(R.id.Recharge);
        Recharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //积分充值
                startActivity(new Intent(getActivity(), IntegralRechargeActivity.class));
            }
        });
        AutoLinearLayout earn=(AutoLinearLayout)window.findViewById(R.id.earn);
        earn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //赚积分
                Intent intent=new Intent(getActivity(),StartActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id", "2");
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }
    public void getT(ResponseList<Companylist> t) {
        for(int i=0;i<t.getData().size();i++){
            Map<String, String> arg=new HashMap<String, String>();
            arg.put("fcust_id",t.getData().get(i).getFcustid());
            arg.put("fcust_name",t.getData().get(i).getFcustname());
            if(t.getData().get(i).getEvaluateStarQty().equals("null"))
            {
                arg.put("fEvaluateStarQty","0");
            }else if(t.getData().get(i).getEvaluateStarQty()==null)
            {
                arg.put("fEvaluateStarQty","0");
            }else{
                arg.put("fEvaluateStarQty",t.getData().get(i).getEvaluateStarQty());
            }
            arg.put("isSee",t.getData().get(i).getIsSee());
            arg.put("sDistance",t.getData().get(i).getsDistance());
            resultList.add(arg);
        }
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        dialog.dismiss();
    }
    @Override
    public void getError(String msg) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
    public void getIndustryList()
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL
                +"company/getIndustryList",
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
                        try{
                            JSONObject jsonObject=new JSONObject(result);
                            JSONObject jsonObject1=new JSONObject(jsonObject.getString("data"));
                            Listcity=new JSONArray(jsonObject1.getString("Industry"));
                            for(int i=0;i<Listcity.length();i++)
                            {
                                Object obj=Listcity.get(i);
                                Map<String, String> arg=new HashMap<String, String>();
                                arg.put("farea_name",""+obj);
                                industryList.add(arg);
                            }
                            JSONArray Listcity2=null;
                            Listcity2=new JSONArray(jsonObject1.getString("Channel"));
                            for(int i=0;i<Listcity2.length();i++)
                            {
                                Object obj=Listcity2.get(i);
                                Map<String, String> arg=new HashMap<String, String>();
                                arg.put("farea_name", "" +obj);
                                ChannelList.add(arg);
                            }

                        }catch (Exception e)
                        {
                        }
                    }
                });

    }
    JSONArray Listcity=null;

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            Common.gpsy=location.getLatitude()+"";
            Common.gpsx=location.getLongitude()+"";
            if(location.getLatitude()!=0||location.getLatitude()!=0.00)
            {
                frequency++;
            }
            if(frequency==1)
            {
                resultList.clear();
                fcust_query("-1",findString,CityId,Channel,Brand
                        ,PageIndex+"");
            }
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        resultList.clear();
        getMyNewsTotal();
        fcust_query(Sort,findString,CityId,Channel,Brand
                ,PageIndex+"");
    }
    public static void getMyNewsTotal()
    {
        OkHttpClientManager.getAsyn(OkHttpClientManager.BASE_URL+"mynews/getMyNewsTotal/"+ Common.fuser_id,
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
                                    number.setVisibility(View.GONE);
                                }else
                                {
                                    number.setText(""+u.getData().getTotal());
                                }
                            }else{
                                number.setVisibility(View.GONE);

                            }
                        }
                    }
                    @Override
                    public void onError(Request request, Exception e) {
                        number.setVisibility(View.GONE);

                    }
                });
    }
}
