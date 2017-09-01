package com.qcjkjg.trafficrules.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zongshuo on 2017/7/26.
 */
public class User implements Parcelable {
    private String openid;//三方登录id
    private String phone;//电话
    private String platformType;//类型
    private String avatar;//头像
    private String nickName;//昵称
    private String result;//分数
    private String minutes;//用时

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(openid);
        parcel.writeString(phone);
        parcel.writeString(platformType);
        parcel.writeString(avatar);
        parcel.writeString(nickName);
        parcel.writeString(result);
        parcel.writeString(minutes);
    }

    public static final Parcelable.Creator<User> CREATOR = new Creator(){

        @Override
        public User createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            User user = new User();
            user.setOpenid(source.readString());
            user.setPhone(source.readString());
            user.setPlatformType(source.readString());
            user.setAvatar(source.readString());
            user.setNickName(source.readString());
            user.setResult(source.readString());
            user.setMinutes(source.readString());
            return user;
        }

        @Override
        public User[] newArray(int size) {
            // TODO Auto-generated method stub
            return new User[size];
        }
    };
}
