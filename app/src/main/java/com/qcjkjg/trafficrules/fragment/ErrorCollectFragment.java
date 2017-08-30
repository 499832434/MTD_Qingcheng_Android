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
import android.widget.ListView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.exam.*;
import com.qcjkjg.trafficrules.adapter.SubErrorCollectAdapter;
import com.qcjkjg.trafficrules.adapter.SubVipAdapter;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ErrorCollectFragment extends Fragment{
    private View currentView = null;
    private String fragmentType = "1",type;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private final static String TYPE = "type";
    protected ErrorCollectActivity mActivity;
    private ListView chapterLV;
    private SubErrorCollectAdapter adapter;
    private List<String> list=new ArrayList<String>();
    private List<String> allList=new ArrayList<String>();
    private TextView allTV,numTV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_error_collect, container, false);
        initData();
        initView();
        return currentView;
    }

    public static ErrorCollectFragment newInstance(String fragmentType,String type) {
        ErrorCollectFragment fr = new ErrorCollectFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FRAGMENT_TYPE, fragmentType);
        bundle.putString(TYPE, type);
        fr.setArguments(bundle);
        return fr;
    }

    private void initData(){
        fragmentType = getArguments().getString(FRAGMENT_TYPE);
        type = getArguments().getString(TYPE);
        if("subcollect".equals(type)){
            DbHelper dbHelper=new DbHelper(mActivity);
            list=dbHelper.selectCollectChapter(true,fragmentType);
            allList=dbHelper.selectCollectAllSubid(true,fragmentType);
        }else if("suberror".equals(type)){
            DbHelper dbHelper=new DbHelper(mActivity);
            list=dbHelper.selectCollectChapter(false,fragmentType);
            allList=dbHelper.selectCollectAllSubid(false,fragmentType);
        }

    }

    private void initView(){
        chapterLV= (ListView) currentView.findViewById(R.id.chapterLV);
        adapter=new SubErrorCollectAdapter(mActivity,list,fragmentType,type);
        chapterLV.setAdapter(adapter);

        allTV= (TextView) currentView.findViewById(R.id.allTV);
        numTV= (TextView) currentView.findViewById(R.id.numTV);
        if("subcollect".equals(type)){
            allTV.setText("全部收藏");
        }else{
            allTV.setText("全部错题");
        }
        numTV.setText(allList.size()+"道题");

        currentView.findViewById(R.id.RL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allList.size()==0){
                    mActivity.toast(mActivity,"暂无题目");
                    return;
                }
                if("subcollect".equals(type)){
                    Intent intent=new Intent(mActivity, AnswerActivity.class);
                    intent.putExtra("fragmentType",fragmentType);
                    intent.putExtra("type","subcollectall");
//                    intent.putExtra("subcollectall","0");
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(mActivity, AnswerActivity.class);
                    intent.putExtra("fragmentType",fragmentType);
                    intent.putExtra("type","suberrorall");
//                    intent.putExtra("suberrorall","0");
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (ErrorCollectActivity)context;
    }


}
