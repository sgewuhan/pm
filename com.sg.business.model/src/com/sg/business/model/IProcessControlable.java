package com.sg.business.model;

import org.bson.types.BasicBSONList;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public interface IProcessControlable {

	public static final String POSTFIX_ACTIVATED = "_activated";

	public static final String POSTFIX_ASSIGNMENT = "_assignment";

	public static final String POSTFIX_ACTORS = "_actors";

	public static final String POSTFIX_INSTANCEID = "_instance";

	public static final String POSTFIX_HISTORY = "_history";

	/**
	 * 当前的流程任务
	 */
	public static final String POSTFIX_TASK = "_task";

	/**
	 * 任务字段,任务id
	 */
	public static final String F_WF_TASK_ID = "taskid";

	/**
	 * 任务字段,任务名称
	 */
	public static final String F_WF_TASK_NAME = "taskname";

	/**
	 * 任务字段，任务备注
	 */
	public static final String F_WF_TASK_DESC = "description";

	/**
	 * 任务字段,实际的活动所有者
	 */
	public static final String F_WF_TASK_ACTUALOWNER = "actualowner";

	/**
	 * 任务字段，任务的创建者
	 */
	public static final String F_WF_TASK_CREATEDBY = "createdby";

	/**
	 * 任务字段，创建时间
	 */
	public static final String F_WF_TASK_CREATEDON = "createdon";

	/**
	 * 任务字段，流程定义id
	 */
	public static final String F_WF_TASK_PROCESSID = "processid";

	/**
	 * 任务字段，流程实例id
	 */
	public static final String F_WF_TASK_PROCESSINSTANCEID = "instanceid";

	/**
	 * 任务字段，任务状态
	 */
	public static final String F_WF_TASK_STATUS = "status";

	/**
	 * 任务字段，流程实例id
	 */
	public static final String F_WF_TASK_WORKITEMID = "workitemid";

	/**
	 * 任务字段，通知日期
	 */
	public static final String F_WF_TASK_NOTICEDATE = "noticedate";

	/**
	 * 任务字段，执行人Id(登录帐户)
	 */
	public static final String F_WF_TASK_ACTOR = "actor";

	/**
	 * 任务字段，开始日期
	 */
	public static final String F_WF_TASK_STARTDATE = "startdate";

	/**
	 * 任务字段，完成日期
	 */
	public static final String F_WF_TASK_FINISHDATE = "finishdata";

	/**
	 * 任务字段，任务的操作
	 */
	public static final String F_WF_TASK_ACTION = "action";

	/**
	 * 任务字段值，任务的开始操作
	 */
	public static final String TASK_ACTION_START = "start";

	/**
	 * 任务字段值，任务的完成操作
	 */
	public static final String TASK_ACTION_COMPLETE = "complete";

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
	 * 获得key流程实例中某个userid为执行的当前的流程任务数据
	 * 
	 * @param key
	 * @param userid
	 * @param query
	 * @return
	 */
	DBObject getCurrentWorkflowTaskData(String key, String userid, boolean query);

	/**
	 * 获得key流程实例的历史数据
	 * 
	 * @param key
	 * @param query
	 * @return
	 */
	BasicBSONList getWorkflowHistroyData(String key, boolean query);

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

}
