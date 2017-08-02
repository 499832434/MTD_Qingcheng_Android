package com.qcjkjg.trafficrules.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.utils.DensityUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/2 0002.
 */
public class ApproveListLayout extends HorizontalScrollView {

    //默认图片大小
    private static final int DEFAULT_PIC_SIZE = 50;
    //默认图片数量
    private static final int DEFAULT_PIC_COUNT = 20;
    //默认图片偏移百分比 0～1
    private static final float DEFAULT_PIC_OFFSET = 0.3f;

    private Context context;
    private List<CircleImageView> headList;

    private int picSize ;
    private int picCount = DEFAULT_PIC_COUNT;
    private float picOffset = DEFAULT_PIC_OFFSET;

    public ApproveListLayout(Context context) {
        this(context, null);
    }

    public ApproveListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApproveListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        picSize = Math.round(DensityUtil.dip2px(context,DEFAULT_PIC_SIZE));
        //初始化自定义属性
        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.ApproveListLayout);
        picCount = ta.getInt(R.styleable.ApproveListLayout_pic_count, DEFAULT_PIC_COUNT);
        picSize = (int) ta.getDimension(R.styleable.ApproveListLayout_pic_size, picSize);
        picOffset = ta.getFloat(R.styleable.ApproveListLayout_pic_offset, DEFAULT_PIC_OFFSET);
        picOffset = picOffset > 1 ? 1 : picOffset;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ApproveListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        //初始化自定义属性
        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.ApproveListLayout);
        picCount = ta.getInt(R.styleable.ApproveListLayout_pic_count, DEFAULT_PIC_COUNT);
        picSize = (int) ta.getDimension(R.styleable.ApproveListLayout_pic_size, picSize);
        picOffset = ta.getFloat(R.styleable.ApproveListLayout_pic_offset, DEFAULT_PIC_OFFSET);
        picOffset = picOffset > 1 ? 1 : picOffset;
        ta.recycle();
        init();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
        //定义一个RelativeLayout
        RelativeLayout relativeLayout = new RelativeLayout(context);
        int offset = picSize - (int) (picSize * picOffset);
        headList = new ArrayList<CircleImageView>(picCount);
        //循环把CircleImageView塞到RelativiLayout中，根据偏移量来摆放位置
        for (int i = 0; i < picCount; i++) {
            CircleImageView head = new CircleImageView(context);
            head.setId(head.hashCode() + i);
//            head.setBorderColor(Color.WHITE);
//            head.setBorderWidth(Math.round(UIUtils.dp2px(1)));
            head.setImageResource(R.drawable.ic_huifu);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(picSize, picSize);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            params.setMargins((picCount - i - 1) * offset, 0, 0, 0);
            if(i==0){
                params.setMargins(0, 0, 0, 0);
            }else{
                params.setMargins((i - 1) * offset, 0, 0, 0);
            }
            relativeLayout.addView(head, params);
            headList.add(head);
        }
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //这里是关键：把定义的RelativeLayout放到布局中，这样才能显示出来
        this.addView(relativeLayout);
    }

    public void setPicSize(int picSize) {
        this.picSize = picSize;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

    public void setPicOffset(int offset) {
        picOffset = DensityUtil.dip2px(context,offset);
    }

    public void initLayout() {
        init();
    }

    //根据传进来的头像列表来更新头像
    public void updateApproveList(List<String> urlList) {
        if (urlList == null) {
            return;
        }
        hideAllHeads();
        int i = 1;
        for (String url : urlList) {
            if(!TextUtils.isEmpty(url)){
                Picasso.with(context).load(url).into(headList.get(i));
            }
            headList.get(i).setVisibility(View.VISIBLE);
            if (i == urlList.size()+1) {
                break;
            }
            i++;
        }
    }

    private void hideAllHeads() {
        for (CircleImageView head : headList) {
            head.setVisibility(View.GONE);
        }
    }
}