package com.mingri.future.airfresh.util;

import com.mingri.future.airfresh.bean.Task;

import java.util.concurrent.ConcurrentLinkedQueue;

import mingrifuture.gizlib.code.util.LogUtils;

public class TaskQueue {
	private static ConcurrentLinkedQueue<Task> queue = new ConcurrentLinkedQueue<Task>();

	public synchronized static void addTask(Task task) {
		if (task != null) {
			queue.add(task);
		}
	}

	public synchronized static void finishTask(Task task) {
		if (task != null) {
			task.setState(Task.State.FINISHED);
			queue.remove(task);
			task = null;
		}
	}

	public synchronized static Task getTask() {

		Task task;
		int num = queue.size();
		if (num <= 0)
			return null;

		LogUtils.d("task queue is " + queue.size());

//		for (int i = 0; i < num; i++) {
//			task = queue.poll();
//			if (Task.State.FINISHED != (task.getState())) {
//				task.setState(Task.State.SENDED);
//				task.setReSendTimes(task.getReSendTimes() + 1);
//
//				if (task.getReSendTimes() < 3) {
//					return task;
//				} else {
//					finishTask(task);
//					continue;
//				}
//			}
//		}
		return queue.poll();
	}

	public synchronized static int getTaskNum() {
		return queue.size();
	}

	public  synchronized static void finishAllTask() {
		Task task;
		int num = queue.size();
		if (num <= 0)
			return;
		for (int i = 0; i < num - 1; i++) {
			task = queue.poll();
		}
	}

	public synchronized static void finishTaskBySn(int sn) {
		Task task;
		int num = queue.size();

		if (num <= 0)
			return;
		for (int i = 0; i < num; i++) {
			task = queue.poll();

			if (task.getSn() == sn) {
				finishTask(task);
				return;
			}
		}
	}
}