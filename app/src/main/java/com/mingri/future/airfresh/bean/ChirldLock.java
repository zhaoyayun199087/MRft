package com.mingri.future.airfresh.bean;

/**
 * Created by Administrator on 2017/3/14.
 */
public class ChirldLock {
    boolean lock = false;
    public ChirldLock(boolean  t){
        lock = t;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
