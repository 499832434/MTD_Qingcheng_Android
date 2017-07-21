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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.adapter.MessageReplyMeAdapter;
import com.qcjkjg.trafficrules.adapter.SignupAdapter;
import com.qcjkjg.trafficrules.utils.ViewFactory;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.squareup.picasso.Picasso;
import me.codeboy.android.cycleviewpager.CycleViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {
    private View currentView = null;
    protected MainActivity mActivity;
    private CycleViewPager cycleViewPager;
    private ListView circleLV;
    private List<View> views = new ArrayList<View>();
    private List<String> list=new ArrayList<String>();
    private SwipeToLoadLayout swipeToLoadLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_circle, container, false);
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
        View view=LayoutInflater.from(mActivity).inflate(R.layout.headview_circle,null);
        swipeToLoadLayout = (SwipeToLoadLayout) currentView.findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        circleLV= (ListView) currentView.findViewById(R.id.swipe_target);
        circleLV.addHeaderView(view);
        circleLV.setAdapter(new MessageReplyMeAdapter(mActivity, null));
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
        Toast.makeText(mActivity,"加载更多",Toast.LENGTH_SHORT).show();
        swipeToLoadLayout.setLoadingMore(false);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                list.clear();
                list.add("http://pic12.nipic.com/20101225/2696160_091326670000_2.jpg");
                list.add("http://img10.3lian.com/c1/newpic/10/15/45.jpg");
                list.add("http://bztzl.com/uploads/allimg/150609/16445U345-2.jpg");
                list.add("http://bizhi.zhuoku.com/2009/02/0222/Ferrari/Ferrari75.jpg");
                views.clear();
                ImageView view1=ViewFactory.getView(mActivity);
                view1.setTag(list.size()-1);
                Picasso.with(mActivity).load(list.get(list.size()-1)).into(view1);
                views.add(view1);
                view1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("zzz", list.get((Integer) view.getTag()));
                    }
                });
                for(int i=0;i<list.size();i++){
                    ImageView view3=ViewFactory.getView(mActivity);
                    view3.setTag(i);
                    Picasso.with(mActivity).load(list.get(i)).into(view3);
                    views.add(view3);
                    view3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("zzz", list.get((Integer) view.getTag()));
                        }
                    });
                }
                ImageView view2=ViewFactory.getView(mActivity);
                view2.setTag(0);
                Picasso.with(mActivity).load(list.get(0)).into(view2);
                views.add(view2);
                view2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("zzz", list.get((Integer) view.getTag()));
                    }
                });

                cycleViewPager.setData(views, true, true, 3000);
                swipeToLoadLayout.setRefreshing(false);
            }
        },3000);


    }

    @Override
    public void onPause() {
        super.onPause();
        if (swipeToLoadLayout.isRefreshing()) {
            swipeToLoadLayout.setRefreshing(false);
        }
        if (swipeToLoadLayout.isLoadingMore()) {
            swipeToLoadLayout.setLoadingMore(false);
        }
    }
}
