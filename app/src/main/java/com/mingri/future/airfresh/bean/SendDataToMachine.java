package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class SendDataToMachine {
    private int[] b;
    public SendDataToMachine(int[] b){
        this.b=b;
    }
    public int[] getSendData(){
        return b;
    }
}
