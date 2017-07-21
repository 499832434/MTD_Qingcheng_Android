package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.*;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.activity.signup.SignupContentActivity;
import com.qcjkjg.trafficrules.activity.web.BaseWebViewActivity;
import com.qcjkjg.trafficrules.adapter.SignupAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Signup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SignupFragment extends Fragment{
    private View currentView = null;
    protected MainActivity mActivity;
    private ListView signupLV;
    private SignupAdapter adapter;
    private List<Signup> signList=new ArrayList<Signup>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_signup, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        ((CustomTitleBar) currentView.findViewById(R.id.customTitleBar)).setRightImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(mActivity, MessageMainActivity.class));
                    }
                });
        signupLV= (ListView) currentView.findViewById(R.id.signupLV);
        adapter=new SignupAdapter(mActivity,signList);
        signupLV.setAdapter(adapter);
        signupLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mActivity, SignupContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(MainActivity.SINGUPTAG,signList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        request();
    }

    /**
     * 网络请求
     */
    private void request() {
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
                                signList.clear();
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    Signup signup=new Signup();
                                    signup.setNewsId(obj.getInt("news_id"));
                                    signup.setTitle(obj.getString("title"));
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
                params.put("page_count", "10");
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
}
