package com.sg.business.model;

public interface IWorkCloneFields {

	/**
	 * ��������ĸ����ɫ���壬{@link RoleDefinition},�����˽�ɫ�����Id
	 */
	public static final String F_CHARGER_ROLE_ID = "charger_roled_id"; //$NON-NLS-1$

	/**
	 * ָ���߽�ɫ����
	 */
	public static final String F_ASSIGNMENT_CHARGER_ROLE_ID = "assignmentcharger_roled_id"; //$NON-NLS-1$
	/**
	 * �����߽�ɫ����
	 */
	public static final String F_PARTICIPATE_ROLE_SET = "participate_roled_set"; //$NON-NLS-1$

	/**
	 * ���������ͬ�����
	 */
	public static final String F_SEQ = "seq"; //$NON-NLS-1$

	/**
	 * �Ƿ�����̱�����
	 */
	public static final String F_MILESTONE = "milestone"; //$NON-NLS-1$

	/**
	 * ����������,DBObject ����<br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "��Ŀ�������", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_EXECUTE = "wf_execute"; //$NON-NLS-1$

	/**
	 * ����������,DBObject ���� <br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "��Ŀ�������", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_CHANGE = "wf_change"; //$NON-NLS-1$

	/**
	 * ����������Ƿ񼤻�
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated"; //$NON-NLS-1$

	/**
	 * ִ�й������Ƿ񼤻�
	 */
	public static final String F_WF_EXECUTE_ACTIVATED = "wf_execute_activated"; //$NON-NLS-1$

	/**
	 * ��׼��ʱ
	 */
	public static final String F_STANDARD_WORKS = "standardworks"; //$NON-NLS-1$

	/**
	 * ������̻ִ���˵Ľ�ɫ����
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment"; //$NON-NLS-1$

	/**
	 * ִ�����̵�ִ���˽�ɫ����
	 */
	public static final String F_WF_EXECUTE_ASSIGNMENT = "wf_execute_assignment"; //$NON-NLS-1$

	/**
	 * �Ƿ�����ֽ⹤��"
	 */
	public static final String F_SETTING_CAN_BREAKDOWN = "s_canbreakdown"; //$NON-NLS-1$

	/**
	 * �Ƿ�������ӽ�����"
	 */
	public static final String F_SETTING_CAN_ADD_DELIVERABLES = "s_canadddeliverables"; //$NON-NLS-1$

	/**
	 * �Ƿ������޸ļƻ���ʱ"
	 */
	public static final String F_SETTING_CAN_MODIFY_PLANWORKS = "s_canmodifyplanworks"; //$NON-NLS-1$

	/**
	 * �ϼ�������ʼʱ���������Զ���ʼ
	 * 
	 */
	public static final String F_SETTING_AUTOSTART_WHEN_PARENT_START = "s_autostartwithparent"; //$NON-NLS-1$

	/**
	 * �ϼ��������ʱ���������Զ����"
	 */
	public static final String F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH = "s_autofinishwithparent"; //$NON-NLS-1$

	/**
	 * �Ƿ�������������е�������ɹ���"
	 */
	public static final String F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH = "s_canskiptofinish"; //$NON-NLS-1$

	/**
	 * �������������ʵʩ�����ĸ���"
	 */
	public static final String F_SETTING_WORKCHANGE_MANDORY = "s_workchangeflowmandory"; //$NON-NLS-1$

	/**
	 * ��������Ŀ�������ʵʩ�����ĸ���
	 */
	public static final String F_SETTING_PROJECTCHANGE_MANDORY = "s_projectchangeflowmandory"; //$NON-NLS-1$

	/**
	 * ��ǰ���ѣ���ǰ����Сʱ
	 */
	public static final String F_REMIND_BEFORE = "remindbefore"; //$NON-NLS-1$

	/**
	 * ����
	 */
	public static final String F_WORK_CATAGORY = "catagory"; //$NON-NLS-1$

	/**
	 * ��Ҫ���Ƶ�������
	 */
	public static final String[] SETTING_FIELDS = new String[] {
			F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
			F_SETTING_AUTOSTART_WHEN_PARENT_START,
			F_SETTING_CAN_ADD_DELIVERABLES, F_SETTING_CAN_BREAKDOWN,
			F_SETTING_CAN_MODIFY_PLANWORKS,
			F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
			F_SETTING_PROJECTCHANGE_MANDORY, F_SETTING_WORKCHANGE_MANDORY,
			F_REMIND_BEFORE,F_WORK_CATAGORY};

}
