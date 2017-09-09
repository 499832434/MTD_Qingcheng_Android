package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
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
import com.qcjkjg.trafficrules.adapter.MyMoneyAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.vo.AccountMoney;
import com.qcjkjg.trafficrules.vo.Signup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/8/5.
 */
public class MyMoneyActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView moneyLV;
    private MyMoneyAdapter adapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private List<AccountMoney> list=new ArrayList<AccountMoney>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);

        initView();
    }

    private void initView(){
        moneyLV= (ListView) findViewById(R.id.swipe_target);
        adapter=new MyMoneyAdapter(MyMoneyActivity.this,list);
        moneyLV.setAdapter(adapter);

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
        request(list.get(list.size() - 1).getPubtime());
    }

    @Override
    public void onRefresh() {
        request("");
    }


    /**
     * 网络请求
     */
    private void request(final String pubtime) {
        if (!NetworkUtils.isNetworkAvailable(MyMoneyActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.SCORE_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("baseListViewRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(pubtime)){
                                    list.clear();
                                }
                                ((TextView)findViewById(R.id.moneyTV)).setText(jsonObject.getString("score"));
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    AccountMoney accountMoney=new AccountMoney();
                                    accountMoney.setScore(obj.getString("score"));
                                    accountMoney.setAvatar(obj.getString("avatar"));
                                    accountMoney.setFormPhone(obj.getString("from_phone"));
                                    accountMoney.setNickName(obj.getString("nick_name"));
                                    accountMoney.setPubtime(obj.getString("pubtime"));
                                    accountMoney.setType(obj.getInt("type"));
                                    accountMoney.setPhone(obj.getString("phone"));
                                    accountMoney.setDate(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                    list.add(accountMoney);
                                }
                                if(list.size()>0){
                                    adapter.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(MyMoneyActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
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
                params.put("phone", getUserInfo(1));
                if(!TextUtils.isEmpty(pubtime)){
                    params.put("pubtime", pubtime);
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

}
