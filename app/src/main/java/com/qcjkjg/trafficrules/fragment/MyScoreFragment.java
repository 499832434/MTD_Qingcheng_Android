package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.exam.RankActivity;
import com.qcjkjg.trafficrules.adapter.ExamScoreAdapter;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.ExamScore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/29.
 */
public class MyScoreFragment extends Fragment{
    private View currentView = null;
    protected RankActivity mActivity;
    private ExamScoreAdapter adapter;
    private List<ExamScore> list=new ArrayList<ExamScore>();
    private MyListView scoreMLV;
    private String fragmentType;
    private final static String TYPE = "type";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_my_score, container, false);
        initData();
        initView();
        return currentView;
    }
    public static MyScoreFragment newInstance(String fragmentType) {
        MyScoreFragment fr = new MyScoreFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }

    private void initData(){
        fragmentType = getArguments().getString(TYPE);
        DbHelper db=new DbHelper(mActivity);
        Log.e("fragmentType2",fragmentType);
        list=db.selectExamScore(fragmentType);
        for(int i=0;i<list.size();i++){
            Log.e("aaa", list.get(i).getDate());
        }
    }
    private void initView(){
        scoreMLV= (MyListView) currentView.findViewById(R.id.scoreMLV);
        adapter=new ExamScoreAdapter(mActivity,list);
        scoreMLV.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (RankActivity)context;
    }
}
