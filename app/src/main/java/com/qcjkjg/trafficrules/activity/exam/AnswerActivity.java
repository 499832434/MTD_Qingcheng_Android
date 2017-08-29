package com.qcjkjg.trafficrules.activity.exam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.adapter.AnswerGridAdapter;
import com.qcjkjg.trafficrules.db.DbCreateHelper;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.fragment.AnswerFragment;
import com.qcjkjg.trafficrules.view.CustomTitleBar;
import com.qcjkjg.trafficrules.view.SubDialog;
import com.qcjkjg.trafficrules.vo.ExamScore;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by zongshuo on 2017/8/17.
 */
public class AnswerActivity extends BaseActivity{
    private ViewPager viewPager;
    public List<Fragment> fragments=new ArrayList<Fragment>();
    private String fragmentType;//科目几
    private List<Subject> subjectList=new ArrayList<Subject>();
    private TextView numFlagTV;
    private TextView rightTV,wrongTV;
    private int wholeRight=0,wholeWrong=0;
    private ImageView collectIV;
    private int fragmentPositon;//第几个fragment
    private String type="";
    public Boolean nodoneFlag=true;//未做练习是否开始作答
    private List<String> noRecordList=new ArrayList<String>();//答题结果记录到list
    private String time="00:00";
    private long useTime=45*60*1000;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initData();
        initView();
        getCollectStatus(0);

    }

    private void initData(){
        fragmentType=getIntent().getStringExtra("fragmentType");
        type=getIntent().getStringExtra("type");

        DbCreateHelper helper=new DbCreateHelper(AnswerActivity.this);
        if("subclass".equals(type)){
            String subclass=getIntent().getStringExtra("subclass");
            if("文字题".equals(subclass)){
                subjectList=helper.getSubjectListPicture(fragmentType,false);
            }else if("图片题".equals(subclass)){
                subjectList=helper.getSubjectListPicture(fragmentType,true);
            }else if("单选题".equals(subclass)){
                subjectList=helper.getSubjectListType(fragmentType,2);
            }else if("多选题".equals(subclass)){
                subjectList=helper.getSubjectListType(fragmentType,3);
            }else if("判断题".equals(subclass)){
                subjectList=helper.getSubjectListType(fragmentType,1);
            }else {
                subjectList=helper.getSubjectList(fragmentType, subclass,"subclass");
            }
//            subjectList=helper.getSubjectList(fragmentType,subclass,"subclass");
        }else if("subseq".equals(type)||"submemory".equals(type)){
            subjectList=helper.getSubjectList(fragmentType,"","subseq");
        }else if("subchapter".equals(type)){
            String subchapter=getIntent().getStringExtra("subchapter");
            subjectList=helper.getSubjectList(fragmentType,subchapter,"subchapter");
        }else if("subnodone".equals(type)){
            List<String> list= queryNoDone();
            subjectList=helper.getNodoneSubjectList(list,fragmentType+"");
        }else if("subvip".equals(type)){
            String subvip=getIntent().getStringExtra("subvip");
            subjectList=helper.getSubjectList(fragmentType,subvip,"subvip");
        }else if("subcollectchapter".equals(type)){
            String subcollectchapter=getIntent().getStringExtra("subcollectchapter");
            DbHelper db=new DbHelper(AnswerActivity.this);
            List<String> list=db.selectCollectChapterSubid(true,subcollectchapter,fragmentType);
            if(list.size()!=0){
                subjectList=helper.getSubjectCollectList(fragmentType, list);
            }
        }else if("subcollectall".equals(type)){
            DbHelper dbHelper=new DbHelper(AnswerActivity.this);
            List<String> list=dbHelper.selectCollectAllSubid(true,fragmentType);
            if(list.size()!=0){
                subjectList=helper.getSubjectCollectList(fragmentType, list);
            }
        }else if("suberrorchapter".equals(type)){
            String suberrorchapter=getIntent().getStringExtra("suberrorchapter");
            DbHelper db=new DbHelper(AnswerActivity.this);
            List<String> list=db.selectCollectChapterSubid(false,suberrorchapter,fragmentType);
            if(list.size()!=0){
                subjectList=helper.getSubjectCollectList(fragmentType, list);
            }
        }else if("suberrorall".equals(type)){
            DbHelper dbHelper=new DbHelper(AnswerActivity.this);
            List<String> list=dbHelper.selectCollectAllSubid(false,fragmentType);
            if(list.size()!=0){
                subjectList=helper.getSubjectCollectList(fragmentType, list);
            }
        }else if("submoni1".equals(type)){
            subjectList=helper.getMoniSubjectList1(fragmentType);
        }else if("submoni2".equals(type)){
            List<String> list= queryNoDone();
            subjectList=helper.getMoniSubjectList2(list,fragmentType+"");
        }




        for(int i=0;i<subjectList.size();i++){
            fragments.add(AnswerFragment.newInstance(i,subjectList.get(i),type,fragmentType));
        }

        if((!"subcollectchapter".equals(type))||(!"subcollectall".equals(type))||(!"suberrorchapter".equals(type))||(!"suberrorall".equals(type))||(!"submoni1".equals(type))||(!"submoni2".equals(type))){
            wholeRight=queryWholeNum(true);
            wholeWrong=queryWholeNum(false);
        }


    }

    private void initView(){
        ((CustomTitleBar)findViewById(R.id.customTitleBar)).setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        collectIV= (ImageView) findViewById(R.id.collectIV);
        if("submoni1".equals(type)||"submoni2".equals(type)){
            ((ImageView)findViewById(R.id.collectIV)).setImageResource(R.drawable.ic_validation);
            ((TextView)findViewById(R.id.collectTV)).setText("交卷");
            ((ImageView)findViewById(R.id.collectIV)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAssignDialog();
                }
            });
            ((TextView)findViewById(R.id.collectTV)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAssignDialog();
                }
            });
            CountDownTimer timer = new CountDownTimer(45*60*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    time=getTime(millisUntilFinished);
                    useTime=millisUntilFinished;
                    ((CustomTitleBar)findViewById(R.id.customTitleBar)).setTitleTextView("倒计时:  "+getTime(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    time="00:00";
                }
            };
            timer.start();
        }else{
            collectIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if("1".equals(collectIV.getTag())){
                        setCollectIV(true,subjectList.get(fragmentPositon).getSubId());
                    }else{
                        setCollectIV(false,subjectList.get(fragmentPositon).getSubId());
                    }
                }
            });
        }





        numFlagTV= (TextView) findViewById(R.id.numFlagTV);
        numFlagTV.setText("1/" + subjectList.size());
        numFlagTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCart();
            }
        });

        rightTV= (TextView) findViewById(R.id.rightTV);
        wrongTV= (TextView) findViewById(R.id.wrongTV);
        rightTV.setText(wholeRight+"");
        wrongTV.setText(wholeWrong+"");

        viewPager = (ViewPager)findViewById(R.id.viewPager);
//        viewPager.setOffscreenPageLimit(subjectList.size());
        //给viewPager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }

        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                numFlagTV.setText((position + 1) + "/" + subjectList.size());
                getCollectStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
    }

    public void setNext(int position){
        if(position<(subjectList.size()-1)){
            viewPager.setCurrentItem(position+1);
        }else{
            toast(AnswerActivity.this,"最后一题");
        }
    }

    private int queryWholeNum(boolean flag){//true:正确false:错误
        if("submemory".equals(type)||"subnodone".equals(type)){
            return 0;
        }
        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubType(Integer.parseInt(fragmentType));
        if("subclass".equals(type)){
            String subclass=getIntent().getStringExtra("subclass");
            subjectSelect.setClassAnswer(subclass);
        }else if("subseq".equals(type)){
            subjectSelect.setSeqAnswer("0");
        }else if("subchapter".equals(type)){
            String subchapter=getIntent().getStringExtra("subchapter");
            subjectSelect.setChapterAnswer(subchapter);
        }else if("subvip".equals(type)){
            String subvip=getIntent().getStringExtra("subvip");
            subjectSelect.setVipAnswer(subvip);
        }
        if(flag){
            subjectSelect.setAnswerStatus(1);
        }else{
            subjectSelect.setAnswerStatus(2);
        }
        DbHelper db=new DbHelper(AnswerActivity.this);
        return db.queryWholeSubNum(subjectSelect);
    }

    public void setWholeTV(boolean flag){//true:正确false:错误
        if(flag){
            wholeRight+=1;
        }else{
            wholeWrong+=1;
        }
        rightTV.setText(wholeRight+"");
        wrongTV.setText(wholeWrong+"");
    }

    public  void setCollectIV(Boolean flag,String subId){
        try{
            DbHelper db=new DbHelper(AnswerActivity.this);
            if(flag){
                db.addCollectSub(true,subId,fragmentType,subjectList.get(fragmentPositon).getSubChapter());
                collectIV.setImageResource(R.drawable.ic_stars);
                collectIV.setTag("0");//收藏
            }else {
                db.delectCollectSub(true,subId,fragmentType);
                collectIV.setImageResource(R.drawable.ic_stars_n);
                collectIV.setTag("1");//取消收藏
            }
        }catch (Exception e){

        }

    }

    public void getCollectStatus(int position){
        fragmentPositon=position;
        if("submoni1".equals(type)||"submoni2".equals(type)){
            return;
        }
        DbHelper db=new DbHelper(AnswerActivity.this);
        boolean flag=db.selectCollectSub(true,subjectList.get(position).getSubId(),fragmentType);
        if(flag){
            collectIV.setImageResource(R.drawable.ic_stars);
            collectIV.setTag("0");//收藏
        }else{
            collectIV.setImageResource(R.drawable.ic_stars_n);
            collectIV.setTag("1");//未收藏
        }
    }

    private void showCart() {
        SubDialog dialog  = new SubDialog(this, R.style.cartdialog,subjectList, (String) collectIV.getTag(),wholeRight+"",wholeWrong+"",numFlagTV.getText().toString(),fragmentPositon,type);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    private List<String> queryNoDone(){
        SubjectSelect subjectSelect=new SubjectSelect();
        subjectSelect.setSubType(Integer.parseInt(fragmentType+""));
        DbHelper db=new DbHelper(AnswerActivity.this);
        return db.queryNodoneSub(subjectSelect);
    }

    public Boolean getNodoneFlag() {
        return nodoneFlag;
    }

    public void setNodoneFlag(Boolean nodoneFlag) {
        this.nodoneFlag = nodoneFlag;
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        wholeRight=queryWholeNum(true);
//        wholeWrong=queryWholeNum(false);
//        rightTV.setText(wholeRight+"");
//        wrongTV.setText(wholeWrong+"");
//    }


    public List<String> getNoRecordList() {
        return noRecordList;
    }

    public void setNoRecordList(List<String> noRecordList) {
        this.noRecordList = noRecordList;
    }



    private void showAssignDialog() {
        final Dialog dialog  = new Dialog(this, R.style.cartdialog);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_assign,null);
        if(wholeRight>=90){
            ((TextView)view.findViewById(R.id.resultTV)).setText("合格");
        }else{
            ((TextView)view.findViewById(R.id.resultTV)).setText("不合格");
        }
        view.findViewById(R.id.closeIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.goonTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.commitTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommitExamScore();
                dialog.dismiss();
                finish();
            }
        });
        ((TextView)view.findViewById(R.id.errorTV)).setText(Html.fromHtml("您已答错<font color='#ea0f1e'>" + wholeWrong + "</font>题"));
        ((TextView)view.findViewById(R.id.scoreTV)).setText(Html.fromHtml("考试得分<font color='#ea0f1e'>"+wholeRight+"</font>分"));
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    private void CommitExamScore(){
        ExamScore examScore=new ExamScore();
        examScore.setScore(wholeRight + "");
        examScore.setDate(new Date().getTime() + "");
        if(noRecordList.size()>0){
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<noRecordList.size();i++){
                sb.append(noRecordList.get(i));
                if (i < noRecordList.size() - 1) {
                    sb.append(",");
                }
            }
            examScore.setAnswer(sb.toString());
        }else{
            examScore.setAnswer("");
        }
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<subjectList.size();i++) {
            sb.append(subjectList.get(i).getSubId());
            if (i < subjectList.size() - 1) {
                sb.append(",");
            }
        }
        examScore.setSubs(sb.toString());
        long time3=45*60*1000-useTime;
        String time2=getTime(time3);
        examScore.setTime(time2);
        DbHelper db=new DbHelper(AnswerActivity.this);
        db.addExamScore(examScore,fragmentType);
    }
}
