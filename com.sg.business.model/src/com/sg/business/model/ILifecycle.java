package com.sg.business.model;

/**
 * 项目和工作的生命周期状态
 * @author jinxitao
 *
 */
public interface ILifecycle {

	/**
	 * 生命周期状态
	 */
	public static final String F_LIFECYCLE = "status";
	
	/**
	 * 无状态
	 */
	public static final String STATUS_NONE = "";
	
	/**
	 * 准备中状态
	 */
	public static final String STATUS_ONREADY = "准备中";

	/**
	 * 进行中状态
	 */
	public static final String STATUS_WIP = "进行中";
	
	/**
	 * 已暂停状态
	 */
	public static final String STATUS_PAUSED = "已暂停";
	
	/**
	 * 已完成状态
	 */
	public static final String STATUS_FINIHED = "已完成";
	
	/**
	 * 已取消状态
	 */
	public static final String STATUS_CANCELED = "已取消";


}
