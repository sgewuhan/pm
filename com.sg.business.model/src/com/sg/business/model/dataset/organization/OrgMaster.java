package com.sg.business.model.dataset.organization;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 组织集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取组织集合信息<br/>
 * 实现以下几种功能：
 * <li>获取组织集合数据信息
 * <li>通过id关联所选组织
 * 
 * @author yangjun
 * 
 */
public class OrgMaster extends MasterDetailDataSetFactory {

	/**
	 * 组织集合构造函数,用于设置组织集合的存放数据库及数据存储表
	 */
	public OrgMaster() {
		//设置组织集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
	}

	/**
	 * 设置组织集合与组织的关联字段：{@link com.sg.business.model.Organization.F__ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return Organization.F__ID;
	}

}
