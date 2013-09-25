package com.sg.business.model;

public interface IWorkCloneFields {

	/**
	 * 工作定义的负责角色定义，{@link RoleDefinition},保存了角色定义的Id
	 */
	public static final String F_CHARGER_ROLE_ID = "charger_roled_id";

	/**
	 * 指派者角色定义
	 */
	public static final String F_ASSIGNMENT_CHARGER_ROLE_ID = "assignmentcharger_roled_id";
	/**
	 * 参与者角色定义
	 */
	public static final String F_PARTICIPATE_ROLE_SET = "participate_roled_set";

	/**
	 * 工作定义的同层序号
	 */
	public static final String F_SEQ = "seq";
	
	/**
	 * 是否是里程碑任务
	 */
	public static final String F_MILESTONE = "milestone";

	/**
	 * 工作流定义,DBObject 类型<br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "项目变更过程", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_EXECUTE = "wf_execute";

	/**
	 * 工作流定义,DBObject 类型 <br/>
	 * { "kbase" : "com.tmt", "processId" : "com.tmt.ProjectChange",
	 * "processName" : "项目变更过程", "processNamespace" : "com.tmt", "type" :
	 * "RuleFlow", "version" : null }
	 */
	public static final String F_WF_CHANGE = "wf_change";

	/**
	 * 变更工作流是否激活
	 */
	public static final String F_WF_CHANGE_ACTIVATED = "wf_change_activated";

	/**
	 * 执行工作流是否激活
	 */
	public static final String F_WF_EXECUTE_ACTIVATED = "wf_execute_activated";

	/**
	 * 标准工时
	 */
	public static final String F_STANDARD_WORKS = "standardworks";

	/**
	 * 变更流程活动执行人的角色定义
	 */
	public static final String F_WF_CHANGE_ASSIGNMENT = "wf_change_assignment";

	/**
	 * 执行流程的执行人角色定义
	 */
	public static final String F_WF_EXECUTE_ASSIGNMENT = "wf_execute_assignment";

	/**
	 * 是否允许分解工作"
	 */
	public static final String F_SETTING_CAN_BREAKDOWN = "s_canbreakdown";

	/**
	 * 是否允许添加交付物"
	 */
	public static final String F_SETTING_CAN_ADD_DELIVERABLES = "s_canadddeliverables";

	/**
	 * 是否允许修改计划工时"
	 */
	public static final String F_SETTING_CAN_MODIFY_PLANWORKS = "s_canmodifyplanworks";

	/**
	 * 上级工作开始时，本工作自动开始
	 * 
	 */
	public static final String F_SETTING_AUTOSTART_WHEN_PARENT_START = "s_autostartwithparent";

	/**
	 * 上级工作完成时，本工作自动完成"
	 */
	public static final String F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH = "s_autofinishwithparent";

	/**
	 * 是否可以跳过进行中的流程完成工作"
	 */
	public static final String F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH = "s_canskiptofinish";

	/**
	 * 需启动变更流程实施工作的更改"
	 */
	public static final String F_SETTING_WORKCHANGE_MANDORY = "s_workchangeflowmandory";

	/**
	 * 需启动项目变更流程实施工作的更改
	 */
	public static final String F_SETTING_PROJECTCHANGE_MANDORY = "s_projectchangeflowmandory";

	/**
	 * 需要复制的设置项
	 */
	public static final String[] SETTING_FIELDS = new String[] {
			F_SETTING_AUTOFINISH_WHEN_PARENT_FINISH,
			F_SETTING_AUTOSTART_WHEN_PARENT_START,
			F_SETTING_CAN_ADD_DELIVERABLES, F_SETTING_CAN_BREAKDOWN,
			F_SETTING_CAN_MODIFY_PLANWORKS,
			F_SETTING_CAN_SKIP_WORKFLOW_TO_FINISH,
			F_SETTING_PROJECTCHANGE_MANDORY, F_SETTING_WORKCHANGE_MANDORY };
}
