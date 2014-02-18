package com.sg.business.model;

import java.util.List;

import com.mobnut.db.model.IContext;
import com.sg.business.resource.nls.Messages;

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
	public static final String F_LIFECYCLE = "status"; //$NON-NLS-1$

	/**
	 * ��״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_NONE_TEXT = Messages.get().ILifecycle_1;
	/**
	 * ��״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_NONE_VALUE = ""; //$NON-NLS-1$

	/**
	 * ׼����״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_ONREADY_TEXT = Messages.get().ILifecycle_3;
	/**
	 * ׼����״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_ONREADY_VALUE = "ready"; //$NON-NLS-1$

	/**
	 * ������״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_WIP_TEXT = Messages.get().ILifecycle_5;
	/**
	 * ������״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_WIP_VALUE = "wip"; //$NON-NLS-1$

	/**
	 * ����ͣ״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_PAUSED_TEXT = Messages.get().ILifecycle_7;
	/**
	 * ����ͣ״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_PAUSED_VALUE = "paused"; //$NON-NLS-1$

	/**
	 * �����״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_FINIHED_TEXT = Messages.get().ILifecycle_9;
	/**
	 * �����״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_FINIHED_VALUE = "finished"; //$NON-NLS-1$

	/**
	 * ��ȡ��״̬����ʾ�ı���{@value}
	 */
	public static final String STATUS_CANCELED_TEXT = Messages.get().ILifecycle_11;
	/**
	 * ��ȡ��״̬��ʵ��ֵ��{@value}
	 */
	public static final String STATUS_CANCELED_VALUE = "canceled"; //$NON-NLS-1$

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
