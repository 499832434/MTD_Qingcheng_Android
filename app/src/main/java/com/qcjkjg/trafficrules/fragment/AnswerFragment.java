package com.qcjkjg.trafficrules.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import com.qcjkjg.trafficrules.ApiConstants;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.MainActivity;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.activity.login.LoginActivity;
import com.qcjkjg.trafficrules.activity.web.BaseWebViewActivity;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.utils.NetworkUtils;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/17.
 */
public class AnswerFragment extends Fragment implements View.OnClickListener{
    private View currentView = null;
    protected AnswerActivity mActivity;
    private final static String POSITION = "position";
    private final static String SUBJECT = "Subject";
    private final static String TYPE = "type";
    private final static String FRAGMENTTYPE = "fragmentType";
    private final static String HISTORYSCORE = "historyscore";
    private int positionFlag=0;//第几题
    private Subject subjectFlag;
    private String fragmentType;//科目几
    private String type;//答题类型
    private TextView subTypeTV,subTitleTV,aTV,bTV,cTV,dTV;
    private TextView subAnswerTV,subInfosTV,soundTV,vipTV,myErrorTV,errorNumTV;
    private ImageView subPicIV,aIV,bIV,cIV,dIV,starIV1,starIV2,starIV3,starIV4,starIV5;
    private ImageView vipIV,infoPicIV;
    private List<ImageView> abcdImageList=new ArrayList<ImageView>();
    private List<ImageView> starsList=new ArrayList<ImageView>();
    private String answerStr;//正确选项
    private String checkedStr;//用户选择项
    private SubjectSelect subjectSelect;//数据库中题目信息
    private int answerNum=0,errorNum=0;
    private List<String> moreList=new ArrayList<String>();//多选选项
    private MediaPlayer mediaPlayer;
    private VideoView animationVV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_answer, container, false);
        initData();
        initView();
        return currentView;
    }

    public static AnswerFragment newInstance(int position,Parcelable subject,String type,String fragmentType,String historyscore) {
        AnswerFragment fr = new AnswerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);
        bundle.putParcelable(SUBJECT, subject);
        bundle.putString(TYPE, type);
        bundle.putString(FRAGMENTTYPE, fragmentType);
        bundle.putString(HISTORYSCORE, historyscore);
        fr.setArguments(bundle);
        return fr;
    }

    private void initData(){
        positionFlag = getArguments().getInt(POSITION);
        subjectFlag = getArguments().getParcelable(SUBJECT);
        type=getArguments().getString(TYPE);
        fragmentType=getArguments().getString(FRAGMENTTYPE);
        answerStr=subjectFlag.getSubAnswer();
        if("subnodone".equals(type)){
            if(((AnswerActivity)mActivity).getNodoneFlag()){
                subjectSelect=new SubjectSelect();
            }else{
                subjectSelect=query();
            }
        }else{
            subjectSelect=query();
        }

        Log.e("aaaaa", positionFlag + "==" + Integer.parseInt(subjectFlag.getSubId()) + "==" + subjectSelect.getAnswerChoice() + "");
    }
    private  void initView(){


        abcdImageList=new ArrayList<ImageView>();
        starsList=new ArrayList<ImageView>();

        aIV= (ImageView) currentView.findViewById(R.id.aIV);
        aIV.setTag("A");
        bIV= (ImageView) currentView.findViewById(R.id.bIV);
        bIV.setTag("B");
        cIV= (ImageView) currentView.findViewById(R.id.cIV);
        cIV.setTag("C");
        dIV= (ImageView) currentView.findViewById(R.id.dIV);
        dIV.setTag("D");

        starIV1= (ImageView) currentView.findViewById(R.id.starIV1);
        starIV2= (ImageView) currentView.findViewById(R.id.starIV2);
        starIV3= (ImageView) currentView.findViewById(R.id.starIV3);
        starIV4= (ImageView) currentView.findViewById(R.id.starIV4);
        starIV5= (ImageView) currentView.findViewById(R.id.starIV5);
        starsList.add(starIV1);
        starsList.add(starIV2);
        starsList.add(starIV3);
        starsList.add(starIV4);
        starsList.add(starIV5);

//        setStar();


        currentView.findViewById(R.id.aLL).setOnClickListener(this);
        currentView.findViewById(R.id.bLL).setOnClickListener(this);
        currentView.findViewById(R.id.cLL).setOnClickListener(this);
        currentView.findViewById(R.id.dLL).setOnClickListener(this);



        String subType="";
        if("1".equals(subjectFlag.getSubType())){
            subType="判断题";
            currentView.findViewById(R.id.cLL).setVisibility(View.GONE);
            currentView.findViewById(R.id.dLL).setVisibility(View.GONE);
            abcdImageList.add(aIV);
            abcdImageList.add(bIV);
        }else if("2".equals(subjectFlag.getSubType())){
            subType="选择题";
            abcdImageList.add(aIV);
            abcdImageList.add(bIV);
            abcdImageList.add(cIV);
            abcdImageList.add(dIV);
        }else if("3".equals(subjectFlag.getSubType())){
            subType="多选题";
            abcdImageList.add(aIV);
            abcdImageList.add(bIV);
            abcdImageList.add(cIV);
            abcdImageList.add(dIV);
        }else{
            subType="题目";
            abcdImageList.add(aIV);
            abcdImageList.add(bIV);
            abcdImageList.add(cIV);
            abcdImageList.add(dIV);
        }

        ((TextView)currentView.findViewById(R.id.subTypeTV)).setText(subType);
        ((TextView)currentView.findViewById(R.id.subTitleTV)).setText("                " + subjectFlag.getSubTitle().toString().trim());

        subPicIV=((ImageView) currentView.findViewById(R.id.subPicIV));
        vipIV=((ImageView) currentView.findViewById(R.id.vipIV));
        infoPicIV=((ImageView) currentView.findViewById(R.id.infoPicIV));
        animationVV= (VideoView) currentView.findViewById(R.id.animationVV);
        if(TextUtils.isEmpty(subjectFlag.getSubPic())){
            subPicIV.setVisibility(View.GONE);
            animationVV.setVisibility(View.GONE);
        }else{
            if(subjectFlag.getSubPic().indexOf("jpg")!=-1||subjectFlag.getSubPic().indexOf("png")!=-1){
                animationVV.setVisibility(View.GONE);
                mActivity.getLocalPicture1(subjectFlag.getSubPic(), subPicIV);
            }else if(subjectFlag.getSubPic().indexOf("mp4")!=-1){
                subPicIV.setVisibility(View.GONE);
                String uri = "android.resource://com.qcjkjg.trafficrules/raw/a"+subjectFlag.getSubPic().substring(0,subjectFlag.getSubPic().length()-4);
                animationVV.setVideoURI(Uri.parse(uri));
                animationVV.start();
                animationVV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        mp.setLooping(true);
                    }
                });
            }
        }

        ((TextView)currentView.findViewById(R.id.aTV)).setText(subjectFlag.getSubA());
        ((TextView)currentView.findViewById(R.id.bTV)).setText(subjectFlag.getSubB());
        ((TextView)currentView.findViewById(R.id.cTV)).setText(subjectFlag.getSubC());
        ((TextView)currentView.findViewById(R.id.dTV)).setText(subjectFlag.getSubD());


        if("3".equals(subjectFlag.getSubType())){
            if("submemory".equals(type)){
                setNoClick();
                setMoreAbcding(false,answerStr);
            }else{
                if(!TextUtils.isEmpty(subjectSelect.getAnswerChoice())){
                    setNoClick();
                    setMoreAbcding(false, subjectSelect.getAnswerChoice());
                }else{
                    currentView.findViewById(R.id.makeB).setVisibility(View.VISIBLE);
                    currentView.findViewById(R.id.makeB).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(moreList.size()==0){
                                mActivity.toast(mActivity,"请至少选择一个答案");
                            }else{
                                view.setVisibility(View.GONE);
                                Collections.sort(moreList);
                                StringBuffer sb=new StringBuffer();
                                for(int i=0;i<moreList.size();i++){
                                    sb.append(moreList.get(i));
                                }
                                Log.e("gg",sb.toString());
                                checkedStr=sb.toString();
                                setMoreAbcding(true,sb.toString());
                            }
                        }
                    });
                }
            }
        }else{
            if("submemory".equals(type)){
                setNoClick();
                for(int i=0;i<abcdImageList.size();i++){
                    if(answerStr.equals(abcdImageList.get(i).getTag())){
                        setAbcd(i,false);
                        break;
                    }
                }
            }else{
                if(!TextUtils.isEmpty(subjectSelect.getAnswerChoice())){
                    setNoClick();
                    if(aIV.getTag().equals(subjectSelect.getAnswerChoice())){
                        setAbcd(0,false);
                    }else if(bIV.getTag().equals(subjectSelect.getAnswerChoice())){
                        setAbcd(1,false);
                    }else if(cIV.getTag().equals(subjectSelect.getAnswerChoice())){
                        setAbcd(2,false);
                    }else{
                        setAbcd(3,false);
                    }
                }
            }
        }

        if("historyscore".equals(type)){
            setNoClick();
        }

        soundTV= (TextView) currentView.findViewById(R.id.soundTV);
        if(TextUtils.isEmpty(subjectFlag.getVipSound())){
            currentView.findViewById(R.id.soundLL).setVisibility(View.GONE);
            currentView.findViewById(R.id.soundLL1).setVisibility(View.GONE);
        }
        currentView.findViewById(R.id.soundLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if("0".equals(mActivity.getUserInfo(3))){
//                    mActivity.toast(mActivity,"请开通会员");
//                }else{
//                    try {
//                        mActivity.mediaPlayer.setDataSource(ApiConstants.SOUND_BASE_API+subjectFlag.getVipSound());
//                        mActivity.mediaPlayer.prepare();
//                        mActivity.mediaPlayer.start();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    mActivity.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
//                        @Override
//                        public void onCompletion(MediaPlayer mp) {
//                            mp.release();
//                        }
//                    });
//                }
                if((!mActivity.getUserIsLogin())||("0".equals(mActivity.getUserInfo(3)))){
                    Intent intent=new Intent(mActivity, BaseWebViewActivity.class);
                    intent.putExtra("url", ApiConstants.VIP_PERMISSION_API);
                    intent.putExtra("fragmentType", fragmentType+ "");
                    intent.putExtra("title","VIP特权");
                    startActivity(intent);
                    return;
                }
                if (!NetworkUtils.isNetworkAvailable(mActivity)) {
                    mActivity.toast(mActivity,"无网络连接");
                    return;
                }
                Log.e("yyyy",subjectFlag.getVipSound()+"==");
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.release();
                    mediaPlayer=null;
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_play);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    soundTV.setCompoundDrawables(drawable, null, null, null);
                    soundTV.setText("播放语音");
                    return;
                }
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(ApiConstants.SOUND_BASE_API+subjectFlag.getVipSound());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    soundTV.setText("停止播放");
                    Drawable drawable= getResources().getDrawable(R.drawable.ic_stop);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    soundTV.setCompoundDrawables(drawable, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    mediaPlayer.release();
                    mediaPlayer=null;
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.release();
                        mediaPlayer=null;
                        Drawable drawable= getResources().getDrawable(R.drawable.ic_play);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        soundTV.setCompoundDrawables(drawable,null,null,null);
                        soundTV.setText("播放语音");
                    }
                });
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity= (AnswerActivity)context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aLL:
                if("3".equals(subjectFlag.getSubType())){
                    setMoreAbcd("A",aIV,true);
                }else{
                    checkedStr="A";
                    setAbcd(0,true);
                }
                break;
            case R.id.bLL:
                if("3".equals(subjectFlag.getSubType())){
                    setMoreAbcd("B",bIV,true);
                }else{
                    checkedStr="B";
                    setAbcd(1,true);
                }
                break;
            case R.id.cLL:
                if("3".equals(subjectFlag.getSubType())){
                    setMoreAbcd("C",cIV,true);
                }else{
                    checkedStr="C";
                    setAbcd(2,true);
                }
                break;
            case R.id.dLL:
                if("3".equals(subjectFlag.getSubType())){
                    setMoreAbcd("D",dIV,true);
                }else{
                    checkedStr="D";
                    setAbcd(3,true);
                }
                break;
        }
    }

    private void setAbcd(int position,boolean flag){
        switch (position){
            case 0:
                setAbcding(aIV, flag);
                break;
            case 1:
                setAbcding(bIV, flag);
                break;
            case 2:
                setAbcding(cIV, flag);
                break;
            case 3:
                setAbcding(dIV, flag);
                break;
        }
    }

    private void setAbcding(ImageView IV,boolean flag){
        if("subnodone".equals(type)&&flag){
            ((AnswerActivity)mActivity).setNodoneFlag(false);
        }
        if(answerStr.equals(IV.getTag())){
            if(flag){
                mActivity.setWholeTV(true);
                add(0);
                mActivity.setNext(positionFlag);
            }
            IV.setImageResource(R.drawable.ic_dui);
        }else{
            IV.setImageResource(R.drawable.ic_cuowu);
            for(int i=0;i<abcdImageList.size();i++){
                if(answerStr.equals(abcdImageList.get(i).getTag())){
                    abcdImageList.get(i).setImageResource(R.drawable.ic_dui);
                    break;
                }
            }
            if(flag){
                mActivity.setWholeTV(false);
                add(1);
                setError();
            }
        }
        setWorngAbcd();
        setNoClick();
    }
    private void setWorngAbcd(){

        answerNum=queryAnswerNum();
        errorNum=queryErrorNum();

        currentView.findViewById(R.id.detailLL).setVisibility(View.VISIBLE);
        ((TextView)currentView.findViewById(R.id.subAnswerTV)).setText("答案    " + subjectFlag.getSubAnswer());
        if(TextUtils.isEmpty(subjectFlag.getSubInfos())){
            ((TextView)currentView.findViewById(R.id.subInfosTV)).setVisibility(View.GONE);
        }else{
            ((TextView)currentView.findViewById(R.id.subInfosTV)).setText(subjectFlag.getSubInfos());
            ((TextView)currentView.findViewById(R.id.subInfosTV)).setVisibility(View.VISIBLE);
        }
        mActivity.getLocalPicture1(subjectFlag.getSubInfoPic(), infoPicIV);
        ((TextView) currentView.findViewById(R.id.vipTV)).setText(subjectFlag.getVipInfos());
        if(!mActivity.getUserInfo(3).equals("0")){
            if(!TextUtils.isEmpty(subjectFlag.getVipInfos())){
                ((TextView)currentView.findViewById(R.id.vipTV)).setText(subjectFlag.getVipInfos());
                ((TextView)currentView.findViewById(R.id.vipTV)).setVisibility(View.VISIBLE);
            }
            mActivity.getLocalPicture1(subjectFlag.getVipPic(),vipIV);
        }
        ((TextView) currentView.findViewById(R.id.myErrorTV)).setText("共做过" + answerNum + "次,做错" + errorNum + "次");

        try {
            if(MainActivity.errorList.size()!=0){
                for(int i=0;i<MainActivity.errorList.size();i++){
                    if(subjectFlag.getSubId().equals(MainActivity.errorList.get(i).split(",")[0])){
                        setStar(Integer.parseInt(MainActivity.errorList.get(i).split(",")[1]));
                        ((TextView)currentView.findViewById(R.id.errorNumTV)).setText("共"+MainActivity.errorList.get(i).split(",")[2]+"人做错");
                        break;
                    }
                }
            }
        }catch (Exception E){

        }

    }

    private void setStar(int num) {
        for(int i=0;i<num;i++){
            starsList.get(i).setImageResource(R.drawable.ic_stars);
        }
    }


    private void setNoClick(){
        currentView.findViewById(R.id.aLL).setClickable(false);
        currentView.findViewById(R.id.bLL).setClickable(false);
        currentView.findViewById(R.id.cLL).setClickable(false);
        currentView.findViewById(R.id.dLL).setClickable(false);
    }

    private void add(int flag){
        Log.e("eee", type);
        if("subcollectchapter".equals(type)||"subcollectall".equals(type)||"suberrorchapter".equals(type)||"suberrorall".equals(type)||"submoni1".equals(type)||"submoni2".equals(type)){
            List<String> list=((AnswerActivity)mActivity).getNoRecordList();
            String str="";
            if (0 == flag) {//0:正确
                str = subjectFlag.getSubId() + "-" + checkedStr + "-1";
            } else {
                str = subjectFlag.getSubId() + "-" + checkedStr + "-2";
            }
            list.add(str);
            ((AnswerActivity)mActivity).setNoRecordList(list);
            return;
        }
        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubType(Integer.parseInt(fragmentType));
        subjectSelect.setSubId(Integer.parseInt(subjectFlag.getSubId()));
        subjectSelect.setAnswerNum(1);
        if(0==flag){//0:正确
            subjectSelect.setErrorNum(0);
            subjectSelect.setAnswerStatus(1);
        }else{
            subjectSelect.setErrorNum(1);
            subjectSelect.setAnswerStatus(2);
        }
        subjectSelect.setAnswerChoice(checkedStr);
        if("subclass".equals(type)){
            subjectSelect.setClassAnswer(subjectFlag.getSubClass());
        }else if("subseq".equals(type)||"subnodone".equals(type)){
            subjectSelect.setSeqAnswer("0");
        }else if("subchapter".equals(type)){
            if("110".equals(subjectFlag.getSubChapter())||"210".equals(subjectFlag.getSubChapter())){
                subjectSelect.setChapterAnswer(subjectFlag.getSubChapter()+"-"+mActivity.getUserInfo(8));
            }else{
                subjectSelect.setChapterAnswer(subjectFlag.getSubChapter());
            }
        }else if("subvip".equals(type)){
            subjectSelect.setVipAnswer(subjectFlag.getSubVip());
        }else if("subnanti".equals(type)){
            subjectSelect.setSeqAnswer("1");
        }
        DbHelper db=new DbHelper(mActivity);
        db.addSub(subjectSelect);
    }

    private SubjectSelect query(){
        if("subcollectchapter".equals(type)||"subcollectall".equals(type)||"suberrorchapter".equals(type)||"suberrorall".equals(type)||"submoni1".equals(type)||"submoni2".equals(type)){
            SubjectSelect subjectSelect=new SubjectSelect();
            List<String> list=((AnswerActivity)mActivity).getNoRecordList();
            if(list.size()>0){
                for(int i=0;i<list.size();i++){
                    if(subjectFlag.getSubId().equals(list.get(i).split("-")[0])){
                        subjectSelect.setAnswerChoice(list.get(i).split("-")[1]);
                        break;
                    }
                }
            }
            return subjectSelect;
        }
        if("historyscore".equals(type)){
            SubjectSelect subjectSelect=new SubjectSelect();
            String historyscore=getArguments().getString(HISTORYSCORE);
            if(!TextUtils.isEmpty(historyscore)){
                for(int i=0;i<historyscore.split(",").length;i++){
                    if(subjectFlag.getSubId().equals(historyscore.split(",")[i].split("-")[0])){
                        subjectSelect.setAnswerChoice(historyscore.split(",")[i].split("-")[1]);
                        break;
                    }
                }

            }
            return subjectSelect;
        }

        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubType(Integer.parseInt(fragmentType));
        subjectSelect.setSubId(Integer.parseInt(subjectFlag.getSubId()));
        if("subclass".equals(type)){
            subjectSelect.setClassAnswer(subjectFlag.getSubClass());
        }else if("subseq".equals(type)||"subnodone".equals(type)){
            subjectSelect.setSeqAnswer("0");
        }else if("subchapter".equals(type)){
            if("110".equals(subjectFlag.getSubChapter())||"210".equals(subjectFlag.getSubChapter())){
                subjectSelect.setChapterAnswer(subjectFlag.getSubChapter()+"-"+mActivity.getUserInfo(8));
            }else{
                subjectSelect.setChapterAnswer(subjectFlag.getSubChapter());
            }
        }else if("subvip".equals(type)){
            subjectSelect.setVipAnswer(subjectFlag.getSubVip());
        }else if("subnanti".equals(type)){
            subjectSelect.setSeqAnswer("1");
        }
        DbHelper db=new DbHelper(mActivity);
        return db.querySub(subjectSelect);
    }

    private int queryAnswerNum(){
        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubId(Integer.parseInt(subjectFlag.getSubId()));
        DbHelper db=new DbHelper(mActivity);
        return db.querySubAnswerNum(subjectSelect);
    }

    private int queryErrorNum(){
        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubId(Integer.parseInt(subjectFlag.getSubId()));
        subjectSelect.setErrorNum(1);
        DbHelper db=new DbHelper(mActivity);
        return db.querySubErrorNum(subjectSelect);
    }


    private void setError(){
//        if("subcollectchapter".equals(type)||"subcollectall".equals(type)||"suberrorchapter".equals(type)||"suberrorall".equals(type)||"submoni1".equals(type)||"submoni2".equals(type)){
//            return;
//        }
        DbHelper db=new DbHelper(mActivity);
        db.addCollectSub(false,subjectFlag.getSubId(),fragmentType,subjectFlag.getSubChapter());
    }

    private void setMoreAbcd(String str,ImageView IV,boolean flag){
        if(!"0".equals(IV.getTag())){
            moreList.add(str);
            if("A".equals(str)){
                IV.setImageResource(R.drawable.ic_aa);
            }else if("B".equals(str)){
                IV.setImageResource(R.drawable.ic_bb);
            }else if("C".equals(str)){
                IV.setImageResource(R.drawable.ic_cc);
            }else if("D".equals(str)){
                IV.setImageResource(R.drawable.ic_dd);
            }
            IV.setTag("0");
        }else{
            for(int i=0;i<moreList.size();i++){
                if(str.equals(moreList.get(i))){
                    moreList.remove(i);
                    if("A".equals(str)){
                        IV.setImageResource(R.drawable.ic_a);
                    }else if("B".equals(str)){
                        IV.setImageResource(R.drawable.ic_b);
                    }else if("C".equals(str)){
                        IV.setImageResource(R.drawable.ic_c);
                    }else if("D".equals(str)){
                        IV.setImageResource(R.drawable.ic_d);
                    }
                    IV.setTag(str);
                    break;
                }
            }
        }
    }

    private void setMoreAbcding(boolean flag,String answer){
        if("subnodone".equals(type)&&flag){
            ((AnswerActivity)mActivity).setNodoneFlag(false);
        }
        if(answerStr.equals(answer)){
            if(flag){
                mActivity.setWholeTV(true);
                add(0);
                mActivity.setNext(positionFlag);
            }
            char[] chars=answerStr.toCharArray();
            for(Character ch:chars){
                if(ch=='A'){
                    abcdImageList.get(0).setImageResource(R.drawable.ic_dui);
                }else if(ch=='B'){
                    abcdImageList.get(1).setImageResource(R.drawable.ic_dui);
                }else if(ch=='C'){
                    abcdImageList.get(2).setImageResource(R.drawable.ic_dui);
                }else if(ch=='D'){
                    abcdImageList.get(3).setImageResource(R.drawable.ic_dui);
                }
            }
        }else{
            char[] chars1=answerStr.toCharArray();
            for(Character ch:chars1){
                if(ch=='A'){
                    abcdImageList.get(0).setImageResource(R.drawable.ic_aaa);
                    abcdImageList.get(0).setTag("1");
                }else if(ch=='B'){
                    abcdImageList.get(1).setImageResource(R.drawable.ic_bbb);
                    abcdImageList.get(1).setTag("1");
                }else if(ch=='C'){
                    abcdImageList.get(2).setImageResource(R.drawable.ic_ccc);
                    abcdImageList.get(2).setTag("1");
                }else if(ch=='D'){
                    abcdImageList.get(3).setImageResource(R.drawable.ic_ddd);
                    abcdImageList.get(3).setTag("1");
                }
            }
            char[] chars2=answer.toCharArray();
            for(Character ch:chars2){
                if(ch=='A'){
                    if("1".equals(abcdImageList.get(0).getTag())){
                        abcdImageList.get(0).setImageResource(R.drawable.ic_dui);
                    }else{
                        abcdImageList.get(0).setImageResource(R.drawable.ic_cuowu);
                    }
                }else if(ch=='B'){
                    if("1".equals(abcdImageList.get(1).getTag())){
                        abcdImageList.get(1).setImageResource(R.drawable.ic_dui);
                    }else{
                        abcdImageList.get(1).setImageResource(R.drawable.ic_cuowu);
                    }
                }else if(ch=='C'){
                    if("1".equals(abcdImageList.get(2).getTag())){
                        abcdImageList.get(2).setImageResource(R.drawable.ic_dui);
                    }else{
                        abcdImageList.get(2).setImageResource(R.drawable.ic_cuowu);
                    }
                }else if(ch=='D'){
                    if("1".equals(abcdImageList.get(3).getTag())){
                        abcdImageList.get(3).setImageResource(R.drawable.ic_dui);
                    }else{
                        abcdImageList.get(3).setImageResource(R.drawable.ic_cuowu);
                    }
                }
            }
            if(flag){
                mActivity.setWholeTV(false);
                add(1);
                setError();
            }
        }
        setWorngAbcd();
        setNoClick();
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            if(mediaPlayer!=null){
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }catch (Exception e){
            mediaPlayer=null;
        }

    }


    @Override
    public void onStop() {
        clearBitmap(subPicIV);
        clearBitmap(vipIV);
        clearBitmap(infoPicIV);
        super.onStop();

    }

    private void clearBitmap(ImageView iv){
        if(iv !=  null &&  iv.getDrawable() != null){
            Bitmap oldBitmap =  ((BitmapDrawable) iv.getDrawable()).getBitmap();
            iv.setImageDrawable(null);
            if(oldBitmap !=  null){
                oldBitmap.recycle();
                oldBitmap =  null;
            }
        }
    }
}
