package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.circle.CircleDetailActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.MyGridLayout;
import com.qcjkjg.trafficrules.vo.ReplyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class CircleReplyMeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<ReplyInfo> mData =new ArrayList<ReplyInfo>();
    private Context context;

    public CircleReplyMeAdapter(FragmentActivity context, List<ReplyInfo> data) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_circle_reply, null);
            holder.avatarIV= (CircleImageView) convertView.findViewById(R.id.avatarIV);
            holder.pictureMGL= (MyGridLayout) convertView.findViewById(R.id.pictureMGL);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.numberTV= (TextView) convertView.findViewById(R.id.numberTV);
            holder.replyTV= (TextView) convertView.findViewById(R.id.replyTV);
            holder.replyContentTV= (TextView) convertView.findViewById(R.id.replyContentTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ((BaseActivity)context).getNetWorkPicture(mData.get(position).getAvater(),holder.avatarIV);
        holder.nameTV.setText(mData.get(position).getNickName());
        holder.timeTV.setText(mData.get(position).getCreateTime());
        if(position==0){
            holder.numberTV.setText("沙发");
        }else if(position==1){
            holder.numberTV.setText("板凳");
        }else{
            holder.numberTV.setText((position+1)+"楼");
        }
        if(mData.get(position).getToReplyId()>0){
            if(TextUtils.isEmpty(mData.get(position).getContentReply())){
                holder.replyContentTV.setText("回复"+mData.get(position).getToNickName()+":[图片]");
            }else{
                holder.replyContentTV.setText("回复"+mData.get(position).getToNickName()+":"+mData.get(position).getContentReply());
            }
            holder.replyContentTV.setVisibility(View.VISIBLE);
        }else {
            holder.replyContentTV.setVisibility(View.GONE);
        }
        if(!TextUtils.isEmpty(mData.get(position).getContent())){
            holder.contentTV.setText(mData.get(position).getContent());
            holder.contentTV.setVisibility(View.VISIBLE);
        }else{
            holder.contentTV.setVisibility(View.GONE);
        }
        holder.replyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!((CircleDetailActivity)context).getUserIsLogin()){
                    context.startActivity(new Intent(context,LoginActivity.class));
                    return;
                }
                ((CircleDetailActivity) context).showReplyDialog(position, mData.get(position));
            }
        });
        List<LocalMedia> selectList = new ArrayList<LocalMedia>();
        List<String> list=mData.get(position).getImagesList();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                LocalMedia media=new LocalMedia();
                media.setPath(list.get(i));
                selectList.add(media);
            }
            holder.pictureMGL.setVisibility(View.VISIBLE);
            addImageView(holder.pictureMGL, list,selectList);
        }else {
            holder.pictureMGL.setVisibility(View.GONE);
        }
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView avatarIV;
        private TextView nameTV;
        private TextView numberTV;
        private TextView replyContentTV;
        private TextView contentTV;
        private TextView timeTV;
        private TextView replyTV;
        private MyGridLayout pictureMGL;
    }

    private void addImageView(MyGridLayout myGL,List<String> list, final List<LocalMedia> selectList){
        myGL.removeAllViews();
        for(int i=0;i<list.size();i++){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout lineLayout = new RelativeLayout(context);
            lineLayout.setLayoutParams(new MyGridLayout.LayoutParams(15, 15));
            lineLayout.setGravity(Gravity.CENTER_VERTICAL);
            lineLayout.setBackgroundColor(Color.parseColor("#000000"));

            int width=(DensityUtil.getResolution((FragmentActivity)context)[1]-DensityUtil.dip2px(context,100))/3;
            ImageView imageView = new ImageView(context);
            imageView.setId(i);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureSelector.create((FragmentActivity) context).externalPicturePreview((Integer) view.getId(), selectList);
                }
            });
            Glide.with(context).load(list.get(i)).into(imageView);
//            Picasso.with(context).load(list.get(i)).placeholder(R.drawable.item_blue).into(imageView);
            lineLayout.addView(imageView);
            myGL.addView(lineLayout);
        }
    }

}
