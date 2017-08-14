package com.qcjkjg.trafficrules.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/1.
 */
public class ReplyInfo {
    private String toNickName;
    private String toAvatar;
    private int toReplyId;
    private String nickName;
    private String toPhone;
    private String content;
    private String phone;
    private String createTime;
    private String replyId;
    private String avatar;
    private String images;
    private String contentReply;
    private List<String> imagesList=new ArrayList<String>();

    private int replyCnt;
    private int zanCnt;
    private int isZan;//1：赞过  0：未赞过
    private int zanId;
    private int cid;
    private int themeFlag=0;//0:没有1:有
    private String customTime;


    public String getCustomTime() {
        return customTime;
    }

    public void setCustomTime(String customTime) {
        this.customTime = customTime;
    }

    public int getThemeFlag() {
        return themeFlag;
    }

    public void setThemeFlag(int themeFlag) {
        this.themeFlag = themeFlag;
    }

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

    public int getZanCnt() {
        return zanCnt;
    }

    public void setZanCnt(int zanCnt) {
        this.zanCnt = zanCnt;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }

    public int getZanId() {
        return zanId;
    }

    public void setZanId(int zanId) {
        this.zanId = zanId;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public String getToAvatar() {
        return toAvatar;
    }

    public void setToAvatar(String toAvatar) {
        this.toAvatar = toAvatar;
    }

    public int getToReplyId() {
        return toReplyId;
    }

    public void setToReplyId(int toReplyId) {
        this.toReplyId = toReplyId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
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

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getContentReply() {
        return contentReply;
    }

    public void setContentReply(String contentReply) {
        this.contentReply = contentReply;
    }
}
