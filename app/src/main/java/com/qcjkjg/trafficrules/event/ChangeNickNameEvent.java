package com.qcjkjg.trafficrules.event;

import com.qcjkjg.trafficrules.vo.MessageInfo;

/**
 * Created by zongshuo on 2017/8/3.
 */
public class ChangeNickNameEvent {
    private String name;
    public ChangeNickNameEvent(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
