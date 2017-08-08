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
import com.bumptech.glide.Glide;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.MessageFabulous;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.Signup;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageFabulousAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageInfo> mData;
    private Context context;

    public MessageFabulousAdapter(FragmentActivity context, List<MessageInfo> data) {
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
            convertView = mInflater.inflate(R.layout.item_message_fabulous, null);
            holder.rightIV= (ImageView) convertView.findViewById(R.id.rightIV);
            holder.leftIV= (CircleImageView) convertView.findViewById(R.id.leftIV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.isEmpty(mData.get(position).getContent())){
            holder.contentTV.setVisibility(View.GONE);
        }else{
            holder.contentTV.setText(mData.get(position).getContent());
            holder.contentTV.setVisibility(View.VISIBLE);
        }
        holder.nameTV.setText(mData.get(position).getNickName());
        holder.timeTV.setText(mData.get(position).getCreateTime());
        if(mData.get(position).getPricturlList().size()==0){
            holder.rightIV.setVisibility(View.GONE);
        }else{
            holder.rightIV.setVisibility(View.VISIBLE);
            Glide.with(context).load(mData.get(position).getPricturlList().get(0)).into(holder.rightIV);
        }
        if(!TextUtils.isEmpty(mData.get(position).getAvatar())){
            Glide.with(context).load(mData.get(position).getAvatar()).into(holder.leftIV);
        }
        return convertView;
    }

    public final class ViewHolder {
        private ImageView rightIV;
        private CircleImageView leftIV;
        private TextView nameTV,contentTV,timeTV;
    }

}
