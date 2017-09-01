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
import com.qcjkjg.trafficrules.vo.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class VideoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Video> mData;
    private Context context;
    private String fragmentType;

    public VideoAdapter(FragmentActivity context, List<Video> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
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
            convertView = mInflater.inflate(R.layout.item_video, null);
            holder.titleTV= (TextView) convertView.findViewById(R.id.titleTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.cntTV= (TextView) convertView.findViewById(R.id.cntTV);
            holder.pictureIV= (ImageView) convertView.findViewById(R.id.pictureIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTV.setText(mData.get(position).getTitle());
        holder.contentTV.setText(mData.get(position).getContent());
        holder.cntTV.setText(mData.get(position).getViewCnt()+"");
        ((BaseActivity)context).getNetWorkPicture(mData.get(position).getPicUrl(),holder.pictureIV);
        return convertView;
    }

    public final class ViewHolder {
        private TextView titleTV,contentTV,cntTV;
        private ImageView pictureIV;
    }

}
