package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.AccountMoney;
import com.qcjkjg.trafficrules.vo.MessageFabulous;

import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyMoneyAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<AccountMoney> mData;
    private Context context;

    public MyMoneyAdapter(FragmentActivity context, List<AccountMoney> data) {
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
            convertView = mInflater.inflate(R.layout.item_my_money, null);
            holder.leftIV= (CircleImageView) convertView.findViewById(R.id.leftIV);
            holder.topTV= (TextView) convertView.findViewById(R.id.topTV);
            holder.bottomTV= (TextView) convertView.findViewById(R.id.bottomTV);
            holder.rightTV= (TextView) convertView.findViewById(R.id.rightTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(0==mData.get(position).getType()){
            holder.leftIV.setImageResource(R.drawable.ic_tixian);
            holder.topTV.setText("提现:"+mData.get(position).getScore()+"元");
            holder.rightTV.setText("-"+mData.get(position).getScore()+"元");
            holder.rightTV.setTextColor(Color.parseColor("#34A853"));
        }else if(1==mData.get(position).getType()){
            holder.topTV.setText("邀请好友:"+mData.get(position).getNickName());
            holder.rightTV.setText("+"+mData.get(position).getScore()+"元");
            holder.rightTV.setTextColor(Color.parseColor("#e85349"));
            ((BaseActivity) context).getNetWorkPicture(mData.get(position).getAvatar(), holder.leftIV);
        }else{
            holder.leftIV.setImageResource(R.drawable.ic_share_1);
            holder.topTV.setText("分享奖励:" + mData.get(position).getScore() + "元");
            holder.rightTV.setTextColor(Color.parseColor("#e85349"));
            holder.rightTV.setText("+" + mData.get(position).getScore()+"元");
        }
        holder.bottomTV.setText(mData.get(position).getDate());
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView leftIV;
        private TextView topTV,bottomTV,rightTV;
    }

}
