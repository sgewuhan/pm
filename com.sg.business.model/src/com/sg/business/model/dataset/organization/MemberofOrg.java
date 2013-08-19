package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Role;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 组织成员集合
 * <p/>
 * 组织成员集合用于获取当前组织的成员集合
 * <br/>
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class MemberofOrg extends MasterDetailDataSetFactory {
	
	/**
	 * 组织成员集合构造函数
	 */
	public MemberofOrg() {
		//设置组织成员集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_USER);
	}

	/**
	 * 设置组织成员集合的MasterID
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Role.F_ORGANIZATION_ID;
	}
}
