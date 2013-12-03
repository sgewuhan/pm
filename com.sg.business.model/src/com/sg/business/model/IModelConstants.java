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
	 * 
	 * 工时绩效数据
	 */
	public static final String C_WORKS_PERFORMENCE = "worksperformence";

	/**
	 * 工时分配数据
	 */
	public static final String C_WORKS_ALLOCATE = "worksallocate";

	/**
	 * 成本科目对照表
	 */
	public static final String C_COSTACCOUNT_ITEM = "costaccount";

	/**
	 * 成本中心的期间成本（从SAP获取）
	 */
	public static final String C_RND_PEROIDCOST_COSTCENTER = "rndcost";
	

	/**
	 * 工作令号的期间成本（分摊计算后保存）
	 */
	public static final String C_RND_PEROIDCOST_ALLOCATION = "rndcostallocation";

	/**
	 * 工作令号的对应研发成本
	 */
	public static final String C_WORKORDER_COST = "workordercost";
	/**
	 * 组织id对应的工作令号
	 */
	public static final String C_COMPANY_WORKORDER = "companyworkorders";
	
	/**
	 * 项目编号的自动编号序列名称,在Project.doInsert()中的编码使用
	 */
	public static final String SEQ_PROJECT_NUMBER = "projectnumber";
	
	/**
	 * 文档模板自动编号
	 */
	public static final String SEQ_DOCUMENT_TEMPLATE_NUMBER = "documenttemplatenumber";
	
	
	/**
	 * 文档自动编号
	 */
	public static final String SEQ_DOCUMENT_NUMBER = "documentnumber";

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
	

	public static final String C_SETTING = "setting";

	/**
	 * 设置: 默认的项目提交工作的周期,此设置只能用于系统设置Interval
	 */
	public static final String S_DEFAULT_PROJECT_COMMIT_DURATION = "PROJECT.COMMIT.DURATION";
	
	/**
	 * 设置：待处理工作的刷新周期，此设置可用于用户设置和系统设置
	 */
	public static final String S_U_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.INTERVAL";

	
	/**
	 * 设置：待处理工作的刷新周期，此设置可用于系统设置
	 */
	public static final String S_S_WORK_RESERVED_REFRESH_INTERVAL = "WORK.RESERVED.REFRESH.SYSTEM";

	/**
	 * 设置: BI数据缓存加载时间间隔 小时
	 */
	public static final String S_S_BI_DATARELOAD_INTERVAL = "BI.CACHE.DATA_RELOAD.INTERVAL";
	
	/**
	 * 设置：收件箱的刷新间隔时间
	 */
	public static final String S_U_MESSAGE_RESERVED_REFRESH_INTERVAL = "MESSAGEINBOX.REFRESH.INTERVAL";

	
	/**
	 * 设置：流程库的URL地址
	 * 
	 */
	public static final String S_PROCESS_BASE_URL = "SYSTEM.PROCESS_BASE_URL";

	/**
	 * 设置：SAP客户端最大连接数
	 */
	public static final String S_EAI_SAP_MAXCONN = "EAI.SAP.MAXCONN";


	/**
	 * 设置：SAP客户端名称
	 */
	public static final String S_EAI_SAP_CLIENT = "EAI.SAP.CLIENT";

	/**
	 * 设置：SAP客户端用户ID
	 */
	public static final String S_EAI_SAP_USERID = "EAI.SAP.USERID";

	/**
	 * 设置：SAP用户登录密码
	 */
	public static final String S_EAI_SAP_PASSWORD = "EAI.SAP.PASSWORD";

	/**
	 * 设置：SAP语言设置
	 */
	public static final String S_EAI_SAP_LANGUAGE = "EAI.SAP.LANG";

	/**
	 * 设置：SAP主机IP
	 */
	public static final String S_EAI_SAP_HOST = "EAI.SAP.HOST";

	/**
	 * 设置：SAP实例编号
	 */
	public static final String S_EAI_SAP_INSTANCENUMBER = "EAI.SAP.INSTANCENUMBER";

	/**
	 * 设置：用户的流程任务
	 */
	public static final String C_USERTASK = "usertask";

	/**
	 * 设置：流程任务延迟标记时间（分钟）
	 */
	public static final String S_S_TASK_DELAY = "TASK.DELAYMARK";
	
	/**
	 * 设置：成本超期预估比例
	 */
	public static final String S_S_BI_OVER_COST_ESTIMATE = "BI.OVER.COST.ESTIMATE";

	/**
	 * 设置：逗号分割的主要版本号序列
	 */
	public static final String S_MAJOR_VID_SEQ = "MAJOR_VID_SEQ";

	/**
	 * 产品物资编码
	 */
	public static final String C_PRODUCT = "productitem";

	/**
	 * 用户项目集
	 */
	public static final String C_USERPROJECTPERF = "userprojectperf";

	/**
	 * 销售数据
	 */
	public static final String C_SALESDATA = "salesdata";

}
