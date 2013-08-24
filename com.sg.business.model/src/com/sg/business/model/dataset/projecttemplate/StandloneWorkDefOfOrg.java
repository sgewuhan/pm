package com.sg.business.model.dataset.projecttemplate;

import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinition;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 独立工作定义集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选组织的独立工作定义集合信息<br/>
 * 实现以下几种功能：
 * <li>获取独立工作定义集合数据信息
 * <li>通过F_ORGANIZATION_ID关联所选组织
 * 
 * @author yangjun
 *
 */
public class StandloneWorkDefOfOrg extends
		MasterDetailDataSetFactory {

	/**
	 * 获取独立工作定义集合数据信息,用于设置独立工作定义集合的存放数据库及数据存储表
	 */
	public StandloneWorkDefOfOrg() {
		//设置独立工作定义集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION);
		//设置独立工作定义的显示条件：
		//WorkDefinition.F_WORK_TYPE为WorkDefinition.WORK_TYPE_STANDLONE
		//WorkDefinition.F_PARENT_ID为NUll的数据
		setQueryCondition(new BasicDBObject().append(
				WorkDefinition.F_WORK_TYPE, WorkDefinition.WORK_TYPE_STANDLONE)
				.append(WorkDefinition.F_PARENT_ID, null));
	}

	/**
	 * 设置独立工作定义集合与组织的关联字段：{@link com.sg.business.model.WorkDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinition.F_ORGANIZATION_ID;
	}

}
