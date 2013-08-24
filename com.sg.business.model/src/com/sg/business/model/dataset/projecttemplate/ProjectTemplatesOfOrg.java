package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 项目模版集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选组织的项目模版集合信息<br/>
 * 实现以下几种功能：
 * <li>获取通用项目模版集合数据信息
 * <li>通过F_ORGANIZATION_ID关联所选组织
 * 
 * @author yangjun
 *
 */
public class ProjectTemplatesOfOrg extends MasterDetailDataSetFactory {

	/**
	 * 获取项目模版集合数据信息,用于设置项目模版集合的存放数据库及数据存储表
	 */
	public ProjectTemplatesOfOrg() {
		//设置项目模版集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_PROJECT_TEMPLATE);
	}

	/**
	 * 设置项目模版集合与组织的关联字段：{@link com.sg.business.model.WorkDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}
	
	
}
