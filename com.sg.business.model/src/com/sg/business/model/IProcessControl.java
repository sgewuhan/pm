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
	 * 当前的流程任务
	 */
	@Deprecated
	public static final String POSTFIX_TASK = "_task"; //$NON-NLS-1$

	/**
	 * 任务字段,任务id
	 */
	@Deprecated
	public static final String F_WF_TASK_ID = "taskid"; //$NON-NLS-1$

	/**
	 * 任务字段,任务名称
	 */
	@Deprecated
	public static final String F_WF_TASK_NAME = "taskname"; //$NON-NLS-1$

	/**
	 * 任务字段，任务备注
	 */
	@Deprecated
	public static final String F_WF_TASK_DESC = "description"; //$NON-NLS-1$

	/**
	 * 任务字段,实际的活动所有者
	 */
	@Deprecated
	public static final String F_WF_TASK_ACTUALOWNER = "actualowner"; //$NON-NLS-1$

	/**
	 * 任务字段，任务的创建者
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDBY = "createdby"; //$NON-NLS-1$

	/**
	 * 任务字段，创建时间
	 */
	@Deprecated
	public static final String F_WF_TASK_CREATEDON = "createdon"; //$NON-NLS-1$

	/**
	 * 任务字段，流程定义id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSID = "processid"; //$NON-NLS-1$

	/**
	 * 任务字段，流程实例id
	 */
	@Deprecated
	public static final String F_WF_TASK_PROCESSINSTANCEID = "instanceid"; //$NON-NLS-1$

	/**
	 * 任务字段，任务状态
	 */
	@Deprecated
	public static final String F_WF_TASK_STATUS = "status"; //$NON-NLS-1$

	/**
	 * 任务字段，流程实例id
	 */
	@Deprecated
	public static final String F_WF_TASK_WORKITEMID = "workitemid"; //$NON-NLS-1$

	/**
	 * 任务字段，通知日期
	 */
	@Deprecated
	public static final String F_WF_TASK_NOTICEDATE = "noticedate"; //$NON-NLS-1$

	/**
	 * 任务字段，执行人Id(登录帐户)
	 */
	public static final String F_WF_TASK_ACTOR = "actor"; //$NON-NLS-1$

	/**
	 * 任务字段，开始日期
	 */
	public static final String F_WF_TASK_STARTDATE = "startdate"; //$NON-NLS-1$

	/**
	 * 任务字段，完成日期
	 */
	public static final String F_WF_TASK_FINISHDATE = "finishdata"; //$NON-NLS-1$

	/**
	 * 任务字段，任务的操作
	 */
	public static final String F_WF_TASK_ACTION = "action"; //$NON-NLS-1$

	/**
	 * 任务字段值，任务的开始操作
	 */
	public static final String TASK_ACTION_START = "start"; //$NON-NLS-1$

	/**
	 * 任务字段值，任务的完成操作
	 */
	public static final String TASK_ACTION_COMPLETE = "complete"; //$NON-NLS-1$

	/**
	 * 判断key代表的流程是否激活
	 * 
	 * @param key
	 * @return
	 */
	boolean isWorkflowActivate(String key);

	/**
	 * 获得key代表的流程定义
	 * 
	 * @param key
	 * @return
	 */
	DroolsProcessDefinition getProcessDefinition(String key);

	/**
	 * 获得key代表的工作流（流程控制，用于启动流程）
	 * 
	 * @param key
	 * @return
	 */
	Workflow getWorkflow(String key);

	/**
	 * 获得key流程的某个节点参数的角色定义
	 * 
	 * @param key
	 * @param nodeActorParameter
	 * @return
	 */
	AbstractRoleDefinition getProcessActionAssignment(String key,
			String nodeActorParameter);

	/**
	 * 获得key流程的某个节点的执行人userid
	 * 
	 * @param key
	 * @param nodeActorParameter
	 * @return
	 */
	String getProcessActionActor(String key, String nodeActorParameter);


	/**
	 * 获得key流程实例的历史数据
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
	 * 获得key流程的定义字段值
	 * 
	 * @param workflowKey
	 * @return
	 */
	DBObject getWorkflowDefinition(String workflowKey);

	DBObject getProcessActorsData(String key);

	DBObject getProcessRoleAssignmentData(String key);

	DBObject getWorkflowTaskData(String key);

	/**
	 * key对应的流程是否可用
	 * @param fWfExecute
	 * @return
	 */
	boolean isWorkflowActivateAndAvailable(String fWfExecute);

	/**
	 * 检查流程是否可以启动
	 * @param fWfExecute
	 * @return
	 */
	List<String[]> checkProcessRunnable(String fWfExecute);

	List<PrimaryObject> getWorkflowHistroy();

}
