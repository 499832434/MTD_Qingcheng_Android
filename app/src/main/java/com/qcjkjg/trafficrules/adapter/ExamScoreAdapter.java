package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.vo.ExamScore;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class ExamScoreAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ExamScore> mData;
    private Context context;
    private SimpleDateFormat format;

    public ExamScoreAdapter(FragmentActivity context, List<ExamScore> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        format = new java.text.SimpleDateFormat("yyyy-MM-dd");
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
            convertView = mInflater.inflate(R.layout.item_exam_score, null);
            holder.positionTV= (TextView) convertView.findViewById(R.id.positionTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.dateTV= (TextView) convertView.findViewById(R.id.dateTV);
            holder.scoreTV= (TextView) convertView.findViewById(R.id.scoreTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.positionTV.setText((position+1)+"");
        holder.timeTV.setText(mData.get(position).getTime());
        long date=(Long.parseLong(mData.get(position).getDate()));
        holder.dateTV.setText(format.format(new Date(date)));
        holder.scoreTV.setText(mData.get(position).getScore()+"分");
        return convertView;
    }

    public final class ViewHolder {
        private TextView positionTV,timeTV,dateTV,scoreTV;
    }

}
