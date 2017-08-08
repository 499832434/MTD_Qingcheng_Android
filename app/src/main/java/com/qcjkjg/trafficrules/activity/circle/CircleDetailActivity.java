package com.qcjkjg.trafficrules.activity.circle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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
import com.qcjkjg.trafficrules.activity.signup.BaseListViewActivity;
import com.qcjkjg.trafficrules.adapter.CircleReplyMeAdapter;
import com.qcjkjg.trafficrules.adapter.GridImageAdapter;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import com.qcjkjg.trafficrules.fragment.CircleFragment;
import com.qcjkjg.trafficrules.net.HighRequest;
import com.qcjkjg.trafficrules.utils.DateUtils;
import com.qcjkjg.trafficrules.utils.FullyGridLayoutManager;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.utils.PrefUtils;
import com.qcjkjg.trafficrules.view.*;
import com.qcjkjg.trafficrules.vo.MessageInfo;
import com.qcjkjg.trafficrules.vo.ReplyInfo;
import com.qcjkjg.trafficrules.vo.User;
import de.greenrobot.event.EventBus;
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
public class CircleDetailActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {
    private ListView detailLV;
//    private MessageInfo info;
    private TextView landlordTV,contentTV,leaveTV, num1TV, num2TV,fabulousTV,zanNumTV,timeTV,deleteTV;
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
    private ImageView fabulousIV;
    private ApproveListLayout approveLL;
    private List<String> approveList=new ArrayList<String>();
    private RelativeLayout zanRL;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int positionFlag;
    private int cid;
    private LinearLayout pictureLL;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewWithStatusBarColorByColorPrimaryDark(R.layout.activity_circle_detail);
//        info=getIntent().getParcelableExtra(CircleFragment.CIRCLEFLAG);
        positionFlag=getIntent().getIntExtra("position", 0);
        cid=getIntent().getIntExtra("cid", 0);
        initView();

        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });

    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        pd = new ProgressDialog(CircleDetailActivity.this);
        pd.setMessage("请稍候...");
        pd.setCanceledOnTouchOutside(false);
        pd.setCancelable(true);
        pd.setIndeterminate(false);


        View view= LayoutInflater.from(CircleDetailActivity.this).inflate(R.layout.headview_circle_detail_picture,null);
        leaveTV=(TextView) view.findViewById(R.id.leaveTV);
        landlordTV= (TextView) view.findViewById(R.id.landlordTV);
        contentTV= (TextView) view.findViewById(R.id.contentTV);
        landlordCIV= (CircleImageView) view.findViewById(R.id.landlordCIV);
        fabulousIV= (ImageView) view.findViewById(R.id.fabulousIV);
        fabulousIV.setTag(0);
        fabulousTV= (TextView) view.findViewById(R.id.fabulousTV);
        view.findViewById(R.id.leaveRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getUserIsLogin()) {
                    startActivity(new Intent(CircleDetailActivity.this, LoginActivity.class));
                    return;
                }
                showReplyDialog(-1, null);
            }
        });
        view.findViewById(R.id.fabulousRL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getUserIsLogin()) {
                    startActivity(new Intent(CircleDetailActivity.this, LoginActivity.class));
                    return;
                }
                requestZan((Integer) fabulousIV.getTag());
            }
        });
        zanRL= (RelativeLayout) view.findViewById(R.id.zanRL);
        approveLL= (ApproveListLayout) view.findViewById(R.id.approveLL);
        approveLL.setCid(cid);
        zanNumTV= (TextView) view.findViewById(R.id.zanNumTV);

        timeTV=(TextView)view.findViewById(R.id.timeTV);
        deleteTV=(TextView)view.findViewById(R.id.deleteTV);
        pictureLL= (LinearLayout) view.findViewById(R.id.pictureLL);

        detailLV= (ListView) findViewById(R.id.swipe_target);
        detailLV.addHeaderView(view);
        replyAdapter=new CircleReplyMeAdapter(CircleDetailActivity.this,replyList);
        detailLV.setAdapter(replyAdapter);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);
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
                texts.add(new BasicNameValuePair("c_id", cid+""));
                texts.add(new BasicNameValuePair("phone", getUserInfo(1)));
                if(-1==flag){
                    texts.add(new BasicNameValuePair("to_phone", ""));
                    texts.add(new BasicNameValuePair("to_reply_id", "0"));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        final String result = EntityUtils.toString(
                                httpResponse.getEntity(), "utf-8");
                        Log.e("circleDetailRe", result);
                        PictureFileUtils.deleteCacheDirFile(CircleDetailActivity.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();

                                try{
                                    JSONObject jsonObject = new JSONObject(result);
                                    if (jsonObject.getString("code").equals("0")) {
                                        int num=Integer.parseInt(leaveTV.getText().toString());
                                        leaveTV.setText((num+1)+"");
                                        Toast.makeText(CircleDetailActivity.this, "成功", Toast.LENGTH_SHORT).show();
                                    }else if(jsonObject.getString("code").equals("404")){
                                        toast(jsonObject.getString("msg"));
                                        finish();
                                    }else{
                                        toast(jsonObject.getString("msg"));
                                    }
                                }catch(Exception e){

                                }finally {
                                    if(dialog!=null&&dialog.isShowing()){
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

    /**
     * 网络请求
     */
    private void request(final String replyId) {
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
                                leaveTV.setText(jsonObject.getString("reply_cnt"));
                                if(TextUtils.isEmpty(replyId)){
                                    replyList.clear();
                                }
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<array.length();i++){
                                    JSONObject infoJo=array.getJSONObject(i);
                                    ReplyInfo  info=new ReplyInfo();
                                    info.setPhone(infoJo.getString("phone"));
                                    info.setContent(infoJo.getString("content"));
                                    info.setAvater(infoJo.getString("avatar"));
                                    info.setCreateTime(DateUtils.getInterval(infoJo.getLong("create_time")));
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
                                        info.setToNickName(infoJo.getString("to_nick_name"));
                                        info.setContentReply(infoJo.getString("content_reply"));
                                    }
                                    replyList.add(info);
                                }

                                if(replyList.size()>0){
                                    replyAdapter.notifyDataSetChanged();
                                }
                            }else if(jsonObject.getString("code").equals("404")){
                                toast(jsonObject.getString("msg"));
                                finish();
                            }else{
                                toast(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                params.put("c_id", cid+"");
                if(!TextUtils.isEmpty(replyId)){
                    params.put("reply_id", replyId);
                    Log.e("reply_id",replyId);
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void requestTitleDetail() {
        if (!NetworkUtils.isNetworkAvailable(CircleDetailActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_DETAIL_TOPIC_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TitleDetailRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                JSONObject json=jsonObject.getJSONObject("info");
                                MessageInfo info=new MessageInfo();
                                info.setNickName(json.getString("nick_name"));
                                info.setContent(json.getString("content"));
                                info.setPhone(json.getString("phone"));
                                info.setAvatar(json.getString("avatar"));
                                info.setCreateTime(DateUtils.getInterval(json.getLong("create_time")));
                                List<String> imagesList=new ArrayList<String>();
                                JSONArray array=json.getJSONArray("images");
                                for(int j=0;j<array.length();j++){
                                    imagesList.add((String) array.get(j));
                                }
                                info.setPricturlList(imagesList);
                                setHead(info);
                            }else if(jsonObject.getString("code").equals("404")){
                                toast(jsonObject.getString("msg"));
                                finish();
                            }else{
                                toast(jsonObject.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            swipeToLoadLayout.setRefreshing(false);
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
                params.put("c_id", cid+"");
                if(getUserIsLogin()){
                    params.put("phone", getUserInfo(1));
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }



    /**
     * 网络请求
     */
    private void requestZan(final  int flag) {
        if (!NetworkUtils.isNetworkAvailable(CircleDetailActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_ZAN_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("zanRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                if(flag==0){
                                    fabulousIV.setImageResource(R.drawable.ic_praise_s);
                                    fabulousIV.setTag(1);
                                }else{
                                    fabulousIV.setImageResource(R.drawable.ic_praise_n);
                                    fabulousIV.setTag(0);
                                }
                                requestZanList();
                            }else if(jsonObject.getString("code").equals("404")){
                                toast(jsonObject.getString("msg"));
                                finish();
                            }else{
                                toast(jsonObject.getString("msg"));
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
                params.put("c_id", cid+"");
                params.put("phone", getUserInfo(1));
                params.put("action",flag+"");
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 网络请求
     */
    private void requestZanList() {
        if (!NetworkUtils.isNetworkAvailable(CircleDetailActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_ZAN_LIST_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("zanListRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                approveList.clear();
                                fabulousTV.setText(jsonObject.getString("zan_cnt"));
                                zanNumTV.setText(jsonObject.getString("zan_cnt") + "人赞过");
                                if("0".equals(jsonObject.getString("is_zan"))){
                                    fabulousIV.setImageResource(R.drawable.ic_praise_n);
                                    fabulousIV.setTag(0);
                                }else {
                                    fabulousIV.setImageResource(R.drawable.ic_praise_s);
                                    fabulousIV.setTag(1);
                                }
                                JSONArray array=jsonObject.getJSONArray("info");
                                for(int i=0;i<array.length();i++){
                                    JSONObject infoJo=array.getJSONObject(i);
                                    approveList.add(infoJo.getString("avatar"));
                                }
                                if(approveList.size()>0){
                                    zanRL.setVisibility(View.VISIBLE);
                                    approveLL.updateApproveList(approveList);
                                }else {
                                    zanRL.setVisibility(View.GONE);
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
                params.put("c_id", cid+"");
                if(getUserIsLogin()){
                    params.put("phone", getUserInfo(1));
                }
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    /**
     * 删除此话题
     */
    private void deleteInfo() {
        if (!NetworkUtils.isNetworkAvailable(CircleDetailActivity.this)) {
            return;
        }

        HighRequest request = new HighRequest(Request.Method.POST, ApiConstants.CIRCLE_DETAIL_DELETE_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("deletetopicRe", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("code").equals("0")) {
                                toast(jsonObject.getString("msg"));
                                finish();
                            }else{
                                toast(jsonObject.getString("msg"));
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
                params.put("c_id", cid+"");
                params.put("phone",getUserInfo(1));
                return params;
            }
        };
        InitApp.initApp.addToRequestQueue(request);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.setLoadingMore(false);
        if(replyList.size()>0){
            request(replyList.get(replyList.size()-1).getReplyId()+"");
        }
    }

    @Override
    public void onRefresh() {
        requestTitleDetail();
        requestZanList();
        request("");
    }

    // 捕获返回键的方法1
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            goBack();
        }
        return super.onKeyDown(keyCode, event);
    }


    //返回处理

    private void goBack(){
        MessageInfo infoFlag=new MessageInfo();
        infoFlag.setReplyCnt(Integer.parseInt(leaveTV.getText().toString()));
        infoFlag.setZanCnt(Integer.parseInt(fabulousTV.getText().toString()));
        infoFlag.setIsZan((Integer) fabulousIV.getTag());
        EventBus.getDefault().post(new CircleDataUpEvent(infoFlag,positionFlag));
        finish();
    }


    private void setHead(MessageInfo info){
        landlordTV.setText(info.getNickName());
        if(!TextUtils.isEmpty(info.getContent())){
            contentTV.setVisibility(View.VISIBLE);
            contentTV.setText(info.getContent());
        }
        timeTV.setText(info.getCreateTime());

        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteInfo();
            }
        });
        if (getUserIsLogin()) {
            if((!TextUtils.isEmpty(info.getPhone()))&&(info.getPhone().equals(getUserInfo(1)))){
                deleteTV.setVisibility(View.VISIBLE);
            }else{
                deleteTV.setVisibility(View.GONE);
            }
        } else {
            deleteTV.setVisibility(View.GONE);

        }

        getNetWorkPicture(info.getAvatar(), landlordCIV);

        final List<LocalMedia> pictureSelectList = new ArrayList<LocalMedia>();
        List<String> list=info.getPricturlList();
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                LocalMedia media=new LocalMedia();
                media.setPath(list.get(i));
                pictureSelectList.add(media);
            }
        }

        for(int i=0;i<list.size();i++){
            WindowManager wm = this.getWindowManager();
            int width = wm.getDefaultDisplay().getWidth()-60;
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PictureSelector.create(CircleDetailActivity.this).externalPicturePreview(view.getId(), pictureSelectList);
                }
            });
            imageView.setAdjustViewBounds(true);//设置图片自适应，只是这句话必须结合下面的setMaxWidth和setMaxHeight才能有效果。
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    width, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(30, 100, 30, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setMaxWidth(width);
            imageView.setMaxHeight(width * 5);// 这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            getNetWorkPicture(list.get(i), imageView);
            pictureLL.addView(imageView); //动态添加图片

        }
    }
}
