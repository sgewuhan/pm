package com.sg.business.model;

import com.sg.business.resource.nls.Messages;

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
	 * ������ʽ
	 */
	public static final String F_MEASUREMENT = "measurement";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_NO_ID = "no";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_NO_VALUE = Messages.get().WorkDefinitionNoMeasurement;

	public static final String MEASUREMENT_TYPE_COMMIT_ID = "commit";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_COMMIT_VALUE = Messages.get().WorkDefinitionCommitMeasurement;

	public static final String MEASUREMENT_TYPE_STANDARD_ID = "standard";//$NON-NLS-1$

	public static final String MEASUREMENT_TYPE_STANDARD_VALUE = Messages.get().WorkDefinitionStandardMeasurement;

	/**
	 * ��ʱ����,���ڹ����͹��������ϣ���BasicBSONList����
	 */
	public static final String F_WORKTIME_PARAX = "worktimepara_x";
	
	/**
	 * ��ʱ
	 */
	public static final String F_WORK_TIME_DATA="worktimedata";
	

	/**
	 *  �ɹ������帴�ƹ����Ĺ�ʱͳ�Ƶ�
	 */
	public static final String F_STATISTICS_POINT = "statisticspoint";

	/**
	 * �ɹ������帴�ƹ�����ͳ�ƽ׶�
	 */
	public static final String F_STATISTICS_STEP = "statisticsstep";

	/**
	 * ��ʱ����id�����ڹ����͹��������ϵ�,��F_WORKTIME_PARA_X�����ֶ�
	 */
	public static final String F_WORKTIME_PARAX_PROGRAM_ID="program_id";
	
	/**
	 * ��ʱ����id�����ڹ����͹��������ϵ�,��F_WORKTIME_PARA_X�����ֶ�
	 */
	public static final String F_WORKTIME_PARAX_ID="para_id";
	
	/**
	 * ������Ŀ���㹤ʱ�����ڶ�������ͳ�ƹ�ʱ
	 */
	public static final String F_JOIN_PROJECT_CALCWORKS="joinprojectcalcworks";

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
			F_REMIND_BEFORE, F_WORK_CATAGORY };

}
