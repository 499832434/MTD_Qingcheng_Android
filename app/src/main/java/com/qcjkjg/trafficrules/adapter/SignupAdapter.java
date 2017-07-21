package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.vo.Signup;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SignupAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Signup> mData;
    private Context context;

    public SignupAdapter(FragmentActivity context, List<Signup> data) {
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
            convertView = mInflater.inflate(R.layout.item_signup, null);
            holder.showIV= (ImageView) convertView.findViewById(R.id.showIV);
            holder.titleTV= (TextView) convertView.findViewById(R.id.titleTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.titleTV.setText(mData.get(position).getTitle());
        if(position%4==0){
            holder.showIV.setImageResource(R.drawable.item_blue);
        }else if(position%4==1){
            holder.showIV.setImageResource(R.drawable.item_red);
        }else if(position%4==2){
            holder.showIV.setImageResource(R.drawable.item_yellow);
        }else{
            holder.showIV.setImageResource(R.drawable.item_purple);
        }
        return convertView;
    }

    public final class ViewHolder {
        private ImageView showIV;
        private TextView titleTV;
    }

}
