package com.qcjkjg.trafficrules.activity.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Signup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/21.
 */
public class BaseWebViewActivity extends BaseActivity{
    private WebView wv;
    private View mProgressBar;
    private String url = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_webview);

        initView();
        initData();

    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        wv = ((WebView) findViewById(R.id.webView));
        WebSettings setting = wv.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mProgressBar = findViewById(R.id.webProgressbar);
        mProgressBar.setVisibility(View.VISIBLE);
        wv.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                setProgress(newProgress * 100);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() == View.GONE)
                        mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("foundUrl", url);
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    wv.setVisibility(View.VISIBLE);
                    wv.loadUrl(url);
                } else if (url.indexOf("tel:") != -1) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                } else {
                    Uri uri = Uri.parse(url);
                    String host = uri.getHost();
                    if ("open_new_view".equals(host)) {

                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void initData(){
        url = getIntent().getExtras().getString("url");
        wv.loadUrl(url);
    }


    public boolean onKeyDown(int keyCode ,KeyEvent keyEvent){
        if(keyCode==keyEvent.KEYCODE_BACK){//监听返回键，如果可以后退就后退
            if(wv.canGoBack()){
                wv.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, keyEvent);

    }
}
