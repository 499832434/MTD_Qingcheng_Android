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
import com.qcjkjg.trafficrules.adapter.CircleListAdapter;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.DateUtils;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageInfo;
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
    private ListView circleLV;
    private List<MessageInfo> messageList=new ArrayList<MessageInfo>();
    private CircleListAdapter messageAdapter;
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
                if(mActivity.getUserIsLogin()){
                    startActivity(new Intent(mActivity, PublishCircleInfoActivity.class));
                }else {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }
        });
        View view=LayoutInflater.from(mActivity).inflate(R.layout.headview_circle,null);
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        circleLV= (ListView) currentView.findViewById(R.id.swipe_target);
        circleLV.addHeaderView(view);
        messageAdapter=new CircleListAdapter(mActivity, messageList);
        circleLV.setAdapter(messageAdapter);
        cycleViewPager= (CycleViewPager) view.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);

        ((BaseActivity)mActivity).adlist("0",cycleViewPager);

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
//        swipeToLoadLayout.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                list.clear();
//                list.add("http://pic12.nipic.com/20101225/2696160_091326670000_2.jpg");
//                list.add("http://img10.3lian.com/c1/newpic/10/15/45.jpg");
//                list.add("http://bztzl.com/uploads/allimg/150609/16445U345-2.jpg");
//                list.add("http://bizhi.zhuoku.com/2009/02/0222/Ferrari/Ferrari75.jpg");
//                views.clear();
//                ImageView view1=ViewFactory.getView(mActivity);
//                view1.setTag(list.size()-1);
//                Picasso.with(mActivity).load(list.get(list.size()-1)).into(view1);
//                views.add(view1);
//                view1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("zzz", list.get((Integer) view.getTag()));
//                    }
//                });
//                for(int i=0;i<list.size();i++){
//                    ImageView view3=ViewFactory.getView(mActivity);
//                    view3.setTag(i);
//                    Picasso.with(mActivity).load(list.get(i)).into(view3);
//                    views.add(view3);
//                    view3.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Log.e("zzz", list.get((Integer) view.getTag()));
//                        }
//                    });
//                }
//                ImageView view2=ViewFactory.getView(mActivity);
//                view2.setTag(0);
//                Picasso.with(mActivity).load(list.get(0)).into(view2);
//                views.add(view2);
//                view2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("zzz", list.get((Integer) view.getTag()));
//                    }
//                });
//
//                cycleViewPager.setData(views, true, true, 3000);
//                swipeToLoadLayout.setRefreshing(false);
//            }
//        },3000);


    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (swipeToLoadLayout.isRefreshing()) {
//            swipeToLoadLayout.setRefreshing(false);
//        }
//        if (swipeToLoadLayout.isLoadingMore()) {
//            swipeToLoadLayout.setLoadingMore(false);
//        }
//    }


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
//                params.put("page_count", "2");
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
