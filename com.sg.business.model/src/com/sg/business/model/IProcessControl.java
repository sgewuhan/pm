package com.sg.business.model;

import java.util.List;

import org.bson.types.BasicBSONList;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public interface IProcessControl {

	public static final String POSTFIX_ACTIVATED = "_activated";

	public static final String POSTFIX_ASSIGNMENT = "_assignment";

	public static final String POSTFIX_ACTORS = "_actors";

	public static final String POSTFIX_INSTANCEID = "_instance";

	public static final String POSTFIX_HISTORY = "_history";

	/**
	 * ��ǰ����������
	 */
	@Deprecated
	public static final String POSTFIX_TASK = "_task";

	/**
	 * �����ֶ�,����id
	 */
	@Deprecated
	public static final String F_WF_TASK_ID = "taskid";

	/**
	 * �����ֶ�,��������
	 */
	@Deprecated
	public static final String F_WF_TASK_NAME = "taskname";

	/**
	 * �����ֶΣ�����ע
	 */
	@Deprecated
	public static final String F_WF_TASK_DESC = "description";

	/**
	 * �����ֶ�,ʵ�ʵĻ������
	 */
	@Deprecated
	public static final String F_WF_TASK_ACTUALOWNER = "actualowner";

	/**
	 * �����ֶΣ�����Ĵ�����
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDBY = "createdby";

	/**
	 * �����ֶΣ�����ʱ��
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDON = "createdon";

	/**
	 * �����ֶΣ����̶���id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSID = "processid";

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSINSTANCEID = "instanceid";

	/**
	 * �����ֶΣ�����״̬
	 */
	@Deprecated
	public static final String F_WF_TASK_STATUS = "status";

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	@Deprecated
	public static final String F_WF_TASK_WORKITEMID = "workitemid";

	/**
	 * �����ֶΣ�֪ͨ����
	 */
	@Deprecated
	public static final String F_WF_TASK_NOTICEDATE = "noticedate";

	/**
	 * �����ֶΣ�ִ����Id(��¼�ʻ�)
	 */
	public static final String F_WF_TASK_ACTOR = "actor";

	/**
	 * �����ֶΣ���ʼ����
	 */
	public static final String F_WF_TASK_STARTDATE = "startdate";

	/**
	 * �����ֶΣ��������
	 */
	public static final String F_WF_TASK_FINISHDATE = "finishdata";

	/**
	 * �����ֶΣ�����Ĳ���
	 */
	public static final String F_WF_TASK_ACTION = "action";

	/**
	 * �����ֶ�ֵ������Ŀ�ʼ����
	 */
	public static final String TASK_ACTION_START = "start";

	/**
	 * �����ֶ�ֵ���������ɲ���
	 */
	public static final String TASK_ACTION_COMPLETE = "complete";

	/**
	 * �ж�key����������Ƿ񼤻�
	 * 
	 * @param key
	 * @return
	 */
	boolean isWorkflowActivate(String key);

	/**
	 * ���key��������̶���
	 * 
	 * @param key
	 * @return
	 */
	DroolsProcessDefinition getProcessDefinition(String key);

	/**
	 * ���key����Ĺ����������̿��ƣ������������̣�
	 * 
	 * @param key
	 * @return
	 */
	Workflow getWorkflow(String key);

	/**
	 * ���key���̵�ĳ���ڵ�����Ľ�ɫ����
	 * 
	 * @param key
	 * @param nodeActorParameter
	 * @return
	 */
	AbstractRoleDefinition getProcessActionAssignment(String key,
			String nodeActorParameter);

	/**
	 * ���key���̵�ĳ���ڵ��ִ����userid
	 * 
	 * @param key
	 * @param nodeActorParameter
	 * @return
	 */
	String getProcessActionActor(String key, String nodeActorParameter);


	/**
	 * ���key����ʵ������ʷ����
	 * 
	 * @param key
	 * @param query
	 * @return
	 */
	@Deprecated
	BasicBSONList getWorkflowHistroyData(String key, boolean query);

	void setProcessDefinition(String key, DroolsProcessDefinition definition);

	void setWorkflowActivate(String key, boolean activated);

	void setProcessActionAssignment(String key, String nodeActorParameter,
			AbstractRoleDefinition newRole);

	void setProcessActionActor(String key, String nodeActorParameter,
			String userid);

	/**
	 * ���key���̵Ķ����ֶ�ֵ
	 * 
	 * @param workflowKey
	 * @return
	 */
	DBObject getWorkflowDefinition(String workflowKey);

	DBObject getProcessActorsData(String key);

	DBObject getProcessRoleAssignmentData(String key);

	DBObject getWorkflowTaskData(String key);

	/**
	 * key��Ӧ�������Ƿ����
	 * @param fWfExecute
	 * @return
	 */
	boolean isWorkflowActivateAndAvailable(String fWfExecute);

	/**
	 * ��������Ƿ��������
	 * @param fWfExecute
	 * @return
	 */
	List<String[]> checkProcessRunnable(String fWfExecute);

}
