package com.sg.sales.model;

public interface IDataStatusControl {
	public static final String STATUS_VALUE_EDITING = "editing";
	public static final String STATUS_VALUE_COMMITED = "commited";
	public static final String STATUS_VALUE_CHECKED = "checked";
	public static final String STATUS_VALUE_APPROVED = "approved";
	public static final String STATUS_VALUE_DEPOSITE = "deposit";
	
	public static final String STATUS_TEXT_EDITING = "�༭";
	public static final String STATUS_TEXT_COMMITED = "�ύ";
	public static final String STATUS_TEXT_CHECKED = "�˶�";
	public static final String STATUS_TEXT_APPROVED = "��׼";
	public static final String STATUS_TEXT_DEPOSITE = "����";
	public static final String F_STATUS = "status";
	
	String getStatusText();

}
