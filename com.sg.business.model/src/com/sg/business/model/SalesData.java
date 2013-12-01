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
	 * ��Ӫ��Χ�Ļ�������
	 */
	public static final String F_PALEDGE = "PALEDGE";

	/**
	 * ������
	 */
	public static final String F_ACCOUNT_YEAR = "GJAHR";

	/**
	 * �ڼ�
	 */
	public static final String F_ACCOUNT_MONTH = "PERDE";

	/**
	 * ��Ʒ���ϱ���
	 */
	public static final String F_MATERIAL_NUMBER = "MATNR";
	
	/**
	 * ��������
	 */
	public static final String F_SALES_INCOME = "VV010";
	
	/**
	 * ���۳ɱ�
	 */
	public static final String F_SALES_COST = "VV030";
	
	/**
	 * ���۳ɱ������̯
	 */
	public static final String F_COST_VAR_APP = "VV040";
	
	/**
	 * ��˾����
	 */
	public static final String F_COMPANY_NUMBER = "BUKRS";
	
	/**
	 * ��������
	 */
	public static final String F_SALES_REGION = "BZIRK";
	
	/**
	 * �ͻ�����
	 */
	public static final String F_CUSTOMER_NUMBER = "KNDNR";
	
	/**
	 * ���۲���
	 */
	public static final String F_SALES_DEPT = "VKBUR";
	
	/**
	 * ������
	 */
	public static final String F_SALES_GROUP = "VKGRP";
	
	/**
	 * ������֯
	 */
	public static final String F_SALES_TEAM = "VKORG";
}
