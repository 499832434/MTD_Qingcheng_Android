package com.qcjkjg.trafficrules.event;

/**
 * Created by zongshuo on 2017/9/15.
 */
public class NewMessageEvent {
    private boolean flag;

    public NewMessageEvent(boolean flag) {
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
