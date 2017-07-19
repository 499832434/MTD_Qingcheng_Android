package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.MyThemeActivity;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.Message;
import com.qcjkjg.trafficrules.vo.MessageTheme;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageTheme> mData;
    private Context context;

    public MyThemeAdapter(FragmentActivity context, List<MessageTheme> data) {
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
            convertView = mInflater.inflate(R.layout.item_my_theme, null);
            holder.myThemeLV= (MyListView) convertView.findViewById(R.id.myThemeLV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MyThemeContentAdapter adapter=new MyThemeContentAdapter((MyThemeActivity)context,null);
        holder.myThemeLV.setAdapter(adapter);
        return convertView;
    }

    public final class ViewHolder {
      private MyListView myThemeLV;
    }

}
