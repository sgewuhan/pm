package com.sg.business.model.dataset.organization;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 组织成员集合
 * <p/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取当前组织的成员集合信息<br/>
 * 实现以下几种功能：
 * <li>获取组织成员集合数据信息
 * <li>通过organizationid关联所选组织
 * 
 * @author yangjun
 * 
 */
public class MemberofOrg extends MasterDetailDataSetFactory {

	/**
	 * 组织成员集合构造函数,用于设置组织成员集合的存放数据库及数据存储表
	 */
	public MemberofOrg() {
		// 设置组织成员集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	/**
	 * 设置组织成员集合与组织的关联字段：{@link com.sg.business.model.Role.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}

}
