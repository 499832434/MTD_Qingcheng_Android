package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
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
import com.qcjkjg.trafficrules.activity.circle.PublishCircleInfoActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.activity.signup.SignupContentActivity;
import com.qcjkjg.trafficrules.adapter.CircleListAdapter;
import com.qcjkjg.trafficrules.adapter.SystemMessageAdapter;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.DateUtils;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Signup;
import de.greenrobot.event.EventBus;
import me.codeboy.android.cycleviewpager.CycleViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    protected MainActivity mActivity;
    private CycleViewPager cycleViewPager;
    private MyListView circleLV,systemLV;
    private List<MessageInfo> messageList=new ArrayList<MessageInfo>();
    private List<Signup> signList=new ArrayList<Signup>();//0:系统消息
    private CircleListAdapter messageAdapter;
    private SystemMessageAdapter systemMessageAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static String CIRCLEFLAG = "circleflag";
    private SwipeToLoadLayout swipeToLoadLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        EventBus.getDefault().register(this);
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
        currentView.findViewById(R.id.publishIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActivity.getUserIsLogin()) {
                    startActivity(new Intent(mActivity, PublishCircleInfoActivity.class));
                } else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }
        });
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        circleLV= (MyListView) currentView.findViewById(R.id.circleLV);
        messageAdapter=new CircleListAdapter(mActivity, messageList);
        circleLV.setAdapter(messageAdapter);

        systemLV= (MyListView) currentView.findViewById(R.id.systemLV);
        systemMessageAdapter=new SystemMessageAdapter(mActivity, signList,false);
        systemLV.setAdapter(systemMessageAdapter);
        systemLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, SignupContentActivity.class);
                intent.putExtra("id", signList.get(i).getNewsId());
                intent.putExtra("flag", "news");
                startActivity(intent);
            }
        });

        cycleViewPager= (CycleViewPager) currentView.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);

        ((BaseActivity)mActivity).adlist("0", cycleViewPager);

        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });

        ((ScrollView)currentView.findViewById(R.id.swipe_target)).smoothScrollTo(0, 20);
        ((ScrollView)currentView.findViewById(R.id.swipe_target)).setFocusable(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        if(messageList.size()>0){
            request(messageList.get(messageList.size()-1).getCid()+"");
        }
    }

    @Override
    public void onRefresh() {
        request("");
    }


    /**
     * 网络请求
     */
    private void request(final String cid) {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("circleRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(TextUtils.isEmpty(cid)){
                                    messageList.clear();
                                }
                                JSONArray infoArr2=jsonObject.getJSONArray("notice_info");
                                for(int i=0;i<infoArr2.length();i++){
                                    JSONObject obj=infoArr2.getJSONObject(i);
                                    Signup signup=new Signup();
                                    signup.setNewsId(obj.getInt("news_id"));
                                    signup.setTitle(obj.getString("title"));
                                    signup.setAbstractStr(obj.getString("abstract"));
                                    signup.setPictureUrl(obj.getString("img_url"));
                                    signup.setPubtime(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                    signList.add(signup);
                                }
                                if(signList.size()>0){
                                    systemMessageAdapter.notifyDataSetChanged();
                                }

                                JSONArray infoArr1=jsonObject.getJSONArray("circle_top_info");
                                for(int i=0;i<infoArr1.length();i++){
                                    JSONObject obj1=infoArr1.getJSONObject(i);
                                    MessageInfo info=new MessageInfo();
                                    info.setCid(obj1.getInt("c_id"));
                                    info.setReplyCnt(obj1.getInt("reply_cnt"));
                                    info.setNickName(obj1.getString("nick_name"));
                                    info.setContent(obj1.getString("content"));
                                    info.setPhone(obj1.getString("phone"));
                                    info.setCreateTime(DateUtils.getInterval(obj1.getLong("create_time")));
                                    info.setZanCnt(obj1.getInt("zan_cnt"));
                                    info.setAvatar(obj1.getString("avatar"));
                                    info.setIsZan(obj1.getInt("is_zan"));
                                    info.setTopFlag(1);
                                    List<String> imagesList=new ArrayList<String>();
                                    JSONArray array=obj1.getJSONArray("images");
                                    for(int j=0;j<array.length();j++){
                                        imagesList.add((String) array.get(j));
                                    }
                                    info.setPricturlList(imagesList);
                                    messageList.add(info);
                                }
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    MessageInfo info=new MessageInfo();
                                    info.setCid(obj.getInt("c_id"));
                                    info.setReplyCnt(obj.getInt("reply_cnt"));
                                    info.setNickName(obj.getString("nick_name"));
                                    info.setContent(obj.getString("content"));
                                    info.setPhone(obj.getString("phone"));
                                    info.setCreateTime(DateUtils.getInterval(obj.getLong("create_time")));
                                    info.setZanCnt(obj.getInt("zan_cnt"));
                                    info.setAvatar(obj.getString("avatar"));
                                    info.setIsZan(obj.getInt("is_zan"));
                                    info.setTopFlag(0);
                                    List<String> imagesList=new ArrayList<String>();
                                    JSONArray array=obj.getJSONArray("images");
                                    for(int j=0;j<array.length();j++){
                                        imagesList.add((String) array.get(j));
                                    }
                                    info.setPricturlList(imagesList);
                                    messageList.add(info);
                                }
                                if(messageList.size()>0){
                                    messageAdapter.notifyDataSetChanged();
                                    if(TextUtils.isEmpty(cid)){
                                        circleLV.setSelection(0);
                                    }
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
                if(!TextUtils.isEmpty(cid)){
                    params.put("c_id", cid);
                }
                params.put("city", "淄博市");
                if(mActivity.getUserIsLogin()){
                    params.put("phone", mActivity.getUserInfo(1));
                }
//                params.put("belong_area", "120");
//                params.put("page_count", "1");
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public  void updataItem(MessageInfo infoFlag,int position){
        MessageInfo info=messageList.get(position);
        info.setZanCnt(infoFlag.getZanCnt());
        info.setReplyCnt(infoFlag.getReplyCnt());
        info.setIsZan(infoFlag.getIsZan());
        messageList.set(position,info);
        int firstvisible = circleLV.getFirstVisiblePosition();
        int lastvisibale = circleLV.getLastVisiblePosition();
        if(position>=firstvisible&&position<=lastvisibale){
            View view = circleLV.getChildAt(position - firstvisible);
            CircleListAdapter.ViewHolder viewHolder = (CircleListAdapter.ViewHolder) view.getTag();
            //然后使用viewholder去更新需要更新的view。
            viewHolder.leaveTV.setText(infoFlag.getReplyCnt()+"");
            viewHolder.fabulousTV.setText(infoFlag.getZanCnt()+"");
            if(infoFlag.getIsZan()==1){
                viewHolder.fabulousIV.setImageResource(R.drawable.ic_praise_s);
                viewHolder.fabulousIV.setTag(1);
            }else{
                viewHolder.fabulousIV.setImageResource(R.drawable.ic_praise_n);
                viewHolder.fabulousIV.setTag(0);
            }
        }

    }


    public void onEvent(CircleDataUpEvent event) {
        MessageInfo info=event.getInfo();
        int postion=event.getPositon();
        if(postion<0){
            return;
        }
        updataItem(info,postion);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 点赞网络请求
     */
    public void requestZan(final  int flag,final int position) {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_ZAN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("zanRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                MessageInfo info=messageList.get(position);
                                if(flag==0){
                                    info.setZanCnt(info.getZanCnt()+1);
                                    info.setIsZan(1);
                                }else{
                                    info.setZanCnt(info.getZanCnt()-1);
                                    info.setIsZan(0);
                                }
                                updataItem(info, position);
                            }else if(jsonObject.getString("code").equals("404")){
                                mActivity.toast(jsonObject.getString("msg"));
                            }else{
                                mActivity.toast(jsonObject.getString("msg"));
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
                params.put("c_id", messageList.get(position).getCid()+"");
                params.put("phone", mActivity.getUserInfo(1));
                params.put("action",flag+"");
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }



}
