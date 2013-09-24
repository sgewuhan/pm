package com.sg.business.model;

/**
 * 模型常量</p>
 * 定义了模型存储的数据库和存储的表
 * @author jinxitao
 *
 */
public interface IModelConstants {

	/**
	 * 注册数据库
	 */
	public final static String DB = "pm2";

	/**
	 * 组织集合
	 */
	public final static String C_ORGANIZATION = "organization";

	/**
	 * 角色集合
	 */
	public static final String C_ROLE = "role";

	/**
	 * 帐户集合
	 */
	public static final String C_USER = "account";

	/**
	 * 角色指派集合
	 */
	public static final String C_ROLE_ASSIGNMENT = "roleassignment";

	/**
	 * 目录集合
	 */
	public static final String C_FOLDER = "folder";

	/**
	 * 文档集合
	 */
	public static final String C_DOCUMENT = "document";

	/**
	 * 工作定义
	 */
	public static final String C_WORK_DEFINITION = "workd";

	/**
	 * 项目模板
	 */
	public static final String C_PROJECT_TEMPLATE = "projecttemplate";

	/**
	 * 预算科目
	 */
	public static final String C_BUDGET_ITEM = "budget";
	
	/**
	 * 系统日历
	 */
	public static final String C_CALENDAR_SETTING = "calendarsetting";
	
	/**
	 * 角色定义
	 */
	public static final String C_ROLE_DEFINITION = "roled";


	/**
	 * 文档模板
	 */
	public static final String C_DOCUMENT_DEFINITION = "documentd";

	/**
	 * 交付物定义
	 */
	public static final String C_DELIEVERABLE_DEFINITION = "deliverabled";

	/**
	 * 工作定义前后置关系
	 */
	public static final String C_WORK_DEFINITION_CONNECTION = "workdconnection";

	/**
	 * 项目集合
	 */
	public static final String C_PROJECT = "project";

	/**
	 * 保存自动编号
	 */
	public static final String C__IDS = "ids";

	/**
	 * 项目编号的自动编号序列名称
	 */
	public static final String SEQ_PROJECT_NUMBER = "projectnumber";
	
	/**
	 * 项目角色
	 */
	public static final String C_PROJECT_ROLE = "projectrole";

	/**
	 * 工作集合
	 */
	public static final String C_WORK = "work";

	/**
	 * 交付物集合
	 */
	public static final String C_DELIEVERABLE = "deliverable";

	/**
	 * 前后置关系
	 */
	public static final String C_WORK_CONNECTION = "workconnection";

	/**
	 * 项目预算
	 */
	public static final String C_PROJECT_BUDGET = "projectbudget";

	
	/**
	 * 角色指派
	 */
	public static final String C_PROJECT_ROLE_ASSIGNMENT = "projectroleassignment";

	/**
	 * 消息
	 */
	public static final String C_MESSAGE = "message";

	/**
	 * 公告板
	 */
	public static final String C_BULLETINBOARD = "bulletinboard";
	

	/**
	 * 设置: 默认的项目提交工作的周期
	 */
	public static final String S_DEFAULT_PROJECT_COMMIT_DURATION = "PROJECT.COMMIT.DURATION";
	
	

}
