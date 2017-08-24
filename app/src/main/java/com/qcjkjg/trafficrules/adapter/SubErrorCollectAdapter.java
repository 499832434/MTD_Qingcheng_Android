package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.vo.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SubErrorCollectAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mData;
    private Context context;
    private String fragmentType;
    private List<Subject> subjectList;
    private List<String> chapterList=new ArrayList<String>();
    private String type;

    public SubErrorCollectAdapter(FragmentActivity context, List<String> data, String fragmentType,String type) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.type=type;
        this.context=context;
        this.fragmentType=fragmentType;
        Resources res =this.context.getResources();
        if("1".equals(fragmentType)){
            String[] array=res.getStringArray(R.array.subtype1_chapter);
            chapterList= Arrays.asList(array);
        }else{
            String[] array=res.getStringArray(R.array.subtype4_chapter);
            chapterList= Arrays.asList(array);
        }
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
            convertView = mInflater.inflate(R.layout.item_suberrorcollect, null);
            holder.subvipNameTV= (TextView) convertView.findViewById(R.id.subvipTV);
            holder.subvipNumTV= (TextView) convertView.findViewById(R.id.subvipNumTV);
            holder.mainRL= (RelativeLayout) convertView.findViewById(R.id.mainRL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        for(int i=0;i<chapterList.size();i++){
            if(chapterList.get(i).split("-")[1].equals(mData.get(position).split("-")[0])){
                holder.subvipNameTV.setText(chapterList.get(i).split("-")[0]);
                holder.subvipNumTV.setText(mData.get(position).split("-")[1]+"道题");
                break;
            }
        }

        final String subcollectchapter=mData.get(position).split("-")[0];
        holder.mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("subcollect".equals(type)){
                    Intent intent=new Intent(context, AnswerActivity.class);
                    intent.putExtra("fragmentType",fragmentType);
                    intent.putExtra("type","subcollectchapter");
                    intent.putExtra("subcollectchapter",subcollectchapter);
                    context.startActivity(intent);
                }else {
                    Intent intent=new Intent(context, AnswerActivity.class);
                    intent.putExtra("fragmentType",fragmentType);
                    intent.putExtra("type","suberrorchapter");
                    intent.putExtra("suberrorchapter",subcollectchapter);
                    context.startActivity(intent);
                }

            }
        });
        return convertView;
    }

    public final class ViewHolder {
        private TextView subvipNameTV,subvipNumTV;
        private RelativeLayout mainRL;
    }

}
