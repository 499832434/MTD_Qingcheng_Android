package com.qcjkjg.trafficrules.activity.web;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.alipay.sdk.pay.util.AlipayCommonLibSignFromServerActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.event.LoginSuccessEvent;
import com.qcjkjg.trafficrules.event.PaySuccessEvent;
import com.qcjkjg.trafficrules.event.RefreshExamNumEvent;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.AccountMoney;
import com.qcjkjg.trafficrules.vo.Signup;
import com.sci99.pay.activity.CashResultActivity;
import com.wx.sdk.pay.util.WeChatPayThroughServerActivity;
import de.greenrobot.event.EventBus;
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
    private String url = "",title="",fragmentType="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_webview);
        EventBus.getDefault().register(this);
        initData();
        initView();


    }
    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wv.canGoBack()){
                    wv.goBack();
                }else{
                    finish();
                }
            }
        });
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView(title);
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
                    if ("vip_free_trial".equals(host)) {
                        Intent intent = new Intent(BaseWebViewActivity.this, AnswerActivity.class);
                        intent.putExtra("fragmentType", fragmentType);
                        intent.putExtra("type", "subvip");
                        if ("1".equals(fragmentType)) {
                            intent.putExtra("subvip", "VIP1");
                        } else {
                            intent.putExtra("subvip", "vip1");
                        }
                        startActivity(intent);
                    } else if ("vip_register".equals(host)) {
                        if (getUserIsLogin()) {
                            getOrder();
                        } else {
                            startActivity(new Intent(BaseWebViewActivity.this, LoginActivity.class));
                        }
                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });

        wv.loadUrl(url);

    }

    private void initData(){
        try{
            url = getIntent().getExtras().getString("url");
            title=getIntent().getStringExtra("title");
            if(getIntent().hasExtra("fragmentType")){
                fragmentType=getIntent().getExtras().getString("fragmentType");
            }
        }catch (Exception e){

        }

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


    /**
     * 网络请求
     */
    private void getOrder() {
        if (!NetworkUtils.isNetworkAvailable(BaseWebViewActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.GET_ORDER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getOrderRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                String str=jsonObject.getJSONObject("info").getString("out_trade_no");
                                showAssignDialog(jsonObject.getJSONObject("info").getString("total_fee"),str);
                            }else{
                                Toast.makeText(BaseWebViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("phone", getUserInfo(1));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    private void pay1(final String str) {
        if (!NetworkUtils.isNetworkAvailable(BaseWebViewActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.WX_PAY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("payRe1", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                Intent wechatPayIntent = new Intent(BaseWebViewActivity.this, WeChatPayThroughServerActivity.class);
                                wechatPayIntent.putExtra("appId", jsonObject.getJSONObject("info").getString("appid"));
                                wechatPayIntent.putExtra("partnerId", jsonObject.getJSONObject("info").getString("partnerid"));
                                wechatPayIntent.putExtra("prepayId", jsonObject.getJSONObject("info").getString("prepayid"));
                                wechatPayIntent.putExtra("nonceStr", jsonObject.getJSONObject("info").getString("noncestr"));
                                wechatPayIntent.putExtra("timeStamp", jsonObject.getJSONObject("info").getString("timestamp"));
                                wechatPayIntent.putExtra("packageValue", jsonObject.getJSONObject("info").getString("package"));
                                wechatPayIntent.putExtra("sign", jsonObject.getJSONObject("info").getString("sign"));
                                startActivity(wechatPayIntent);
                            }else{
                                Toast.makeText(BaseWebViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("out_trade_no", str);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
    private void pay2(final String str) {
        if (!NetworkUtils.isNetworkAvailable(BaseWebViewActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.ALI_PAY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("payRe2", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                Intent intent = new Intent(BaseWebViewActivity.this, AlipayCommonLibSignFromServerActivity.class);
//                                intent.putExtra("payInfo", jsonObject.getJSONObject("info").getString("urlstr") + "&sign=\"" + jsonObject.getJSONObject("info").getString("sign") + "\"&sign_type=\"RSA\"");
                                intent.putExtra("payInfo",jsonObject.getString("info"));
                                startActivityForResult(intent, 1);
                            }else{
                                Toast.makeText(BaseWebViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("out_trade_no", str);
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    public void onEvent(PaySuccessEvent event) {
        PrefUtils.putString(BaseWebViewActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, "1");
        finish();
    }

    public void onEvent(LoginSuccessEvent event) {
        if("1".equals(getUserInfo(3))){
            finish();
        }else{
            getOrder();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showAssignDialog(String money,final String str) {
        final Dialog dialog  = new Dialog(this, R.style.cartdialog);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_pay,null);
        view.findViewById(R.id.closeIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        ((TextView)view.findViewById(R.id.moneyTV)).setText("¥"+money);
        view.findViewById(R.id.weixinLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(str)){
                    toast(BaseWebViewActivity.this,"无法下单,请联系客服");
                }else {
                    pay1(str);
                }
            }
        });
        view.findViewById(R.id.zhifubaoLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(str)){
                    toast(BaseWebViewActivity.this,"无法下单,请联系客服");
                }else {
                    pay2(str);
                }
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }
}
