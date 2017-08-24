package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.exam.*;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.util.List;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ExamOneFragment extends Fragment{
    private View currentView = null;
    private int fragmentType = 1;
    private final static String FRAGMENT_TYPE = "fragmentType";
    protected MainActivity mActivity;
    private int seqNum=0,moniNum=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam_one, container, false);
        initData();
        initView();
        return currentView;
    }

    public static ExamOneFragment newInstance(int fragmentType) {
        ExamOneFragment fr = new ExamOneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }

    private void initData(){
        DbCreateHelper helper=new DbCreateHelper(mActivity);
        seqNum=helper.getSubjectList(fragmentType+"","","subseq").size();
    }

    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        if(fragmentType==1){
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科一考规");
        }else{
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科四考规");
        }

        ((TextView)currentView.findViewById(R.id.seqAnswerTV)).setText("共"+seqNum+"题");

        currentView.findViewById(R.id.seqAnswerLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                intent.putExtra("type", "subseq");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.beitiTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                intent.putExtra("type", "submemory");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.zhuanxiangTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mActivity, SubClassActivity.class);
                intent.putExtra("fragmentType",fragmentType+"");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.zhangjieTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, SubchapterActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.weizuoTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(mActivity, SubchapterActivity.class);
//                intent.putExtra("fragmentType",fragmentType+"");
//                startActivity(intent);

                Intent intent=new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType+"");
                intent.putExtra("type","subnodone");
                startActivity(intent);

            }
        });

        currentView.findViewById(R.id.vipTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, VipActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.cuotiTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ErrorCollectActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }


}
