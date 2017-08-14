package com.qcjkjg.trafficrules.activity.account;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.GridImageAdapter;
import com.qcjkjg.trafficrules.event.ChangeNickNameEvent;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import de.greenrobot.event.EventBus;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/31.
 */
public class PersonalActivity extends BaseActivity{
    private TextView commitTV,nameTV;
    private CircleImageView avatarCIV;
    private List<LocalMedia> selectList = new ArrayList<LocalMedia>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView(){
        findViewById(R.id.closeIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commitTV= (TextView) findViewById(R.id.commitTV);
        commitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<BasicNameValuePair> texts = new ArrayList<BasicNameValuePair>();
                texts.add(new BasicNameValuePair("phone", getUserInfo(1)));
                texts.add(new BasicNameValuePair("nick_name", nameTV.getText().toString()));
                HashMap<File, String> files = new HashMap<File, String>();
                if(selectList.size()>0){
                    LocalMedia media = selectList.get(0);
                    files.put(new File(media.getCompressPath()), "file");
                }
                upload(texts, files, ApiConstants.UPDATE_USER_INFO_API);
            }
        });
        nameTV= (TextView) findViewById(R.id.nameTV);
        avatarCIV= (CircleImageView) findViewById(R.id.avatarCIV);
        avatarCIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPicClickListener.onAddPicClick();
            }
        });
        if(!TextUtils.isEmpty(getUserInfo(0))){
            nameTV.setText(getUserInfo(0));
        }
        if(!TextUtils.isEmpty(getUserInfo(2))){
            getNetWorkPicture(getUserInfo(2),avatarCIV);
        }

        findViewById(R.id.changeRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonalActivity.this,ChangeNameActivity.class));
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(PersonalActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.CUSTOM_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(true)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(true)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    if(selectList.size()>0){
                        LocalMedia media=new LocalMedia();
                        media=selectList.get(0);
                        Glide.with(PersonalActivity.this).load(new File(media.getCompressPath())).into(avatarCIV);
                    }
                    selectList.clear();
                    break;
            }
        }
    }

    public void onEvent(ChangeNickNameEvent event) {
        String name=event.getName();
        nameTV.setText(name);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void upload(final List<BasicNameValuePair> texts,
                       final HashMap<File, String> files,final String url) {
        new Thread(){
            @Override
            public void run()
            {
                DefaultHttpClient defaultHttpClient=iniClient();
                HttpPost httpPost = iniHttpPost(texts, files, url);
                try {
                    Log.e("hhhh","zzzz");
                    HttpResponse httpResponse = defaultHttpClient
                            .execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        // 上传成功
                        closeConnect();
                        //由于我这边服务器编码为gbk，所以编码设置gbk，如果乱码就改为utf-8
                        String result = EntityUtils.toString(
                                httpResponse.getEntity(), "utf-8");
                        Log.e("上传成功........", result);
                        PictureFileUtils.deleteCacheDirFile(PersonalActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PersonalActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (ClientProtocolException e) {
                    // e.printStackTrace();
                } catch (IOException e) {
                    // e.printStackTrace();
                } finally {

                }
            }
        }.start();
    }
}
