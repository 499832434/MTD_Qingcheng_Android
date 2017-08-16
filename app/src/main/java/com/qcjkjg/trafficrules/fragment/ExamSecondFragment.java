package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ExamSecondFragment extends Fragment{
    private View currentView = null;
    private int fragmentType = 2;
    private final static String FRAGMENT_TYPE = "fragmentType";
    protected MainActivity mActivity;
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
        if(fragmentType==2){
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科二考规");
        }else{
            ((TextView)currentView.findViewById(R.id.ruleTV)).setText("科三考规");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }
}
