package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.exam.ErrorCollectActivity;
import com.qcjkjg.trafficrules.activity.exam.RankActivity;
import com.qcjkjg.trafficrules.adapter.RankAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.vo.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class RankListFragment extends Fragment{
    private View currentView = null;
    protected RankActivity mActivity;
    private final static String TYPE = "type";
    private int type;
    private ListView rankLV;
    private List<User> list=new ArrayList<User>();
    private RankAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_rank_list, container, false);
        initView();
        initData();
        return currentView;
    }
    public static RankListFragment newInstance(int fragmentType) {
        RankListFragment fr = new RankListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }

    private void initView(){
        type = getArguments().getInt(TYPE);
        rankLV= (ListView) currentView.findViewById(R.id.rankLV);
        adapter=new RankAdapter(mActivity,list);
        rankLV.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (RankActivity)context;
    }

    private void initData(){
        request();
    }


    private void request(){

        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.GET_EXAM_RANK_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("rankRe", response);
                        try {
                            list.clear();
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    User user=new User();
                                    user.setMinutes(obj.getString("minutes"));
                                    user.setNickName(obj.getString("nick_name"));
                                    user.setResult(obj.getString("result"));
                                    user.setAvatar(obj.getString("avatar"));
                                    list.add(user);
                                }
                                if(list.size()>0){
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
                params.put("type", type+"");
                if(type==1){
                    params.put("scope", mActivity.getUserInfo(7));
                }else if(type==2){
                    params.put("scope", mActivity.getUserInfo(8));
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

}
