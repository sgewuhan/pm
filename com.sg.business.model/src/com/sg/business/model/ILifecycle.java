package com.sg.business.model;

/**
 * 项目和工作的生命周期状态
 * @author jinxitao
 *
 */
public interface ILifecycle {

	public static final String F_LIFECYCLE = "status";
	
	public static final String STATUS_NONE = "";
	
	public static final String STATUS_ONREADY = "准备中";

	public static final String STATUS_WIP = "进行中";
	
	public static final String STATUS_PAUSED = "已暂停";
	
	public static final String STATUS_FINIHED = "已完成";
	
	public static final String STATUS_CANCELED = "已取消";


}
