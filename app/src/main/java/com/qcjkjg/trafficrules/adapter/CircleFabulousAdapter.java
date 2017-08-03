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
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.MessageFabulous;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleFabulousAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageInfo> mData;
    private Context context;

    public CircleFabulousAdapter(FragmentActivity context, List<MessageInfo> data) {
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
            convertView = mInflater.inflate(R.layout.item_circle_fabulous, null);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.pictureCIV= (CircleImageView) convertView.findViewById(R.id.pictureCIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(mData.get(position).getAvatar())){
            Picasso.with(context).load(mData.get(position).getAvatar()).into(holder.pictureCIV);
        }
        holder.nameTV.setText(mData.get(position).getNickName());
        return convertView;
    }

    public final class ViewHolder {
        private TextView nameTV;
        private CircleImageView pictureCIV;
    }

}
