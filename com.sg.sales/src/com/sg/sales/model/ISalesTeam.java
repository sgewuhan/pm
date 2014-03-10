package com.sg.sales.model;

public interface ISalesTeam {

	public static final String F_CUSTOMER_REP = "cust_representative";
	public static final String F_SALES_MANAGER = "sales_manager";
	public static final String F_SALES_SUP = "sales_supervisor";
	public static final String F_SERVICE_MANAGER = "service_manager";
	public static final String[] VISIABLE_FIELDS = new String[]{F_CUSTOMER_REP,F_SALES_MANAGER,F_SALES_SUP,F_SERVICE_MANAGER};
	public static final String[] DESIGNATED_FIELDS_BY_ROLE = new String[]{F_SALES_SUP};

}
