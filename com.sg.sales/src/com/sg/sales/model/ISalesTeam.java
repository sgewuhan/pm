package com.sg.sales.model;

public interface ISalesTeam {

	public static String F_CUSTOMER_REP = "cust_representative";
	public static String F_SALES_MANAGER = "sales_manager";
	public static String F_SALES_SUP = "sales_supervisor";
	public static String F_SERVICE_MANAGER = "service_manager";
	public static String[] VISIABLE_FIELDS = new String[]{F_CUSTOMER_REP,F_SALES_MANAGER,F_SALES_SUP,F_SERVICE_MANAGER};
}
