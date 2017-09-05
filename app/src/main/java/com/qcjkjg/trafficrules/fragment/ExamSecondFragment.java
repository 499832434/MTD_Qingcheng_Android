package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.tubiao.TubiaoActivity;
import com.qcjkjg.trafficrules.activity.web.BaseWebViewActivity;
import com.qcjkjg.trafficrules.adapter.VideoAdapter;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Video;
import me.codeboy.android.cycleviewpager.CycleViewPager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ExamSecondFragment extends Fragment{
    private View currentView = null;
    private int fragmentType = 2;
    private final static String FRAGMENT_TYPE = "fragmentType";
    protected MainActivity mActivity;
    private CycleViewPager cycleViewPager;
    private MyListView videoMLV;
    private List<Video> list=new ArrayList<Video>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private VideoAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam_second, container, false);
        initView();
        return currentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        request();
    }

    public static ExamSecondFragment newInstance(int fragmentType) {
        ExamSecondFragment fr = new ExamSecondFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }

    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);

        cycleViewPager= (CycleViewPager) currentView.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);

        if(fragmentType==2){
            ((BaseActivity)mActivity).adlist("2", cycleViewPager);
        }else {
            ((BaseActivity)mActivity).adlist("3", cycleViewPager);
        }

        if(fragmentType==2){
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科二考规");
        }else{
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科三考规");
        }

        currentView.findViewById(R.id.aboutTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mActivity, TubiaoActivity.class);
//                intent.putExtra("type", fragmentType + "");
//                startActivity(intent);
            }
        });

        mActivity.setShareView(currentView.findViewById(R.id.shareLL));

        videoMLV= (MyListView) currentView.findViewById(R.id.videoMLV);
        adapter=new VideoAdapter(mActivity,list);
        videoMLV.setAdapter(adapter);
        videoMLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(mActivity, BaseWebViewActivity.class);
                intent.putExtra("url",list.get(i).getVideoUrl());
                intent.putExtra("title","详情");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }


    /**
     * 视频列表
     */
    public void request() {
        if (!NetworkUtils.isNetworkAvailable(mActivity)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.VIDEO_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("videoRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONArray infoArr=jsonObject.getJSONArray("info");
                                for(int i=0;i<infoArr.length();i++){
                                    JSONObject obj=infoArr.getJSONObject(i);
                                    Video video=new Video();
                                    video.setContent(obj.getString("content"));
                                    video.setId(obj.getInt("id"));
                                    video.setPicUrl(obj.getString("pic_url"));
                                    video.setPubtime(sdf.format(new Date(obj.getLong("pubtime") * 1000)));
                                    video.setTitle(obj.getString("title"));
                                    video.setViewCnt(obj.getInt("view_cnt"));
                                    video.setType(obj.getInt("type"));
                                    video.setVideoUrl(obj.getString("video_url"));
                                    list.add(video);
                                }
                                if(list.size()>0){
                                    adapter.notifyDataSetChanged();
                                }
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
                params.put("type", fragmentType+"");
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }
}
