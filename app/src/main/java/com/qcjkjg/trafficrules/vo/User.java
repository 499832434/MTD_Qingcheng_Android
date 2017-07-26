package com.qcjkjg.trafficrules.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zongshuo on 2017/7/26.
 */
public class User implements Parcelable {
    private String openid;
    private String phone;
    private String platformType;
    private String avatar;
    private String nickName;

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
            return user;
        }

        @Override
        public User[] newArray(int size) {
            // TODO Auto-generated method stub
            return new User[size];
        }
    };
}
