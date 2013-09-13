package com.sg.business.model;

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
	
	boolean isWorkflowActivate(String fieldKey) ;
	
	DroolsProcessDefinition getProcessDefinition(String fieldKey);

	Workflow getWorkflow(String key);

	ProjectRole getProcessActionAssignment(String key, String nodeActorParameter);

	String getProcessActionActor(String key, String nodeActorParameter);


	
}
