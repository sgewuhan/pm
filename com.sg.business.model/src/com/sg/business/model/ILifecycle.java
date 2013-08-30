package com.sg.business.model;

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
	 * ��״̬
	 */
	public static final String STATUS_NONE_TEXT = "";
	
	public static final String STATUS_NONE_VALUE = "";
	
	/**
	 * ׼����״̬
	 */
	public static final String STATUS_ONREADY_TEXT = "׼����";
	
	public static final String STATUS_ONREADY_VALUE = "ready";


	/**
	 * ������״̬
	 */
	public static final String STATUS_WIP_TEXT = "������";
	
	public static final String STATUS_WIP_VALUE = "wip";

	/**
	 * ����ͣ״̬
	 */
	public static final String STATUS_PAUSED_TEXT = "����ͣ";
	
	public static final String STATUS_PAUSED_VALUE = "paused";
	
	/**
	 * �����״̬
	 */
	public static final String STATUS_FINIHED_TEXT = "�����";
	
	public static final String STATUS_FINIHED_VALUE = "finished";

	
	/**
	 * ��ȡ��״̬
	 */
	public static final String STATUS_CANCELED_TEXT = "��ȡ��";

	public static final String STATUS_CANCELED_VALUE = "canceled";

	String getLifecycleStatus();

	String getLifecycleStatusText();

}
