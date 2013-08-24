package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

/**
 * <p>
 * 顶层组织集合
 * <p/>
 * 继承于 {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}，
 * 用于获取所有顶层组织项下的文档信息<br/>
 * 实现以下几种功能：
 * <li>获取所有顶层组织集合数据信息,会过滤掉所有{@link com.sg.business.model.Organization.F_PARENT_ID}不为空的组织数据
 * 
 * @author yangjun
 * 
 */
public class OrgRoot extends SingleDBCollectionDataSetFactory {

	/**
	 * 顶层组织集合构造函数,用于设置文档集合的存放数据库及数据存储表
	 * <br/>
	 * 会过滤掉所有{@link com.sg.business.model.Organization.F_PARENT_ID}不为空的组织数据
	 */
	public OrgRoot() {
		//设置组织集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		//设置查询条件
		setQueryCondition(new BasicDBObject().append(Organization.F_PARENT_ID, null));
	}


	
}
