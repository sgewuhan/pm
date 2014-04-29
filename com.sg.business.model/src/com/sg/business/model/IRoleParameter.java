package com.sg.business.model;

import java.util.Map;

public interface IRoleParameter {
	/**
	 * 适用范围，不使用规则
	 */
	public static final int TYPE_NONE = 0;
	
	/**
	 * 适用范围，项目
	 */
	public static final int TYPE_PROJECT = 1;
	
	/**
	 * 适用范围，工作
	 */
	public static final int TYPE_WORK = 2;
	
	/**
	 * 适用范围，工作中的流程
	 */
	public static final int TYPE_WORK_PROCESS = 3;
	
	/**
	 * 适用范围
	 */
	public static final String TYPE = "type";
	

	/**
	 * 指派人员列表，适用所有情况
	 */
	public static final String USERID = "userIdList";

	/**
	 * 项目ID，用于项目取角色成员
	 */
	public static final String PROJECT_ID = "projectId";
	/**
	 * 项目，用于项目取角色成员
	 */
	public static final String PROJECT = "project";
	/**
	 * 项目负责人，用于项目取角色成员
	 */
	public static final String PROJECT_CHARGER = "charger";
	/**
	 * 项目的适用标准，用于项目取角色成员
	 */
	public static final String PROJECT_STANDARD_OPTION = "standardOption";
	/**
	 * 项目的产品类型，用于项目取角色成员
	 */
	public static final String PROJECT_PRODUCT_OPTION = "productOption";
	/**
	 * 项目的项目类型，用于项目取角色成员
	 */
	public static final String PROJECT_TYPE_OPTION = "projectTypeOption";
	/**
	 * 项目商务部门，用于项目取角色成员
	 */
	public static final String PROJECT_BUSINESS_ORGANIZATION = "businessOrganization";
	/**
	 * 项目管理部门，用于项目取角色成员
	 */
	public static final String PROJECT_FUNCTION_ORGANIZATION = "functionOrganization";
	/**
	 * 项目承担部门，用于项目取角色成员
	 */
	public static final String PROJECT_LAUNCH_ORGANIZATION = "launchOrganizationList";
	/**
	 * 项目模板名称，用于项目取角色成员
	 */
	public static final String PROJECT_TEMPLATE_DESC = "projectTemplateDesc";


	/**
	 * 工作ID，用于工作、流程取角色成员
	 */
	public static final String WORK_ID = "workId";
	/**
	 * 工作，用于工作、流程取角色成员
	 */
	public static final String WORK = "work";
	/**
	 * 工作负责人，用于工作、流程取角色成员
	 */
	public static final String WORK_CHARGER = "workCharger";
	/**
	 * 工作里程碑，用于工作、流程取角色成员
	 */
	public static final String WORK_MILESTONE = "milestone";
	
	/**
	 * 工作类型，用于工作取角色成员
	 */
	public static final String WORK_TYPE = "worktype"; 
	
	/**
	 * 流程变量，用于流程取角色成员，暂时无法使用
	 */
	public static final String PROCESS_INPUT = "processinput"; //$NON-NLS-1$
	
	public Map<String, Object> getParameters();
}
