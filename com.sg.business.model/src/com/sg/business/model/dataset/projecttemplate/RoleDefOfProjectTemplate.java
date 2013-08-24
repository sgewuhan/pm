package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.RoleDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 角色集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选项目模版的角色集合信息<br/>
 * 实现以下几种功能：
 * <li>获取角色集合数据信息
 * <li>通过F_PROJECT_TEMPLATE_ID关联所选项目模版
 * 
 * @author yangjun
 *
 */
public class RoleDefOfProjectTemplate extends MasterDetailDataSetFactory {

	/**
	 * 获取角色集合数据信息,用于设置角色集合的存放数据库及数据存储表
	 */
	public RoleDefOfProjectTemplate() {
		//设置角色集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ROLE_DEFINITION);
	}

	/**
	 * 设置角色集合与项目模版的关联字段：{@link com.sg.business.model.RoleDefinition.F_PROJECT_TEMPLATE_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return RoleDefinition.F_PROJECT_TEMPLATE_ID;
	}

}
