package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.vo.Image;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SubclassGridAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mData;
    private Context context;
    private String fragmentType;
    private List<Subject> subjectList;

    public SubclassGridAdapter(FragmentActivity context, List<String> data,String fragmentType) {
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
            convertView = mInflater.inflate(R.layout.item_subclss, null);
            holder.subclassNameTV= (TextView) convertView.findViewById(R.id.subclassNameTV);
            holder.subclassNumTV= (TextView) convertView.findViewById(R.id.subclassNumTV);
            holder.circleIV= (ImageView) convertView.findViewById(R.id.circleIV);
            holder.mainRL= (RelativeLayout) convertView.findViewById(R.id.mainRL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position%6==0||position%6==1){
            holder.circleIV.setBackgroundResource(R.drawable.ic_bg_blue);
        }else if(position%6==2||position%6==3){
            holder.circleIV.setBackgroundResource(R.drawable.ic_bg_red);
        }else {
            holder.circleIV.setBackgroundResource(R.drawable.ic_bg_yellow);
        }
        holder.subclassNameTV.setText(mData.get(position));
        DbCreateHelper helper=new DbCreateHelper(context);
        subjectList=new ArrayList<Subject>();
        if("文字题".equals(mData.get(position))){
            subjectList=helper.getSubjectListPicture(fragmentType,false);
        }else if("图片题".equals(mData.get(position))){
            subjectList=helper.getSubjectListPicture(fragmentType,true);
        }else if("单选题".equals(mData.get(position))){
            subjectList=helper.getSubjectListType(fragmentType,2);
        }else if("多选题".equals(mData.get(position))){
            subjectList=helper.getSubjectListType(fragmentType,3);
        }else if("判断题".equals(mData.get(position))){
            subjectList=helper.getSubjectListType(fragmentType,1);
        }else {
            subjectList=helper.getSubjectList(fragmentType, mData.get(position),"subclass");
        }
        holder.subclassNumTV.setText(subjectList.size() + "");
        final String subclass=mData.get(position);
        holder.mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("0".equals(holder.subclassNumTV.getText().toString())){
                    ((BaseActivity)context).toast(context,"暂无题目");
                    return;
                }
                Intent intent=new Intent(context, AnswerActivity.class);
                intent.putExtra("fragmentType",fragmentType);
                intent.putExtra("type","subclass");
                intent.putExtra("subclass",subclass);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public final class ViewHolder {
        private TextView subclassNameTV,subclassNumTV;
        private ImageView circleIV;
        private RelativeLayout mainRL;
    }

}
