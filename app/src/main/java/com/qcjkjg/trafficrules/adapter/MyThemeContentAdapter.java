package com.qcjkjg.trafficrules.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.activity.signup.MyThemeActivity;
import com.qcjkjg.trafficrules.view.MyGridView;
import com.qcjkjg.trafficrules.vo.MessageThemeContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MyThemeContentAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<MessageThemeContent> mData;
    private Context context;


    public MyThemeContentAdapter(FragmentActivity context, List<MessageThemeContent> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 2;
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
            convertView = mInflater.inflate(R.layout.item_my_theme_content, null);
            holder.pictureMGV= (MyGridView) convertView.findViewById(R.id.pictureMGV);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        MyThemeContentPictureAdapter adapter=new MyThemeContentPictureAdapter((BaseListViewActivity)context,new ArrayList<String>());
//        holder.pictureMGV.setAdapter(adapter);
        return convertView;
    }

    public final class ViewHolder {
        private MyGridView pictureMGV;
    }

//    private void addImageView(MyGridLayout myGL){
//        myGL.removeAllViews();
//        for(int i=0;i<9;i++){
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            RelativeLayout lineLayout = new RelativeLayout(context);
//            lineLayout.setLayoutParams(new MyGridLayout.LayoutParams(15, 15));
//            lineLayout.setGravity(Gravity.CENTER_VERTICAL);
//            lineLayout.setBackgroundColor(Color.parseColor("#000000"));
//
//            int width=(DensityUtil.getResolution((MyThemeActivity)context)[1]-DensityUtil.dip2px(context,100))/3;
//            ImageView imageView = new ImageView(context);
//            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
//            imageView.setLayoutParams(lp);
//
//
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            Picasso.with(context).load("http://b.zol-img.com.cn/desk/bizhi/image/4/960x600/1396085330945.jpg").into(imageView);
//            lineLayout.addView(imageView);
//            myGL.addView(lineLayout);
//        }
//    }

}
