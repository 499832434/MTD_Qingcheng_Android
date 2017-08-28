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
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.db.DbTubiaoHelper;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.Tubiao;
import com.qcjkjg.trafficrules.vo.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class RankAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<User> mData;
    private Context context;

    public RankAdapter(FragmentActivity context, List<User> data) {
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
            convertView = mInflater.inflate(R.layout.item_rank, null);
            holder.rankTV= (TextView) convertView.findViewById(R.id.rankTV);
            holder.nickTV= (TextView) convertView.findViewById(R.id.nickTV);
            holder.scoreTV= (TextView) convertView.findViewById(R.id.scoreTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.rankIV= (ImageView) convertView.findViewById(R.id.rankIV);
            holder.avtarCIV= (CircleImageView) convertView.findViewById(R.id.avtarCIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position==0){
            holder.rankTV.setVisibility(View.GONE);
            holder.rankIV.setVisibility(View.VISIBLE);
            holder.rankIV.setImageResource(R.drawable.ic_gold);
        }else if(position==1){
            holder.rankTV.setVisibility(View.GONE);
            holder.rankIV.setVisibility(View.VISIBLE);
            holder.rankIV.setImageResource(R.drawable.ic_silver);
        }else if(position==2){
            holder.rankTV.setVisibility(View.GONE);
            holder.rankIV.setVisibility(View.VISIBLE);
            holder.rankIV.setImageResource(R.drawable.ic_bronze);
        }else {
            holder.rankTV.setVisibility(View.VISIBLE);
            holder.rankIV.setVisibility(View.GONE);
            holder.rankTV.setText(position+"");
        }
        ((BaseActivity)context).getNetWorkPicture(mData.get(position).getAvatar(),holder.avtarCIV);
        holder.nickTV.setText(mData.get(position).getNickName());
        holder.scoreTV.setText(mData.get(position).getResult()+"分");
        holder.timeTV.setText(mData.get(position).getMinutes());
        return convertView;
    }

    public final class ViewHolder {
        private TextView rankTV,nickTV,scoreTV,timeTV;
        private ImageView rankIV;
        private CircleImageView avtarCIV;
    }

}
