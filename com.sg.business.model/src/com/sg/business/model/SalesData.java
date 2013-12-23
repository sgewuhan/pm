package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class SalesData extends PrimaryObject {

	// "_id" : ObjectId("52988632e5abdc1a0b184fee"),
	// "PALEDGER" : "02",
	// "GJAHR" : "2013",
	// "PERDE" : "010",
	// "MATNR" : "TXA660018",
	// "VV010" : 2250.0,
	// "VV030" : 2488.95,
	// "VV040" : 0.0,
	// "BUKRS" : "4120",
	// "BZIRK" : "01",
	// "KNDNR" : "0000150013",
	// "VKBUR" : "X340",
	// "VKGRP" : " ",
	// "VKORG" : "4120"

	/**
	 * 经营范围的货币类型
	 */
	public static final String F_PALEDGE = "PALEDGE"; //$NON-NLS-1$

	/**
	 * 会计年度
	 */
	public static final String F_ACCOUNT_YEAR = "GJAHR"; //$NON-NLS-1$

	/**
	 * 期间
	 */
	public static final String F_ACCOUNT_MONTH = "PERDE"; //$NON-NLS-1$

	/**
	 * 产品物料编码
	 */
	public static final String F_MATERIAL_NUMBER = "MATNR"; //$NON-NLS-1$
	
	/**
	 * 销售收入
	 */
	public static final String F_SALES_INCOME = "VV010"; //$NON-NLS-1$
	
	/**
	 * 销售成本
	 */
	public static final String F_SALES_COST = "VV030"; //$NON-NLS-1$
	
	/**
	 * 销售成本差异分摊
	 */
	public static final String F_COST_VAR_APP = "VV040"; //$NON-NLS-1$
	
	/**
	 * 公司代码
	 */
	public static final String F_COMPANY_NUMBER = "BUKRS"; //$NON-NLS-1$
	
	/**
	 * 销售区域
	 */
	public static final String F_SALES_REGION = "BZIRK"; //$NON-NLS-1$
	
	/**
	 * 客户代码
	 */
	public static final String F_CUSTOMER_NUMBER = "KNDNR"; //$NON-NLS-1$
	
	/**
	 * 销售部门
	 */
	public static final String F_SALES_DEPT = "VKBUR"; //$NON-NLS-1$
	
	/**
	 * 销售组
	 */
	public static final String F_SALES_GROUP = "VKGRP"; //$NON-NLS-1$
	
	/**
	 * 销售组织
	 */
	public static final String F_SALES_TEAM = "VKORG"; //$NON-NLS-1$
}
