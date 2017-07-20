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
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.MessageFabulous;
import com.qcjkjg.trafficrules.vo.Signup;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageFabulousAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageFabulous> mData;
    private Context context;

    public MessageFabulousAdapter(FragmentActivity context, List<MessageFabulous> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 9;
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
            convertView = mInflater.inflate(R.layout.item_message_fabulous, null);
            holder.rightIV= (ImageView) convertView.findViewById(R.id.rightIV);
            holder.leftIV= (CircleImageView) convertView.findViewById(R.id.leftIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load("http://b.zol-img.com.cn/desk/bizhi/image/4/960x600/1396085330945.jpg").into(holder.rightIV);
        Picasso.with(context).load("http://b.zol-img.com.cn/desk/bizhi/image/4/960x600/1396085330945.jpg").into(holder.leftIV);
        return convertView;
    }

    public final class ViewHolder {
        private ImageView rightIV;
        private CircleImageView leftIV;
    }

}
