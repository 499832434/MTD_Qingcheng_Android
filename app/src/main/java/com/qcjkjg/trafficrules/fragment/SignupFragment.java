package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.adapter.SignupAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SignupFragment extends Fragment{
    private View currentView = null;
    protected MainActivity mActivity;
    private ListView signupLV;
    private SignupAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_signup, container, false);
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
        signupLV= (ListView) currentView.findViewById(R.id.signupLV);
        adapter=new SignupAdapter(mActivity,null);
        signupLV.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }
}
