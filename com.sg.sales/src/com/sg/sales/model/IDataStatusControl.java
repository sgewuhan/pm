package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public interface IDataStatusControl {
	public static final String BASIC_VALUE_EDITING = "editing";
	public static final String BASIC_TEXT_EDITING = "�༭";

	public static final String BASIC_VALUE_COMMITED = "commited";
	public static final String BASIC_TEXT_COMMITED = "�ύ";

	public static final String BASIC_VALUE_CHECKED = "checked";
	public static final String BASIC_TEXT_CHECKED = "�˶�";

	public static final String BASIC_VALUE_APPROVED = "approved";
	public static final String BASIC_TEXT_APPROVED = "��׼";

	public static final String BASIC_VALUE_DEPOSITE = "deposit";
	public static final String BASIC_TEXT_DEPOSITE = "����";

	
	public static final String CONTRACT_VALUE_NOTVALID = "δ��Ч";
	public static final String CONTRACT_TEXT_NOTVALID = "δ��Ч";

	public static final String CONTRACT_VALUE_EXECTUTING = "ִ����";
	public static final String CONTRACT_TEXT_EXECTUTING = "ִ����";

	public static final String CONTRACT_VALUE_TERMINATED = "����ֹ";
	public static final String CONTRACT_TEXT_TERMINATED = "����ֹ";

	public static final String CONTRACT_VALUE_CLOSED = "�ѹر�";
	public static final String CONTRACT_TEXT_CLOSED = "�ѹر�";


	public static final String EXPENSE_TEXT_EDITING = "���";
	public static final String EXPENSE_VALUE_EDITING = "���";

	public static final String EXPENSE_TEXT_APPLY = "���ύ";
	public static final String EXPENSE_VALUE_APPLY = "���ύ";
	
	public static final String EXPENSE_TEXT_CHECKED = "�Ѻ˶�";
	public static final String EXPENSE_VALUE_CHECKED = "�Ѻ˶�";
	
	public static final String EXPENSE_TEXT_AUDITED = "�����";
	public static final String EXPENSE_VALUE_AUDITED = "�����";
	
	public static final String EXPENSE_TEXT_APPROVED = "����׼";
	public static final String EXPENSE_VALUE_APPROVED = "����׼";
	
	public static final String EXPENSE_TEXT_TRANSFER = "�ѷ���";
	public static final String EXPENSE_VALUE_TRANSFER = "�ѷ���";
	
	public static final String EXPENSE_TEXT_REJECTED = "�ѷ��";
	public static final String EXPENSE_VALUE_REJECTED = "�ѷ��";

	public static final String MESSAGE_CANNOT_MODIFY = "���ݵ�ǰ״̬�������޸�";
	public static final String MESSAGE_CANNOT_REMOVE = "���ݵ�ǰ״̬������ɾ��";
	public static final String MESSAGE_CANNOT_APPLY = "���ݵ�ǰ״̬�������ύ";


	String getStatusText();

	void checkDataStatusForRemove(IContext context) throws Exception;

	void checkDataStatusForUpdate(IContext context) throws Exception;

	void checkDataStatusForApply(IContext context) throws Exception;

}
