package com.sg.business.taskforms;

import com.sg.business.taskforms.nls.Messages;

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
	public static final String ROLE_DEVELOPMENTDIRECTOR_ID = Messages.get().IRoleConstance_12;
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
	
}
