package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.adapter.CircleReplyMeAdapter;
import com.qcjkjg.trafficrules.adapter.MessageReplyAdapter;
import com.qcjkjg.trafficrules.adapter.CircleListAdapter;
import com.qcjkjg.trafficrules.adapter.MyThemeAdapter;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.MessageTheme;
import com.qcjkjg.trafficrules.vo.ReplyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyFragment extends Fragment{
    private View currentView = null;
    protected MessageReplyActivity mActivity;
    private ListView replyLV;
    private MyThemeAdapter adapter1;
    private CircleReplyMeAdapter adapter2;
    private final static String FRAGMENT_TYPE = "fragmentType";
    private int fragmentType = 0;
    private List<ReplyInfo> list2=new ArrayList<ReplyInfo>();
    private List<MessageTheme> list1=new ArrayList<MessageTheme>();
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
            adapter1=new MyThemeAdapter(mActivity,list1);
            replyLV.setAdapter(adapter1);
        }else{
            adapter2=new CircleReplyMeAdapter(mActivity,list2);
            replyLV.setAdapter(adapter2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MessageReplyActivity)context;
    }
}
