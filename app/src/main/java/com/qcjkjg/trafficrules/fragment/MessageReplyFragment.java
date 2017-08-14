package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.adapter.*;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.DateUtils;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.MessageTheme;
import com.qcjkjg.trafficrules.vo.ReplyInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    protected MessageReplyActivity mActivity;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private int fragmentType = 0;
    private List<MessageTheme> themeList=new ArrayList<MessageTheme>();//1:我的主题
    private List<ReplyInfo> newThemeList=new ArrayList<ReplyInfo>();//1:我的主题
    private ListView listView;
    private MyThemeAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int reply_id;
    public static MessageReplyFragment newInstance(MessageReplyActivity parentActivity,  int fragmentType) {
        MessageReplyFragment fr = new MessageReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_message_reply, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        listView= (ListView) currentView.findViewById(R.id.swipe_target);
        if(0==fragmentType){
            adapter=new MyThemeAdapter(mActivity,newThemeList,1);
        }else{
            adapter=new MyThemeAdapter(mActivity,newThemeList,2);
        }
        listView.setAdapter(adapter);
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MessageReplyActivity)context;
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        if(0==fragmentType){
            request0();
        }else {
            request1();
        }
    }

    @Override
    public void onRefresh() {
        reply_id=-1;
        if(0==fragmentType){
            request0();
        }else {
            request1();
        }
    }

    /**
     * 网络请求
     */
    private void request0() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_MY_REPLY_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("re0Re", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(reply_id==-1){
                                    themeList.clear();
                                    newThemeList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");

                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    ReplyInfo info=new ReplyInfo();
                                    info.setCid(obj.getInt("c_id"));
                                    reply_id=obj.getInt("reply_id");
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setContent(obj.getString("content"));
                                    info.setContentReply(obj.getString("to_content"));
                                    String dateStr=sdf.format(new Date(obj.getLong("create_time") * 1000));
                                    info.setCustomTime(dateStr);
                                    info.setCreateTime(DateUtils.getInterval(obj.getLong("create_time")));
                                    info.setAvatar(obj.getString("avatar"));
                                    List<String> imagesList=new ArrayList<String>();
                                    JSONArray array=obj.getJSONArray("images");
                                    for(int j=0;j<array.length();j++){
                                        imagesList.add((String) array.get(j));
                                    }
                                    info.setImagesList(imagesList);

                                    if(themeList.size()==0){
                                        List<ReplyInfo> messageList=new ArrayList<ReplyInfo>();
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
                                                List<ReplyInfo> messageList=theme.getList();
                                                messageList.add(info);
                                                theme.setList(messageList);
                                                themeList.set(j,theme);
                                                flag=false;
                                                break;
                                            }
                                        }
                                        if(flag){
                                            List<ReplyInfo> messageList=new ArrayList<ReplyInfo>();
                                            MessageTheme theme=new MessageTheme();
                                            theme.setTime(dateStr);
                                            messageList.add(info);
                                            theme.setList(messageList);
                                            themeList.add(theme);
                                        }
                                    }
                                }
                                if(themeList.size()>0){
                                    for(int i=0;i<themeList.size();i++){
                                        for(int j=0;j<themeList.get(i).getList().size();j++){
                                            ReplyInfo info=themeList.get(i).getList().get(j);
                                            if(j==0){
                                                info.setThemeFlag(1);
                                            }
                                            newThemeList.add(info);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(mActivity, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                if(-1!=reply_id){
                    params.put("reply_id", reply_id+"");
                }
                if(mActivity.getUserIsLogin()){
                    params.put("phone",mActivity.getUserInfo(1));
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
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_REPLY_ME_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("re1Re", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(reply_id==-1){
                                    themeList.clear();
                                    newThemeList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");

                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    ReplyInfo info=new ReplyInfo();
                                    info.setCid(obj.getInt("c_id"));
                                    reply_id=obj.getInt("reply_id");
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setContent(obj.getString("content"));
                                    info.setContentReply(obj.getString("to_content"));
                                    String dateStr=sdf.format(new Date(obj.getLong("create_time") * 1000));
                                    info.setCustomTime(dateStr);
                                    info.setCreateTime(DateUtils.getInterval(obj.getLong("create_time")));
                                    info.setAvatar(obj.getString("avatar"));
                                    List<String> imagesList=new ArrayList<String>();
                                    JSONArray array=obj.getJSONArray("images");
                                    for(int j=0;j<array.length();j++){
                                        imagesList.add((String) array.get(j));
                                    }
                                    info.setImagesList(imagesList);

                                    if(themeList.size()==0){
                                        List<ReplyInfo> messageList=new ArrayList<ReplyInfo>();
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
                                                List<ReplyInfo> messageList=theme.getList();
                                                messageList.add(info);
                                                theme.setList(messageList);
                                                themeList.set(j,theme);
                                                flag=false;
                                                break;
                                            }
                                        }
                                        if(flag){
                                            List<ReplyInfo> messageList=new ArrayList<ReplyInfo>();
                                            MessageTheme theme=new MessageTheme();
                                            theme.setTime(dateStr);
                                            messageList.add(info);
                                            theme.setList(messageList);
                                            themeList.add(theme);
                                        }
                                    }
                                }
                                if(themeList.size()>0){
                                    for(int i=0;i<themeList.size();i++){
                                        for(int j=0;j<themeList.get(i).getList().size();j++){
                                            ReplyInfo info=themeList.get(i).getList().get(j);
                                            if(j==0){
                                                info.setThemeFlag(1);
                                            }
                                            newThemeList.add(info);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }

                                for(int i=0;i<newThemeList.size();i++){
                                    Log.e("===="+i,newThemeList.get(i).getContent()+"");
                                }
                            }else{
                                Toast.makeText(mActivity, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                if(-1!=reply_id){
                    params.put("reply_id", reply_id+"");
                }
                if(mActivity.getUserIsLogin()){
                    params.put("phone",mActivity.getUserInfo(1));
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
