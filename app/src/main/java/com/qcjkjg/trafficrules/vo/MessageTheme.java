package com.qcjkjg.trafficrules.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageTheme {
    private String time;
    private List<ReplyInfo> list=new ArrayList<ReplyInfo>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ReplyInfo> getList() {
        return list;
    }

    public void setList(List<ReplyInfo> list) {
        this.list = list;
    }
}
