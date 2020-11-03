package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class WifiChangeEvent {

    //信号强度
    private int level;

    //连接状态
    boolean conn;
    public WifiChangeEvent(int level, boolean conn){
        this.level = level;
        this.conn = conn;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isConn() {
        return conn;
    }

    public void setConn(boolean conn) {
        this.conn = conn;
    }
}
