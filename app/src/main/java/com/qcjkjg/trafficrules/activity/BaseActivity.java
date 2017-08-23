package com.qcjkjg.trafficrules.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.InitApp;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.adapter.GridImageAdapter;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.utils.StatusBarColorCompat;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.ReplyInfo;
import de.greenrobot.event.EventBus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class BaseActivity extends AppCompatActivity {
    private String url;
    private DefaultHttpClient defaultHttpClient;
    private RequestOptions options;
    private List<BasicNameValuePair> texts;
    private HashMap<File, String> files;
    private List<LocalMedia> selectList = new ArrayList<LocalMedia>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private int compressMode = PictureConfig.SYSTEM_COMPRESS_MODE;
    private ProgressDialog pd;
    private AlertDialog dialog;
    private EditText contentET;
    private TextView num1TV,num2TV;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData(){
        pd = new ProgressDialog(BaseActivity.this);
        pd.setMessage("请稍候...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setIndeterminate(false);

        options = new RequestOptions()
                .placeholder(R.drawable.item_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }


    /**
     * 拨打电话
     */
    public void call(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(tel));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 隐藏键盘
     */
    public  void hiddenSoftInput() {
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void toast(Context context,String str){
        if(!TextUtils.isEmpty(str)){
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 初始化post的内容
     *
     * @return  HttpPost
     */
    public HttpPost iniHttpPost(List<BasicNameValuePair> texts,
                                 HashMap<File, String> files,String url) {
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder create = MultipartEntityBuilder.create();
        // 普通文本的发送，用户名&密码等
        if (texts != null && texts.size() > 0) {
            for (BasicNameValuePair iterable_element : texts) {
                create.addTextBody(iterable_element.getName(),
                        iterable_element.getValue(),contentType);
            }
        }
        // 二进制的发送，文件
        if (files != null && files.size() > 0) {
            Set<Map.Entry<File, String>> entrySet = files.entrySet();
            for (Map.Entry<File, String> iterable_element : entrySet) {
                create.addBinaryBody(iterable_element.getValue(),
                        iterable_element.getKey());

            }
        }
        HttpEntity httpEntity = create.build();
        // post内容的设置............
        httpPost.setEntity(httpEntity);
        return httpPost;

    }

    /**
     * 初始化httpClient
     */
    public DefaultHttpClient iniClient() {
        if (defaultHttpClient == null) {
            defaultHttpClient = new DefaultHttpClient();
            // HTTP协议版本1.1
            defaultHttpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
            // 连接超时
            defaultHttpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        }
        return defaultHttpClient;

    };
    /**
     * 关闭连接
     */
    public void closeConnect(){
        if(defaultHttpClient!=null){
            ClientConnectionManager connectionManager = defaultHttpClient.getConnectionManager();
            if(connectionManager!=null)
                connectionManager.shutdown();
            defaultHttpClient=null;
        }
    }

    /**
     * 记录用户的登录信息
     */

    public void loginInfo(String name,String phone,String avatar,String isvip,String platform){
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, name);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, phone);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AVATAR_KEY, avatar);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, isvip);
        PrefUtils.putString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PLATFORM_KEY, platform);
    }

    /**
     * 判断用户是否登录
     */

    public boolean getUserIsLogin(){
        String phone=PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, "");
        if(TextUtils.isEmpty(phone)){
            return false;
        }
        return  true;
    }

    /**
     * 获取用户的登录信息
     */
    public String getUserInfo(int flag){
        switch (flag){
            case 0:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_NAME_KEY, "");
            case 1:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_PHONE_KEY, "");
            case 2:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_AVATAR_KEY, "");
            case 3://0:非会员1:会员
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_IS_VIP_KEY, "0");
            case 4:
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CLIENT_ID_KEY, "");
            case 5://驾驶证类型
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_CAR_TYPE_KEY, "1");
            case 6://收藏题目
                return PrefUtils.getString(BaseActivity.this, InitApp.USER_PRIVATE_DATA, InitApp.USER_COLLECT_SUB_KEY, "");
        }
        return  "";
    }


    public void sign() {
        final String url = ApiConstants.SIGN_API;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("signMain",response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            if (jo.has("code")) {
                                if ("0".equalsIgnoreCase(jo.getString("code"))) {
//                                    Toast.makeText(BaseActivity.this,"mainActivity签到",Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                //POST 参数
                Map<String, String> params = new HashMap<String, String>();
                params.put("client_id", getUserInfo(4));
                params.put("phone", getUserInfo(1));
                params.put("device_type", InitApp.DEVICE_TYPE);
                params.put("device_token", InitApp.DEVICE_TOKEN);
                return params;
            }
        };
        Volley.newRequestQueue(BaseActivity.this).add(req);
    }


    public void getNetWorkPicture(String url,ImageView imageView){
        Glide.with(BaseActivity.this).load(url).apply(options).into(imageView);
    }

    public void toast(String str){
        if(!TextUtils.isEmpty(str)){
            Toast.makeText(BaseActivity.this,str,Toast.LENGTH_SHORT).show();
        }
    }


    public void showReplyDialog(final int flag,final ReplyInfo replyInfo,final int cid,final MessageInfo info,final int positionFlag) {
//        if(dialog!=null){
//            selectList.clear();
//            recyclerView.setVisibility(View.GONE);
//            num1TV.setVisibility(View.GONE);
//            num2TV.setVisibility(View.GONE);
//            dialog.show();
//        }
        if(dialog!=null){
           dialog=null;
        }
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
        adapter = new GridImageAdapter(this, onAddPicClickListener,num1TV,num2TV);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    PictureSelector.create(BaseActivity.this).externalPicturePreview(position, selectList);
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
                texts.add(new BasicNameValuePair("c_id", cid+""));
                texts.add(new BasicNameValuePair("phone", getUserInfo(1)));
                if(-1==flag){
                    texts.add(new BasicNameValuePair("to_phone", ""));
                    texts.add(new BasicNameValuePair("to_reply_id", "0"));
                }
                HashMap<File, String> files = new HashMap<File, String>();
                for (int i = 0; i < selectList.size(); i++) {
                    LocalMedia media = selectList.get(i);
                    files.put(new File(media.getCompressPath()), "file");
                }
                upload(texts, files, ApiConstants.CIRCLE_GET_REPLY_CONTENT_API,info,positionFlag);
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
            PictureSelector.create(BaseActivity.this)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
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
                       final HashMap<File, String> files,final String url,final MessageInfo info,final int positonFlag) {
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
                        final String result = EntityUtils.toString(
                                httpResponse.getEntity(), "utf-8");
                        Log.e("circleDetailRe", result);
                        PictureFileUtils.deleteCacheDirFile(BaseActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();

                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getString("code").equals("0")) {
                                        info.setReplyCnt(info.getReplyCnt()+1);
                                        EventBus.getDefault().post(new CircleDataUpEvent(info, positonFlag));
                                        Toast.makeText(BaseActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                    } else if (jsonObject.getString("code").equals("404")) {
                                        toast(jsonObject.getString("msg"));
                                    } else {
                                        toast(jsonObject.getString("msg"));
                                    }
                                } catch (Exception e) {

                                } finally {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
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