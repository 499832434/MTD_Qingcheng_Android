package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.circle.CircleDetailActivity;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.activity.signup.SignupContentActivity;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.MyGridLayout;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.MessageMyReply;
import com.qcjkjg.trafficrules.vo.MessageReplyMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyMeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageInfo> mData =new ArrayList<MessageInfo>();
    private Context context;

    public MessageReplyMeAdapter(FragmentActivity context, List<MessageInfo> data) {
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
            convertView = mInflater.inflate(R.layout.item_message_reply_me, null);
            holder.pictureIV= (CircleImageView) convertView.findViewById(R.id.pictureIV);
            holder.pictureMGL= (MyGridLayout) convertView.findViewById(R.id.pictureMGL);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.leaveTV= (TextView) convertView.findViewById(R.id.leaveTV);
            holder.fabulousTV= (TextView) convertView.findViewById(R.id.fabulousTV);
            holder.mainRL= (RelativeLayout) convertView.findViewById(R.id.mainRL);
            holder.fabulousIV= (ImageView) convertView.findViewById(R.id.fabulousIV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageInfo info=mData.get(position);
        if(!TextUtils.isEmpty(info.getAvatar())){
            ((BaseActivity)context).getNetWorkPicture(info.getAvatar(), holder.pictureIV);
        }
        if(!TextUtils.isEmpty(info.getContent())){
            holder.contentTV.setText(info.getContent());
            holder.contentTV.setVisibility(View.VISIBLE);
        }else{
            holder.contentTV.setVisibility(View.GONE);
        }
        if(info.getIsZan()==1){
            holder.fabulousIV.setImageResource(R.drawable.ic_praise_s);
        }else{
            holder.fabulousIV.setImageResource(R.drawable.ic_praise_n);
        }
        holder.nameTV.setText(info.getNickName());
        holder.timeTV.setText(info.getCreateTime());
        holder.leaveTV.setText(info.getReplyCnt()+"");
        holder.fabulousTV.setText(info.getZanCnt() + "");
        List<String> list=info.getPricturlList();
        holder.mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CircleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CircleFragment.CIRCLEFLAG, mData.get(position));
                bundle.putInt("position",position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        addImageView(holder.pictureMGL,list);
        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView pictureIV;
        private TextView nameTV;
        private TextView contentTV;
        private TextView timeTV;
        public TextView leaveTV;
        public TextView fabulousTV;
        private RelativeLayout mainRL;
        private MyGridLayout pictureMGL;
        public ImageView fabulousIV;
    }

    private void addImageView(MyGridLayout myGL,List<String> list){
        myGL.removeAllViews();
        for(int i=0;i<list.size();i++){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            RelativeLayout lineLayout = new RelativeLayout(context);
            lineLayout.setLayoutParams(new MyGridLayout.LayoutParams(15, 15));
            lineLayout.setGravity(Gravity.CENTER_VERTICAL);
            lineLayout.setBackgroundColor(Color.parseColor("#000000"));

            int width=(DensityUtil.getResolution((FragmentActivity)context)[1]-DensityUtil.dip2px(context,100))/3;
            ImageView imageView = new ImageView(context);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            Picasso.with(context).load(list.get(i)).into(imageView);
//            new GlideBuilder(context).setMemoryCache(DiskCacheStrategy.NONE);
            Glide.with(context).load(list.get(i)).into(imageView);
            lineLayout.addView(imageView);
            myGL.addView(lineLayout);
        }
    }

}
