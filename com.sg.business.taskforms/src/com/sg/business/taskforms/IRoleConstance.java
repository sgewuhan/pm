package com.sg.business.taskforms;

import com.sg.business.resource.nls.Messages;

public interface IRoleConstance {

	/**
	 * 总工
	 */
	public static final String ROLE_CHIEF_ENGINEER_ID = "Chief Engineer"; //$NON-NLS-1$
	public static final String ROLE_CHIEF_ENGINEER_TEXT = Messages.get().IRoleConstance_1;
	
	/**
	 * 立项审核者
	 */
	public static final String ROLE_PROJECR_AUDIT_ID = "Project Audit"; //$NON-NLS-1$
	public static final String ROLE_PROJECR_AUDIT_TEXT = Messages.get().IRoleConstance_3;
	
	/**
	 * 立项批准者
	 */
	public static final String ROLE_PROJECR_APPROVER_ID = "Project Approver"; //$NON-NLS-1$
	public static final String ROLE_PROJECR_APPROVER_TEXT = Messages.get().IRoleConstance_5;
	
	/**
	 * 常务副主任
	 */
	public static final String ROLE_DEPUTY_DIRECTOR_ID = "Deputy Director"; //$NON-NLS-1$
	public static final String ROLE_DEPUTY_DIRECTOR_TEXT = Messages.get().IRoleConstance_7;
	
	
	public static final String ROLE_CHANGE_APPROVER_ID = "Change Approver"; //$NON-NLS-1$
	public static final String ROLE_CHANGE_APPROVER_TEXT = Messages.get().IRoleConstance_9;
	
	/**
	 * 研究室主任
	 */
//	public static final String ROLE_DIRECTOR_ID = "Director";
//	public static final String ROLE_DIRECTOR_TEXT = "研究室主任";
	
	
	/**
	 * 资料审核员
	 */
	public static final String ROLE_DATAAUDIT_ID = "Data Audit"; //$NON-NLS-1$
	public static final String ROLE_DATAAUDIT_TEXT = Messages.get().IRoleConstance_11;
	
	/**
	 * 开发中心主任
	 */
	public static final String ROLE_DEVELOPMENTDIRECTOR_ID = "Development Director";//$NON-NLS-1$
	public static final String ROLE_DEVELOPMENTDIRECTOR_TEXT = Messages.get().IRoleConstance_13;
	
	/**
	 * 项目指派者
	 */
	public static final String ROLE_A_PROJECR_ID= "A Project"; //$NON-NLS-1$
	public static final String ROLE_A_PROJECR_TEXT = Messages.get().IRoleConstance_15;
	
	/**
	 * 技术支持指派者
	 */
	public static final String ROLE_A_SUPPORT_ID = "A Support"; //$NON-NLS-1$
	public static final String ROLE_A_SUPPORT_TEXT = Messages.get().IRoleConstance_17;
	
	/**
	 * 技术支持通知人
	 */
	public static final String ROLE_SUPPORT_NOTICE_ID = "Support Notice"; //$NON-NLS-1$
	public static final String ROLE_SUPPORT_NOTICE_TEXT = Messages.get().IRoleConstance_19;
	
	/**
	 * 技术支持校核人
	 */
	public static final String ROLE_SUPPORT_CHECKER_ID = "Support Checker"; //$NON-NLS-1$
	public static final String ROLE_SUPPORT_CHECKER_TEXT = Messages.get().IRoleConstance_21;
	
	/**
	 * 技术支持审核人
	 */
	public static final String ROLE_SUPPORT_AUDITOR_ID = "Support Auditor"; //$NON-NLS-1$
	public static final String ROLE_SUPPORT_AUDITOR_TEXT = Messages.get().IRoleConstance_23;
	
	/**
	 * 技术支持批准人
	 */
	public static final String ROLE_SUPPORT_APPROVE_ID = "Support Approve"; //$NON-NLS-1$
	public static final String ROLE_SUPPORT_APPROVE_TEXT = Messages.get().IRoleConstance_25;
	
	/**
	 * 事业部模具批准者
	 */
	public static final String ROLE_MOLD_APPROVER_DEPT_ID = "Mold Approver Dept";
	public static final String ROLE_MOLD_APPROVER_DEPT_TEXT =Messages.get().IRoleConstance_26;
	
	/**
	 * 设备模具批准者
	 */
	public static final String ROLE_MOLD_APPROVER_DEVICE_ID = "Mold Approver Device";
	public static final String ROLE_MOLD_APPROVER_DEVICE_TEXT =Messages.get().IRoleConstance_27;
	
	/**
	 * 总工程师批准
	 */
	public static final String ROLE_CHIEF_ENGINEER_APPROVER_ID = "Chief Engineer Approver";
	public static final String ROLE_CHIEF_ENGINEER_APPROVER_TEXT =Messages.get().IRoleConstance_28;
	
	/**
	 * 公司模具批准者
	 */
	public static final String ROLE_MOLD_GENERAL_MANAGER_ID = "Mold General Manager";
	public static final String ROLE_MOLD_GENERAL_MANAGER_TEXT =Messages.get().IRoleConstance_29;
	
	/**
	 * 技术部审核
	 */
	public static final String ROLE_TECHNOLOGY_CHECKER_ID = "Technology Checker";
	public static final String ROLE_TECHNOLOGY_CHECKER_TEXT=Messages.get().IRoleConstance_30;
	
	/**
	 *生产部审核 
	 */
	public static final String ROLE_PRODUCTION_CHECKER_ID = "Production Checker";
	public static final String ROLE_PRODUCTION_CHECKER_TEXT=Messages.get().IRoleConstance_31;
	
	/**
	 * 市场部审核
	 */
	public static final String ROLE_MARKET_CHECKER_ID = "Market Checker";
	public static final String ROLE_MARKET_CHECKER_TEXT=Messages.get().IRoleConstance_32;
	
	/**
	 * 质量部审核
	 */
	public static final String ROLE_QUALITY_CHECKER_ID = "Quality Checker";
	public static final String ROLE_QUALITY_CHECKER_TEXT=Messages.get().IRoleConstance_33;
	
	/**
	 * 技术负责人审核
	 */
	public static final String ROLE_TECHNICAL_LEADER_CHECKER_ID = "Technical Leader Checker";
	public static final String ROLE_TECHNICAL_LEADER_CHECKER_TEXT=Messages.get().IRoleConstance_34;
	
	/**
	 * 高级设计师校核
	 */
	public static final String ROLE_SENIOR_DESIGNER_ID = "Senior Designer";
	public static final String ROLE_SENIOR_DESIGNER_TEXT=Messages.get().IRoleConstance_35;
	
	/**
	 * 技术部审核（独立工作）
	 */
	public static final String ROLE_TECHNOLOGY_CHECKER_STANDLONE_ID="Technology Checker Standlone";
	public static final String ROLE_TECHNOLOGY_CHECKER_STANDLONE_TEXT=Messages.get().IRoleConstance_36;
	
	/**
	 * 技术负责人审核（独立工作）
	 */
	public static final String ROLE_TECHNICAL_LEADER_CHECKER_STANDLONE_ID = "Technical Leader Checker Standlone";
	public static final String ROLE_TECHNICAL_LEADER_CHECKER_STANDLONE_TEXT=Messages.get().IRoleConstance_37;
	
	/**
	 * 项目支持2审核人
	 */
	public static final String ROLE_SUPPORT_AUDITOR_2_ID = "Project Support Auditor";
	public static final String ROLE_SUPPORT_AUDITOR_2_TEXT = Messages.get().IRoleConstance_38;
	
	/**
	 * 项目支持2批准人
	 */
	public static final String ROLE_SUPPORT_APPROVE_2_ID = "Project Support Approve";
	public static final String ROLE_SUPPORT_APPROVE_2_TEXT =  Messages.get().IRoleConstance_39;
}
