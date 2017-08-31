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
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.tubiao.TubiaoActivity;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.Video;
import me.codeboy.android.cycleviewpager.CycleViewPager;

import java.util.ArrayList;
import java.util.List;

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam_second, container, false);
        initView();
        return currentView;
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
                Intent intent = new Intent(mActivity, TubiaoActivity.class);
                intent.putExtra("type", fragmentType + "");
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
}
