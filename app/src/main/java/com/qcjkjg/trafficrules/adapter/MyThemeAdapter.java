package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.circle.CircleDetailActivity;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.utils.NumberFormatUtil;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.MyGridLayout;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageTheme;
import com.qcjkjg.trafficrules.vo.ReplyInfo;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ReplyInfo> mData=new ArrayList<ReplyInfo>();
    private Context context;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String nowDate;
    private int flag;//0:主题1:信息回复(我的回复)2:信息回复(回复我的)

    public MyThemeAdapter(FragmentActivity context,  List<ReplyInfo> data,int flag) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
        this.flag=flag;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_my_theme, null);
            holder.dayTV= (TextView) convertView.findViewById(R.id.dayTV);
            holder.leftLL= (LinearLayout) convertView.findViewById(R.id.leftLL);
            holder.monthTV= (TextView) convertView.findViewById(R.id.monthTV);
            holder.yearTV= (TextView) convertView.findViewById(R.id.yearTV);
            holder.pictureIV= (CircleImageView) convertView.findViewById(R.id.pictureIV);
            holder.pictureMGL= (MyGridLayout) convertView.findViewById(R.id.pictureMGL);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.contentTV1= (TextView) convertView.findViewById(R.id.contentTV1);
            holder.contentTV2= (TextView) convertView.findViewById(R.id.contentTV2);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.leaveTV= (TextView) convertView.findViewById(R.id.leaveTV);
            holder.fabulousTV= (TextView) convertView.findViewById(R.id.fabulousTV);
            holder.fabulousIV= (ImageView) convertView.findViewById(R.id.fabulousIV);
            holder.leaveRL= (RelativeLayout) convertView.findViewById(R.id.leaveRL);
            holder.fabulousRL= (RelativeLayout) convertView.findViewById(R.id.fabulousRL);
            holder.mainRL= (RelativeLayout) convertView.findViewById(R.id.mainRL);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReplyInfo info=mData.get(position);
        if(0==flag){
            if(info.getThemeFlag()==1){
                holder.leftLL.setVisibility(View.VISIBLE);
                if(nowDate==mData.get(position).getCustomTime()){
                    holder.dayTV.setText("今天");
                    holder.monthTV.setVisibility(View.GONE);
                    holder.yearTV.setVisibility(View.GONE);
                }else{
                    holder.dayTV.setText(mData.get(position).getCustomTime().split("-")[2]);
                    holder.monthTV.setText(NumberFormatUtil.formatInteger(Integer.parseInt(mData.get(position).getCustomTime().split("-")[1]))+"月");
                    holder.yearTV.setText(mData.get(position).getCustomTime().split("-")[0]);
                    holder.monthTV.setVisibility(View.VISIBLE);
                    holder.yearTV.setVisibility(View.VISIBLE);
                }
            }else {
                holder.leftLL.setVisibility(View.INVISIBLE);
            }
            holder.leaveTV.setText(info.getReplyCnt() + "");
            holder.fabulousTV.setText(info.getZanCnt() + "");
            if(info.getIsZan()==1){
                holder.fabulousIV.setImageResource(R.drawable.ic_praise_s);
                holder.fabulousIV.setTag(1);
            }else{
                holder.fabulousIV.setImageResource(R.drawable.ic_praise_n);
                holder.fabulousIV.setTag(0);
            }
            holder.leaveRL.setVisibility(View.VISIBLE);
            holder.fabulousRL.setVisibility(View.VISIBLE);
        }else if(1==flag){
            if(info.getThemeFlag()==1){
                holder.leftLL.setVisibility(View.VISIBLE);
                if(nowDate==mData.get(position).getCustomTime()){
                    holder.dayTV.setText("今天");
                    holder.monthTV.setVisibility(View.GONE);
                    holder.yearTV.setVisibility(View.GONE);
                }else{
                    holder.dayTV.setText(mData.get(position).getCustomTime().split("-")[2]);
                    holder.monthTV.setText(NumberFormatUtil.formatInteger(Integer.parseInt(mData.get(position).getCustomTime().split("-")[1]))+"月");
                    holder.yearTV.setText(mData.get(position).getCustomTime().split("-")[0]);
                    holder.monthTV.setVisibility(View.VISIBLE);
                    holder.yearTV.setVisibility(View.VISIBLE);
                }
            }else {
                holder.leftLL.setVisibility(View.INVISIBLE);
            }
            if(!TextUtils.isEmpty(info.getContentReply())){
                holder.contentTV1.setText("话题:"+info.getContentReply());
                holder.contentTV1.setVisibility(View.VISIBLE);
            }
        }else if(2==flag){
            if(!TextUtils.isEmpty(info.getAvatar())){
                Glide.with(context).load(info.getAvatar()).into(holder.pictureIV);
                holder.pictureIV.setVisibility(View.VISIBLE);
            }else{
                holder.pictureIV.setVisibility(View.INVISIBLE);
            }
            if(!TextUtils.isEmpty(info.getContentReply())){
                holder.contentTV2.setText("话题:"+info.getContentReply());
                holder.contentTV2.setVisibility(View.VISIBLE);
            }else{
                holder.contentTV2.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(info.getNickName())){
                holder.nameTV.setVisibility(View.VISIBLE);
                holder.nameTV.setText(info.getNickName());
            }
        }
        if(!TextUtils.isEmpty(info.getContent())){
            holder.contentTV.setText(info.getContent());
            holder.contentTV.setVisibility(View.VISIBLE);
        }else{
            holder.contentTV.setVisibility(View.GONE);
        }

        holder.timeTV.setText(info.getCreateTime());
        holder.mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CircleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("cid", mData.get(position).getCid());
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        List<String> list=info.getImagesList();
        if(list.size()==0){
            holder.pictureMGL.setVisibility(View.GONE);
        }else{
            holder.pictureMGL.setVisibility(View.VISIBLE);
            addImageView(holder.pictureMGL, list);
        }

        return convertView;
    }

    public final class ViewHolder {
        private TextView dayTV,monthTV,yearTV;
        private CircleImageView pictureIV;
        private TextView nameTV;
        private TextView contentTV,contentTV1,contentTV2;
        private TextView timeTV;
        public TextView leaveTV;
        public TextView fabulousTV;
        private MyGridLayout pictureMGL;
        public ImageView fabulousIV;
        private LinearLayout leftLL;
        private RelativeLayout leaveRL,fabulousRL,mainRL;
    }
    private void addImageView(MyGridLayout myGL,List<String> list){
        myGL.removeAllViews();
        for(int i=0;i<list.size();i++){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout lineLayout = new RelativeLayout(context);
            lineLayout.setLayoutParams(new MyGridLayout.LayoutParams(15, 15));
            lineLayout.setGravity(Gravity.CENTER_VERTICAL);
            lineLayout.setBackgroundColor(Color.parseColor("#000000"));

            int width=(DensityUtil.getResolution((FragmentActivity) context)[1]-DensityUtil.dip2px(context,100))/3;
            ImageView imageView = new ImageView(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(context).load(list.get(i)).into(imageView);
            lineLayout.addView(imageView);
            myGL.addView(lineLayout);
        }
    }
}
