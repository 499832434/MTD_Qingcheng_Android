package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.activity.signup.MyThemeActivity;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageMyReply;
import com.qcjkjg.trafficrules.vo.MessageTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageMyReplyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageMyReply> mData ;
    private Context context;

    public MessageMyReplyAdapter(FragmentActivity context, List<MessageMyReply> data) {
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
            convertView = mInflater.inflate(R.layout.item_message_my_reply, null);
            holder.pictureMGV= (MyGridView) convertView.findViewById(R.id.pictureMGV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        MyThemeContentPictureAdapter adapter=new MyThemeContentPictureAdapter((MessageReplyActivity)context,new ArrayList<String>());
//        holder.pictureMGV.setAdapter(adapter);
        return convertView;
    }

    public final class ViewHolder {
        private MyGridView pictureMGV;
    }

}
