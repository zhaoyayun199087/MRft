package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class SendUpdateDataToMachine {
    private int[] b;
    public SendUpdateDataToMachine(int[] b){
        this.b=b;
    }
    public int[] getSendData(){
        return b;
    }
}
