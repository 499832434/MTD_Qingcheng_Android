package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MyThemeActivity;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.vo.MessageThemeContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeContentPictureAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mData=new ArrayList<String>();
    private Context context;
    private int width;
    public MyThemeContentPictureAdapter(FragmentActivity context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.width=(DensityUtil.getResolution((FragmentActivity)context)[1]-DensityUtil.dip2px(context,100))/3;
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
            convertView = mInflater.inflate(R.layout.item_my_theme_content_picture, null);
            holder.pictureIV= (ImageView) convertView.findViewById(R.id.pictureIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(0==width){
            width=DensityUtil.dip2px(context,80);
        }
        DensityUtil.setWidth(holder.pictureIV,width);
        Picasso.with(context).load(mData.get(position)).into(holder.pictureIV);
        return convertView;
    }


    public final class ViewHolder {
        private ImageView pictureIV;
    }

}
