package com.qcjkjg.trafficrules.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;

/**
 * Created by zongshuo on 2017/7/10.
 */
public class CustomTitleBar extends LinearLayout {
    private ImageView leftImageView,rightImageView;
    private TextView titleTextView,leftTextView;

    private String text;
    private int imageLeft,imageRight;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View titleBar = LayoutInflater.from(context).inflate(R.layout.custom_title_bar_view, null);
        titleTextView = (TextView) titleBar.findViewById(R.id.titleTextView);
        leftTextView = (TextView) titleBar.findViewById(R.id.leftTextView);
        leftImageView = (ImageView) titleBar.findViewById(R.id.leftImageView);
        rightImageView= (ImageView) titleBar.findViewById(R.id.rightImageView);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        text = ta.getString(R.styleable.CustomTitleBar_text);
        imageLeft = ta.getResourceId(R.styleable.CustomTitleBar_imageLeft, R.drawable.nav_ic_back);
        imageRight= ta.getResourceId(R.styleable.CustomTitleBar_imageRight, R.drawable.nav_ic_back);
        titleTextView.setText(text);
        leftImageView.setImageResource(imageLeft);
        rightImageView.setImageResource(imageRight);
        ta.recycle();

        addView(titleBar, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public void setLeftImageOnClickListener(OnClickListener listener) {
        leftImageView.setVisibility(View.VISIBLE);
        leftImageView.setOnClickListener(listener);
    }

    public void setRightImageOnClickListener(OnClickListener listener) {
        rightImageView.setVisibility(View.VISIBLE);
        rightImageView.setOnClickListener(listener);
    }

    public void setRightImage(int id){
        rightImageView.setImageResource(id);
    }
    public void setLeftTextViewOnClickListener(OnClickListener listener) {
        leftTextView.setOnClickListener(listener);
    }


    /**
     * 修改中间标题
     */
    public void setTitleTextView(String title) {
        if (title != null)
            titleTextView.setText(title);
    }

    /**
     * 修改中间标题
     */
    public void setLeftTextView(String str) {
        if(TextUtils.isEmpty(str)){
            leftTextView.setText("区域选择");
        }else {
            leftTextView.setText(str);
        }
        leftTextView.setVisibility(View.VISIBLE);
    }


    /**
     * 获取中间标题内容
     */
    public String getTitleTextView() {

        return titleTextView.getText().toString().trim();
    }

}
