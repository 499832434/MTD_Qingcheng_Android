package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.luck.picture.lib.PictureExternalPreviewActivity;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.view.SubjectImageView;
import com.qcjkjg.trafficrules.vo.Signup;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleDetailAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mData;
    private Context context;

    public CircleDetailAdapter(FragmentActivity context, List<String> data) {
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
            convertView = mInflater.inflate(R.layout.item_circle_detail_picture, null);
            holder.pictureIV= (SubjectImageView) convertView.findViewById(R.id.pictureIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(mData.get(position)).into(holder.pictureIV);
        return convertView;
    }

    public final class ViewHolder {
        private SubjectImageView pictureIV;
    }

}
