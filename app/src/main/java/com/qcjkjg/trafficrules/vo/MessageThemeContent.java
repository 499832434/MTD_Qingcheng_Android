package com.qcjkjg.trafficrules.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageThemeContent {
    private int fabulous;//点赞数
    private int leave;//留言数
    private String title;
    private String time;
    private List<String> prictureList=new ArrayList<String>();

    public int getFabulous() {
        return fabulous;
    }

    public void setFabulous(int fabulous) {
        this.fabulous = fabulous;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getPrictureList() {
        return prictureList;
    }

    public void setPrictureList(List<String> prictureList) {
        this.prictureList = prictureList;
    }
}
