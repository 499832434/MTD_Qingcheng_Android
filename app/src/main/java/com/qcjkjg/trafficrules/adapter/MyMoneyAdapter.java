package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            convertView = mInflater.inflate(R.layout.item_my_money, null);
            holder.leftIV= (CircleImageView) convertView.findViewById(R.id.leftIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ((BaseActivity)context).getNetWorkPicture("http://b.zol-img.com.cn/desk/bizhi/image/4/960x600/1396085330945.jpg", holder.leftIV);
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView leftIV;
    }

}
