package com.qcjkjg.trafficrules.vo;

/**
 * Created by zongshuo on 2017/8/18.
 */
public class SubjectSelect {
    private String userId="";
    private int subId;
    private int answerNum;
    private int errorNum;
    private String seqAnswer;
    private String chapterAnswer;
    private String classAnswer;
    private String vipAnswer;
    private String topAnswer;
    private String collectAnswer;
    private int answerStatus;//0:未作答1:正确2:错误
    private String answerChoice;//答题选项
    private int subType;
    private String carId;//车型
    private String errorAnswer;//错题
    private String orderId;

    public SubjectSelect() {
        this.seqAnswer = "-1";
        this.chapterAnswer = "-1";
        this.classAnswer = "-1";
        this.vipAnswer = "-1";
        this.topAnswer = "-1";
        this.collectAnswer = "-1";
        this.errorAnswer = "-1";
        this.subType = 1;
        this.carId = "1";
        this.answerStatus=0;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public String getSeqAnswer() {
        return seqAnswer;
    }

    public void setSeqAnswer(String seqAnswer) {
        this.seqAnswer = seqAnswer;
    }

    public String getChapterAnswer() {
        return chapterAnswer;
    }

    public void setChapterAnswer(String chapterAnswer) {
        this.chapterAnswer = chapterAnswer;
    }

    public String getClassAnswer() {
        return classAnswer;
    }

    public void setClassAnswer(String classAnswer) {
        this.classAnswer = classAnswer;
    }

    public String getVipAnswer() {
        return vipAnswer;
    }

    public void setVipAnswer(String vipAnswer) {
        this.vipAnswer = vipAnswer;
    }

    public String getTopAnswer() {
        return topAnswer;
    }

    public void setTopAnswer(String topAnswer) {
        this.topAnswer = topAnswer;
    }

    public int getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        this.answerStatus = answerStatus;
    }

    public String getAnswerChoice() {
        return answerChoice;
    }

    public void setAnswerChoice(String answerChoice) {
        this.answerChoice = answerChoice;
    }

    public String getCollectAnswer() {
        return collectAnswer;
    }

    public void setCollectAnswer(String collectAnswer) {
        this.collectAnswer = collectAnswer;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getErrorAnswer() {
        return errorAnswer;
    }

    public void setErrorAnswer(String errorAnswer) {
        this.errorAnswer = errorAnswer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
