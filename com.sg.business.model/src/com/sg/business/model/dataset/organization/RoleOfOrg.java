package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 组织项下的角色集合
 * <p/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 获得所选组织项下的角色集合
 * <br/>
 * 实现以下几种功能：
 * <li>获取角色集合数据信息
 * <li>通过ORGANIZATIONID关联所选的组织
 * @author yangjun
 * 
 */
public class RoleOfOrg extends MasterDetailDataSetFactory {

	/**
	 * 组织项下的角色集合构造函数,用于设置角色集合的存放数据库及数据存储表
	 */
	public RoleOfOrg() {
		//设置角色集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * 设置角色集合与组织的关联字段：{@link com.sg.business.model.Role.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
