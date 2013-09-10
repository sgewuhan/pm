package com.sg.business.model;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public interface IProcessControlable {

	public static final String POSTFIX_ACTIVATED = "_activated";

	public static final String POSTFIX_ASSIGNMENT = "_assignment";

	public static final String POSTFIX_ACTORS = "_actors";
	
	public static final String POSTFIX_INSTANCEID = "_instance";

	/**
	 * ��ǰ����������
	 */
	public static final String POSTFIX_TASK = "_task";

	/**
	 * �����ֶ�,��������
	 */
	public static final String F_WF_TASK_NAME = "taskname";
	
	/**
	 * �����ֶΣ�����ע
	 */
	public static final String F_WF_TASK_DESC = "description";
	
	/**
	 * �����ֶ�,ʵ�ʵĻ������
	 */
	public static final String F_WF_TASK_ACTUALOWNER = "actualowner";
	
	/**
	 * �����ֶΣ�����Ĵ�����
	 */
	public static final String F_WF_TASK_CREATEDBY = "createdby";

	/**
	 * �����ֶΣ�����ʱ��
	 */
	public static final String F_WF_TASK_CREATEDON = "createdon";
	
	/**
	 * �����ֶΣ����̶���id
	 */
	public static final String F_WF_TASK_PROCESSID = "processid";

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_WF_TASK_PROCESSINSTANCEID = "instanceid";

	/**
	 * �����ֶΣ�����״̬
	 */
	public static final String F_WF_TASK_STATUS = "status";
	
	/**
	 * �����ֶΣ�����ʵ��id
	 */
	public static final String F_WF_TASK_WORKITEMID = "workitemid";
	
	/**
	 * �����ֶΣ�֪ͨ����
	 */
	public static final String F_WF_TASK_NOTICEDATE = "noticedate";
	
	boolean isWorkflowActivate(String fieldKey) ;
	
	DroolsProcessDefinition getProcessDefinition(String fieldKey);

	Workflow getWorkflow(String key);

	ProjectRole getProcessActionAssignment(String key, String nodeActorParameter);

	String getProcessActionActor(String key, String nodeActorParameter);


	
}
