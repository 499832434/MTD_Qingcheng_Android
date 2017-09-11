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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.exam.*;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.activity.web.BaseWebViewActivity;
import com.qcjkjg.trafficrules.adapter.ExamScoreAdapter;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.view.CircleImageView;
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
    private ImageView vipIV;
    private TextView accountTV;
    private CircleImageView accountIV;
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
    }
    private void initView(){
        accountIV= (CircleImageView) currentView.findViewById(R.id.accountIV);
        accountTV= (TextView) currentView.findViewById(R.id.accountTV);
        vipIV= (ImageView) currentView.findViewById(R.id.vipIV);
        currentView.findViewById(R.id.loginRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mActivity.getUserIsLogin()){
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }
            }
        });
        scoreMLV= (MyListView) currentView.findViewById(R.id.scoreMLV);
        adapter=new ExamScoreAdapter(mActivity,list);
        scoreMLV.setAdapter(adapter);
        scoreMLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ExamScore examScore=list.get(position);
                Intent intent=new Intent(mActivity, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType);
                intent.putExtra("type","historyscore");
                intent.putExtra("answer",examScore.getAnswer());
                intent.putExtra("subs",examScore.getSubs());
                startActivity(intent);

            }
        });

        if(list.size()>0){
            ((TextView)currentView.findViewById(R.id.timeTV)).setText(list.size()+"次");
            int want=0;
            if(list.size()<=3){
                for(int i=0;i<list.size();i++){
                    want=want+Integer.parseInt(list.get(i).getScore());
                }
                want=want/list.size();
            }else{
                want=(Integer.parseInt(list.get(0).getScore())+ Integer.parseInt(list.get(1).getScore())+
                        Integer.parseInt(list.get(2).getScore()))/3;
            }
            ((TextView)currentView.findViewById(R.id.wantTV)).setText(want+"分");
            int avger=0;
            for(int i=0;i<list.size();i++){
                avger=avger+Integer.parseInt(list.get(i).getScore());
            }
            avger=avger/list.size();
            ((TextView)currentView.findViewById(R.id.avgertimeTV)).setText(avger+"分");
        }

        currentView.findViewById(R.id.moniTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mActivity, MockExamActivity.class);
//                intent.putExtra("fragmentType", fragmentType + "");
//                startActivity(intent);
                if((!mActivity.getUserIsLogin())||("0".equals(mActivity.getUserInfo(3)))){
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
                mActivity.finish();
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

        currentView.findViewById(R.id.weizuoTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (RankActivity)context;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoginStatus();
    }

    private void getLoginStatus(){
        if(mActivity.getUserIsLogin()){
            vipIV.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mActivity.getUserInfo(0))){
                accountTV.setText(mActivity.getUserInfo(0));
            }
            if(!TextUtils.isEmpty(mActivity.getUserInfo(2))){
                ((BaseActivity) mActivity).getNetWorkPicture(mActivity.getUserInfo(2), accountIV);
            }
            if(!TextUtils.isEmpty(mActivity.getUserInfo(3))){
                if("0".equals(mActivity.getUserInfo(3))){
                    vipIV.setImageResource(R.drawable.ic_vip_n);
                }else{
                    vipIV.setImageResource(R.drawable.ic_vip_s);
                }
            }
        }else{
            vipIV.setVisibility(View.GONE);
            accountTV.setText("点击登录");
            accountIV.setImageResource(R.drawable.ic_male);
            vipIV.setImageResource(R.drawable.ic_vip_n);
        }
    }
}
