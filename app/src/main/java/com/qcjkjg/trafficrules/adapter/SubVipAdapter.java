package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.vo.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SubVipAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mData;
    private Context context;
    private String fragmentType;
    private List<Subject> subjectList;

    public SubVipAdapter(FragmentActivity context, List<String> data, String fragmentType) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.fragmentType=fragmentType;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_subvip, null);
            holder.subvipNameTV= (TextView) convertView.findViewById(R.id.subvipTV);
            holder.subvipNumTV= (TextView) convertView.findViewById(R.id.subvipNumTV);
            holder.mainRL= (RelativeLayout) convertView.findViewById(R.id.mainRL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.subvipNameTV.setText(mData.get(position).split("-")[0]);
        DbCreateHelper helper=new DbCreateHelper(context);
        subjectList=new ArrayList<Subject>();
        subjectList=helper.getSubjectList(fragmentType, mData.get(position).split("-")[1],"subvip");
        holder.subvipNumTV.setText(subjectList.size()+"道题");
        final String subvip=mData.get(position).split("-")[1];
        holder.mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType);
                intent.putExtra("type","subvip");
                intent.putExtra("subvip",subvip);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        private TextView subvipNameTV,subvipNumTV;
        private RelativeLayout mainRL;
    }

}
