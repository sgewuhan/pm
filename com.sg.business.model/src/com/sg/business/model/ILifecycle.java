package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;

/**
 * ��Ŀ�͹�������������״̬
 * 
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

	Object doStart(IContext context) throws Exception;

	Object doPause(IContext context) throws Exception;

	Object doFinish(IContext context) throws Exception;

	Object doCancel(IContext context) throws Exception;

	List<Object[]> checkStartAction(IContext context) throws Exception;

	List<Object[]> checkCancelAction(IContext context) throws Exception;

	List<Object[]> checkFinishAction(IContext context) throws Exception;

	List<Object[]> checkPauseAction(IContext context) throws Exception;

}
