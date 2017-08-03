package com.qcjkjg.trafficrules.event;

import com.qcjkjg.trafficrules.vo.MessageInfo;

/**
 * Created by zongshuo on 2017/8/3.
 */
public class CircleDataUpEvent {
    private MessageInfo info;
    private int positon;
    public CircleDataUpEvent(MessageInfo info,int position) {
        this.info=info;
        this.positon=position;
    }

    public MessageInfo getInfo() {
        return info;
    }

    public void setInfo(MessageInfo info) {
        this.info = info;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }
}
