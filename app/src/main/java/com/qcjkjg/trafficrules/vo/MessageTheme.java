package com.qcjkjg.trafficrules.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/7/19.
 */
public class MessageTheme {
    private String time;
    private List<MessageTheme> list=new ArrayList<MessageTheme>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<MessageTheme> getList() {
        return list;
    }

    public void setList(List<MessageTheme> list) {
        this.list = list;
    }
}
