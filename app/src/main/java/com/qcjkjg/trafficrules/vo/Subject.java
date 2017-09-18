package com.qcjkjg.trafficrules.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zongshuo on 2017/8/17.
 */
public class Subject implements Parcelable {
    private String subId;
    private String subType;//1:判断题2:选择题
    private String subTitle;
    private String subPic;
    private String subA;
    private String subB;
    private String subC;
    private String subD;
    private String subAnswer;
    private String subInfos;
    private String subInfoPic;
    private String errorNum;//答错的人数
    private String star;//星级
    private String vipInfos;
    private String vipSound;
    private String vipPic;
    private String subChapter;//题目章节
    private String subClass;//题目专项
    private String subVip;//vip题目
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSubChapter() {
        return subChapter;
    }

    public void setSubChapter(String subChapter) {
        this.subChapter = subChapter;
    }

    public String getSubClass() {
        return subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
    }

    public String getSubVip() {
        return subVip;
    }

    public void setSubVip(String subVip) {
        this.subVip = subVip;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }


    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSubPic() {
        return subPic;
    }

    public void setSubPic(String subPic) {
        this.subPic = subPic;
    }

    public String getSubA() {
        return subA;
    }

    public void setSubA(String subA) {
        this.subA = subA;
    }

    public String getSubB() {
        return subB;
    }

    public void setSubB(String subB) {
        this.subB = subB;
    }

    public String getSubC() {
        return subC;
    }

    public void setSubC(String subC) {
        this.subC = subC;
    }

    public String getSubD() {
        return subD;
    }

    public void setSubD(String subD) {
        this.subD = subD;
    }

    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }

    public String getSubInfos() {
        return subInfos;
    }

    public void setSubInfos(String subInfos) {
        this.subInfos = subInfos;
    }

    public String getSubInfoPic() {
        return subInfoPic;
    }

    public void setSubInfoPic(String subInfoPic) {
        this.subInfoPic = subInfoPic;
    }

    public String getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(String errorNum) {
        this.errorNum = errorNum;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getVipInfos() {
        return vipInfos;
    }

    public void setVipInfos(String vipInfos) {
        this.vipInfos = vipInfos;
    }

    public String getVipSound() {
        return vipSound;
    }

    public void setVipSound(String vipSound) {
        this.vipSound = vipSound;
    }

    public String getVipPic() {
        return vipPic;
    }

    public void setVipPic(String vipPic) {
        this.vipPic = vipPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(subId);
        parcel.writeString(subType);
        parcel.writeString(subTitle);
        parcel.writeString(subPic);
        parcel.writeString(subA);
        parcel.writeString(subB);
        parcel.writeString(subC);
        parcel.writeString(subD);
        parcel.writeString(subAnswer);
        parcel.writeString(subInfos);
        parcel.writeString(subInfoPic);
        parcel.writeString(errorNum);
        parcel.writeString(star);
        parcel.writeString(vipInfos);
        parcel.writeString(vipSound);
        parcel.writeString(vipPic);
        parcel.writeString(subChapter);
        parcel.writeString(subClass);
        parcel.writeString(subVip);
        parcel.writeString(orderId);
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Creator(){

        @Override
        public Subject createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Subject subject = new Subject();
            subject.setSubId(source.readString());
            subject.setSubType(source.readString());
            subject.setSubTitle(source.readString());
            subject.setSubPic(source.readString());
            subject.setSubA(source.readString());
            subject.setSubB(source.readString());
            subject.setSubC(source.readString());
            subject.setSubD(source.readString());
            subject.setSubAnswer(source.readString());
            subject.setSubInfos(source.readString());
            subject.setSubInfoPic(source.readString());
            subject.setErrorNum(source.readString());
            subject.setStar(source.readString());
            subject.setVipInfos(source.readString());
            subject.setVipSound(source.readString());
            subject.setVipPic(source.readString());
            subject.setSubChapter(source.readString());
            subject.setSubClass(source.readString());
            subject.setSubVip(source.readString());
            subject.setOrderId(source.readString());
            return subject;
        }

        @Override
        public Subject[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Subject[size];
        }
    };
}
