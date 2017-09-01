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
            holder.topTV.setText("提现:"+mData.get(position).getScore()+"元");
            holder.rightTV.setText("-"+mData.get(position).getScore()+"元");
        }else{
            holder.topTV.setText("分享获得:"+mData.get(position).getScore()+"元");
            holder.rightTV.setText("+"+mData.get(position).getScore()+"元");
        }
        holder.bottomTV.setText(mData.get(position).getDate());
        ((BaseActivity) context).getNetWorkPicture(mData.get(position).getAvatar(), holder.leftIV);
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView leftIV;
        private TextView topTV,bottomTV,rightTV;
    }

}
