package com.qcjkjg.trafficrules.activity.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zongshuo on 2017/7/21.
 */
public class SignupContentActivity extends BaseActivity{
    private WebView wv;
    private View mProgressBar;
    private Signup signup;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_content);

        signup=getIntent().getParcelableExtra(MainActivity.SINGUPTAG);
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

        if (!NetworkUtils.isNetworkAvailable(SignupContentActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_UP_DETAIL_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("signupDetailRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                mProgressBar.setVisibility(View.GONE);
                                JSONArray array=jsonObject.getJSONArray("info");
                                JSONObject obj=array.getJSONObject(0);
                                String content=obj.getString("content");
                                String contentStr = reformatContent1(content);
                                ((TextView)findViewById(R.id.titleTV)).setText(obj.getString("title"));
                                ((TextView)findViewById(R.id.timeTV)).setText(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                ((TextView)findViewById(R.id.cntTV)).setText("浏览量:"+obj.getString("view_cnt"));
                                final String mimeType = "text/html";
                                final String encoding = "UTF-8";
                                wv.loadDataWithBaseURL("http://47.92.112.59:2017/", contentStr, mimeType, encoding, "");
                            }else{
                                Toast.makeText(SignupContentActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("news_id", signup.getNewsId()+"");
                params.put("sign", InitApp.initApp.getSig(params));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    private String reformatContent1(String content) {
        StringBuilder titleStr = new StringBuilder();

        titleStr.append("<html><body>");
        titleStr.append(content);
        titleStr.append("</body></html>");

        return titleStr.toString();
    }
    private String reformatContent(String content) {
        StringBuilder titleStr = new StringBuilder();

        titleStr.append("<html><meta content=\"telephone=no\" name=\"format-detection\" /><head><style>\n").append("body {margin-left:auto; margin-right:auto; word-break:break-all;}\n")
                .append(".about_we {\n" +
                        "            margin-top: 20px;\n" +
                        "            color:#393939;\n" +
                        "            width: 100%;\n" +
                        "            height: 30px;\n" +
                        "            line-height: 30px;\n" +
                        "            background-color: #F3F3F3;\n" +
                        "            font-size: 18px;\n" +
                        "            text-indent: 6px;\n" +
                        "        }\n" +
                        "        .red-line {\n" +
                        "            float: left;\n" +
                        "            height: 16px;\n" +
                        "            width: 4px;\n" +
                        "            margin-top: 6px;\n" +
                        "            margin-left: 8px;\n" +
                        "            background-color: #F4756F;\n" +
                        "        }")
                .append(".scrolldiv {width:100%;overflow:scroll;-webkit-overflow-scrolling: touch}\n")
                .append("::-webkit-scrollbar {width: 0px;height: 4px;}\n")
                .append("::-webkit-scrollbar-thumb {border-radius: 8px;background-color: #000;border: 2px solid #666;}\n")
                .append("::-webkit-scrollbar-track {-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.2);}\n")
                .append("</style>\n</head>")
                .append("<body><div style='height:auto;width:auto;padding:0;margin:0'>");
        titleStr.append(content).append("</div>");
        titleStr.append("</body></html>");

        return titleStr.toString();
    }
}
