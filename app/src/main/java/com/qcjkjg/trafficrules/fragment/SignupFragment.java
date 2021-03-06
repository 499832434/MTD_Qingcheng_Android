package com.qcjkjg.trafficrules.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.*;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.activity.signup.SignupContentActivity;
import com.qcjkjg.trafficrules.adapter.SignupAdapter;
import com.qcjkjg.trafficrules.event.NewMessageEvent;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Signup;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zaaach.citypicker.CityPickerActivity;
import de.greenrobot.event.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SignupFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    protected MainActivity mActivity;
    private ListView signupLV;
    private SignupAdapter adapter;
    private List<Signup> signList=new ArrayList<Signup>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SwipeToLoadLayout swipeToLoadLayout;
    private CustomTitleBar customTitleBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_signup, container, false);
        initView();
        EventBus.getDefault().register(this);
        return currentView;
    }

    private void initView(){
        customTitleBar=((CustomTitleBar) currentView.findViewById(R.id.customTitleBar));
        customTitleBar.setRightImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mActivity, MessageMainActivity.class));
                    }
                });
        customTitleBar.setLeftTextView(mActivity.getUserInfo(8));
        customTitleBar.setLeftTextViewOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivityForResult(new Intent(mActivity, CityPickerActivity.class), MainActivity.REQUEST_CODE_PICK_CITY);
            }
        });
        signupLV= (ListView) currentView.findViewById(R.id.swipe_target);
        adapter=new SignupAdapter(mActivity,signList);
        signupLV.setAdapter(adapter);
        signupLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, SignupContentActivity.class);
                intent.putExtra("id", signList.get(i).getNewsId());
                intent.putExtra("flag","news");
                startActivity(intent);
            }
        });
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

    /**
     * 网络请求
     */
    private void request(final String page) {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SIGN_UP_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("signupRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(page)){
                                    signList.clear();
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    Signup signup=new Signup();
                                    signup.setNewsId(obj.getInt("news_id"));
                                    signup.setTitle(obj.getString("title"));
                                    signup.setPictureUrl(obj.getString("img_url"));
                                    signup.setPage(obj.getString("pubtime"));
                                    signup.setPubtime(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                    signList.add(signup);
                                }
                                if(signList.size()>0){
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
                if(!TextUtils.isEmpty(page)){
                    params.put("pubtime", page);
                }
//                params.put("page_count", "1");
                params.put("type", "0");
                params.put("sign", InitApp.initApp.getSig(params));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        if(signList.size()>0){
            request(signList.get(signList.size()-1).getPage() + "");
        }
    }

    @Override
    public void onRefresh() {
        request("");
    }

    public void setArea(String str){
        customTitleBar.setLeftTextView(str);
    }

    public void onEvent(NewMessageEvent event) {
        boolean flag=event.isFlag();
        if(flag){
            customTitleBar.setRightImage(R.drawable.ic_message1);
        }else{
            customTitleBar.setRightImage(R.drawable.ic_message);
        }
    }
}
