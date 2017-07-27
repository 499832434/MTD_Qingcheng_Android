package com.qcjkjg.trafficrules.activity.circle;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.load.Encoder;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qcjkjg.trafficrules.adapter.GridImageAdapter;
import com.qcjkjg.trafficrules.service.LocationService;
import com.qcjkjg.trafficrules.utils.FullyGridLayoutManager;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by zongshuo on 2017/7/25 0025.
 */
public class PublishCircleInfoActivity extends BaseActivity {
    private final static String TAG = PublishCircleInfoActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<LocalMedia>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private LocationService locationService;
    private TextView positionTV;
    private BDLocation locationInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_circle_info);

        initView();


    }

    private void initView(){
        findViewById(R.id.closeIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        positionTV= (TextView) findViewById(R.id.positionTV);
        positionTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPositon();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishCircleInfoActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(PublishCircleInfoActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(PublishCircleInfoActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PublishCircleInfoActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PublishCircleInfoActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });


        findViewById(R.id.publishTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<BasicNameValuePair> texts = new ArrayList<BasicNameValuePair>();
                String content = ((EditText) findViewById(R.id.contentET)).getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    texts.add(new BasicNameValuePair("content", content));
                }
//                try {
//                    if (locationInfo != null) {
//                        texts.add(new BasicNameValuePair("longitude", locationInfo.getLongitude() + ""));
//                        texts.add(new BasicNameValuePair("latitude", locationInfo.getLatitude() + ""));
//                        texts.add(new BasicNameValuePair("provice", URLEncoder.encode(locationInfo.getProvince(), "utf-8")));
//                        texts.add(new BasicNameValuePair("city", URLEncoder.encode(locationInfo.getCity(), "utf-8")));
//                        texts.add(new BasicNameValuePair("area", URLEncoder.encode(locationInfo.getDistrict(), "utf-8")));
//                        texts.add(new BasicNameValuePair("address", URLEncoder.encode(locationInfo.getAddrStr(), "utf-8")));
//                    }
//                } catch (Exception e) {
//
//                }

                if (locationInfo != null) {
                    texts.add(new BasicNameValuePair("longitude", locationInfo.getLongitude() + ""));
                    texts.add(new BasicNameValuePair("latitude", locationInfo.getLatitude() + ""));
                    texts.add(new BasicNameValuePair("provice",locationInfo.getProvince()));
                    texts.add(new BasicNameValuePair("city", locationInfo.getCity()));
                    texts.add(new BasicNameValuePair("area", locationInfo.getDistrict()));
                    texts.add(new BasicNameValuePair("address", locationInfo.getAddrStr()));
                }
                Log.e("aaaa", locationInfo.getProvince() + "===" + locationInfo.getCity() + "===" + locationInfo.getDistrict() + "===" + locationInfo.getAddrStr());
                HashMap<File, String> files = new HashMap<File, String>();
                for (int i = 0; i < selectList.size(); i++) {
                    LocalMedia media = selectList.get(i);
                    files.put(new File(media.getPath()), "file");
                }
                upload(texts, files, ApiConstants.PUBLISH_CIRCLE_INFO_API);
            }
        });
    }
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(PublishCircleInfoActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    //.compressMaxKB()//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    //.compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    break;
            }
        }
    }

    private void getPositon(){
        // -----------location config ------------
        locationService = InitApp.initApp.locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    Toast.makeText(PublishCircleInfoActivity.this,"gps定位成功",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    Toast.makeText(PublishCircleInfoActivity.this,"网络定位成功",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Toast.makeText(PublishCircleInfoActivity.this,"离线定位成功，离线定位结果也是有效的",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(PublishCircleInfoActivity.this,"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(PublishCircleInfoActivity.this,"网络不同导致定位失败，请检查网络是否通畅",Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(PublishCircleInfoActivity.this,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机",Toast.LENGTH_SHORT).show();
                }
                locationInfo=location;
                logMsg(location.getAddrStr());
            }else{
                Toast.makeText(PublishCircleInfoActivity.this,"11111",Toast.LENGTH_SHORT).show();
            }
        }

        public void onConnectHotSpotMessage(String s, int i){
        }
    };


    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (positionTV != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        positionTV.post(new Runnable() {
                            @Override
                            public void run() {
                                positionTV.setText(s);
                            }
                        });

                    }
                }).start();
            }
            //LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        if(locationService!=null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
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
                    HttpResponse httpResponse = defaultHttpClient
                            .execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        // 上传成功
                        closeConnect();
                        //由于我这边服务器编码为gbk，所以编码设置gbk，如果乱码就改为utf-8
                        String result = EntityUtils.toString(
                                httpResponse.getEntity(), "utf-8");
//                        String result = EntityUtils.toString(
//                                httpResponse.getEntity());
                        Log.e("上传成功........", result);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PublishCircleInfoActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                } catch (ClientProtocolException e) {
                    // e.printStackTrace();
                } catch (IOException e) {
                    // e.printStackTrace();
                }
            }
        }.start();
    }
}
