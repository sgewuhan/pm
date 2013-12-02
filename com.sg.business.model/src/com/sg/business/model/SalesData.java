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
	public static final String F_PALEDGE = "PALEDGE";

	/**
	 * 会计年度
	 */
	public static final String F_ACCOUNT_YEAR = "GJAHR";

	/**
	 * 期间
	 */
	public static final String F_ACCOUNT_MONTH = "PERDE";

	/**
	 * 产品物料编码
	 */
	public static final String F_MATERIAL_NUMBER = "MATNR";
	
	/**
	 * 销售收入
	 */
	public static final String F_SALES_INCOME = "VV010";
	
	/**
	 * 销售成本
	 */
	public static final String F_SALES_COST = "VV030";
	
	/**
	 * 销售成本差异分摊
	 */
	public static final String F_COST_VAR_APP = "VV040";
	
	/**
	 * 公司代码
	 */
	public static final String F_COMPANY_NUMBER = "BUKRS";
	
	/**
	 * 销售区域
	 */
	public static final String F_SALES_REGION = "BZIRK";
	
	/**
	 * 客户代码
	 */
	public static final String F_CUSTOMER_NUMBER = "KNDNR";
	
	/**
	 * 销售部门
	 */
	public static final String F_SALES_DEPT = "VKBUR";
	
	/**
	 * 销售组
	 */
	public static final String F_SALES_GROUP = "VKGRP";
	
	/**
	 * 销售组织
	 */
	public static final String F_SALES_TEAM = "VKORG";
}
