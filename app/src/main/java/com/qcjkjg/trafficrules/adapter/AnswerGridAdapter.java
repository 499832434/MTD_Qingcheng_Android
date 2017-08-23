package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class AnswerGridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Subject> mData;
    private Context context;
    private List<SubjectSelect> subjectSelectList=new ArrayList<SubjectSelect>();
    private int fragmentPositon;

    public AnswerGridAdapter(FragmentActivity context, List<Subject> data,List<SubjectSelect> subjectSelectList,int fragmentPositon) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.subjectSelectList=subjectSelectList;
        this.fragmentPositon=fragmentPositon;
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
            convertView = mInflater.inflate(R.layout.item_answer_gird, null);
            holder.numTV= (TextView) convertView.findViewById(R.id.numTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.numTV.setText((position+1)+"");
        holder.numTV.setBackgroundResource(R.drawable.ic_bg_sub_uncheck);
        holder.numTV.setTextColor(Color.parseColor("#8A8A8A"));
        for(int i=0;i<subjectSelectList.size();i++){
            if(mData.get(position).getSubId().equals(subjectSelectList.get(i).getSubId()+"")){
                if(1==subjectSelectList.get(i).getAnswerStatus()){
                    holder.numTV.setBackgroundResource(R.drawable.ic_bg_sub_right);
                    holder.numTV.setTextColor(Color.parseColor("#8BB336"));
                    break;
                }else if(2==subjectSelectList.get(i).getAnswerStatus()){
                    holder.numTV.setBackgroundResource(R.drawable.ic_bg_sub_wrong);
                    holder.numTV.setTextColor(Color.parseColor("#EB6C66"));
                    break;
                }
            }
        }
        if(fragmentPositon==position){
            holder.numTV.setBackgroundResource(R.drawable.ic_bg_sub_checked);
            holder.numTV.setTextColor(Color.parseColor("#AEB2B1"));
        }
        return convertView;
    }

    public final class ViewHolder {
        private TextView numTV;
    }

}
