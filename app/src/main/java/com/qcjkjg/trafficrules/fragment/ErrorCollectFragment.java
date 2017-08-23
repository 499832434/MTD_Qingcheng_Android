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
import com.qcjkjg.trafficrules.db.DbCreateHelper;

/**
 * Created by zongshuo on 2017/8/16.
 */
public class ErrorCollectFragment extends Fragment{
    private View currentView = null;
    private String fragmentType = "1",type;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private final static String TYPE = "type";
    protected ErrorCollectActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_exam_one, container, false);
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

    }

    private void initView(){
        fragmentType = getArguments().getString(FRAGMENT_TYPE);
        type = getArguments().getString(TYPE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (ErrorCollectActivity)context;
    }


}
