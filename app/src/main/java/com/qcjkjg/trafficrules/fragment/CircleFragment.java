package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.circle.PublishCircleInfoActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.adapter.MessageReplyMeAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.ViewFactory;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Signup;
import com.squareup.picasso.Picasso;
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
    private List<View> views = new ArrayList<View>();
    private List<MessageInfo> messageList=new ArrayList<MessageInfo>();
    private SwipeToLoadLayout swipeToLoadLayout;
    private MessageReplyMeAdapter messageAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String[] urls;
    public static String CIRCLEFLAG = "circleflag";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        urls = new String[]{
                "http://a.hiphotos.baidu.com/pic/w%3D230/sign=bf59456cc9fcc3ceb4c0ce30a244d6b7/4afbfbedab64034f80b90b48aec379310a551d0c.jpg",
                "http://d.hiphotos.baidu.com/pic/w%3D230/sign=e92db34bc995d143da76e32043f18296/8601a18b87d6277f35a56d7029381f30e824fcc7.jpg",
                "http://a.hiphotos.baidu.com/pic/w%3D230/sign=f71ba7639c16fdfad86cc1ed848e8cea/241f95cad1c8a78699e316466609c93d71cf50a8.jpg",
                "http://f.hiphotos.baidu.com/pic/w%3D230/sign=dbe3e3fab8014a90813e41be99763971/63d0f703918fa0eccd0e3845279759ee3c6ddba9.jpg",
                "http://d.hiphotos.baidu.com/pic/w%3D230/sign=bb37f35dbd315c6043956cecbdb0cbe6/d000baa1cd11728b1cd9d36ac9fcc3cec2fd2c85.jpg",
                "http://c.hiphotos.baidu.com/pic/w%3D230/sign=59a8fcd9f91986184147e8877aec2e69/3c6d55fbb2fb4316879103c221a4462308f7d3f8.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=20f4fb5b96dda144da096bb182b6d009/95eef01f3a292df5ccddf35dbd315c6034a8735f.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=dc101ae5bba1cd1105b675238910c8b0/d01373f082025aaf80c3801efaedab64024f1a75.jpg",
                "http://f.hiphotos.baidu.com/pic/w%3D230/sign=4e07a985fd039245a1b5e60cb795a4a8/024f78f0f736afc3f4f8602db219ebc4b6451285.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=1522ba04b151f819f1250449eab44a76/58ee3d6d55fbb2fb6f26643d4e4a20a44623dc8b.jpg",
                "http://d.hiphotos.baidu.com/pic/w%3D230/sign=348b736477c6a7efb926af25cdfbafe9/4034970a304e251f46503243a686c9177e3e53e2.jpg",
                "http://b.hiphotos.baidu.com/pic/w%3D230/sign=60e9d1767aec54e741ec1d1d89399bfd/d058ccbf6c81800a580ed546b03533fa838b478a.jpg",
                "http://b.hiphotos.baidu.com/pic/w%3D230/sign=3f09e4c9ca1349547e1eef67664f92dd/b812c8fcc3cec3fd9dd5fb3dd788d43f8694278a.jpg",
                "http://b.hiphotos.baidu.com/pic/w%3D230/sign=3f09e4c9ca1349547e1eef67664f92dd/b812c8fcc3cec3fd9dd5fb3dd788d43f8694278a.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=f95c1f21b3b7d0a27bc9039efbee760d/e1fe9925bc315c6060c0187c8cb1cb13485477cf.jpg",
                "http://f.hiphotos.baidu.com/pic/w%3D230/sign=3497a62dc8177f3e1034fb0e40ce3bb9/7a899e510fb30f24a7f8d898c995d143ac4b03ce.jpg",
                "http://a.hiphotos.baidu.com/pic/w%3D230/sign=f61a1f6efcfaaf5184e386bcbc5494ed/94cad1c8a786c917473fe571c83d70cf3bc757bd.jpg",
                "http://f.hiphotos.baidu.com/pic/w%3D230/sign=55b651b1279759ee4a5067c882fa434e/b7003af33a87e9506866c6d011385343fbf2b41c.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=3a06cb97cf1b9d168ac79d62c3dfb4eb/314e251f95cad1c8eb46a56f7e3e6709c83d51fc.jpg",
                "http://g.hiphotos.baidu.com/pic/w%3D230/sign=ea85ce6bf11f3a295ac8d2cda924bce3/c83d70cf3bc79f3d9e0d8b85bba1cd11738b2992.jpg",
                "http://h.hiphotos.baidu.com/pic/w%3D230/sign=8b92084b359b033b2c88fbd925cf3620/203fb80e7bec54e769690f51b8389b504ec26af3.jpg",
                "http://b.hiphotos.baidu.com/pic/w%3D230/sign=60e61a8883025aafd33279c8cbecab8d/060828381f30e924096cc0794d086e061c95f7aa.jpg",
                "http://a.hiphotos.baidu.com/pic/w%3D230/sign=e869d0a6d52a283443a631086bb4c92e/00e93901213fb80ef3dca16c37d12f2eb9389478.jpg",
                "http://e.hiphotos.baidu.com/pic/w%3D230/sign=b9aae94c0ff41bd5da53eff761db81a0/b64543a98226cffcdaa3b1d5b8014a90f703eac8.jpg",
                "http://d.hiphotos.baidu.com/pic/w%3D230/sign=3bde0aab08f79052ef1f403d3cf2d738/9213b07eca80653817adddab96dda144ac3482c6.jpg",
                "http://b.hiphotos.baidu.com/pic/w%3D230/sign=639f5a60a9d3fd1f3609a539004f25ce/b7fd5266d01609244a0c6b55d50735fae6cd3457.jpg"
        };
        ((CustomTitleBar) currentView.findViewById(R.id.customTitleBar)).setRightImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mActivity.getUserIsLogin()){
                            startActivity(new Intent(mActivity, PublishCircleInfoActivity.class));
                        }else {
                            startActivity(new Intent(mActivity, LoginActivity.class));
                        }
//                        startActivity(new Intent(mActivity, MessageMainActivity.class));
                    }
                });
        View view=LayoutInflater.from(mActivity).inflate(R.layout.headview_circle,null);
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        circleLV= (ListView) currentView.findViewById(R.id.swipe_target);
//        circleLV.addHeaderView(view);
        messageAdapter=new MessageReplyMeAdapter(mActivity, messageList);
        circleLV.setAdapter(messageAdapter);
        cycleViewPager= (CycleViewPager) view.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);


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
                                    info.setCreateTime(sdf.format(new Date(obj.getLong("create_time") * 1000)));
                                    info.setZanCnt(obj.getInt("zan_cnt"));
                                    info.setAvatar(obj.getString("avatar"));
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
}
