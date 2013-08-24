package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.DocumentDefinition;
import com.sg.business.model.IModelConstants;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 文档模板集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选组织的文档模板集合信息<br/>
 * 实现以下几种功能：
 * <li>获取文档模板集合数据信息
 * <li>通过F_ORGANIZATION_ID关联所选组织
 * 
 * @author yangjun
 *
 */
public class DocDefOfOrg extends
		MasterDetailDataSetFactory {

	/**
	 * 获取文档模板集合数据信息,用于设置文档模版集合的存放数据库及数据存储表
	 */
	public DocDefOfOrg() {
		//用于设置文档模版集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_DOCUMENT_DEFINITION);
	}

	/**
	 * 设置文档模板集合与组织的关联字段：{@link com.sg.business.model.DocumentDefinition.F_ORGANIZATION_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return DocumentDefinition.F_ORGANIZATION_ID;
	}

}
