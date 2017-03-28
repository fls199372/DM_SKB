package com.example.dm_skb.ui.activity.registrationorpassword;

import android.os.Bundle;
import android.view.View;

import com.example.dm_skb.R;

import com.example.dm_skb.ui.base.BaseActivity;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
/**
 * A login screen that offers login via email/password.
 */
public class AgreementActivity extends BaseActivity {
    private ImageView backImg;
    private WebView webView;
    private TextView title_view;
    ProgressBar progressBar;
    private TextView share_fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earn_points_main);

        webView = (WebView) findViewById(R.id.webView);
        share_fragment=(TextView)findViewById(R.id.share_fragment);
        share_fragment.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebSettings sws = webView.getSettings();
        sws.setSupportZoom(true);
        sws.setBuiltInZoomControls(true);
        webView.setInitialScale(25);
        webView.getSettings().setUseWideViewPort(true);
        title_view = (TextView) findViewById(R.id.title_view);
        title_view.setText("公司介绍");
        webView.loadUrl("file:///android_asset/Agreement.html");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

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
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    public OnClickListener clickListener=new OnClickListener() {

        public void onClick(View v) {

        }
    };
}
