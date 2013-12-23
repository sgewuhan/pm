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
	public static final String F_PLAN_START = "planstart"; //$NON-NLS-1$

	/**
	 * 计划完成时间
	 */
	public static final String F_PLAN_FINISH = "planfinish"; //$NON-NLS-1$

	/**
	 * 实际开始时间
	 */
	public static final String F_ACTUAL_START = "actualstart"; //$NON-NLS-1$
	
	/**
	 * 实际完成时间
	 */
	public static final String F_ACTUAL_FINISH = "actualfinish"; //$NON-NLS-1$
	
	/**
	 * 计划工时
	 */
	public static final String F_PLAN_WORKS = "planworks"; //$NON-NLS-1$

	/**
	 * 实际工时
	 */
	public static final String F_ACTUAL_WORKS = "actualworks"; //$NON-NLS-1$

	/**
	 * 计划工期
	 */
	public static final String F_PLAN_DURATION = "planduration"; //$NON-NLS-1$

	/**
	 * 实际工期
	 */
	public static final String F_ACTUAL_DURATION = "actualduration"; //$NON-NLS-1$
}
