package com.sg.business.model;

import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.runtime.Workflow;

public interface IProcessControlable {

	public static final String POSTFIX_ACTIVATED = "_activated";

	public static final String POSTFIX_ASSIGNMENT = "_assignment";

	public static final String POSTFIX_ACTORS = "_actors";
	
	public static final String POSTFIX_INSTANCEID = "_instance";

	/**
	 * 当前的流程任务
	 */
	public static final String POSTFIX_TASK = "_task";

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
	
	boolean isWorkflowActivate(String fieldKey) ;
	
	DroolsProcessDefinition getProcessDefinition(String fieldKey);

	Workflow getWorkflow(String key);

	ProjectRole getProcessActionAssignment(String key, String nodeActorParameter);

	String getProcessActionActor(String key, String nodeActorParameter);


	
}
