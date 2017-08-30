package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.Tubiao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class TubiaoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Tubiao> mData;
    private Context context;
    private String type;

    public TubiaoAdapter(FragmentActivity context, List<Tubiao> data,String type) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.type=type;
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
            convertView = mInflater.inflate(R.layout.item_tubiao, null);
            holder.numTV= (TextView) convertView.findViewById(R.id.numTV);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.IV1= (ImageView) convertView.findViewById(R.id.IV1);
            holder.IV2= (ImageView) convertView.findViewById(R.id.IV2);
            holder.IV3= (ImageView) convertView.findViewById(R.id.IV3);
            holder.IV4= (ImageView) convertView.findViewById(R.id.IV4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.numTV.setText(mData.get(position).getNum()+"张");
        holder.nameTV.setText(mData.get(position).getName());
        DbTubiaoHelper db=new DbTubiaoHelper(context);
        List<View> views=new ArrayList<View>();
        views.add(holder.IV1);
        views.add(holder.IV2);
        views.add(holder.IV3);
        views.add(holder.IV4);
        List<String> list=db.getTubiaoListLimit(type,mData.get(position).getCode());
        for(int i=0;i<list.size();i++){
            ((BaseActivity)context).getLocalPicture(list.get(i), (ImageView) views.get(i));
        }
        return convertView;
    }

    public final class ViewHolder {
        private TextView nameTV,numTV;
        private ImageView IV1,IV2,IV3,IV4;
    }


}
