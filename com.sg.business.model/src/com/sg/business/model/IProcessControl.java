package com.sg.business.model;

import java.util.List;

import org.bson.types.BasicBSONList;

import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public interface IProcessControl {

	public static final String POSTFIX_ACTIVATED = "_activated"; //$NON-NLS-1$

	public static final String POSTFIX_ASSIGNMENT = "_assignment"; //$NON-NLS-1$

	public static final String POSTFIX_ACTORS = "_actors"; //$NON-NLS-1$

	public static final String POSTFIX_INSTANCEID = "_instance"; //$NON-NLS-1$

	public static final String POSTFIX_HISTORY = "_history"; //$NON-NLS-1$

	/**
	 * ��ǰ����������
	 */
	@Deprecated
	public static final String POSTFIX_TASK = "_task"; //$NON-NLS-1$

	/**
	 * �����ֶ�,����id
	 */
	@Deprecated
	public static final String F_WF_TASK_ID = "taskid"; //$NON-NLS-1$

	/**
	 * �����ֶ�,��������
	 */
	@Deprecated
	public static final String F_WF_TASK_NAME = "taskname"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ע
	 */
	@Deprecated
	public static final String F_WF_TASK_DESC = "description"; //$NON-NLS-1$

	/**
	 * �����ֶ�,ʵ�ʵĻ������
	 */
	@Deprecated
	public static final String F_WF_TASK_ACTUALOWNER = "actualowner"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����Ĵ�����
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDBY = "createdby"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʱ��
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDON = "createdon"; //$NON-NLS-1$

	/**
	 * �����ֶΣ����̶���id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSID = "processid"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSINSTANCEID = "instanceid"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����״̬
	 */
	@Deprecated
	public static final String F_WF_TASK_STATUS = "status"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����ʵ��id
	 */
	@Deprecated
	public static final String F_WF_TASK_WORKITEMID = "workitemid"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�֪ͨ����
	 */
	@Deprecated
	public static final String F_WF_TASK_NOTICEDATE = "noticedate"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�ִ����Id(��¼�ʻ�)
	 */
	public static final String F_WF_TASK_ACTOR = "actor"; //$NON-NLS-1$

	/**
	 * �����ֶΣ���ʼ����
	 */
	public static final String F_WF_TASK_STARTDATE = "startdate"; //$NON-NLS-1$

	/**
	 * �����ֶΣ��������
	 */
	public static final String F_WF_TASK_FINISHDATE = "finishdata"; //$NON-NLS-1$

	/**
	 * �����ֶΣ�����Ĳ���
	 */
	public static final String F_WF_TASK_ACTION = "action"; //$NON-NLS-1$

	/**
	 * �����ֶ�ֵ������Ŀ�ʼ����
	 */
	public static final String TASK_ACTION_START = "start"; //$NON-NLS-1$

	/**
	 * �����ֶ�ֵ���������ɲ���
	 */
	public static final String TASK_ACTION_COMPLETE = "complete"; //$NON-NLS-1$

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
	BasicBSONList getWorkflowHistroyData();

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

	List<PrimaryObject> getWorkflowHistroy();

}
