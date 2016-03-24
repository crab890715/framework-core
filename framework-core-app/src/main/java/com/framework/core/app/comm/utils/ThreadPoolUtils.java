package com.framework.core.app.comm.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

public class ThreadPoolUtils implements InitializingBean{
	private static int corePoolSize = 20;
	private static int maximumPoolSize = 100;
	public static int getCorePoolSize() {
		return corePoolSize;
	}
	public static void setCorePoolSize(int corePoolSize) {
		ThreadPoolUtils.corePoolSize = corePoolSize;
	}
	public static int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	public static void setMaximumPoolSize(int maximumPoolSize) {
		ThreadPoolUtils.maximumPoolSize = maximumPoolSize;
	}
	public static int getKeepAliveTime() {
		return keepAliveTime;
	}
	public static void setKeepAliveTime(int keepAliveTime) {
		ThreadPoolUtils.keepAliveTime = keepAliveTime;
	}
	public static int getQueueSize() {
		return queueSize;
	}
	public static void setQueueSize(int queueSize) {
		ThreadPoolUtils.queueSize = queueSize;
	}
	private static int keepAliveTime = 3;
	private static int queueSize = 150;
	public static ThreadPoolExecutor executor = null;
	public static ThreadPoolExecutor getExecutor(){ 
		if(executor==null){create();}
		return executor; 
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		create();
	}
	private static void create(){
		executor = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize,
				keepAliveTime, 
				TimeUnit.SECONDS, 
				new ArrayBlockingQueue<Runnable>(queueSize),
				//抛弃当前的任务
				new ThreadPoolExecutor.DiscardPolicy());
	}
}
