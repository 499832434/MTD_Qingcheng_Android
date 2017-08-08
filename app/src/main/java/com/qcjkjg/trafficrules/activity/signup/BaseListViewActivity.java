package com.qcjkjg.trafficrules.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.adapter.CircleFabulousAdapter;
import com.qcjkjg.trafficrules.adapter.MessageFabulousAdapter;
import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
import com.qcjkjg.trafficrules.adapter.SystemMessageAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.DateUtils;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageFabulous;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.MessageTheme;
import com.qcjkjg.trafficrules.vo.Signup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class BaseListViewActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    private ListView listView;
    private SystemMessageAdapter adapter0;
    private MyThemeAdapter adapter1;
    private MessageFabulousAdapter adapter2;
    private CircleFabulousAdapter adapter3;
    private int flag;//0:系统消息1:我的主题2:收到的赞3:赞的车友
    private List<Signup> signList=new ArrayList<Signup>();//0:系统消息
    private List<MessageInfo> infoList=new ArrayList<MessageInfo>();//3:赞的车友
    private List<MessageTheme> themeList=new ArrayList<MessageTheme>();//1:我的主题
    private List<MessageInfo> fabulousList=new ArrayList<MessageInfo>();//2:收到的赞
    private SwipeToLoadLayout swipeToLoadLayout;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int cid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_base_listview);
        initData();
        initView();
    }

    private void initData(){
        flag=getIntent().getIntExtra("flag",0);
    }
    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        listView= (ListView) findViewById(R.id.swipe_target);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (flag){
                    case 0:
                        Intent intent = new Intent(BaseListViewActivity.this, SignupContentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(MainActivity.SINGUPTAG, signList.get(i));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }
        });
        switch (flag){
            case 0:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("系统消息");
                adapter0=new SystemMessageAdapter(BaseListViewActivity.this,signList);
                listView.setAdapter(adapter0);
                break;
            case 1:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("我的主题");
                adapter1=new MyThemeAdapter(BaseListViewActivity.this,themeList);
                listView.setAdapter(adapter1);
                break;
            case 2:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("收到的赞");
                adapter2=new MessageFabulousAdapter(BaseListViewActivity.this,fabulousList);
                listView.setAdapter(adapter2);
                break;
            case 3:
                cid=getIntent().getIntExtra("cid",0);
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("赞的车友");
                adapter3=new CircleFabulousAdapter(BaseListViewActivity.this,infoList);
                listView.setAdapter(adapter3);
                break;
        }

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        if(signList.size()>0){
            switch (flag){
                case 0:
                    request0(signList.get(signList.size() - 1).getNewsId() + "");
                    break;
                case 1:
                    request1();
                    break;
                case 2:
                    request2(fabulousList.get(fabulousList.size()-1).getZanId()+"");
                    break;
                case 3:
                    request3(infoList.get(infoList.size() - 1).getZanId() + "");
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        switch (flag){
            case 0:
                request0("");
                break;
            case 1:
                cid=-1;
                request1();
                break;
            case 2:
                swipeToLoadLayout.setRefreshing(false);
                request2("");
                break;
            case 3:
                request3("");
                break;
        }
    }


    /**
     * 网络请求
     */
    private void request0(final String newsid) {
        if (!NetworkUtils.isNetworkAvailable(BaseListViewActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_UP_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("baseListViewRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(newsid)){
                                    signList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    Signup signup=new Signup();
                                    signup.setNewsId(obj.getInt("news_id"));
                                    signup.setTitle(obj.getString("title"));
                                    signup.setAbstractStr(obj.getString("abstract"));
                                    signup.setPictureUrl(obj.getString("img_url"));
                                    signup.setPubtime(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                    signList.add(signup);
                                }
                                if(signList.size()>0){
                                    adapter0.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(BaseListViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                if(!TextUtils.isEmpty(newsid)){
                    params.put("news_id", newsid);
                }
                params.put("page_count", "10");
                params.put("type", "1");
                params.put("sign", InitApp.initApp.getSig(params));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void request3(final String zanid) {
        if (!NetworkUtils.isNetworkAvailable(BaseListViewActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_ZAN_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("request3Re", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(zanid)){
                                    infoList.clear();
                                }
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<array.length();i++){
                                    JSONObject obj=array.getJSONObject(i);
                                    MessageInfo info=new MessageInfo();
                                    info.setAvatar(obj.getString("avatar"));
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setZanId(obj.getInt("zan_id"));
                                    infoList.add(info);
                                }
                                if(infoList.size()>0){
                                    adapter3.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                params.put("c_id", cid+"");
                if(getUserIsLogin()){
                    params.put("phone", getUserInfo(1));
                }
                if(!TextUtils.isEmpty(zanid)){
                    params.put("zan_id",zanid);
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void request1() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.MY_THEME_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("mythemeRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(cid==-1){
                                    themeList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");

                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    MessageInfo info=new MessageInfo();
                                    info.setCid(obj.getInt("c_id"));
                                    cid=obj.getInt("c_id");
                                    info.setReplyCnt(obj.getInt("reply_cnt"));
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setContent(obj.getString("content"));
                                    info.setPhone(obj.getString("phone"));
                                    String dateStr=sdf.format(new Date(obj.getLong("create_time") * 1000));
//                                    info.setCustomTime(dateStr);
                                    info.setCreateTime(DateUtils.getInterval(obj.getLong("create_time")));
                                    info.setZanCnt(obj.getInt("zan_cnt"));
                                    info.setAvatar(obj.getString("avatar"));
                                    info.setIsZan(obj.getInt("is_zan"));
                                    List<String> imagesList=new ArrayList<String>();
                                    JSONArray array=obj.getJSONArray("images");
                                    for(int j=0;j<array.length();j++){
                                        imagesList.add((String) array.get(j));
                                    }
                                    info.setPricturlList(imagesList);

                                    if(themeList.size()==0){
                                        List<MessageInfo> messageList=new ArrayList<MessageInfo>();
                                        MessageTheme theme=new MessageTheme();
                                        theme.setTime(dateStr);
                                        messageList.add(info);
                                        theme.setList(messageList);
                                        themeList.add(theme);
                                    }else{
                                        boolean flag=true;
                                        for(int j=0;j<themeList.size();j++){
                                            if(dateStr.equals(themeList.get(j).getTime())){
                                                MessageTheme theme=themeList.get(j);
                                                List<MessageInfo> messageList=theme.getList();
                                                messageList.add(info);
                                                theme.setList(messageList);
                                                themeList.set(j,theme);
                                                flag=false;
                                                break;
                                            }
                                        }
                                        if(flag){
                                            List<MessageInfo> messageList=new ArrayList<MessageInfo>();
                                            MessageTheme theme=new MessageTheme();
                                            theme.setTime(dateStr);
                                            messageList.add(info);
                                            theme.setList(messageList);
                                            themeList.add(theme);
                                        }
                                    }
                                }
                                if(themeList.size()>0){
                                    adapter1.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(BaseListViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                if(-1!=cid){
                    params.put("c_id", cid+"");
                }
                if(getUserIsLogin()){
                    params.put("phone",getUserInfo(1));
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void request2(final String zanid) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_ZAN_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("fabulousRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(zanid)){
                                    fabulousList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    MessageInfo info=new MessageInfo();
                                    info.setCid(obj.getInt("c_id"));
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setContent(obj.getString("to_content"));
                                    String dateStr=sdf.format(new Date(obj.getLong("pubtime") * 1000));
                                    info.setCreateTime(dateStr);
                                    info.setAvatar(obj.getString("avatar"));
                                    info.setZanId(obj.getInt("zan_id"));
                                    List<String> imagesList=new ArrayList<String>();
                                    if(!TextUtils.isEmpty(obj.getString("to_image"))){
                                        imagesList.add(obj.getString("to_image"));
                                    }
                                    info.setPricturlList(imagesList);
                                    fabulousList.add(info);
                                }
                                if(fabulousList.size()>0){
                                    Log.e("ffff",fabulousList.size()+"");
                                    adapter2.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(BaseListViewActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                if(getUserIsLogin()){
                    params.put("phone",getUserInfo(1));
                }
                if(!TextUtils.isEmpty(zanid)){
                    params.put("zan_id",zanid);
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
