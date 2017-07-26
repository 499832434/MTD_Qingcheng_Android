package com.qcjkjg.trafficrules.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.vo.Defaultcontent;
import com.umeng.ShareUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageMainActivity extends BaseActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        initView();
    }

    private void initView(){
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setRightImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
        ((CustomTitleBar) findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
        findViewById(R.id.systemRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessageMainActivity.this, BaseListViewActivity.class);
                intent.putExtra("flag", 0);
                startActivity(intent);
            }
        });
        findViewById(R.id.themeRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MessageMainActivity.this, BaseListViewActivity.class);
//                intent.putExtra("flag", 1);
//                startActivity(intent);
                ShareUtils.shareWeb(MessageMainActivity.this, Defaultcontent.url, Defaultcontent.title
                        , Defaultcontent.text, Defaultcontent.imageurl,   R.drawable.ic_qq, null
                );
            }
        });
        findViewById(R.id.replyRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.shareWeb(MessageMainActivity.this, Defaultcontent.url, Defaultcontent.title
                        , Defaultcontent.text, Defaultcontent.imageurl,  R.drawable.ic_qq, null
                );
//                startActivity(new Intent(MessageMainActivity.this, MessageReplyActivity.class));
            }
        });
        findViewById(R.id.praiseRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MessageMainActivity.this, BaseListViewActivity.class);
//                intent.putExtra("flag", 2);
//                startActivity(intent);
//                shareGetIntegral("11111", R.drawable.ic_qq, null);
                ShareUtils.shareWeb(MessageMainActivity.this, Defaultcontent.url, Defaultcontent.title
                        , Defaultcontent.text, Defaultcontent.imageurl, R.drawable.ic_qq, null
                );

            }
        });
    }


    static final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(MessageMainActivity.this).onActivityResult(requestCode,resultCode,data);
    }
}
