package com.sg.business.model;

/**
 * 项目于项目内容的关联
 * @author jinxitao
 *
 */
public interface IProjectRelative {
	
	/**
	 * 所属项目_id字段，用以保存项目_id的值
	 */
	public static final String F_PROJECT_ID = "project_id";

	public Project getProject();

}
