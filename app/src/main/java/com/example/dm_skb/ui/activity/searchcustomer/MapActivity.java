package com.example.dm_skb.ui.activity.searchcustomer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.example.dm_skb.R;
import android.view.WindowManager;
import com.example.dm_skb.ui.base.BaseActivity;
import android.content.Intent;
import com.example.dm_skb.util.Common;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.baidu.mapapi.map.MapView;
import android.net.Uri;
import android.widget.Button;
import android.view.Window;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import android.app.AlertDialog;
import android.content.ComponentName;
import java.io.File;
import java.util.List;

/**
 * Created by yangxiaoping on 16/9/10.
 * 提醒计划
 */

public  class MapActivity extends BaseActivity  {
    private String fcust_id="";
    private String fcust_name="";
    private String fGPSX="";
    private String fGPSY="";
    private TextView register_main;
    private MapView mMapView;
    BaiduMap mBaiduMap;
    private TextView navigation;
    private String fAddress;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_main);
        Bundle bundle=getIntent().getExtras();
        fcust_id=bundle.getString("fcust_id");
        fcust_name=bundle.getString("fcust_name");
        fGPSX=bundle.getString("fGPSX");
        fGPSY=bundle.getString("fGPSY");
        fAddress=bundle.getString("fAddress");
        navigation=(TextView)findViewById(R.id.navigation);
        register_main=(TextView)findViewById(R.id.register_main);
        register_main.setText(""+fcust_name);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        if(fGPSX.equals("")||fGPSX.equals("0")||fGPSX.equals("0.0"))
        {

        }else {
            //定义Maker坐标点
            LatLng point = new LatLng(Double.parseDouble(fGPSY), Double.parseDouble(fGPSX));
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            mBaiduMap.addOverlay(option);
            LatLng qidian = new LatLng(Double.parseDouble(fGPSY), Double.parseDouble(fGPSX));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(qidian,17);
            mBaiduMap.setMapStatus(u);
        }
        mLocationClient =new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        SDKInitializer.initialize(this);
        initLocation();//注册监听函数
        mLocationClient.start();
    }
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }else if(view.getId()==R.id.navigation)
        {
            showUploadFileDialog();
        }
    }

    private void showUploadFileDialog() {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.share_dialog_layout);
        //设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        //设置显示位置
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        window.setAttributes(wl);
        dlg.setCanceledOnTouchOutside(true);//设置点击外围解散
        dlg.show();
        //取消
        Button cancel=(Button)window.findViewById(R.id.cancel);
        cancel.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.cancel();
            }
        });

        Button baiduMap=(Button)window.findViewById(R.id.baiduMap);
        baiduMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse("baidumap://map/direction?origin="+Common.AddrStr+"&"
                        + "destination="+fAddress+"&mode=driving&region="));
                if (isInstallByread("com.baidu.BaiduMap")) {
                    startActivity(intent); // 启动调用
                } else {
                    postmainHandler("请先安装百度地图客户端");
                }
                dlg.cancel();
            }
        });
        Button	GDMap=(Button)window.findViewById(R.id.GDMap);

        GDMap.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                double[] lat_lon=bdToGaoDe(Double.parseDouble(fGPSX),Double.parseDouble(fGPSY));
                double[] lat_lon1=bdToGaoDe(Double.parseDouble(Common.gpsx),Double.parseDouble
                        (Common.gpsy));

                intent.setData(Uri.parse("androidamap://route?sourceApplication=softname"
                        +"&slat="+lat_lon1[0]+"&slon="+lat_lon1[1]+"&sname="+fAddress+"&dlat="+lat_lon[0]+""
                        +"&dlon="+lat_lon[1]+"&dname="+ Common.AddrStr+"&dev=0&m=0&t=1&showType=1"));
                if (isInstallByread("com.autonavi.minimap"))
                {
                    intent.setPackage("com.autonavi.minimap");

                    startActivity(intent);
                    // 启动调用
                } else {
                    postmainHandler("请先安装高德地图客户端");
                }
                dlg.cancel();
            }
        });

    }
    private double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName)
                .exists();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=3000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
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
            Common.gpsy=location.getLatitude()+"";
            Common.gpsx=location.getLongitude()+"";
            Common.AddrStr=location.getAddrStr()+"";

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
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
            android.util.Log.i("BaiduLocationApiDem", sb.toString());
        }
    }
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        // 退出时销毁定位
        mLocationClient.stop();
        // 关闭定位图层

        super.onDestroy();
    }
}
