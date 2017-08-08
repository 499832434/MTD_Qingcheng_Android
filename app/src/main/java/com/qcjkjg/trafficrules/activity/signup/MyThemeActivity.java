//package com.qcjkjg.trafficrules.activity.signup;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.Toast;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
//import com.aspsine.swipetoloadlayout.OnRefreshListener;
//import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
//import com.qcjkjg.trafficrules.ApiConstants;
//import com.qcjkjg.trafficrules.InitApp;
//import com.qcjkjg.trafficrules.R;
//import com.qcjkjg.trafficrules.activity.BaseActivity;
//import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
//import com.qcjkjg.trafficrules.net.HighRequest;
//import com.qcjkjg.trafficrules.utils.DateUtils;
//import com.qcjkjg.trafficrules.utils.NetworkUtils;
//import com.qcjkjg.trafficrules.view.CustomTitleBar;
//import com.qcjkjg.trafficrules.vo.MessageInfo;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created by zongshuo on 2017/7/19.
// */
//public class MyThemeActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
//    private ListView themeLV;
//    private MyThemeAdapter adapter;
//    private Map<String,List<MessageInfo>>  map=new HashMap<String, List<MessageInfo>>();
//    private SwipeToLoadLayout swipeToLoadLayout;
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    private int cid=-1;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_my_theme);
//        initView();
//    }
//
//    private void initView(){
//        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        finish();
//                    }
//                });
//        themeLV= (ListView) findViewById(R.id.swipe_target);
//        adapter=new MyThemeAdapter(MyThemeActivity.this,map);
//        themeLV.setAdapter(adapter);
//
//        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
//        swipeToLoadLayout.setOnRefreshListener(this);
//        swipeToLoadLayout.setOnLoadMoreListener(this);
//        swipeToLoadLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeToLoadLayout.setRefreshing(true);
//            }
//        });
//    }
//
//
//    /**
//     * 网络请求
//     */
//    private void request() {
//        if (!NetworkUtils.isNetworkAvailable(this)) {
//            return;
//        }
//
//        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.MY_THEME_LIST_API,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("mythemeRe", response);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.getString("code").equals("0")) {
//                                if(cid==-1){
//                                    map.clear();
//                                }
//                                JSONArray infoArr=jsonObject.getJSONArray("info");
//                                List<MessageInfo> messageList=null;
//                                for(int i=0;i<infoArr.length();i++){
//                                    JSONObject obj=infoArr.getJSONObject(i);
//                                    MessageInfo info=new MessageInfo();
//                                    info.setCid(obj.getInt("c_id"));
//                                    cid=obj.getInt("c_id");
//                                    info.setReplyCnt(obj.getInt("reply_cnt"));
//                                    info.setNickName(obj.getString("nick_name"));
//                                    info.setContent(obj.getString("content"));
//                                    info.setPhone(obj.getString("phone"));
//                                    String dateStr=sdf.format(new Date(obj.getLong("pubtime") * 1000));
//                                    info.setCreateTime(dateStr);
//                                    info.setZanCnt(obj.getInt("zan_cnt"));
//                                    info.setAvatar(obj.getString("avatar"));
//                                    info.setIsZan(obj.getInt("is_zan"));
//                                    List<String> imagesList=new ArrayList<String>();
//                                    JSONArray array=obj.getJSONArray("images");
//                                    for(int j=0;j<array.length();j++){
//                                        imagesList.add((String) array.get(j));
//                                    }
//                                    info.setPricturlList(imagesList);
//                                    if(map.size()>0){
//                                        boolean flag=true;
//                                        for(String key:map.keySet()){
//                                            if(key==dateStr){
//                                                messageList=map.get(key);
//                                                messageList.add(info);
//                                                flag=false;
//                                                break;
//                                            }
//                                        }
//                                        if(flag){
//                                            messageList=new ArrayList<MessageInfo>();
//                                            messageList.add(info);
//                                            map.put(sdf.format(new Date(obj.getLong("pubtime") * 1000)), messageList);
//                                        }
//                                    }else{
//                                        messageList=new ArrayList<MessageInfo>();
//                                        messageList.add(info);
//                                        map.put(sdf.format(new Date(obj.getLong("pubtime") * 1000)),messageList);
//                                    }
//                                }
//                                if(map.size()>0){
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }else{
//                                Toast.makeText(MyThemeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }finally {
//                            swipeToLoadLayout.setRefreshing(false);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String, String> params = new HashMap<String, String>();
//                if(-1!=cid){
//                    params.put("c_id", cid+"");
//                }
//                if(getUserIsLogin()){
//                    params.put("phone",getUserInfo(1));
//                }
//                return params;
//            }
//        };
//        InitApp.initApp.addToRequestQueue(request);
//    }
//
//    @Override
//    public void onLoadMore() {
//        swipeToLoadLayout.setLoadingMore(false);
//        request();
//    }
//
//    @Override
//    public void onRefresh() {
//        cid=-1;
//        request();
//    }
//}
