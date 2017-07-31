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
import com.qcjkjg.trafficrules.adapter.MessageFabulousAdapter;
import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
import com.qcjkjg.trafficrules.adapter.SystemMessageAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageFabulous;
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
    private int flag;//0:系统消息1:我的主题2:收到的赞
    private List<Signup> signList=new ArrayList<Signup>();
    private SwipeToLoadLayout swipeToLoadLayout;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                adapter1=new MyThemeAdapter(BaseListViewActivity.this,null);
                listView.setAdapter(adapter1);
                break;
            case 2:
                ((CustomTitleBar) findViewById(R.id.customTitleBar)).setTitleTextView("收到的赞");
                adapter2=new MessageFabulousAdapter(BaseListViewActivity.this,null);
                listView.setAdapter(adapter2);
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
                    break;
                case 2:
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
                swipeToLoadLayout.setRefreshing(false);
                break;
            case 2:
                swipeToLoadLayout.setRefreshing(false);
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
                                    switch (flag){
                                        case 0:
                                            adapter0.notifyDataSetChanged();
                                            break;
                                        case 1:
                                            adapter1.notifyDataSetChanged();
                                            break;
                                        case 2:
                                            adapter2.notifyDataSetChanged();
                                            break;
                                    }
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
}
