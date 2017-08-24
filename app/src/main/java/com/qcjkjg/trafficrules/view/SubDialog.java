package com.qcjkjg.trafficrules.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.exam.AnswerActivity;
import com.qcjkjg.trafficrules.adapter.AnswerGridAdapter;
import com.qcjkjg.trafficrules.db.DbHelper;
import com.qcjkjg.trafficrules.vo.Subject;
import com.qcjkjg.trafficrules.vo.SubjectSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/21.
 */
public class SubDialog extends Dialog {
    private Context context;
    private GridView subGV;
    private List<Subject> subjectList=new ArrayList<Subject>();
    private String collectFlag,rightStr,wrongStr,numStr;
    private int fragmentPositon;


    private List<SubjectSelect> subjectSelectList=new ArrayList<SubjectSelect>();
    private String type;

    public SubDialog(Context context, int themeResId,List<Subject> subjectList,String collectFlag,String rightStr,String wrongStr,String numStr,int fragmentPositon,String type) {
        super(context, themeResId);
        this.context=context;
        this.subjectList=subjectList;
        this.collectFlag=collectFlag;
        this.rightStr=rightStr;
        this.wrongStr=wrongStr;
        this.numStr=numStr;
        this.fragmentPositon=fragmentPositon;
        this.type=type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_answer_gird);

        initData();
        initView();



    }

    private void initData(){
        subjectSelectList=query();
    }

    private void initView(){
        subGV= (GridView) findViewById(R.id.subGV);

        AnswerGridAdapter adapter=new AnswerGridAdapter((FragmentActivity)context,subjectList,subjectSelectList,fragmentPositon);
        subGV.setAdapter(adapter);

        subGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((AnswerActivity)context).getViewPager().setCurrentItem(i);
                SubDialog.super.dismiss();
            }
        });
        ImageView collectIV= (ImageView) findViewById(R.id.collectIV);
        collectIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("1".equals(view.getTag())){
                    ((AnswerActivity)context).setCollectIV(true, subjectList.get(fragmentPositon).getSubId());
                }else{
                    ((AnswerActivity)context).setCollectIV(false,subjectList.get(fragmentPositon).getSubId());
                }
            }
        });
        if("0".equals(collectFlag)){
            collectIV.setImageResource(R.drawable.ic_stars);
            collectIV.setTag("0");//收藏
        }else{
            collectIV.setImageResource(R.drawable.ic_stars_n);
            collectIV.setTag("1");//取消收藏
        }

        ((TextView)findViewById(R.id.rightTV)).setText(rightStr);
        ((TextView)findViewById(R.id.wrongTV)).setText(wrongStr);
        ((TextView)findViewById(R.id.numFlagTV)).setText(numStr);
    }
    @Override
    public void show() {
        super.show();
        animationShow(500);
    }

    @Override
    public void dismiss() {
        animationHide(500);
    }



    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(findViewById(R.id.LL), "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(findViewById(R.id.LL), "translationY",0,1000).setDuration(mDuration)
        );
        animatorSet.start();


        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                SubDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    private List<SubjectSelect> query(){
        SubjectSelect subjectSelect=new SubjectSelect();
        if("subclass".equals(type)){
            subjectSelect.setClassAnswer(subjectList.get(0).getSubClass());
        }else if("subseq".equals(type)||"subnodone".equals(type)){
            subjectSelect.setSeqAnswer("0");
        }else if("subchapter".equals(type)){
            subjectSelect.setChapterAnswer(subjectList.get(0).getSubChapter());
        }else if("subvip".equals(type)){
            subjectSelect.setVipAnswer(subjectList.get(0).getSubVip());
        }else if("subcollectchapter".equals(type)){
            subjectSelect.setChapterAnswer(subjectList.get(0).getSubChapter());
            subjectSelect.setCollectAnswer("0");
        }else if("subcollectall".equals(type)){
            subjectSelect.setChapterAnswer("0");
            subjectSelect.setCollectAnswer("0");
        }else if("suberrorchapter".equals(type)){
            subjectSelect.setChapterAnswer(subjectList.get(0).getSubChapter());
            subjectSelect.setErrorAnswer("0");
        }else if("suberrorall".equals(type)){
            subjectSelect.setChapterAnswer("0");
            subjectSelect.setErrorAnswer("0");
        }
        DbHelper db=new DbHelper(context);
        return db.queryWholeSub(subjectSelect);
    }

}
