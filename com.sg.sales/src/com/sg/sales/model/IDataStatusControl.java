package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public interface IDataStatusControl {
	public static final String BASIC_VALUE_EDITING = "editing";
	public static final String BASIC_VALUE_COMMITED = "commited";
	public static final String BASIC_VALUE_CHECKED = "checked";
	public static final String BASIC_VALUE_APPROVED = "approved";
	public static final String BASIC_VALUE_DEPOSITE = "deposit";
	
	public static final String BASIC_TEXT_EDITING = "�༭";
	public static final String BASIC_TEXT_COMMITED = "�ύ";
	public static final String BASIC_TEXT_CHECKED = "�˶�";
	public static final String BASIC_TEXT_APPROVED = "��׼";
	public static final String BASIC_TEXT_DEPOSITE = "����";

	public static final String CONTRACT_VALUE_NOTVALID = "δ��Ч";
	public static final String CONTRACT_VALUE_EXECTUTING = "ִ����";
	public static final String CONTRACT_VALUE_TERMINATED = "����ֹ";
	public static final String CONTRACT_VALUE_CLOSED = "�ѹر�";
	
	public static final String CONTRACT_TEXT_NOTVALID = "δ��Ч";
	public static final String CONTRACT_TEXT_EXECTUTING = "ִ����";
	public static final String CONTRACT_TEXT_TERMINATED = "����ֹ";
	public static final String CONTRACT_TEXT_CLOSED = "�ѹر�";
	

	public static final String MESSAGE_CANNOT_MODIFY = "���ݵ�ǰ��״̬�������޸�";

	public static final String MESSAGE_CANNOT_REMOVE = "���ݵ�ǰ��״̬������ɾ��";
	
	String getStatusText();
	
	void checkDataStatusForRemove(IContext context)throws Exception;

	void checkDataStatusForUpdate(IContext context) throws Exception;

}
