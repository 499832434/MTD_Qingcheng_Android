package com.qcjkjg.trafficrules.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/28.
 */
public class MessageInfo implements Parcelable {
    private int cid;
    private int replyCnt;
    private String nickName;
    private String content;
    private String phone;
    private String createTime;
    private int zanCnt;
    private String avatar;
    private List<String> pricturlList=new ArrayList<String>();

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getReplyCnt() {
        return replyCnt;
    }

    public void setReplyCnt(int replyCnt) {
        this.replyCnt = replyCnt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getZanCnt() {
        return zanCnt;
    }

    public void setZanCnt(int zanCnt) {
        this.zanCnt = zanCnt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getPricturlList() {
        return pricturlList;
    }

    public void setPricturlList(List<String> pricturlList) {
        this.pricturlList = pricturlList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cid);
        parcel.writeInt(replyCnt);
        parcel.writeString(nickName);
        parcel.writeString(content);
        parcel.writeString(phone);
        parcel.writeString(createTime);
        parcel.writeInt(zanCnt);
        parcel.writeString(avatar);
        parcel.writeStringList(pricturlList);
    }


    public static final Parcelable.Creator<MessageInfo> CREATOR = new Creator(){

        @Override
        public MessageInfo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            MessageInfo messageInfo = new MessageInfo();
            messageInfo.setCid(source.readInt());
            messageInfo.setReplyCnt(source.readInt());
            messageInfo.setNickName(source.readString());
            messageInfo.setPhone(source.readString());
            messageInfo.setCreateTime(source.readString());
            messageInfo.setZanCnt(source.readInt());
            messageInfo.setAvatar(source.readString());
            messageInfo.setPricturlList(source.createStringArrayList());
            return messageInfo;
        }

        @Override
        public MessageInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MessageInfo[size];
        }
    };

}
