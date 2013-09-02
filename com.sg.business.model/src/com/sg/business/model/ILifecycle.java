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
	public static final String STATUS_NONE_TEXT = "";
	
	public static final String STATUS_NONE_VALUE = "";
	
	/**
	 * 准备中状态
	 */
	public static final String STATUS_ONREADY_TEXT = "准备中";
	
	public static final String STATUS_ONREADY_VALUE = "ready";


	/**
	 * 进行中状态
	 */
	public static final String STATUS_WIP_TEXT = "进行中";
	
	public static final String STATUS_WIP_VALUE = "wip";

	/**
	 * 已暂停状态
	 */
	public static final String STATUS_PAUSED_TEXT = "已暂停";
	
	public static final String STATUS_PAUSED_VALUE = "paused";
	
	/**
	 * 已完成状态
	 */
	public static final String STATUS_FINIHED_TEXT = "已完成";
	
	public static final String STATUS_FINIHED_VALUE = "finished";

	
	/**
	 * 已取消状态
	 */
	public static final String STATUS_CANCELED_TEXT = "已取消";

	public static final String STATUS_CANCELED_VALUE = "canceled";

	String getLifecycleStatus();

	String getLifecycleStatusText();

}
