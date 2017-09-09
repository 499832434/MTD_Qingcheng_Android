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
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.exam.*;
import com.qcjkjg.trafficrules.activity.login.BindPhoneActivity;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.activity.tubiao.TubiaoActivity;
import com.qcjkjg.trafficrules.activity.web.BaseWebViewActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.event.LoginSuccessEvent;
import com.qcjkjg.trafficrules.event.LoginoutSuccessEvent;
import com.qcjkjg.trafficrules.event.RefreshExamNumEvent;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;
import com.qcjkjg.trafficrules.vo.User;
import de.greenrobot.event.EventBus;
import me.codeboy.android.cycleviewpager.CycleViewPager;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ExamOneFragment extends Fragment{
    private View currentView = null;
    private int fragmentType = 1;
    private final static String FRAGMENT_TYPE = "fragmentType";
    protected MainActivity mActivity;
    private int seqNum=0,moniNum=0;
    private CycleViewPager cycleViewPager;
    private TextView seqAnswerTV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam_one, container, false);
        EventBus.getDefault().register(this);
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
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
    }

    private void initView(){

        refresh();

        cycleViewPager= (CycleViewPager) currentView.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);

        if(fragmentType==1){
            ((BaseActivity)mActivity).adlist("1", cycleViewPager);
        }else {
            ((BaseActivity)mActivity).adlist("4", cycleViewPager);
        }

        if(fragmentType==1){
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科一考规");
        }else{
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科四考规");
        }
        seqAnswerTV=((TextView)currentView.findViewById(R.id.seqAnswerTV));


        currentView.findViewById(R.id.ruleTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BaseListViewActivity.class);
                intent.putExtra("flag", 4);
                intent.putExtra("type", fragmentType+"");
                intent.putExtra("sub_type", "1");
                if(fragmentType==1){
                    intent.putExtra("title", "科一考规");
                }else{
                    intent.putExtra("title", "科四考规");
                }
                startActivity(intent);
            }
        });


        currentView.findViewById(R.id.questionTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, BaseListViewActivity.class);
                intent.putExtra("flag", 4);
                intent.putExtra("type", fragmentType+"");
                intent.putExtra("sub_type", "2");
                intent.putExtra("title", "答题技巧");
                startActivity(intent);
            }
        });



        currentView.findViewById(R.id.scoreTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, RankActivity.class);
                intent.putExtra("type", fragmentType + "");
                intent.putExtra("name", "rank");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.aboutTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, TubiaoActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.seqAnswerLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(0==seqNum){
                    mActivity.toast(mActivity,"暂无题目");
                    return;
                }
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
        currentView.findViewById(R.id.nantiTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                intent.putExtra("type", "subnanti");
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
                Intent intent=new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType+"");
                intent.putExtra("type","subnodone");
                startActivity(intent);

            }
        });
        currentView.findViewById(R.id.moniLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MockExamActivity.class);
                intent.putExtra("fragmentType", fragmentType + "");
                startActivity(intent);
            }
        });
        currentView.findViewById(R.id.vipTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("0".equals(mActivity.getUserInfo(3))){
                    Intent intent=new Intent(mActivity, BaseWebViewActivity.class);
                    intent.putExtra("url", ApiConstants.VIP_PERMISSION_API);
                    intent.putExtra("fragmentType", fragmentType+ "");
                    intent.putExtra("title","VIP特权");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(mActivity, VipActivity.class);
                    intent.putExtra("fragmentType", fragmentType + "");
                    startActivity(intent);
                }
//                Intent intent=new Intent(mActivity, BaseWebViewActivity.class);
//                intent.putExtra("url", ApiConstants.VIP_PERMISSION_API);
//                intent.putExtra("fragmentType", fragmentType+ "");
//                intent.putExtra("title","VIP特权");
//                startActivity(intent);
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

        mActivity.setShareView(currentView.findViewById(R.id.shareLL));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }



    public void onEvent(RefreshExamNumEvent event) {
        refresh();
    }

    public void onEvent(LoginoutSuccessEvent event) {
        refresh();
    }


    public void onEvent(LoginSuccessEvent event) {
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void refresh(){
        DbCreateHelper helper=new DbCreateHelper(mActivity);
        seqNum=helper.getSubjectList(fragmentType+"","","subseq").size();
        DbHelper helper1=new DbHelper(mActivity);
        BaseActivity.subtypePosition1=helper1.selectPosition("1");
        DbHelper helper4=new DbHelper(mActivity);
        BaseActivity.subtypePosition4=helper4.selectPosition("4");



        DbHelper helper2=new DbHelper(mActivity);
        int score=helper2.selectExamScoreMax(fragmentType+"");
        String str="";
        if(score==-1){
            str="没有成绩";
        }else{
            str="最高"+score+"分";
        }
        ((TextView) currentView.findViewById(R.id.moniTV)).setText(str);
    }


    @Override
    public void onResume() {
        super.onResume();
        int num=queryWholeNum();
        if(fragmentType==1){
            if(num==0){
                seqAnswerTV.setText("共" + seqNum + "题");
            }else{
                seqAnswerTV.setText(num+"/" + seqNum + "题");
            }
        }else{
            if(num==0){
                seqAnswerTV.setText("共" + seqNum + "题");
            }else{
                seqAnswerTV.setText(num+"/" + seqNum + "题");
            }
        }
    }


    private int queryWholeNum() {//true:正确false:错误
        SubjectSelect subjectSelect = new SubjectSelect();
        subjectSelect.setSubType(fragmentType);
        subjectSelect.setSeqAnswer("0");
        DbHelper db = new DbHelper(mActivity);
        return db.queryWholeSubNum1(subjectSelect);
    }


}
