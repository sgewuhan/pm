package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class WorkTimeProgram extends PrimaryObject {
	/**
	 * 列类型
	 */
	public static final String F_COLUMNTYPES="columntypes";
	/**
	 * 工时类型
	 */
	public static final String F_WORKTIMETYPES="worktimetypes";
	/**
	 * 工时数据
	 */
	public static final String F_WORKTIMEDATA="worktimedata";
	/**
	 * 说明
	 */
	public static final String F_DESCRIPTION="description";
	/**
	 * 是否启用
	 */
	public static final String F_ACTIVATED="activated";
	
	/**
	 * 所属组织ID
	 * 
	 * @see #Orgainzation
	 */
	public static final String F_ORGANIZATION_ID = "organization_id"; //$NON-NLS-1$
	
	
	/**
	 * 类型选项，用于ColumnType 子记录的字段，DBObject类型
	 */
	public static final String F_TYPE_OPTIONS = "options";
	


}
