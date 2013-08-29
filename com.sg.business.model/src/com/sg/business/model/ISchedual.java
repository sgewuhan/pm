package com.sg.business.model;

/**
 * 排程
 * @author jinxitao
 *
 */
public interface ISchedual {
	
	/**
	 * 计划开始时间
	 */
	public static final String F_PLAN_START = "planstart";

	/**
	 * 计划完成时间
	 */
	public static final String F_PLAN_FINISH = "planfinish";

	/**
	 * 实际开始时间
	 */
	public static final String F_ACTUAL_START = "actualstart";
	
	/**
	 * 实际完成时间
	 */
	public static final String F_ACTUAL_FINISH = "actualfinish";
	
	/**
	 * 计划工时
	 */
	public static final String F_PLAN_WORKS = "planworks";

	/**
	 * 实际工时
	 */
	public static final String F_ACTUAL_WORKS = "actualworks";

	/**
	 * 计划工期
	 */
	public static final String F_PLAN_DURATION = "planduration";

	/**
	 * 实际工期
	 */
	public static final String F_ACTUAL_DURATION = "actualduration";
}
