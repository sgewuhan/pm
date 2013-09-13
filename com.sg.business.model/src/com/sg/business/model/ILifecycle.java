package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;

/**
 * 项目和工作的生命周期状态
 * 
 * @author jinxitao
 * 
 */
public interface ILifecycle {

	/**
	 * 生命周期状态
	 */
	public static final String F_LIFECYCLE = "status";

	/**
	 * 无状态，显示文本：{@value}
	 */
	public static final String STATUS_NONE_TEXT = "";
	/**
	 * 无状态，实际值：{@value}
	 */
	public static final String STATUS_NONE_VALUE = "";

	/**
	 * 准备中状态，显示文本：{@value}
	 */
	public static final String STATUS_ONREADY_TEXT = "准备中";
	/**
	 * 准备中状态，实际值：{@value}
	 */
	public static final String STATUS_ONREADY_VALUE = "ready";

	/**
	 * 进行中状态，显示文本：{@value}
	 */
	public static final String STATUS_WIP_TEXT = "进行中";
	/**
	 * 进行中状态，实际值：{@value}
	 */
	public static final String STATUS_WIP_VALUE = "wip";

	/**
	 * 已暂停状态，显示文本：{@value}
	 */
	public static final String STATUS_PAUSED_TEXT = "已暂停";
	/**
	 * 已暂停状态，实际值：{@value}
	 */
	public static final String STATUS_PAUSED_VALUE = "paused";

	/**
	 * 已完成状态，显示文本：{@value}
	 */
	public static final String STATUS_FINIHED_TEXT = "已完成";
	/**
	 * 已完成状态，实际值：{@value}
	 */
	public static final String STATUS_FINIHED_VALUE = "finished";

	/**
	 * 已取消状态，显示文本，{@value}
	 */
	public static final String STATUS_CANCELED_TEXT = "已取消";
	/**
	 * 已取消状态，实际值，{@value}
	 */
	public static final String STATUS_CANCELED_VALUE = "canceled";

	String getLifecycleStatus();

	String getLifecycleStatusText();

	boolean canCheck();

	boolean canCommit();

	boolean canStart();

	boolean canPause();

	boolean canFinish();

	boolean canCancel();

	Object doStart(IContext context) throws Exception;

	Object doPause(IContext context) throws Exception;

	Object doFinish(IContext context) throws Exception;

	Object doCancel(IContext context) throws Exception;

	List<Object[]> checkStartAction(IContext context) throws Exception;

	List<Object[]> checkCancelAction(IContext context) throws Exception;

	List<Object[]> checkFinishAction(IContext context) throws Exception;

	List<Object[]> checkPauseAction(IContext context) throws Exception;

}
