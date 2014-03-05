package com.sg.sales.model;

import com.mobnut.db.model.IContext;

public interface IDataStatusControl {
	public static final String BASIC_VALUE_EDITING = "editing";
	public static final String BASIC_VALUE_COMMITED = "commited";
	public static final String BASIC_VALUE_CHECKED = "checked";
	public static final String BASIC_VALUE_APPROVED = "approved";
	public static final String BASIC_VALUE_DEPOSITE = "deposit";
	
	public static final String BASIC_TEXT_EDITING = "编辑";
	public static final String BASIC_TEXT_COMMITED = "提交";
	public static final String BASIC_TEXT_CHECKED = "核对";
	public static final String BASIC_TEXT_APPROVED = "批准";
	public static final String BASIC_TEXT_DEPOSITE = "废弃";

	public static final String CONTRACT_VALUE_NOTVALID = "未生效";
	public static final String CONTRACT_VALUE_EXECTUTING = "执行中";
	public static final String CONTRACT_VALUE_TERMINATED = "已中止";
	public static final String CONTRACT_VALUE_CLOSED = "已关闭";
	
	public static final String CONTRACT_TEXT_NOTVALID = "未生效";
	public static final String CONTRACT_TEXT_EXECTUTING = "执行中";
	public static final String CONTRACT_TEXT_TERMINATED = "已中止";
	public static final String CONTRACT_TEXT_CLOSED = "已关闭";
	

	public static final String MESSAGE_CANNOT_MODIFY = "数据当前的状态不允许修改";

	public static final String MESSAGE_CANNOT_REMOVE = "数据当前的状态不允许删除";
	
	String getStatusText();
	
	void checkDataStatusForRemove(IContext context)throws Exception;

	void checkDataStatusForUpdate(IContext context) throws Exception;

}
