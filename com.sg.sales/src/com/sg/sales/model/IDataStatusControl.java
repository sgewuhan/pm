package com.sg.sales.model;

public interface IDataStatusControl {
	public static final String STATUS_VALUE_EDITING = "editing";
	public static final String STATUS_VALUE_COMMITED = "commited";
	public static final String STATUS_VALUE_CHECKED = "checked";
	public static final String STATUS_VALUE_APPROVED = "approved";
	public static final String STATUS_VALUE_DEPOSITE = "deposit";
	
	public static final String STATUS_TEXT_EDITING = "编辑";
	public static final String STATUS_TEXT_COMMITED = "提交";
	public static final String STATUS_TEXT_CHECKED = "核对";
	public static final String STATUS_TEXT_APPROVED = "批准";
	public static final String STATUS_TEXT_DEPOSITE = "废弃";
	public static final String F_STATUS = "status";
	
	String getStatusText();

}
