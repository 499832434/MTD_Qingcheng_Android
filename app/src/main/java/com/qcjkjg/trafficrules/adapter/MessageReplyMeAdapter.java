package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MessageReplyActivity;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.MessageMyReply;
import com.qcjkjg.trafficrules.vo.MessageReplyMe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageReplyMeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageInfo> mData;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_message_reply_me, null);
            holder.pictureIV= (CircleImageView) convertView.findViewById(R.id.pictureIV);
            holder.pictureMGV= (MyGridView) convertView.findViewById(R.id.pictureMGV);
            holder.nameTV= (TextView) convertView.findViewById(R.id.nameTV);
            holder.contentTV= (TextView) convertView.findViewById(R.id.contentTV);
            holder.timeTV= (TextView) convertView.findViewById(R.id.timeTV);
            holder.leaveTV= (TextView) convertView.findViewById(R.id.leaveTV);
            holder.fabulousTV= (TextView) convertView.findViewById(R.id.fabulousTV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MessageInfo info=mData.get(position);
        if(!TextUtils.isEmpty(info.getAvatar())){
            Picasso.with(context).load(info.getAvatar()).into(holder.pictureIV);
        }
        if(!TextUtils.isEmpty(info.getContent())){
            holder.contentTV.setText(info.getContent());
            holder.contentTV.setVisibility(View.VISIBLE);
        }else{
            holder.contentTV.setVisibility(View.GONE);
        }
        holder.nameTV.setText(info.getNickName());
        holder.timeTV.setText(info.getCreateTime());
        holder.leaveTV.setText(info.getReplyCnt()+"");
        holder.fabulousTV.setText(info.getZanCnt()+"");
        List<String> list=info.getPricturlList();
        if(list.size()>0){
            holder.pictureMGV.setVisibility(View.VISIBLE);
            MyThemeContentPictureAdapter adapter=new MyThemeContentPictureAdapter((FragmentActivity)context,list);
            holder.pictureMGV.setAdapter(adapter);
//            holder.pictureMGV.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position, View v) {
//                    if (selectList.size() > 0) {
//                        LocalMedia media = selectList.get(position);
//                        String pictureType = media.getPictureType();
//                        int mediaType = PictureMimeType.pictureToVideo(pictureType);
//                        switch (mediaType) {
//                            case 1:
//                                // 预览图片 可自定长按保存路径
//                                PictureSelector.create(PublishCircleInfoActivity.this).externalPicturePreview(position, selectList);
//                                break;
//                            case 2:
//                                // 预览视频
//                                PictureSelector.create(PublishCircleInfoActivity.this).externalPictureVideo(media.getPath());
//                                break;
//                            case 3:
//                                // 预览音频
//                                PictureSelector.create(PublishCircleInfoActivity.this).externalPictureAudio(media.getPath());
//                                break;
//                        }
//                    }
//                }
//            });
        }else{
            holder.pictureMGV.setVisibility(View.GONE);
        }

        return convertView;
    }

    public final class ViewHolder {
        private CircleImageView pictureIV;
        private MyGridView pictureMGV;
        private TextView nameTV;
        private TextView contentTV;
        private TextView timeTV;
        private TextView leaveTV;
        private TextView fabulousTV;
    }

}
