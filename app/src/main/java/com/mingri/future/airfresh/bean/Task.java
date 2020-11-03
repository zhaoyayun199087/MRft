package com.mingri.future.airfresh.bean;

public class Task {
	//具体数据
	private int [] data;
	//状态
	private int state;
	//重发次数
	private int reSendTimes = 0;
	//任务序号
	private int sn;

	public Task(int[] date, int sn) {
		// TODO Auto-generated constructor stub
		this.data = date;
		this.sn = sn;
		this.state = State.NEW;
	}

	public class State {
		public static final int FINISHED = 1;		//完成发送
		public static final int NEW = 2;			//未发送
		public static final int SENDED = 3;		//已经发送，但未确认
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getReSendTimes() {
		return reSendTimes;
	}

	public void setReSendTimes(int reSend) {
		this.reSendTimes = reSend;
	}

	public int getSn() {
		return sn;
	}

	public void setSn(int sn) {
		this.sn = sn;
	}



}
