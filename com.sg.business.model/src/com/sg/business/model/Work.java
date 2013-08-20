package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;


public class Work extends PrimaryObject implements IWorkCloneFields,IProjectRelative{

	/**
	 * 根工作_id字段，用于保存根工作的_id的值
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * 必需的，不可删除，布尔类型的字段
	 */
	public static final String F_MANDATORY = "mandatory";

	public static final String F_PARENT_ID = "parent_id";

	
}
