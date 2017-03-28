package com.example.dm_skb.ui.activity.news;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dm_skb.R;
import com.example.dm_skb.ui.base.BaseActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * A login screen that offers login via email/password.
 */
public class AgreementActivity extends BaseActivity {
    private ImageView backImg;
    private WebView webView;
    private TextView title_view;
    ProgressBar progressBar;
    String url="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_main);
        Bundle bundle=getIntent().getExtras();
        url=bundle.getString("url");
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        WebSettings sws = webView.getSettings();
        sws.setSupportZoom(true);
        sws.setBuiltInZoomControls(true);
        webView.setInitialScale(25);
        webView.getSettings().setUseWideViewPort(true);
        title_view = (TextView) findViewById(R.id.title_view);
        title_view.setText(""+bundle.getString("name"));
        webView.loadUrl(""+url);
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
    public void onClickAuth(View view) {
        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.Return){
            finish();
        }
    }

}
