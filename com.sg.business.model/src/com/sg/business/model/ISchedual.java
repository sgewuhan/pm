package com.sg.business.model;

/**
 * �ų�
 * @author jinxitao
 *
 */
public interface ISchedual {
	
	/**
	 * �ƻ���ʼʱ��
	 */
	public static final String F_PLAN_START = "planstart";

	/**
	 * �ƻ����ʱ��
	 */
	public static final String F_PLAN_FINISH = "planfinish";

	/**
	 * ʵ�ʿ�ʼʱ��
	 */
	public static final String F_ACTUAL_START = "actualstart";
	
	/**
	 * ʵ�����ʱ��
	 */
	public static final String F_ACTUAL_FINISH = "actualfinish";
	
	/**
	 * �ƻ���ʱ
	 */
	public static final String F_PLAN_WORKS = "planworks";

	/**
	 * ʵ�ʹ�ʱ
	 */
	public static final String F_ACTUAL_WORKS = "actualworks";

	/**
	 * �ƻ�����
	 */
	public static final String F_PLAN_DURATION = "planduration";

	/**
	 * ʵ�ʹ���
	 */
	public static final String F_ACTUAL_DURATION = "actualduration";
}
