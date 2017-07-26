package com.qcjkjg.trafficrules.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
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
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder create = MultipartEntityBuilder.create();
        // 普通文本的发送，用户名&密码等
        if (texts != null && texts.size() > 0) {
            for (BasicNameValuePair iterable_element : texts) {
                create.addTextBody(iterable_element.getName(),
                        iterable_element.getValue());
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
}