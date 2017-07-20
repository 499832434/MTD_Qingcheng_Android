package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageMainActivity;
import com.qcjkjg.trafficrules.utils.ViewFactory;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import me.codeboy.android.cycleviewpager.CycleViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleFragment extends Fragment{
    private View currentView = null;
    protected MainActivity mActivity;
    private CycleViewPager cycleViewPager;
    private List<View> views = new ArrayList<View>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_circle, container, false);
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
        cycleViewPager= (CycleViewPager) currentView.findViewById(R.id.cycleViewPager);
        cycleViewPager.setIndicatorCenter();
        cycleViewPager.setIndicatorsSpace(10);
        cycleViewPager.setIndicatorBackground(R.drawable.ic_image_unselected, R.drawable.ic_image_selected);
        List<Integer> list=new ArrayList<Integer>();
        list.add(R.drawable.item_blue);
        list.add(R.drawable.item_red);
        list.add(R.drawable.item_yellow);
        list.add(R.drawable.item_purple);

        ImageView view1=ViewFactory.getView(mActivity);
        view1.setImageResource(list.get(list.size()-1));
        views.add(view1);
        for(int i=0;i<list.size();i++){
            ImageView view=ViewFactory.getView(mActivity);
            view.setImageResource(list.get(i));
            views.add(view);
        }
        ImageView view2=ViewFactory.getView(mActivity);
        view2.setImageResource(list.get(0));
        views.add(view2);

        cycleViewPager.setData(views, true, true, 2000);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (MainActivity)context;
    }
}
