package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.vo.Signup;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class SystemMessageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Signup> mData;
    private Context context;
    private boolean flag;

    public SystemMessageAdapter(FragmentActivity context, List<Signup> data,boolean flag) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.flag=flag;
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
            convertView = mInflater.inflate(R.layout.item_system_message, null);
            holder.pictureIV= (ImageView) convertView.findViewById(R.id.pictureIV);
            holder.titleTV= (TextView) convertView.findViewById(R.id.titleTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.abstractTV= (TextView) convertView.findViewById(R.id.abstractTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(flag){
            holder.timeTV.setText(mData.get(position).getPubtime());
            holder.timeTV.setVisibility(View.VISIBLE);
        }else {
            holder.timeTV.setVisibility(View.GONE);
        }
        holder.abstractTV.setText(mData.get(position).getAbstractStr());
        holder.titleTV.setText(mData.get(position).getTitle());
        if(!TextUtils.isEmpty(mData.get(position).getPictureUrl())){
            ((BaseActivity)context).getNetWorkPicture(mData.get(position).getPictureUrl(),holder.pictureIV);
            holder.pictureIV.setVisibility(View.VISIBLE);
        }else {
            holder.pictureIV.setVisibility(View.GONE);
        }
        return convertView;
    }

    public final class ViewHolder {
        private ImageView pictureIV;
        private TextView titleTV;
        private TextView timeTV;
        private TextView abstractTV;
    }

}
