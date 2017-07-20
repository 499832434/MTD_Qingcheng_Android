package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.vo.MessageMyReply;
import com.qcjkjg.trafficrules.vo.MessageReplyMe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyMeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageReplyMe> mData;
    private Context context;

    public MessageReplyMeAdapter(FragmentActivity context, List<MessageReplyMe> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 4;
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
            convertView = mInflater.inflate(R.layout.item_message_reply_me, null);
            holder.pictureIV= (CircleImageView) convertView.findViewById(R.id.pictureIV);
            holder.pictureMGV= (MyGridView) convertView.findViewById(R.id.pictureMGV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyThemeContentPictureAdapter adapter=new MyThemeContentPictureAdapter((MessageReplyActivity)context,null);
        holder.pictureMGV.setAdapter(adapter);
        Picasso.with(context).load("http://b.zol-img.com.cn/desk/bizhi/image/4/960x600/1396085330945.jpg").into(holder.pictureIV);
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView pictureIV;
        private MyGridView pictureMGV;
    }

}
