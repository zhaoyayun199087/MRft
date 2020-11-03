package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class PopupEvent {
    /**
     * 1  co2
     * 2  ch4
     * 3  hcho
     * 4  voc
     * 5 初效滤网
     * 6 中效滤网
     * 7 活性滤网
     * 8 高效滤网
     * 9 uvc寿命
     */
    private int type;

    public PopupEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
