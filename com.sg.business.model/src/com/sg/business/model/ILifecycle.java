package com.sg.business.model;

/**
 * ��Ŀ�͹�������������״̬
 * @author jinxitao
 *
 */
public interface ILifecycle {

	public static final String F_LIFECYCLE = "status";
	
	public static final String STATUS_NONE = "";
	
	public static final String STATUS_ONREADY = "׼����";

	public static final String STATUS_WIP = "������";
	
	public static final String STATUS_PAUSED = "����ͣ";
	
	public static final String STATUS_FINIHED = "�����";
	
	public static final String STATUS_CANCELED = "��ȡ��";


}
