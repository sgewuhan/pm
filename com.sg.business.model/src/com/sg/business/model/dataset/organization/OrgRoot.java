package com.sg.business.model.dataset.organization;

import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;

/**
 * <p>
 * 组织集合
 * <p/>
 * 组织集合用于获取当前组织集合
 * <br/>
 * 继承于 {@link com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory}
 * 
 * @author yangjun
 * 
 */
public class OrgRoot extends SingleDBCollectionDataSetFactory {

	/**
	 * 组织集合构造函数
	 */
	public OrgRoot() {
		//设置组织集合的存在数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		//设置查询条件
		setQueryCondition(new BasicDBObject().append(Organization.F_PARENT_ID, null));
	}


	
}
