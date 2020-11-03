package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ReceDataFromMachine {
    private byte[] b;
    public ReceDataFromMachine(byte[] b){
        this.b=b;
    }
    public byte[] getBytes(){
        return b;
    }
}
