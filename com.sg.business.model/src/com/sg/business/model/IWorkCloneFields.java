package com.sg.business.model;

public interface IWorkCloneFields {

	/**
	 * ��������ĸ����ɫ���壬{@link RoleDefinition},�����˽�ɫ�����Id
	 */
	public static final String F_CHARGER_ROLE_ID = "charger_roled_id";

	/**
	 * ָ���߽�ɫ����
	 */
	public static final String F_ASSIGNMENT_CHARGER_ROLE_ID = "assignmentcharger_roled_id";
	/**
	 * �����߽�ɫ����
	 */
	public static final String F_PARTICIPATE_ROLE_SET = "participate_roled_set";

	/**
	 * ���������ͬ�����
	 */
	public static final String F_SEQ = "seq";
	
	/**
	 * �Ƿ�����̱�����
	 */
	public static final String F_MILESTONE = "milestone";

	/**
	 * ����������,DBObject ����<br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "��Ŀ�������", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_EXECUTE = "wf_execute";

	/**
	 * ����������,DBObject ���� <br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "��Ŀ�������", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_CHANGE = "wf_change";

	/**
	 * ����������Ƿ񼤻�
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	/**
	 * ִ�й������Ƿ񼤻�
	 */
	public static final String F_WF_EXECUTE_ACTIVATED = "wf_execute_activated";

	/**
	 * ��׼��ʱ
	 */
	public static final String F_STANDARD_WORKS = "standardworks";

	/**
	 * ������̻ִ���˵Ľ�ɫ����
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";

	/**
	 * ִ�����̵�ִ���˽�ɫ����
	 */
	public static final String F_WF_EXECUTE_ASSIGNMENT = "wf_execute_assignment";

	/**
	 * �Ƿ�����ֽ⹤��"
	 */
	public static final String F_SETTING_CAN_BREAKDOWN = "s_canbreakdown";

	/**
	 * �Ƿ�������ӽ�����"
	 */
	public static final String F_SETTING_CAN_ADD_DELIVERABLES = "s_canadddeliverables";

	/**
	 * �Ƿ������޸ļƻ���ʱ"
	 */
	public static final String F_SETTING_CAN_MODIFY_PLANWORKS = "s_canmodifyplanworks";

	/**
	 * �ϼ�������ʼʱ���������Զ���ʼ
	 * 
	 */
	public static final String F_SETTING_AUTOSTART_WHEN_PARENT_START = "s_autostartwithparent";

	/**
	 * �ϼ��������ʱ���������Զ����"
	 */
	public static final String F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH = "s_autofinishwithparent";

	/**
	 * �Ƿ�������������е�������ɹ���"
	 */
	public static final String F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH = "s_canskiptofinish";

	/**
	 * �������������ʵʩ�����ĸ���"
	 */
	public static final String F_SETTING_WORKCHANGE_MANDORY = "s_workchangeflowmandory";

	/**
	 * ��������Ŀ�������ʵʩ�����ĸ���
	 */
	public static final String F_SETTING_PROJECTCHANGE_MANDORY = "s_projectchangeflowmandory";

	/**
	 * ��Ҫ���Ƶ�������
	 */
	public static final String[] SETTING_FIELDS = new String[] {
			F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
			F_SETTING_AUTOSTART_WHEN_PARENT_START,
			F_SETTING_CAN_ADD_DELIVERABLES, F_SETTING_CAN_BREAKDOWN,
			F_SETTING_CAN_MODIFY_PLANWORKS,
			F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
			F_SETTING_PROJECTCHANGE_MANDORY, F_SETTING_WORKCHANGE_MANDORY };
}
