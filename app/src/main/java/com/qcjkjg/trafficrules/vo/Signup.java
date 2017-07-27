package com.qcjkjg.trafficrules.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class Signup implements Parcelable {
    private String title;
    private String content;
    private int newsId;
    private String pubtime;
    private String pictureUrl;
    private String abstractStr;


    public String getAbstractStr() {
        return abstractStr;
    }

    public void setAbstractStr(String abstractStr) {
        this.abstractStr = abstractStr;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public String getPubtime() {
        return pubtime;
    }

    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(pubtime);
        parcel.writeInt(newsId);
        parcel.writeString(pictureUrl);
        parcel.writeString(abstractStr);
    }


    public static final Parcelable.Creator<Signup> CREATOR = new Creator(){

        @Override
        public Signup createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Signup signup = new Signup();
            signup.setTitle(source.readString());
            signup.setContent(source.readString());
            signup.setPubtime(source.readString());
            signup.setNewsId(source.readInt());
            signup.setPictureUrl(source.readString());
            signup.setAbstractStr(source.readString());
            return signup;
        }

        @Override
        public Signup[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Signup[size];
        }
    };


}
