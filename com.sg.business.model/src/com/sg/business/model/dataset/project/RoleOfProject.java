package com.sg.business.model.dataset.project;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.ProjectRole;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;
/**
 * <p>
 * 项目角色集合
 * </p>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获得项目角色的集合信息，应用于新建项目的项目组角色显示<br/>
 * 包括以下功能：
 * <li>获取项目角色集合数据信息
 * <li>通过PROJECTID关联所选项目
 * 
 * @author yangjun
 *
 */
public class RoleOfProject extends MasterDetailDataSetFactory {

	/**
	 * 项目角色的集合构造函数,用于设置项目角色的集合的存放数据库及数据存储表
	 */
	public RoleOfProject() {
		//设置项目角色的集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT_ROLE);
	}

	/**
	 * 设置项目角色集合与项目的关联字段：{@link com.sg.business.model.IProjectRelative.F_PROJECT_ID }
	 */
	@Override
	protected String getDetailCollectionKey() {
		return ProjectRole.F_PROJECT_ID;
	}

}
