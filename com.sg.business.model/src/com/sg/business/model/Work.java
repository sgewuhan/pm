package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;


public class Work extends PrimaryObject implements IWorkCloneFields{

	/**
	 * 归属项目id, 字段，保存项目_id的值
	 */
	public static final String F_PROJECT_ID = "project_id";

	/**
	 * 根工作_id字段，用于保存根工作的_id的值
	 */
	public static final String F_ROOT_ID = "root_id";

	/**
	 * 必需的，不可删除，布尔类型的字段
	 */
	public static final String F_MONDARY = "mondary";

	public static final String F_PARENT_ID = "parent_id";

	
}
