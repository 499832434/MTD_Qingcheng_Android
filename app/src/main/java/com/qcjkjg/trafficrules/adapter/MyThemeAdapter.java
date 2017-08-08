package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.utils.NumberFormatUtil;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageTheme;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageTheme> mData=new ArrayList<MessageTheme>();
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String nowDate;

    public MyThemeAdapter(FragmentActivity context,  List<MessageTheme> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        nowDate=sdf.format(new Date());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return 0;
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
            holder.dayTV= (TextView) convertView.findViewById(R.id.dayTV);
            holder.monthTV= (TextView) convertView.findViewById(R.id.monthTV);
            holder.yearTV= (TextView) convertView.findViewById(R.id.yearTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(nowDate==mData.get(position).getTime()){
            holder.dayTV.setText("今天");
            holder.monthTV.setVisibility(View.GONE);
            holder.yearTV.setVisibility(View.GONE);
        }else{
            holder.dayTV.setText(mData.get(position).getTime().split("-")[2]);
            holder.monthTV.setText(NumberFormatUtil.formatInteger(Integer.parseInt(mData.get(position).getTime().split("-")[1]))+"月");
            holder.yearTV.setText(mData.get(position).getTime().split("-")[0]);
            holder.monthTV.setVisibility(View.VISIBLE);
            holder.yearTV.setVisibility(View.VISIBLE);
        }
        if(mData.get(position).getList().size()>0){
            CircleListAdapter messageAdapter=new CircleListAdapter((FragmentActivity) context, mData.get(position).getList(),1);
            holder.myThemeLV.setAdapter(messageAdapter);
        }

        return convertView;
    }

    public final class ViewHolder {
      private MyListView myThemeLV;
        private TextView dayTV,monthTV,yearTV;
    }

}
