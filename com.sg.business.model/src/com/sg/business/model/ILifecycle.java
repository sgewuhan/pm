package com.sg.business.model;

import com.mobnut.db.model.IContext;

/**
 * ��Ŀ�͹�������������״̬
 * @author jinxitao
 *
 */
public interface ILifecycle {

	/**
	 * ��������״̬
	 */
	public static final String F_LIFECYCLE = "status";
	
	/**
	 * ��״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_NONE_TEXT = "";
	/**
	 * ��״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_NONE_VALUE = "";
	
	/**
	 * ׼����״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_ONREADY_TEXT = "׼����";
	/**
	 * ׼����״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_ONREADY_VALUE = "ready";


	/**
	 * ������״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_WIP_TEXT = "������";
	/**
	 * ������״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_WIP_VALUE = "wip";

	/**
	 * ����ͣ״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_PAUSED_TEXT = "����ͣ";
	/**
	 * ����ͣ״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_PAUSED_VALUE = "paused";
	
	/**
	 * �����״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_FINIHED_TEXT = "�����";
	/**
	 * �����״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_FINIHED_VALUE = "finished";

	
	/**
	 * ��ȡ��״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_CANCELED_TEXT = "��ȡ��";
	/**
	 * ��ȡ��״̬��ʵ��ֵ��{@value}
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

	void doStart(IContext context) throws Exception;

	void doPause(IContext context) throws Exception;

	void doFinish(IContext context) throws Exception;

	void doCancel(IContext context) throws Exception;

}
