package com.mingri.future.airfresh.bean;

/**
 * Created by pengl on 2016/1/17.
 */
public class WifItemInfo {
    /**
     * 网络类型
     */
    private int type;
    private String caps;
    private String ssid;
    private String networkType;
    private int cip;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCaps() {
        return caps;
    }

    public void setCaps(String caps) {
        this.caps = caps;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public int getCip() {
        return cip;
    }

    public void setCip(int cip) {
        this.cip = cip;
    }

}
