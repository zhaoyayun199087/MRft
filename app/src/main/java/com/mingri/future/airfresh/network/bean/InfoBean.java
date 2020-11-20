package com.mingri.future.airfresh.network.bean;

public class InfoBean {
    String softwareName;
    String tboxSn;
    int percent;
    int type  ; //1 下载  2 刷新

    public InfoBean(String softwareName, String tbox, int percent, int type) {
        this.softwareName = softwareName;
        this.percent = percent;
        this.type = type;
        this.tboxSn = tbox;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getTboxSn() {
        return tboxSn;
    }

    public void setTboxSn(String tboxSn) {
        this.tboxSn = tboxSn;
    }
}
