package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by zongshuo on 2017/7/31.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    private View currentView = null;
    private CircleImageView accountIV;
    private ImageView vipIV;
    private TextView accountTV;
    private RelativeLayout loginRL;
    private MainActivity mActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        accountIV= (CircleImageView) currentView.findViewById(R.id.accountIV);
        accountTV= (TextView) currentView.findViewById(R.id.accountTV);
        vipIV= (ImageView) currentView.findViewById(R.id.vipIV);
        loginRL= (RelativeLayout) currentView.findViewById(R.id.loginRL);
        loginRL.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivity.getUserIsLogin()){
            if(!TextUtils.isEmpty(mActivity.getUserInfo(0))){
                accountTV.setText(mActivity.getUserInfo(0));
            }
            if(!TextUtils.isEmpty(mActivity.getUserInfo(2))){
                Picasso.with(mActivity).load(mActivity.getUserInfo(2)).into(accountIV);
            }
            if(!TextUtils.isEmpty(mActivity.getUserInfo(3))){
                if("0".equals(mActivity.getUserInfo(3))){
                    vipIV.setImageResource(R.drawable.ic_vip_n);
                }else{
                    vipIV.setImageResource(R.drawable.ic_vip_s);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.accountIV:
                break;
            case R.id.accountTV:
                break;
            case R.id.vipIV:
                break;
            case R.id.loginRL:
                if(!mActivity.getUserIsLogin()){
                    startActivity(new Intent(mActivity, LoginActivity.class));
                }else{

                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }
}
