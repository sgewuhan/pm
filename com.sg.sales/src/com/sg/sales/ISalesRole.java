package com.sg.sales;

public interface ISalesRole {

	/**
	 * 销售模块所使用的角色
	 */
	public static final String ROLE_EXPENSE_CHECK_NUMBER = "T102";
	public static final String ROLE_EXPENSE_CHECK_TEXT = "费用复核";

	public static final String ROLE_EXPENSE_APPROVER_NUMBER = "T101";
	public static final String ROLE_EXPENSE_APPROVER_TEXT = "费用批准";
	
	public static final String ROLE_EXPENSE_AUDIT_NUMBER = "T105";
	public static final String ROLE_EXPENSE_AUDIT_TEXT = "费用审核";

//	public static final String TEAM_MANAGER_NUMBER = "T005";
//	public static final String TEAM_MANAGER_TEXT = "管理者";
	
	public static final String FINANCAIL_MANAGER_NUMBER = "T006";
	public static final String FINANCAIL_MANAGER_TEXT = "财务经理";
	
	public static final String SALES_SUPERVISOR_NUMBER = "T100";
	public static final String SALES_SUPERVISOR_AUDIT_TEXT = "销售主管";
	
	public static final String CRM_ADMIN_NUMBER = "T901";
	public static final String CRM_ADMIN_TEXT = "销售数据授权人";
	

}
