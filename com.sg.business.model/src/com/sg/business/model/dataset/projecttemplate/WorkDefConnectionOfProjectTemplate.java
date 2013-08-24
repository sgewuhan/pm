package com.sg.business.model.dataset.projecttemplate;

import com.sg.business.model.IModelConstants;
import com.sg.business.model.WorkDefinitionConnection;
import com.sg.widgets.commons.dataset.MasterDetailDataSetFactory;

/**
 * <p>
 * 工作前后置关系集合
 * <p/> 
 * 继承于 {@link com.sg.widgets.commons.dataset.MasterDetailDataSetFactory}，
 * 用于获取业务管理中所选项目模版的工作前后置关集合信息<br/>
 * 实现以下几种功能：
 * <li>获取工作前后置关集合数据信息
 * <li>通过F_PROJECT_TEMPLATE_ID关联所选项目模版
 * 
 * @author yangjun
 *
 */
public class WorkDefConnectionOfProjectTemplate extends
		MasterDetailDataSetFactory {

	/**
	 * 获取工作前后置关系集合数据信息,用于设置工作前后置关系集合的存放数据库及数据存储表
	 */
	public WorkDefConnectionOfProjectTemplate() {
		//设置工作前后置关系集合的存放数据库及数据存储表
		super(IModelConstants.DB, IModelConstants.C_WORK_DEFINITION_CONNECTION);
	}

	/**
	 * 设置工作前后置关系集合与项目模版的关联字段：
	 * {@link com.sg.business.model.WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID}
	 */
	@Override
	protected String getDetailCollectionKey() {
		return WorkDefinitionConnection.F_PROJECT_TEMPLATE_ID;
	}

}
