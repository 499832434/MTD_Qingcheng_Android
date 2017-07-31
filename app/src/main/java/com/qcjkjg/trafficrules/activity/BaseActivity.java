package com.qcjkjg.trafficrules.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.utils.StatusBarColorCompat;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class BaseActivity extends AppCompatActivity {
    private String url;
    private DefaultHttpClient defaultHttpClient;
    /**
     * 上传的文本集合...........
     */
    private List<BasicNameValuePair> texts;
    /**
     * 上传的文件集合..........
     */
    private HashMap<File, String> files;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setContentViewWithStatusBarColorByColorPrimaryDark(int layoutResID) {
        StatusBarColorCompat.setContentViewWithStatusBarColorByColorPrimaryDark(this, layoutResID);
        StatusBarColorCompat.setStatusBarColor(BaseActivity.this, getResources().getColor(R.color.white));
    }

    /**
     * 拨打电话
     */
    public void call(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 隐藏键盘
     */
    public  void hiddenSoftInput() {
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void toast(Context context,String str){
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }


    /**
     * 初始化post的内容
     *
     * @return  HttpPost
     */
    public HttpPost iniHttpPost(List<BasicNameValuePair> texts,
                                 HashMap<File, String> files,String url) {
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder create = MultipartEntityBuilder.create();
        // 普通文本的发送，用户名&密码等
        if (texts != null && texts.size() > 0) {
            for (BasicNameValuePair iterable_element : texts) {
                create.addTextBody(iterable_element.getName(),
                        iterable_element.getValue(),contentType);
            }
        }
        // 二进制的发送，文件
        if (files != null && files.size() > 0) {
            Set<Map.Entry<File, String>> entrySet = files.entrySet();
            for (Map.Entry<File, String> iterable_element : entrySet) {
                create.addBinaryBody(iterable_element.getValue(),
                        iterable_element.getKey());

            }
        }
        HttpEntity httpEntity = create.build();
        // post内容的设置............
        httpPost.setEntity(httpEntity);
        return httpPost;

    }

    /**
     * 初始化httpClient
     */
    public DefaultHttpClient iniClient() {
        if (defaultHttpClient == null) {
            defaultHttpClient = new DefaultHttpClient();
            // HTTP协议版本1.1
            defaultHttpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            // 连接超时
            defaultHttpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        }
        return defaultHttpClient;

    };
    /**
     * 关闭连接
     */
    public void closeConnect(){
        if(defaultHttpClient!=null){
            ClientConnectionManager connectionManager = defaultHttpClient.getConnectionManager();
            if(connectionManager!=null)
                connectionManager.shutdown();
            defaultHttpClient=null;
        }
    }

    /**
     * 记录用户的登录信息
     */

    public void loginInfo(String name,String phone,String avatar,String isvip,String platform){
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, name);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, phone);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AVATAR_KEY, avatar);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, isvip);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PLATFORM_KEY, platform);
    }

    /**
     * 判断用户是否登录
     */

    public boolean getUserIsLogin(){
        String phone=PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, "");
        if(TextUtils.isEmpty(phone)){
            return false;
        }
        return  true;
    }

    /**
     * 获取用户的登录信息
     */
    public String getUserInfo(int flag){
        switch (flag){
            case 0:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, "");
            case 1:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, "");
            case 2:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AVATAR_KEY, "");
            case 3:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, "");
        }
        return  "";
    }

}