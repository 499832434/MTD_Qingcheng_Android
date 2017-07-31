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
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.adapter.MessageMyReplyAdapter;
import com.qcjkjg.trafficrules.adapter.MessageReplyMeAdapter;
import com.qcjkjg.trafficrules.adapter.SignupAdapter;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.MessageInfo;

import java.util.ArrayList;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyFragment extends Fragment{
    private View currentView = null;
    protected MessageReplyActivity mActivity;
    private ListView replyLV;
    private MessageReplyMeAdapter replyMeAdapter;
    private MessageMyReplyAdapter myReplyAdapter;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private int fragmentType = 0;

    public static MessageReplyFragment newInstance(MessageReplyActivity parentActivity,  int fragmentType) {
        MessageReplyFragment fr = new MessageReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_TYPE, fragmentType);
        fr.setArguments(bundle);
        return fr;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_message_reply, container, false);
        initView();
        return currentView;
    }

    private void initView(){
        fragmentType = getArguments().getInt(FRAGMENT_TYPE);
        replyLV= (ListView) currentView.findViewById(R.id.replyLV);
        if(0==fragmentType){
            myReplyAdapter=new MessageMyReplyAdapter(mActivity,null);
            replyLV.setAdapter(myReplyAdapter);
        }else{
            replyMeAdapter=new MessageReplyMeAdapter(mActivity,new ArrayList<MessageInfo>());
            replyLV.setAdapter(replyMeAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MessageReplyActivity)context;
    }
}
