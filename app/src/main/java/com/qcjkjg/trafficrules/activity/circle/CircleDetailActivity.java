package com.qcjkjg.trafficrules.activity.circle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.login.BindPhoneActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.adapter.CircleDetailAdapter;
import com.qcjkjg.trafficrules.adapter.CircleReplyMeAdapter;
import com.qcjkjg.trafficrules.adapter.GridImageAdapter;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.FullyGridLayoutManager;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.view.CircleImageView;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.MyListView;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.ReplyInfo;
import com.qcjkjg.trafficrules.vo.User;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zongshuo on 2017/8/1.
 */
public class CircleDetailActivity extends BaseActivity{
    private ListView detailLV;
    private MyListView pictureLV;
    private CircleDetailAdapter pictureAdapter;
    private List<String> pictureList=new ArrayList<String>();
    private MessageInfo info;
    private TextView landlordTV,contentTV,leaveTV, num1TV, num2TV;
    private CircleImageView landlordCIV;
    private CircleReplyMeAdapter replyAdapter;
    private List<ReplyInfo> replyList=new ArrayList<ReplyInfo>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<LocalMedia> selectList = new ArrayList<LocalMedia>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private ProgressDialog pd;
    private AlertDialog dialog;
    private EditText contentET;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_detail);
        info=getIntent().getParcelableExtra(CircleFragment.CIRCLEFLAG);
        initView();
        request();
    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pd = new ProgressDialog(CircleDetailActivity.this);
        pd.setMessage("请稍候...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setIndeterminate(false);


        View view= LayoutInflater.from(CircleDetailActivity.this).inflate(R.layout.headview_circle_detail_picture,null);
        leaveTV=(TextView) view.findViewById(R.id.leaveTV);
        leaveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReplyDialog(-1,null);
            }
        });
        landlordTV= (TextView) view.findViewById(R.id.landlordTV);
        contentTV= (TextView) view.findViewById(R.id.contentTV);
        landlordCIV= (CircleImageView) view.findViewById(R.id.landlordCIV);
        landlordTV.setText(info.getNickName());
        if(!TextUtils.isEmpty(info.getContent())){
            contentTV.setVisibility(View.VISIBLE);
            contentTV.setText(info.getContent());
        }
        Picasso.with(CircleDetailActivity.this).load(info.getAvatar()).into(landlordCIV);
        pictureLV= (MyListView) view.findViewById(R.id.pictureLV);
        pictureAdapter=new CircleDetailAdapter(CircleDetailActivity.this,info.getPricturlList());
        pictureLV.setAdapter(pictureAdapter);

        detailLV= (ListView) findViewById(R.id.detailLV);
        detailLV.addHeaderView(view);
        replyAdapter=new CircleReplyMeAdapter(CircleDetailActivity.this,replyList);
        detailLV.setAdapter(replyAdapter);

    }



    /**
     * 网络请求
     */
    private void request() {
        if (!NetworkUtils.isNetworkAvailable(CircleDetailActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_GET_REPLY_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("replyRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                replyList.clear();
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<array.length();i++){
                                    JSONObject infoJo=array.getJSONObject(i);
                                    ReplyInfo  info=new ReplyInfo();
                                    info.setPhone(infoJo.getString("phone"));
                                    info.setContent(infoJo.getString("content"));
                                    info.setAvater(infoJo.getString("avatar"));
                                    info.setCreateTime(sdf.format(new Date(infoJo.getLong("create_time") * 1000)));
                                    JSONArray imageArray=infoJo.getJSONArray("images");
                                    List<String> imageList=new ArrayList<String>();
                                    for(int j=0;j<imageArray.length();j++){
                                        imageList.add((String) imageArray.get(j));
                                    }
                                    info.setImagesList(imageList);
                                    info.setNickName(infoJo.getString("nick_name"));
                                    info.setReplyId(infoJo.getString("reply_id"));
                                    info.setToPhone(infoJo.getString("to_phone"));
                                    info.setToReplyId(infoJo.getInt("to_reply_id"));
                                    if(infoJo.getInt("to_reply_id")>0){
                                        info.setContentReply(infoJo.getString("content_reply"));
                                    }
                                    replyList.add(info);
                                }

                                if(replyList.size()>0){
                                    replyAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("c_id", info.getCid()+"");
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }


    public void showReplyDialog(final int flag,final ReplyInfo replyInfo) {
        dialog = new AlertDialog.Builder(this, R.style.style_dialog).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        View view = View.inflate(this, R.layout.dialog_circle_reply, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        num1TV= (TextView) view.findViewById(R.id.num1TV);
        num2TV= (TextView) view.findViewById(R.id.num2TV);
        adapter = new GridImageAdapter(CircleDetailActivity.this, onAddPicClickListener,num1TV,num2TV);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    PictureSelector.create(CircleDetailActivity.this).externalPicturePreview(position, selectList);
                }
            }
        });
        contentET= (EditText) view.findViewById(R.id.contentET);

        view.findViewById(R.id.sendTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content=contentET.getText().toString();
                List<BasicNameValuePair> texts = new ArrayList<BasicNameValuePair>();
                texts.add(new BasicNameValuePair("content",content));
                texts.add(new BasicNameValuePair("c_id", info.getCid()+""));
                texts.add(new BasicNameValuePair("phone", getUserInfo(1)));
                if(-1==flag){
                    texts.add(new BasicNameValuePair("to_phone", ""));
                    texts.add(new BasicNameValuePair("to_reply_id", "1"));
                }else{
                    texts.add(new BasicNameValuePair("to_phone", replyInfo.getPhone()));
                    texts.add(new BasicNameValuePair("to_reply_id", replyInfo.getReplyId()+""));
                }


                HashMap<File, String> files = new HashMap<File, String>();
                for (int i = 0; i < selectList.size(); i++) {
                    LocalMedia media = selectList.get(i);
                    files.put(new File(media.getCompressPath()), "file");
                }
                upload(texts, files, ApiConstants.CIRCLE_GET_REPLY_CONTENT_API);
            }
        });
        ImageView addIV= (ImageView) view.findViewById(R.id.addIV);
        addIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPicClickListener.onAddPicClick();
            }
        });
        window.setContentView(view);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//如果不设置,可能部分机型出现左右有空隙,也就是产生margin的感觉
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;//显示dialog的时候,就显示软键盘
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点,
        params.dimAmount=0.5f;//设置对话框的透明程度背景(非布局的透明度)
        window.setAttributes(params);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(CircleDetailActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_QQ_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
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
                    .enableCrop(false)// 是否裁剪
                    .compress(true)// 是否压缩
                    .compressMode(compressMode)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(selectList)// 是否传入已选图片
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
                    if(selectList.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        num1TV.setVisibility(View.VISIBLE);
                        num1TV.setText(selectList.size() + "");
                        num2TV.setVisibility(View.VISIBLE);
                        num2TV.setText(selectList.size()+"/9");
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        num1TV.setVisibility(View.GONE);
                        num2TV.setVisibility(View.GONE);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }


    public void upload(final List<BasicNameValuePair> texts,
                       final HashMap<File, String> files,final String url) {
        pd.show();
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
                        Log.e("circleDetailRe", result);
                        PictureFileUtils.deleteCacheDirFile(CircleDetailActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
//                                request();
//                                detailLV.setSelection(1);
                                Toast.makeText(CircleDetailActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                if(dialog!=null&&dialog.isShowing()){
                                    dialog.dismiss();
                                }
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
