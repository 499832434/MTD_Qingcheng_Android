package com.umeng;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng_social_sdk_res_lib.R;

/**
 * Author     wildma
 * DATE       2017/07/16
 * Des	      ${友盟分享工具类}
 */
public class ShareUtils {

    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, UMShareListener umShareListener) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }
//        new ShareAction(activity)
//                .setPlatform(platform)
//                .withMedia(web)
//                .setCallback(new UMShareListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//
//                    }
//
//                    @Override
//                    public void onResult(final SHARE_MEDIA share_media) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
//                                    Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(activity, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
//                        if (throwable != null) {
//                            Log.d("throw", "throw:" + throwable.getMessage());
//                        }
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(activity, share_media + " 分享失败", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancel(final SHARE_MEDIA share_media) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(activity, share_media + " 分享取消", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                })
//                .share();

        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/

        new ShareAction(activity)
                .setDisplayList(displaylist)
                .withMedia(web)
                .setCallback(umShareListener)
                .open();
    }

    static final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    public static void share(int  flag,String url,Activity activity, UMShareListener umShareListener) {
        UMWeb  web = new UMWeb(url);
        web.setTitle("启程考驾照,学员过关法宝");//标题
        web.setThumb(new UMImage(activity, R.drawable.ic_qicheng));
//        web.setThumb(thumb);  //缩略图
        web.setDescription("掌握技巧,考试再难都不怕,分享给你呀~");//描述

        SHARE_MEDIA platform=null;
        switch (flag){
            case 1:
                platform=SHARE_MEDIA.WEIXIN;
                break;
            case 2:
                platform=SHARE_MEDIA.WEIXIN_CIRCLE;
                break;
            case 3:
                platform=SHARE_MEDIA.QQ;
                break;
            case 4:
                platform=SHARE_MEDIA.QZONE;
                break;
            case 5:
                platform=SHARE_MEDIA.SINA;
                break;
        }
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
    }
}
