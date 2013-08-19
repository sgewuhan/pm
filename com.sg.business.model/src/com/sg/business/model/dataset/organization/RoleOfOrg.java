package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 组织项下的角色集合
 * <p/>
 * 组织项下的角色集合用于获取当前组织项下的角色集合
 * <br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class RoleOfOrg extends MasterDetailDataSetFactory {

	/**
	 * 组织项下的角色集合构造函数
	 */
	public RoleOfOrg() {
		//设置角色集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ROLE);
	}

	/**
	 * 设置组织项下的角色集合的MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
