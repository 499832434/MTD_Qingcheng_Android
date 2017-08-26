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
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class TubiaoDetailAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Tubiao> mData;
    private Context context;

    public TubiaoDetailAdapter(FragmentActivity context, List<Tubiao> data) {
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
            convertView = mInflater.inflate(R.layout.item_tubiao_detail, null);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.pictureIV= (ImageView) convertView.findViewById(R.id.pictureIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contentTV.setText(mData.get(position).getContent());
        ((BaseActivity)context).getLocalPicture(mData.get(position).getPictureUrl(), holder.pictureIV);
        return convertView;
    }

    public final class ViewHolder {
        private TextView contentTV;
        private ImageView pictureIV;
    }

}
