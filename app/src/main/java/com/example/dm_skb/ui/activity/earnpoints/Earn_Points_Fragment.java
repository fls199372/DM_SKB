package com.example.dm_skb.ui.activity.earnpoints;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dm_skb.R;
import com.example.dm_skb.bean.Meta;
import com.example.dm_skb.bean.Register;
import com.example.dm_skb.bean.RegisterJson;
import com.example.dm_skb.ui.activity.fragment.BaseFragment;
import com.example.dm_skb.ui.activity.main.Defaultcontent;
import com.example.dm_skb.util.Common;
import com.example.dm_skb.util.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Earn_Points_Fragment extends BaseFragment  {

    private WebView webView;
    ProgressBar progressBar;
    private TextView share_fragment;
    UMImage image = new UMImage(getActivity(), "http://shdingmou.cn/dmsock/MainIcon.png");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.earn_points_main, container, false);

    }
    /**
     * 获取xml控件
     */
   
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=(WebView) view.findViewById(R.id.webView);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBar);
        share_fragment=(TextView) view.findViewById(R.id.share_fragment);
        share_fragment.setOnClickListener(clickListener);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebSettings sws = webView.getSettings();
        sws.setSupportZoom(true);
        sws.setBuiltInZoomControls(true);
        webView.setInitialScale(25);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("http://www.dingmou.net/Home/MyHelp");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url)
            {

                super.onPageFinished(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

	}
    public View.OnClickListener clickListener=new View.OnClickListener() {

        public void onClick(View v) {
            if(v==share_fragment)
            {
                new ShareAction(getActivity()).setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA
                        .QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withTitle(Defaultcontent.title)
                        .withText(Defaultcontent.text+"")
                        .withTargetUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.example.dm_skb")
                        .withMedia(image)
                        .setCallback(umShareListener)
                        .open();
            }
        }
    };
    private UMShareListener umShareListener = new UMShareListener() {
        public void onResult(SHARE_MEDIA platform) {
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(getActivity(),platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
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
                }else if(platform.name().equals("WEIXIN_CIRCLE"))
                {
                    submitshare("4");
                }

            }
        }
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity()," 您的分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
            }
        }
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(),"您的分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
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
            main.put("ShareType",type);//手机号

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
                                Toast.makeText(getActivity(),""+u.getData().getSuessCode(), Toast.LENGTH_SHORT)
                                        .show();

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
        //Log.d("result","onActivityResult");
    }
}
